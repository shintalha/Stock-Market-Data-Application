package com.personal.stockmarketmanagement.repository;

import com.personal.stockmarketmanagement.model.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<Market, Integer> {
}