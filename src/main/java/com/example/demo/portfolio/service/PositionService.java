/* (C) 2024 */ 

package com.example.demo.portfolio.service;

import com.example.demo.market.model.Option;
import com.example.demo.market.model.Quote;
import com.example.demo.market.model.Stock;
import com.example.demo.market.option.OptionManager;
import com.example.demo.market.option.pricing.OptionPricing;
import com.example.demo.market.producer.StockPool;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.model.Portfolio;
import com.example.demo.portfolio.model.Position;
import com.example.demo.portfolio.model.SymbolType;
import com.example.demo.portfolio.repository.PositionRepository;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //
    private final StockPool stockPool;
    private final PositionRepository positionRepository;
    private final OptionManager optionManager;
    /**
     * holdings of portfolios
     */
    private final ConcurrentHashMap<String, Position> holdings = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher applicationEventPublisher;

    public PositionService(
            StockPool stockPool,
            PositionRepository positionRepository,
            OptionManager optionManager,
            ApplicationEventPublisher applicationEventPublisher) {
        this.stockPool = stockPool;
        this.positionRepository = positionRepository;
        this.optionManager = optionManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

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
                        .setStockSymbol(item.getRelStockSymbol())
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
        //
        Position stockPosition = holdings.get(symbol);
        if (null == stockPosition) {
            return;
        }
        // update stock nav
        stockPosition = updateNavOnSymbol(stockPosition, price);
        // filter positions by stock symbol
        // maybe can calculate in other place.
        List<Position> positions = holdings.entrySet().stream()
                .filter(stringPositionEntry ->
                        stringPositionEntry.getValue().getStockSymbol().equalsIgnoreCase(symbol))
                .parallel()
                .map(stringPositionEntry -> {
                    Position position = stringPositionEntry.getValue();
                    if (SymbolType.PUT.equals(position.getSymbolType())) {
                        return updateOptionNav(stockQuote, position);
                    } else if (SymbolType.CALL.equals(position.getSymbolType())) {
                        return updateOptionNav(stockQuote, position);
                    }
                    return position;
                })
                .collect(Collectors.toList());
        // sort by symbols
        positions.add(stockPosition);
        positions.sort(Comparator.comparing(Position::getSymbol));
        // notify dashboard
        double sumOfNav = getSumOfNav(positions);
        //
        Portfolio portfolio = Portfolio.newBuilder()
                .setTotal(sumOfNav)
                .addAllHoldings(positions)
                .setUpdateTime(System.currentTimeMillis())
                .build();
        applicationEventPublisher.publishEvent(portfolio);
    }

    public double getSumOfNav(List<Position> positions) {
        return positions.stream().map(Position::getNav).reduce(0.0d, Double::sum);
    }

    /**
     * for testing
     *
     * @param quote
     * @param position
     * @return
     */
    public Position updateOptionNav(Quote quote, Position position) {
        OptionPricing optionPricing = optionManager.getPricing(position.getSymbolType());
        if (null == optionPricing) {
            logger.info("Option Pricing NOT FOUND. {}", position.getSymbolType());
            return position;
        }
        Stock stock = stockPool.getLatestPrice(quote.getSymbol());
        Option option = optionManager.findOption(position.getSymbol());
        double price = optionPricing.price(stock, quote, option);
        return updateNavOnSymbol(position, price);
    }

    /**
     * for testing
     *
     * @param position
     * @param price
     * @return
     */
    private Position updateNavOnSymbol(Position position, double price) {
        Position positionUpdated = position.toBuilder()
                .setPrice(price)
                .setNav(price * position.getQty())
                .setUpdateTime(System.currentTimeMillis())
                .build();
        holdings.put(position.getSymbol(), positionUpdated);
        logger.debug("update {} position nav. {}", position.getSymbol(), positionUpdated);
        return positionUpdated;
    }

    public Position findBySymbol(String symbol) {
        return holdings.get(symbol);
    }

    public Integer count() {
        return holdings.size();
    }
}
