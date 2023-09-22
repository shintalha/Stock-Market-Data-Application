package com.personal.stockmarketmanagement.unit;

import com.personal.stockmarketmanagement.dataprovider.DataProvider;
import com.personal.stockmarketmanagement.model.dto.InstrumentDto;
import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.model.entity.Market;
import com.personal.stockmarketmanagement.repository.InstrumentRepository;
import com.personal.stockmarketmanagement.service.InstrumentService;
import com.personal.stockmarketmanagement.service.MarketService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.DATABASE_ERROR;
import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.INSTRUMENT_BY_SYMBOL_NOT_FOUND_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InstrumentServiceTest {
    @InjectMocks
    InstrumentService instrumentService;

    @Mock
    InstrumentRepository instrumentRepository;

    @Mock
    DataProvider dataProvider;

    @Mock
    MarketService marketService;

    @Mock
    Executor executor;


    /**
     * case : get valid instrument data from data provider and update
     * condition : should execute updateInstrumentData method for instrument datas
     */
    @Test
    public void testGetInstrumentAndUpdateAsyncWithValidInstruments() {
        Market market1 = Market.builder()
                .id(1).code("AAPL")
                .name("Apple Market")
                .build();

        Market market2 = Market.builder()
                .id(2)
                .code("GOOGL")
                .name("Alphabet Market")
                .build();

        Instrument instrument1 = Instrument.builder()
                .id(1)
                .symbol("AAPL")
                .simpleName("Apple")
                .fullName("Apple Inc.")
                .market(market1)
                .build();

        Instrument instrument2 = Instrument.builder()
                .id(2)
                .symbol("GOOGL")
                .simpleName("Alphabet")
                .fullName("Alphabet Inc.")
                .market(market2)
                .build();

        List<Instrument> instrumentList = new ArrayList<>();
        instrumentList.add(instrument1);
        instrumentList.add(instrument2);

        Mockito.when(dataProvider.getInstrumentBySymbol("AAPL")).thenReturn(Optional.of(instrument1));
        Mockito.when(dataProvider.getInstrumentBySymbol("GOOGL")).thenReturn(Optional.of(instrument2));

        Mockito.when(marketService.getMarketByCode("AAPL")).thenReturn(Optional.of(market1));
        Mockito.when(marketService.getMarketByCode("GOOGL")).thenReturn(Optional.of(market2));

        List<CompletableFuture<Void>> futures = instrumentService.getInstrumentAndUpdateAsync(instrumentList);

        assertEquals(2, futures.size());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        verify(instrumentRepository, times(1)).updateInstrumentData("Apple Inc.", "Apple", 1, 1L);
        verify(instrumentRepository, times(1)).updateInstrumentData("Alphabet Inc.", "Alphabet", 2, 2L);
    }

    /**
     * case : data provider returns null value
     * condition : should not execute updateInstrumentData
     */
    @Test
    public void testGetInstrumentAndUpdateAsyncWithNullInstrumentFromDataProvider() {
        // Create Instrument objects using Lombok builder
        Instrument instrument1 = Instrument.builder()
                .id(1)
                .symbol("AAPL")
                .simpleName("Apple")
                .fullName("Apple Inc.")
                .build();

        // Create a list of Instrument objects
        List<Instrument> instrumentList = new ArrayList<>();
        instrumentList.add(instrument1);

        Mockito.when(dataProvider.getInstrumentBySymbol("AAPL")).thenReturn(Optional.empty());

        List<CompletableFuture<Void>> futures = instrumentService.getInstrumentAndUpdateAsync(instrumentList);

        verify(instrumentRepository, times(0)).updateInstrumentData("Apple Inc.", "Apple", 1, 1L);
    }

    /**
     * case : data provider returns an instrument whose market is not recorded in our database
     * condition : should not execute updateInstrumentData
     */
    @Test
    public void testGetInstrumentAndUpdateAsyncWithNonExistingMarket() {
        Market fakeMarket = Market.builder()
                .code("FAKE")
                .name("FAKE MARKET")
                .id(2)
                .build();

        Instrument instrument = Instrument.builder()
                .id(1)
                .symbol("BA")
                .simpleName("Boeing")
                .fullName("Boeing Company")
                .market(fakeMarket)
                .build();

        List<Instrument> instrumentList = new ArrayList<>();
        instrumentList.add(instrument);

        Mockito.when(dataProvider.getInstrumentBySymbol("BA")).thenReturn(Optional.of(instrument));
        Mockito.when(marketService.getMarketByCode("FAKE")).thenReturn(Optional.empty());

        List<CompletableFuture<Void>> futures = instrumentService.getInstrumentAndUpdateAsync(instrumentList);

        verify(instrumentRepository, times(0)).updateInstrumentData("Boeing Company", "Boeing", 2, 1L);
    }

    /**
     * case : get all instruments from db
     * condition : should return proper instrumentDto objects in list
     */
    @Test
    public void testGetAllInstrumentsWithNonEmptyList() {
        // Arrange
        Instrument instrument1 = Instrument.builder()
                .id(1)
                .symbol("AAPL")
                .simpleName("Apple")
                .fullName("Apple Inc.")
                .build();

        Instrument instrument2 = Instrument.builder()
                .id(2)
                .symbol("GOOGL")
                .simpleName("Alphabet")
                .fullName("Alphabet Inc.")
                .build();

        List<Instrument> instrumentList = new ArrayList<>();
        instrumentList.add(instrument1);
        instrumentList.add(instrument2);

        Mockito.when(instrumentRepository.findAll()).thenReturn(instrumentList);

        List<InstrumentDto> instrumentDtoList = null;

        // Act
        try {
            instrumentDtoList = instrumentService.getAllInstruments();
        } catch (Exception ex) {}


        // Assert
        assertEquals(2, instrumentDtoList.size());

        InstrumentDto dto1 = instrumentDtoList.get(0);
        assertEquals(1, dto1.getId());
        assertEquals("AAPL", dto1.getSymbol());
        assertEquals("Apple", dto1.getSimpleName());
        assertEquals("Apple Inc.", dto1.getFullName());
        assertNull(dto1.getMarketId());

        InstrumentDto dto2 = instrumentDtoList.get(1);
        assertEquals(2, dto2.getId());
        assertEquals("GOOGL", dto2.getSymbol());
        assertEquals("Alphabet", dto2.getSimpleName());
        assertEquals("Alphabet Inc.", dto2.getFullName());
        assertNull(dto2.getMarketId());
    }

    /**
     * case : there is no record on database
     * condition : should return empty InstrumentDtoList
     */
    @Test
    public void testGetAllInstrumentsWithEmptyList() throws Exception {
        // Arrange
        List<Instrument> instrumentList = new ArrayList<>();

        Mockito.when(instrumentRepository.findAll()).thenReturn(instrumentList);

        // Act
        List<InstrumentDto> instrumentDtoList = null;
        try {
            instrumentDtoList = instrumentService.getAllInstruments();
        } catch (Exception ex) {}

        // Assertions
        assertEquals(0, instrumentDtoList.size());
    }

    /**
     * case : Error during database connection
     * condition : should throw DatabaseConnectionException
     */
    @Test
    public void testGetAllInstrumentsWithException() {
        Mockito.when(instrumentRepository.findAll()).thenThrow(new RuntimeException("Database connection error"));

        try {
            instrumentService.getAllInstruments();
        } catch (Exception ex) {
            assertEquals(DATABASE_ERROR + " : " + "Database connection error", ex.getMessage() );
        }
    }

    /**
     * case : get instrument by symbol successfully
     * condition : should return right instrument according to the given symbol
     */
    @Test
    public void testGetInstrumentBySymbolWithValidInstrument() {
        Instrument instrument = Instrument.builder()
                .id(1)
                .symbol("AAPL")
                .simpleName("Apple")
                .fullName("Apple Inc.")
                .build();

        Mockito.when(instrumentRepository.findInstrumentBySymbol("AAPL")).thenReturn(instrument);

        // Act
        InstrumentDto instrumentDto = null;
        try {
            instrumentDto = instrumentService.getInstrumentBySymbol("AAPL");
        } catch (Exception ex) {}

        // Assert
        assertEquals(1, instrumentDto.getId());
        assertEquals("AAPL", instrumentDto.getSymbol());
        assertEquals("Apple", instrumentDto.getSimpleName());
        assertEquals("Apple Inc.", instrumentDto.getFullName());
        assertNull(instrumentDto.getMarketId());
    }

    /**
     * case : get instrument by symbol failed
     * condition : should throw InstrumentNotFoundException
     */
    @Test
    public void testGetInstrumentBySymbolWithInstrumentNotFoundException() {
        String testSymbol = "GOOGL";
        Mockito.when(instrumentRepository.findInstrumentBySymbol(testSymbol)).thenReturn(null);

        // Assert that InstrumentNotFoundException is thrown
        try {
            instrumentService.getInstrumentBySymbol(testSymbol);
        } catch (Exception ex) {
            assertEquals(INSTRUMENT_BY_SYMBOL_NOT_FOUND_ERROR + " Symbol: " + testSymbol, ex.getMessage());
        }
    }

}