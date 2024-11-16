/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.repository;

import com.example.demo.market.entity.StockEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {

    Optional<StockEntity> findBySymbol(String symbol);
}
