/* (C) 2024 */ 

package com.example.demo.portfolio.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.entity.ProductType;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PositionRepositoryTests {

    @Autowired
    private PositionRepository positionRepository;

    @BeforeEach
    void setup() {
        positionRepository.deleteAll();
    }

    @Test
    void test_on_create_position() {
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setSymbol("A1");
        positionEntity.setPositionSize(10);
        positionEntity.setType(ProductType.STOCK);
        //
        positionRepository.save(positionEntity);
        Assertions.assertNotNull(positionEntity.getId());
        Assertions.assertTrue(positionEntity.getId() > 0);
        //
        Optional<PositionEntity> optionalPosition = positionRepository.findBySymbol("A1");
        Assertions.assertNotNull(optionalPosition.orElse(null));
    }

    @Test
    void test_on_count_of_type_after_create() {
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setSymbol("A2");
        positionEntity.setPositionSize(10);
        positionEntity.setType(ProductType.STOCK);
        //
        positionRepository.save(positionEntity);
        Assertions.assertNotNull(positionEntity.getId());
        Assertions.assertTrue(positionEntity.getId() > 0);
        //
        int count = positionRepository.countByType(ProductType.STOCK);
        Assertions.assertEquals(1, count);
    }

    @Test
    void test_on_count_of_type_empty() {
        //
        int count = positionRepository.countByType(ProductType.STOCK);
        Assertions.assertEquals(0, count);
    }

    @Test
    void test_find_by_symbol_empty() {
        //
        Optional<PositionEntity> optionalPosition = positionRepository.findBySymbol("FAKE");
        Assertions.assertFalse(optionalPosition.isPresent());
    }

    @Test
    void test_find_by_type_stock_records() {
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setSymbol("A");
        positionEntity.setPositionSize(10);
        positionEntity.setType(ProductType.STOCK);
        //
        positionRepository.save(positionEntity);
        Assertions.assertNotNull(positionEntity.getId());
        Assertions.assertTrue(positionEntity.getId() > 0);
        //
        List<PositionEntity> byType = positionRepository.findByType(ProductType.STOCK);
        Assertions.assertEquals(1, byType.size());
        //
        byType.forEach(new Consumer<PositionEntity>() {
            @Override
            public void accept(PositionEntity item) {
                Assertions.assertEquals(positionEntity.getId(), item.getId());
                Assertions.assertEquals(positionEntity.getType(), item.getType());
                assertEquals(0, positionEntity.getPositionSize().compareTo(item.getPositionSize()));
                Assertions.assertEquals(positionEntity.getSymbol(), item.getSymbol());
            }
        });
    }
}
