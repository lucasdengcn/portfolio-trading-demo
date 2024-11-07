/* (C) 2024 */ 

package com.example.demo.portfolio.repository;

import com.example.demo.portfolio.entity.PositionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<PositionEntity, Long> {

    Optional<PositionEntity> findBySymbol(String symbol);

    int countBySymbolType(int symbolType);

    List<PositionEntity> findBySymbolType(int symbolType);
}
