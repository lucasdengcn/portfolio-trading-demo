/* (C) 2024 */ 

package com.example.demo.portfolio.consumer;

import com.example.demo.market.model.Quote;
import com.example.demo.market.producer.StockPool;
import com.example.demo.portfolio.service.PositionService;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class QuoteConsumerImpl extends AbstractQuoteConsumerImpl {

    private final StockPool stockPool;
    private final PositionService positionService;

    public QuoteConsumerImpl(StockPool stockPool, PositionService positionService) {
        this.stockPool = stockPool;
        this.positionService = positionService;
    }

    @Override
    public void handle(ByteString byteString) throws Exception {
        //
        try {
            Quote stockQuote = Quote.parseFrom(byteString);
            logger.info("receive stock quote: {}", stockQuote);
            if (!stockPool.contains(stockQuote.getSymbol())) {
                return;
            }
            positionService.updateOnPriceChange(stockQuote);
        } catch (InvalidProtocolBufferException e) {
            logger.error(e.getMessage(), e);
            // would need throws for certain cases
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("QuoteConsumer Stopping");
    }
}
