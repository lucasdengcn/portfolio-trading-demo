/* (C) 2024 */ 

package com.example.demo.portfolio.dashboard;

import com.example.demo.portfolio.model.Portfolio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ConsoleDashboard {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @EventListener
    public void update(Portfolio portfolio) {
        logger.info("receive: {}", portfolio);
        logger.info("sum: {}", portfolio.getTotal());
    }
}
