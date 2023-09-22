package com.personal.stockmarketmanagement.integration;

import com.personal.stockmarketmanagement.model.constant.ResponseStatus;
import com.personal.stockmarketmanagement.model.contracts.controller.ControllerResponse;
import com.personal.stockmarketmanagement.model.dto.InstrumentDto;
import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.repository.InstrumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.CacheManager;
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

    @Autowired
    private CacheManager cacheManager;

    @ServiceConnection
    protected static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("talha")
            .withPassword("postgres")
            .withInitScript("./scripts/init-all.sql");
    static {
        postgreSQLContainer.start();
    }

    /**
     * case : sync instrument data with success
     * condition : returns HttpStatus Ok and synced instrument data can be found in db
     */
    @Test
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

    /**
     * case : sync instrument data with success
     * condition : returns HttpStatus Ok and synced instrument data in db is proper
     */
    @Test
    public void testGetAllInstrumentsWithSuccess() {
        ResponseEntity<ControllerResponse> response = restTemplate.getForEntity("/api/instruments", ControllerResponse.class);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());

        ObjectMapper mapper = new ObjectMapper();
        List<InstrumentDto> returnedInstrumentList = mapper.convertValue(response.getBody().getData().get(0), List.class);

        assertFalse(returnedInstrumentList.isEmpty());
    }

    /**
     * case : required Instrument returned with success and cached
     * condition : returns HttpStatus Ok and relevant InstrumentDto, cache value with key of symbol is not null
     */
    @Test
    public void testGetInstrumentBySymbolWithSuccess() {
        ResponseEntity<ControllerResponse> response = restTemplate.getForEntity("/api/instruments/BA", ControllerResponse.class);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());

        String instrumentSymbol = "BA";
        long instrumentId = 2;


        ObjectMapper mapper = new ObjectMapper();
        InstrumentDto returnedInstrument = mapper.convertValue(response.getBody().getData().get(0), InstrumentDto.class);

        InstrumentDto cachedInstrument = cacheManager.getCache("instrument").get("BA", InstrumentDto.class);

        assertEquals(instrumentSymbol, returnedInstrument.getSymbol());
        assertEquals(instrumentId, returnedInstrument.getId());
        assertNotNull(cachedInstrument);
        assertEquals(instrumentSymbol, cachedInstrument.getSymbol());
        assertEquals(instrumentId, cachedInstrument.getId());

    }

    /**
     * case : get instrument data by symbol with error
     * condition : returns HttpStatus INTERNAL_SERVER_ERROR
     */
    @Test
    public void testGetInstrumentBySymbolWithError() {
        ResponseEntity<ControllerResponse> response = restTemplate.getForEntity("/api/instruments/TEST", ControllerResponse.class);

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ResponseStatus.ERROR, response.getBody().getStatus());

    }

    /**
     * case : get instrument by symbol and sync instrument datas
     * condition : cached instrument should be deleted from cache after sync instrument executed
     */
    @Test
    public void testSyncInstrumentDataWithCachedInstrument() {
        ResponseEntity<ControllerResponse> getInstrumentBySymbolResponse = restTemplate.getForEntity("/api/instruments/BA", ControllerResponse.class);
        assertEquals(HttpStatus.OK, getInstrumentBySymbolResponse.getStatusCode());
        assertEquals(ResponseStatus.SUCCESS, getInstrumentBySymbolResponse.getBody().getStatus());

        InstrumentDto cachedInstrument = cacheManager.getCache("instrument").get("BA", InstrumentDto.class);
        assertNotNull(cachedInstrument);

        ResponseEntity<ControllerResponse> response = restTemplate.getForEntity("/api/instruments/sync", ControllerResponse.class);
        cachedInstrument = cacheManager.getCache("instrument").get("BA", InstrumentDto.class);

        assertNull(cachedInstrument);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }


}