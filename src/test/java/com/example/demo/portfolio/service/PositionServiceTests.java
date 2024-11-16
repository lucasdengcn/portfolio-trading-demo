/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.service;

import com.example.demo.market.option.OptionManager;
import com.example.demo.market.stock.StockPool;
import com.example.demo.model.Option;
import com.example.demo.model.Position;
import com.example.demo.model.Stock;
import com.example.demo.model.SymbolType;
import com.example.demo.portfolio.entity.PositionEntity;
import com.google.common.collect.Lists;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PositionServiceTests {

    @Autowired
    private PositionService positionService;

    @Autowired
    private OptionManager optionManager;

    @Autowired
    private StockPool stockPool;

    @BeforeEach
    void setup() {
        positionService.deleteAll();
        optionManager.clear();
    }

    @Test
    void save_positions() {
        Stock stock = Stock.builder()
                .symbol("A")
                .expectedReturn(110.0f)
                .deviation(0.12)
                .build();
        stockPool.registerPrice(stock);
        //
        Option option = Option.builder()
                .symbol("A-100-C")
                .maturity(1)
                .symbolType(SymbolType.CALL)
                .strikePrice(80.0f)
                .build();
        optionManager.register(option);
        option = Option.builder()
                .symbol("A-100-P")
                .maturity(1)
                .symbolType(SymbolType.PUT)
                .strikePrice(105.0f)
                .build();
        optionManager.register(option);
        //
        PositionEntity positionEntity = new PositionEntity("A", 1, SymbolType.STOCK, "");
        PositionEntity positionEntity2 = new PositionEntity("A-100-P", -1, SymbolType.PUT, "A");
        PositionEntity positionEntity3 = new PositionEntity("A-100-C", 1, SymbolType.CALL, "A");
        positionService.save(Lists.newArrayList(positionEntity, positionEntity2, positionEntity3));
        //
        Assertions.assertEquals(3, positionService.count());
    }

    @Test
    void update_nav_on_stock_price_change() {
        save_positions();
        //
        String symbol = "A";
        positionService.updateNavOnPriceChange(symbol, 100.0f);
        //
        Position position = positionService.findBySymbol(symbol);
        Assertions.assertEquals(1, position.getQty());
        Assertions.assertEquals(100.0f, position.getNav());
        Assertions.assertEquals(100.0f, position.getPrice());
    }

    @Test
    void test_sum_of_nav() {
        Position position = Position.builder().nav(1.0f).build();
        List<Position> positionList = Lists.newArrayList(position, position, position);
        double sumOfNav = positionService.calculatePositionNav(positionList);
        Assertions.assertEquals(3.0f, sumOfNav);
    }
}
