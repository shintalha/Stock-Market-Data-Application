package com.personal.stockmarketmanagement.integration;

import com.personal.stockmarketmanagement.model.constant.ResponseStatus;
import com.personal.stockmarketmanagement.model.contracts.controller.ControllerResponse;
import com.personal.stockmarketmanagement.model.entity.Market;
import com.personal.stockmarketmanagement.repository.MarketRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MarketControllerIntegrationTest {

    @ServiceConnection
    protected static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("talha")
            .withPassword("postgres")
            .withInitScript("./scripts/create-tables.sql");
    static {
        postgreSQLContainer.start();
    }
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MarketRepository marketRepository;

    @Test
    public void testSyncMarketDataWithSuccess() {
        // Perform a GET request to the /api/markets/sync endpoint
        ResponseEntity<ControllerResponse> response = restTemplate.getForEntity("/api/markets/sync", ControllerResponse.class);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());

        List<Market> markets = marketRepository.findAll();

        assertFalse(markets.isEmpty());
    }
}
