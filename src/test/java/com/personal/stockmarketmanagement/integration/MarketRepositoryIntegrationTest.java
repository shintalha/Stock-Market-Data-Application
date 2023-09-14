package com.personal.stockmarketmanagement.integration;

import com.personal.stockmarketmanagement.model.entity.Market;
import com.personal.stockmarketmanagement.repository.MarketRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MarketRepositoryIntegrationTest {
    @Autowired
    MarketRepository marketRepository;

    @ServiceConnection
    protected static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("talha")
            .withPassword("postgres")
            .withInitScript("scripts/create-tables.sql");
    static {
        postgreSQLContainer.start();
    }

    /**
     * case : save list of market to the database
     * condition : if succeeded, saved markets can be found
     */
    @Test
    @Transactional
    public void testSaveMarketData() {
        String marketCode = "marketCode";
        String marketName = "marketName";
        String marketSymbol = "marketSymbol";
        String marketCountry = "marketCountry";
        String marketWebsite = "marketWebsite";

        List<Market> marketList = new ArrayList<>();

        Market market = Market.builder()
                .code(marketCode)
                .symbol(marketSymbol)
                .name(marketName)
                .country(marketCountry)
                .website(marketWebsite)
                .build();

        marketList.add(market);

        List<Market> savedMarketList = marketRepository.saveAll(marketList);

        Market savedMarket = marketRepository.findById(savedMarketList.get(0).getId()).orElse(null);
        assertNotNull(savedMarket);
        assertEquals(marketSymbol, savedMarket.getSymbol());
    }

    /**
     * case : get all market data
     * condition : size of returned market list should be 3
     */
    @Test
    @Sql("/scripts/seed-market.sql")
    @Transactional
    public void testFindAllMarketData() {
        List<Market> marketList = marketRepository.findAll();
        assertNotNull(marketList);
        assertFalse(marketList.isEmpty());
        assertEquals(3, marketList.size());
    }
}