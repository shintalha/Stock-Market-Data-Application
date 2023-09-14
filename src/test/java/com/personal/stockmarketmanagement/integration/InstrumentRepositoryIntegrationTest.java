package com.personal.stockmarketmanagement.integration;

import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.repository.InstrumentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
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
            .withInitScript("scripts/create-tables.sql");
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
    @Sql("/scripts/seed-instrument.sql")
    @Transactional
    public void testFindAllInstrumentData() {
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertNotNull(instrumentList);
        assertFalse(instrumentList.isEmpty());
        assertEquals(5, instrumentList.size());
    }
}
