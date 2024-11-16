/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.repository;

import com.example.demo.model.SymbolType;
import com.example.demo.portfolio.entity.PositionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    Optional<PositionEntity> findBySymbol(String symbol);

    int countBySymbolType(SymbolType symbolType);

    List<PositionEntity> findBySymbolType(SymbolType symbolType);
}
