package com.example.demo.portfolio.consumer;

import com.example.demo.market.StockPool;
import com.example.demo.market.model.Quote;
import com.example.demo.portfolio.service.PositionService;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class QuoteConsumerImpl extends AbstractQuoteConsumerImpl {

    private final OptionPool putOptionPool;
    private final StockPool stockPool;
    private final PositionService positionService;

    public QuoteConsumerImpl(OptionPool optionPool,
                             OptionPool putOptionPool,
                             StockPool stockPool,
                             PositionService positionService) {
        super(optionPool);
        this.putOptionPool = putOptionPool;
        this.stockPool = stockPool;
        this.positionService = positionService;
    }

    @Override
    public void handle(ByteString byteString) throws Exception {
        //
        Quote stockQuote = Quote.parseFrom(byteString);
        logger.info("receive stock quote: {}", stockQuote);
        if (!stockPool.contains(stockQuote.getSymbol())){
            return;
        }
        positionService.updateOnPriceChange(stockQuote);
        //
        triggerOptionsChanges(optionPool, byteString, stockQuote);
        triggerOptionsChanges(putOptionPool, byteString, stockQuote);
    }

    private void triggerOptionsChanges(OptionPool optionPool, ByteString byteString, Quote stockQuote) {
        Set<String> options = optionPool.getOptions(stockQuote.getSymbol());
        List<Integer> collect = options.stream().parallel().map(symbol -> updateCallOptionNav(symbol, byteString)).collect(Collectors.toList());
        logger.info("update {} Options Nav: {}", optionPool.getProductType(), collect);
    }

    private int updateCallOptionNav(String optionSymbol, ByteString byteString) {
        // calculate price
        try {
            Quote optionQuote = Quote.parseFrom(byteString);
            Quote build = optionQuote.toBuilder().setSymbol(optionSymbol).setPrice(2.0f).build();
            return positionService.updateOnPriceChange(build);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("QuoteConsumer Stopping");
    }
}
