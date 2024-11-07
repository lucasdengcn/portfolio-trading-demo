/* (C) 2024 */ 

package com.example.demo.portfolio.dashboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ConsoleDashboardTests {

    @Autowired
    private ConsoleDashboard consoleDashboard;

    @Test
    void test_number_format_00() {
        String s = consoleDashboard.formatNumeric(1.0);
        Assertions.assertEquals("1.00", s);
    }

    @Test
    void test_number_format_01() {
        String s = consoleDashboard.formatNumeric(1.02);
        Assertions.assertEquals("1.02", s);
    }

    @Test
    void test_number_format_02() {
        String s = consoleDashboard.formatNumeric(1.023);
        Assertions.assertEquals("1.02", s);
    }

    @Test
    void test_number_format_sep_tail() {
        String s = consoleDashboard.formatNumeric(1000.023);
        Assertions.assertEquals("1,000.02", s);
    }

    @Test
    void test_number_format_sep_normal() {
        String s = consoleDashboard.formatNumeric(1000.0);
        Assertions.assertEquals("1,000.00", s);
    }

    @Test
    void test_number_format_sep_long() {
        String s = consoleDashboard.formatNumeric(123000.0);
        Assertions.assertEquals("123,000.00", s);
    }

    @Test
    void test_number_format_sep_longest() {
        String s = consoleDashboard.formatNumeric(123123000.123);
        Assertions.assertEquals("123,123,000.12", s);
    }
}
