package com.personal.stockmarketmanagement.repository;

import com.personal.stockmarketmanagement.model.entity.Instrument;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE instrument SET name = :name, custom_name = :customName, market_id = :marketId \n" +
            "WHERE id = :id", nativeQuery = true)
    void updateInstrumentData(@Param("name") String name,
                              @Param("customName") String customName,
                              @Param("marketId") int marketId,
                              @Param("id") Long id);
}
