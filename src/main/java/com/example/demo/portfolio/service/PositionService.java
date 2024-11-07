/* (C) 2024 */ 

package com.example.demo.portfolio.service;

import com.example.demo.market.model.Option;
import com.example.demo.market.model.Quote;
import com.example.demo.market.model.Stock;
import com.example.demo.market.option.OptionManager;
import com.example.demo.market.option.pricing.OptionPricing;
import com.example.demo.market.stock.StockPool;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.model.Portfolio;
import com.example.demo.portfolio.model.Position;
import com.example.demo.portfolio.model.SymbolType;
import com.example.demo.portfolio.repository.PositionRepository;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
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
        // init price, qty.
        holdings.values().stream()
                .filter(item -> item.getSymbolType().equals(SymbolType.STOCK))
                .forEach(new Consumer<Position>() {
                    @Override
                    public void accept(Position position) {
                        Stock price = stockPool.getLatestPrice(position.getSymbol());
                        Quote quote = Quote.newBuilder()
                                .setPrice(price.getPrice())
                                .setSymbol(position.getSymbol())
                                .build();
                        updateOnPriceChange(quote);
                    }
                });
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
        updateNavOnSymbol(stockPosition, price);
        // filter positions by stock symbol
        // maybe can calculate in other place.
        holdings.entrySet().stream()
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
    }

    public double getSumOfNav(Collection<Position> positions) {
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
        //
        BigDecimal v1 = BigDecimal.valueOf(price).setScale(2, RoundingMode.DOWN);
        BigDecimal v2 = BigDecimal.valueOf(position.getQty());
        //
        BigDecimal v3 = v1.multiply(v2).setScale(2, RoundingMode.DOWN);
        Position positionUpdated = position.toBuilder()
                .setPrice(v1.doubleValue())
                .setNav(v3.doubleValue())
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

    public Portfolio getPortfolioDetail() {
        List<Position> positions = Lists.newArrayList(this.holdings.values());
        double sumOfNav = getSumOfNav(positions);
        //
        positions.sort(Comparator.comparing(Position::getSymbol));
        //
        return Portfolio.newBuilder()
                .setTotal(sumOfNav)
                .addAllHoldings(positions)
                .setUpdateTime(System.currentTimeMillis())
                .build();
    }
}
