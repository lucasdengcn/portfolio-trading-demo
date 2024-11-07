/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.consumer;

import com.example.demo.market.model.Quote;
import com.example.demo.market.model.QuoteBatch;
import com.example.demo.market.producer.QuoteBroker;
import com.example.demo.market.stock.StockPool;
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
public class QuoteConsumerImpl extends AbstractQuoteConsumerImpl {

    private final StockPool stockPool;
    private final PositionService positionService;
    private final QuoteBroker quoteBroker;
    private final ApplicationEventPublisher applicationEventPublisher;

    public QuoteConsumerImpl(
            StockPool stockPool,
            PositionService positionService,
            QuoteBroker quoteBroker,
            ApplicationEventPublisher applicationEventPublisher) {
        this.stockPool = stockPool;
        this.positionService = positionService;
        this.quoteBroker = quoteBroker;
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
        this.quoteBroker.add(this);
    }
}
