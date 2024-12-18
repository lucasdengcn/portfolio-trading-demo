/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo;

import com.example.demo.loader.CSVDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private CSVDataLoader csvDataLoader;

    @Override
    public void run(String... args) throws Exception {
        csvDataLoader.loadAll();
    }
}
