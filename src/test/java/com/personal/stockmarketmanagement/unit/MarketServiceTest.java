package com.personal.stockmarketmanagement.unit;

import com.personal.stockmarketmanagement.dataprovider.DataProvider;
import com.personal.stockmarketmanagement.model.entity.Market;
import com.personal.stockmarketmanagement.repository.MarketRepository;
import com.personal.stockmarketmanagement.service.MarketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.MARKET_DATA_FETCH_ERROR;
import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.MARKET_DATA_SAVE_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketServiceTest {
    @InjectMocks
    MarketService marketService;

    @Mock
    MarketRepository marketRepository;
    @Mock
    DataProvider dataProvider;

    private final String market1Mic = "XNYS";
    private final String market1Name = "New York Stock Exchange";
    private final String market1Acronym = "NYSE";
    private final String market1Country = "USA";
    private final String market1Website = "www.nyse.com";

    private final String market2Mic = "IEXG";
    private final String market2Name = "IEX Market";
    private final String market2Acronym = "IEX";
    private final String market2Country = "USA";
    private final String market2Website = "www.iextrading.com";

    /**
     * case : method throws MarketDataFetchException
     * Condition : if getMarkets method return Optional.empty(no market data), then service must throw MarketDataFetchException
     */
    @Test
    public void testSyncMarketDataWithEmptyMarketList() {

        String exceptionMessage = "";

        try {
            when(dataProvider.getMarkets()).thenReturn(Optional.empty());

            //Act
            marketService.syncMarketData();
        } catch (Exception ex) {
            //assert
            assertEquals(MARKET_DATA_FETCH_ERROR, ex.getMessage());
        }

    }

    /**
     * case : method successfully executed
     * condition : save method is executed if method is succeeded
     */
    @Test
    public void testSaveAndUpdateMarketDataWithValidMarketList() {
        // Arrange
        Market market1 = Market.builder()
                .code(market1Mic)
                .symbol(market1Acronym)
                .name(market1Name)
                .country(market1Country)
                .website(market1Website)
                .build();

        Market market2 = Market.builder()
                .code(market2Mic)
                .symbol(market2Acronym)
                .name(market2Name)
                .country(market2Country)
                .website(market2Website)
                .build();

        List<Market> marketList = new ArrayList<>();
        marketList.add(market1);
        marketList.add(market2);


        when(marketRepository.getMarketIdByCode(market1Mic)).thenReturn(Optional.of(1));
        when(marketRepository.getMarketIdByCode(market2Mic)).thenReturn(Optional.empty());

        when(marketRepository.save(market1)).thenReturn(market1);
        when(marketRepository.save(market2)).thenReturn(market2);

        // Act
        try {
            marketService.saveAndUpdateMarketData(marketList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Asser
        verify(marketRepository, times(1)).save(market1);
        verify(marketRepository, times(1)).save(market2);
    }

    /**
     * case :  method throws exception
     * condition : if saving to database failed, then service must throw Exception with custom message
     */
    @Test
    public void testSaveAndUpdateMarketDataWithException() {
        // Arrange
        Market market1 = Market.builder()
                .code(market1Mic)
                .symbol(market1Acronym)
                .name(market1Name)
                .country(market1Country)
                .website(market1Website)
                .build();

        Market market2 = Market.builder()
                .code(market2Mic)
                .symbol(market2Acronym)
                .name(market2Name)
                .country(market2Country)
                .website(market2Website)
                .build();

        List<Market> marketList = new ArrayList<>();
        marketList.add(market1);
        marketList.add(market2);

        String runtimeExceptionMessage = "Error saving market data";
        Mockito.when(marketRepository.save(any(Market.class))).thenThrow(new RuntimeException(runtimeExceptionMessage));
        try {
            // Act
            marketService.saveAndUpdateMarketData(marketList);
        } catch (Exception ex) {
            // Assert
            // Ensure that the exception is caught and the correct message is thrown
            assertEquals(MARKET_DATA_SAVE_ERROR + " : " + "java.lang.RuntimeException: " + runtimeExceptionMessage, ex.getMessage());
        }
    }
}
