package com.example.demo.portfolio.consumer;

import com.example.demo.market.QuoteConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfiguration {

    @Bean("callOptionPool")
    public OptionPool callOptionPool(){
        return new OptionPool();
    }

    @Bean("putOptionPool")
    public OptionPool putOptionPool(){
        return new OptionPool();
    }

    @Bean("callOptionConsumer")
    public CallOptionConsumerImpl callOptionConsumer(OptionPool callOptionPool){
        return new CallOptionConsumerImpl(callOptionPool);
    }

    @Bean("putOptionConsumer")
    public PutOptionConsumerImpl putOptionConsumer(OptionPool putOptionPool){
        return new PutOptionConsumerImpl(putOptionPool);
    }

}
