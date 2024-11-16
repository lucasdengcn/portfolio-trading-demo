/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.dashboard;

import com.example.demo.market.stock.StockPool;
import com.example.demo.model.Portfolio;
import com.example.demo.model.Position;
import com.example.demo.model.Stock;
import com.example.demo.portfolio.service.PositionService;
import com.google.common.base.Strings;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ConsoleDashboard {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AtomicInteger counting = new AtomicInteger(0);
    private final PositionService positionService;
    private final StockPool stockPool;
    /**
     *
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("###,###,###.00");

    public ConsoleDashboard(PositionService positionService, StockPool stockPool) {
        this.positionService = positionService;
        this.stockPool = stockPool;
    }

    @EventListener
    public synchronized void updateOnStockPriceChange(@NonNull String symbol) {
        logger.debug("receive updates: {}", symbol);
        //
        Stock stock = stockPool.getOne(symbol);
        if (null == stock) {
            return;
        }
        //
        int seq = counting.incrementAndGet();
        StringBuilder sw = new StringBuilder(2000);
        // head
        sw.append("## ").append(seq).append(" Market Data Update\n");
        sw.append(stock.getSymbol())
                .append(" change to ")
                .append(decimalFormat.format(stock.getPrice()))
                .append("\n");
        // position list
        Portfolio portfolioDetail = positionService.getPortfolioDetail();
        sw.append("\n");
        sw.append("## Portfolio\n");
        //
        sw.append(formatSymbol("symbol")) // 40
                .append(formatNumberCol("price")) // 20
                .append(formatNumberCol("qty")) // 20
                .append(formatNumberCol("value"))
                .append("\n"); // 20
        for (Position position : portfolioDetail.getHoldings()) {
            sw.append(formatSymbol(position.getSymbol()))
                    .append(formatNumberCol(decimalFormat.format(position.getPrice())))
                    .append(formatNumberCol(decimalFormat.format(position.getQty())))
                    .append(formatNumberCol(decimalFormat.format(position.getNav())))
                    .append("\n");
        }
        // footer
        int totalWidth = 100;
        String string = decimalFormat.format(portfolioDetail.getTotalNav());
        String title = "## Total portfolio";
        sw.append("\n").append(title).append(Strings.padStart(string, totalWidth - title.length(), ' '));
        sw.append("\n\n");
        System.out.println(sw);
        logger.info("total length: {}", sw.length());
    }

    private String formatNumberCol(String text) {
        return Strings.padStart(text, 20, ' ');
    }

    private String formatSymbol(String text) {
        return Strings.padEnd(text, 40, ' ');
    }

    public String formatNumeric(double value) {
        return decimalFormat.format(value);
    }
}
