package com.example.demo;

import com.example.demo.market.QuoteBroker;
import com.example.demo.market.QuoteProducer;
import com.example.demo.market.StockPool;
import com.example.demo.portfolio.consumer.CallOptionConsumerImpl;
import com.example.demo.portfolio.consumer.OptionPool;
import com.example.demo.portfolio.consumer.PutOptionConsumerImpl;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.entity.ProductType;
import com.example.demo.portfolio.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private QuoteProducer quoteProducer;

	@Autowired
	private QuoteBroker quoteBroker;

	@Autowired
	private StockPool stockPool;

	@Autowired
	private PositionService positionService;

	@Autowired
	private CSVDataLoader csvDataLoader;

	@Autowired @Qualifier("callOptionPool")
	OptionPool callOptionPool;

	@Autowired @Qualifier("putOptionPool")
	OptionPool putOptionPool;

	@Autowired
	CallOptionConsumerImpl callOptionConsumer;

	@Autowired
	PutOptionConsumerImpl putOptionConsumer;

	@Override
	public void run(String... args) throws Exception {
		//
		loadPositionData();
		//
		quoteBroker.subscribe(callOptionConsumer);
		quoteBroker.subscribe(putOptionConsumer);
		//
		quoteProducer.start();
	}

	public void loadPositionData() throws IOException {
		csvDataLoader.load(new Consumer<List<PositionEntity>>() {
			@Override
			public void accept(List<PositionEntity> positionEntities) {
				//
				positionService.save(positionEntities);
				//
				positionEntities.forEach(item -> {
					if (item.getType().equals(ProductType.STOCK)){
						stockPool.registerSymbol(item.getSymbol());
					} else if (item.getType().equals(ProductType.CALL)){
						callOptionPool.registerOption(item.getSymbol());
					} else if (item.getType().equals(ProductType.PUT)){
						putOptionPool.registerOption(item.getSymbol());
					}
				});
			}
		});
	}


}
