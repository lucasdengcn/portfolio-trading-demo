/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.repository;

import com.example.demo.market.entity.OptionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionRepository extends CrudRepository<OptionEntity, Long> {

    Optional<OptionEntity> findBySymbol(String symbol);
}
