package com.example.demo.portfolio.repository;

import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.entity.ProductType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends CrudRepository<PositionEntity, Long> {

    Optional<PositionEntity> findBySymbol(String symbol);

    int countByType(ProductType type);

    List<PositionEntity> findByType(ProductType type);
}
