/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.service;

import com.example.demo.market.option.OptionManager;
import com.example.demo.market.stock.StockPool;
import com.example.demo.model.*;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.repository.PositionRepository;
import com.example.demo.pricing.option.OptionPricing;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
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

    public void save(@NonNull List<PositionEntity> entityList) {
        //
        Iterable<PositionEntity> positionEntities = positionRepository.saveAll(entityList);
        //
        positionEntities.forEach(item -> holdings.put(
                item.getSymbol(),
                Position.builder()
                        .positionId(item.getId())
                        .symbol(item.getSymbol())
                        .qty(item.getPositionSize())
                        .symbolType(item.getSymbolType())
                        .stockSymbol(item.getRelStockSymbol())
                        .nav(0.0f)
                        .price(0.0f)
                        .build()));
        // init price, qty.
        holdings.values().stream()
                .filter(item -> item.getSymbolType().equals(SymbolType.STOCK))
                .forEach(new Consumer<Position>() {
                    @Override
                    public void accept(Position position) {
                        Stock stock = stockPool.getOne(position.getSymbol());
                        updateNavOnPriceChange(stock.getSymbol(), stock.getPrice());
                    }
                });
    }

    public void deleteAll() {
        positionRepository.deleteAll();
        holdings.clear();
    }

    /**
     *
     * @param symbol
     * @param price
     */
    public void updateNavOnPriceChange(@NonNull String symbol, double price) {
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
                    return updateOptionNav(symbol, price, position);
                })
                .collect(Collectors.toList());
    }

    public double calculatePositionNav(@NonNull Collection<Position> positions) {
        return positions.stream().map(Position::getNav).reduce(0.0d, Double::sum);
    }

    /**
     * for testing
     *
     * @param symbol
     * @param newPrice
     * @param position
     * @return
     */
    public Position updateOptionNav(@NonNull String symbol, double newPrice, @NonNull Position position) {
        OptionPricing optionPricing = optionManager.getPricing(position.getSymbolType());
        if (null == optionPricing) {
            logger.info("Option Pricing NOT FOUND. {}", position.getSymbolType());
            return position;
        }
        Stock stock = stockPool.getOne(symbol);
        Option option = optionManager.findOption(position.getSymbol());
        double priceChanged = optionPricing.price(stock, newPrice, option);
        return updateNavOnSymbol(position, priceChanged);
    }

    /**
     * for testing
     *
     * @param position
     * @param price
     * @return
     */
    private Position updateNavOnSymbol(@NonNull Position position, double price) {
        //
        BigDecimal priceScale = BigDecimal.valueOf(price).setScale(2, RoundingMode.DOWN);
        BigDecimal qtyScale = BigDecimal.valueOf(position.getQty());
        //
        BigDecimal navScale = priceScale.multiply(qtyScale).setScale(2, RoundingMode.DOWN);
        position.setPrice(priceScale.doubleValue());
        position.setNav(navScale.doubleValue());
        position.setUpdateTime(LocalDateTime.now());
        holdings.put(position.getSymbol(), position);
        logger.debug("update {} position nav. {}", position.getSymbol(), position);
        return position;
    }

    public Position findBySymbol(String symbol) {
        return holdings.get(symbol);
    }

    public Integer count() {
        return holdings.size();
    }

    public Portfolio getPortfolioDetail() {
        List<Position> positions = Lists.newArrayList(this.holdings.values());
        double sumOfNav = calculatePositionNav(positions);
        //
        positions.sort(Comparator.comparing(Position::getSymbol));
        //
        return Portfolio.builder()
                .totalNav(sumOfNav)
                .holdings(positions)
                .updateTime(LocalDateTime.now())
                .build();
    }
}
