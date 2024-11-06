package com.example.demo;

import com.example.demo.market.QuoteBroker;
import com.example.demo.market.QuoteProducer;
import com.example.demo.portfolio.consumer.QuoteConsumerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private CSVDataLoader csvDataLoader;

	@Autowired
	private QuoteProducer quoteProducer;

	@Autowired
	private QuoteBroker quoteBroker;

	@Autowired
	private QuoteConsumerImpl quoteConsumer;

	@Override
	public void run(String... args) throws Exception {
//		csvDataLoader.load();
//		quoteBroker.subscribe(quoteConsumer);
//		quoteProducer.start();
	}


}
