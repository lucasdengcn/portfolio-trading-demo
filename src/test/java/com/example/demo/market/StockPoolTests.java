package com.example.demo.market;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StockPoolTests {

    @Autowired
    StockPool stockPool;

    @Test
    void test_on_randoms(){
        for (int i = 0; i < 10; i++) {
            List<String> randoms = stockPool.randoms();
            System.out.println(randoms);
        }
    }

}