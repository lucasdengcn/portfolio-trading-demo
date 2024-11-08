/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.repository;

import com.example.demo.market.entity.StockEntity;
import com.example.demo.portfolio.entity.PositionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends CrudRepository<StockEntity, Long> {

    Optional<StockEntity> findBySymbol(String symbol);
}
