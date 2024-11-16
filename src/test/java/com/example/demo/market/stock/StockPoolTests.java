/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.stock;

import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StockPoolTests {

    @Autowired
    StockPool stockPool;

    @RepeatedTest(10)
    void test_on_randoms() {
        Set<String> randoms = stockPool.randoms();
        Assertions.assertFalse(randoms.isEmpty());
    }

    @RepeatedTest(10)
    void test_random_verify_element() {
        Set<String> randoms = stockPool.randoms();
        for (String s : randoms) {
            boolean contains = stockPool.contains(s);
            Assertions.assertTrue(contains);
        }
    }
}
