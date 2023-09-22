package com.personal.stockmarketmanagement.integration;

import com.personal.stockmarketmanagement.model.constant.ResponseStatus;
import com.personal.stockmarketmanagement.model.contracts.controller.ControllerResponse;
import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.repository.InstrumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InstrumentControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @ServiceConnection
    protected static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("talha")
            .withPassword("postgres")
            .withInitScript("./scripts/create-tables.sql");
    static {
        postgreSQLContainer.start();
    }

    /**
     * case : sync instrument data with success
     * condition : returns HttpStatus Ok and synced instrument data can be found in db
     */
    @Test
    @Sql("/scripts/seed-instrument.sql")
    public void testSyncInstrumentDataWithSuccess() {
        ResponseEntity<ControllerResponse> response = restTemplate.getForEntity("/api/instruments/sync", ControllerResponse.class);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());

        String instrumentSymbol = "BA";
        Integer instrumentMarketId = 2;
        String instrumentName = "Boeing Company";
        String instrumentCustomName = "Boeing";
        long instrumentId = 2;


        Instrument syncedInstrument = instrumentRepository.findById(instrumentId).orElse(null);

        assertNotNull(syncedInstrument);
        assertEquals(instrumentSymbol, syncedInstrument.getSymbol());
        assertEquals(instrumentName, syncedInstrument.getFullName());
        assertEquals(instrumentMarketId, syncedInstrument.getMarket().getId());
        assertEquals(instrumentCustomName, syncedInstrument.getSimpleName());
    }
}