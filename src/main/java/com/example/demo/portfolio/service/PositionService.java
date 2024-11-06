package com.example.demo.portfolio.service;

import com.example.demo.market.model.Quote;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.model.Position;
import com.example.demo.portfolio.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PositionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PositionRepository positionRepository;

    private final ConcurrentHashMap<String, Position> holdings = new ConcurrentHashMap<>();

    public void save(List<PositionEntity> entityList) {
        //
        Iterable<PositionEntity> positionEntities = positionRepository.saveAll(entityList);
        //
        positionEntities.forEach(item -> holdings.put(item.getSymbol(),
                Position.newBuilder()
                    .setPositionId(item.getId())
                        .setSymbol(item.getSymbol())
                        .setQty(item.getPositionSize())
                        .setNav(0.0f)
                        .setPrice(0.0f)
                        .build()
        ));
    }

    public void deleteAll(){
        positionRepository.deleteAll();
        holdings.clear();
    }

    /**
     *
     * @param quote
     */
    public int updateOnPriceChange(Quote quote){
        String symbol = quote.getSymbol();
        double price = quote.getPrice();
        //
        return updateNavOnSymbol(symbol, price);
    }

    private int updateNavOnSymbol(String symbol, double price) {
        Position position = holdings.get(symbol);
        if (null == position){
            return 0;
        }
        Position build = position.toBuilder()
                .setPrice(price)
                .setNav(price * position.getQty())
                .build();
        holdings.put(symbol, build);
        logger.debug("update position nav. {}", build);
        return 1;
    }

    public Position findBySymbol(String symbol){
        return holdings.get(symbol);
    }

    public Integer count(){
        return holdings.size();
    }

}
