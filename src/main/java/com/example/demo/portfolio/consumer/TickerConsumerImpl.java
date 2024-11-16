/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.consumer;

import com.example.demo.broker.DataBroker;
import com.example.demo.market.stock.StockPool;
import com.example.demo.model.Ticker;
import com.example.demo.portfolio.service.PositionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TickerConsumerImpl extends AbstractTickerConsumerImpl {

    private final StockPool stockPool;
    private final PositionService positionService;
    private final DataBroker dataBroker;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TickerConsumerImpl(
            StockPool stockPool,
            PositionService positionService,
            DataBroker dataBroker,
            ApplicationEventPublisher applicationEventPublisher) {
        this.stockPool = stockPool;
        this.positionService = positionService;
        this.dataBroker = dataBroker;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void handle(@NonNull Ticker ticker) throws Exception {
        //
        logger.info("receive stock ticker: {}", ticker);
        positionService.updateNavOnPriceChange(ticker.getSymbol(), ticker.getPrice());
        logger.info("update position navs: {}", ticker);
        applicationEventPublisher.publishEvent(ticker.getSymbol());
    }

    @Override
    public void destroy() throws Exception {
        logger.info("TickerConsumer Stopping");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dataBroker.add(this);
    }
}
