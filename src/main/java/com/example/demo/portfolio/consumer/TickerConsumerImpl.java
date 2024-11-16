/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.consumer;

import com.example.demo.broker.DataBroker;
import com.example.demo.market.stock.StockPool;
import com.example.demo.messaging.model.Quote;
import com.example.demo.messaging.model.QuoteBatch;
import com.example.demo.portfolio.service.PositionService;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
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
    public void handle(@NonNull ByteString byteString) throws Exception {
        //
        try {
            QuoteBatch stockQuotes = QuoteBatch.parseFrom(byteString);
            logger.info("receive stock quotes: {}", stockQuotes);
            List<Boolean> collect = stockQuotes.getItemsList().stream()
                    .parallel()
                    .map(new Function<Quote, Boolean>() {
                        @Override
                        public Boolean apply(Quote quote) {
                            positionService.updateOnPriceChange(quote);
                            return true;
                        }
                    })
                    .collect(Collectors.toList());
            logger.info("update position navs: {}, {}", stockQuotes, collect);
            applicationEventPublisher.publishEvent(stockQuotes);
        } catch (InvalidProtocolBufferException e) {
            logger.error(e.getMessage(), e);
            // would need throws for certain cases
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("QuoteConsumer Stopping");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dataBroker.add(this);
    }
}
