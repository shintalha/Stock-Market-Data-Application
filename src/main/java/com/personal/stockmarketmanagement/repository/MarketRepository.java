package com.personal.stockmarketmanagement.repository;

import com.personal.stockmarketmanagement.model.entity.Market;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<Market, Integer> {
    @Query("SELECT m.id FROM Market m WHERE m.code = :code")
    Optional<Integer> getMarketIdByCode(@Param("code") String code);
}