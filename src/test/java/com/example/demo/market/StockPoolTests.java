/* (C) 2024 */ 

package com.example.demo.market;

import com.example.demo.market.producer.StockPool;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StockPoolTests {

    @Autowired
    StockPool stockPool;

    @Test
    void test_on_randoms() {
        for (int i = 0; i < 10; i++) {
            List<String> randoms = stockPool.randoms();
            System.out.println(randoms);
        }
    }
}
