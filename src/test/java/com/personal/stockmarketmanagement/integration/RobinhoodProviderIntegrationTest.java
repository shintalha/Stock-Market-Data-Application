package com.personal.stockmarketmanagement.integration;

import com.personal.stockmarketmanagement.dataprovider.RobinhoodProvider;
import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.model.entity.Market;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RestClientTest(RobinhoodProvider.class)
@RunWith(SpringRunner.class)
public class RobinhoodProviderIntegrationTest {
    @Autowired
    RobinhoodProvider robinhoodProvider;

    /**
     * case : should return proper instrument with given symbol
     * condition : returned value is not null and has custom name
     */
    @Test
    public void testGetInstrumentBySymbolWithSuccess() {
        Optional<Instrument> robinhoodInstrument = robinhoodProvider.getInstrumentBySymbol("AAPL");
        assertTrue(robinhoodInstrument.isPresent());
        assertEquals("Apple", robinhoodInstrument.get().getSimpleName());
    }

    /**
     * case : should return null with given symbol
     * condition : returned value is null
     */
    @Test
    public void testGetInstrumentBySymbolWithReturnedNull() {
        Optional<Instrument> robinhoodInstrument = robinhoodProvider.getInstrumentBySymbol("TEST");
        assertTrue(robinhoodInstrument.isEmpty());
    }

    /**
     * case : should return all markets
     * condition : returned market list is not null and not empty
     */
    @Test
    public void testGetMarketsWithSuccess() {
        Optional<List<Market>> robinhoodMarketList = robinhoodProvider.getMarkets();
        assertTrue(robinhoodMarketList.isPresent());
        assertFalse(robinhoodMarketList.get().isEmpty());
    }
}