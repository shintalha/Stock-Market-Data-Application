package com.personal.stockmarketmanagement.integration;

import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.repository.InstrumentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InstrumentRepositoryIntegrationTest {
    @ServiceConnection
    protected static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("talha")
            .withPassword("postgres")
            .withInitScript("scripts/init-all.sql");
    static {
        postgreSQLContainer.start();
    }
    @Autowired
    InstrumentRepository instrumentRepository;

    /**
     * case : get all instrument data
     * condition : size of returned instrument list should be 5
     */
    @Test
    @Transactional
    public void testFindAllInstrumentData() {
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertNotNull(instrumentList);
        assertFalse(instrumentList.isEmpty());
        assertEquals(5, instrumentList.size());
    }

    /**
     * case: update entity successfully
     */
    @Test
    @Transactional
    public void testUpdateInstrumentData() {
        Long instrumentId = 2L;
        String name = "Boeing Company";
        String custom_name = "Boeing";
        int market_id = 2;

        instrumentRepository.updateInstrumentData(name, custom_name, market_id, instrumentId);

        Instrument updatedInstrument = instrumentRepository.findById(2L).orElse(null);

        assertEquals(name, updatedInstrument.getFullName());
        assertEquals(custom_name, updatedInstrument.getSimpleName());
        assertEquals(market_id, updatedInstrument.getMarket().getId());

    }

    /**
     * case : find existing instrument data by symbol
     * condition : if method gets the relevant instrument data
     */
    @Test
    public void testFindInstrumentBySymbolByExistingData() {
        String symbol = "AAPL";
        String customName = "Apple";
        Instrument instrument = instrumentRepository.findInstrumentBySymbol(symbol);
        assertNotNull(instrument);
        assertEquals(customName, instrument.getSimpleName());
    };

    /**
     * case : try to find non-existing instrument data by symbol
     * condition : if method returns null
     */
    @Test
    public void testFindInstrumentBySymbolByNonExistingData() {
        String symbol = "test";
        Instrument instrument = instrumentRepository.findInstrumentBySymbol(symbol);
        assertNull(instrument);
    };
}
