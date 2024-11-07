/* (C) 2024 */ 

package com.example.demo.portfolio.service;

import com.example.demo.market.model.Quote;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.model.Position;
import com.example.demo.portfolio.model.SymbolType;
import com.example.demo.portfolio.pricing.OptionPricing;
import com.example.demo.portfolio.repository.PositionRepository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PositionRepository positionRepository;
    private final OptionManager optionManager;

    public PositionService(PositionRepository positionRepository,
                           OptionManager optionManager) {
        this.positionRepository = positionRepository;
        this.optionManager = optionManager;
    }

    /**
     * holdings of portfolios
     */
    private final ConcurrentHashMap<String, Position> holdings = new ConcurrentHashMap<>();

    public void save(List<PositionEntity> entityList) {
        //
        Iterable<PositionEntity> positionEntities = positionRepository.saveAll(entityList);
        //
        positionEntities.forEach(item -> holdings.put(
                item.getSymbol(),
                Position.newBuilder()
                        .setPositionId(item.getId())
                        .setSymbol(item.getSymbol())
                        .setQty(item.getPositionSize())
                        .setSymbolType(SymbolType.forNumber(item.getSymbolType()))
                        .setNav(0.0f)
                        .setPrice(0.0f)
                        .build()));
    }

    public void deleteAll() {
        positionRepository.deleteAll();
        holdings.clear();
    }

    /**
     * @param stockQuote
     */
    public void updateOnPriceChange(Quote stockQuote) {
        String symbol = stockQuote.getSymbol();
        double price = stockQuote.getPrice();
        // update stock nav
        Position stockPosition = updateNavOnSymbol(symbol, price);
        if (null == stockPosition){
            return;
        }
        List<Position> positionList = optionManager.findSymbols(stockQuote.getSymbol())
                .stream().map(holdings::get).collect(Collectors.toList());
        // update options nav
        List<Position> positions = positionList.stream().parallel().map(new Function<Position, Position>() {
            @Override
            public Position apply(Position position) {
                if (SymbolType.PUT.equals(position.getSymbolType())){
                    return updateOptionNav(stockQuote, position);
                } else if (SymbolType.CALL.equals(position.getSymbolType())){
                    return updateOptionNav(stockQuote, position);
                }
                return position;
            }
        }).collect(Collectors.toList());
        // notify dashboard

    }

    private Position updateOptionNav(Quote stock, Position position) {
        OptionPricing optionPricing = optionManager.getPricing(position.getSymbolType());
        if (null == optionPricing){
            return position;
        }
        double price = optionPricing.price(stock, position);
        return updateNavOnSymbol(position.getSymbol(), price);
    }

    private Position updateNavOnSymbol(String symbol, double price) {
        Position position = holdings.get(symbol);
        if (null == position) {
            return null;
        }
        Position build = position.toBuilder()
                .setPrice(price)
                .setNav(price * position.getQty())
                .setUpdateTime(System.currentTimeMillis())
                .build();
        holdings.put(symbol, build);
        logger.debug("update {} position nav. {}", position.getSymbol(), build);
        return position;
    }

    public Position findBySymbol(String symbol) {
        return holdings.get(symbol);
    }

    public Integer count() {
        return holdings.size();
    }
}
