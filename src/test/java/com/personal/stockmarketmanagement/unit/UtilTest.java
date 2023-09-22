package com.personal.stockmarketmanagement.unit;

import com.personal.stockmarketmanagement.model.dto.InstrumentDto;
import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.model.entity.Market;
import com.personal.stockmarketmanagement.utility.Util;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UtilTest {

    /**
     * case : map non-empty instrument entity list with 2 objects to InstrumentDto list
     * condition : method should return list having 2 instrumentDto whose properties are proper
     */
    @Test
    public void testMapInstrumentEntityListToDtoListWithNonEmptyList() {
        // Arrange
        Instrument instrument1 = Instrument.builder()
                .id(1)
                .symbol("AAPL")
                .simpleName("Apple")
                .fullName("Apple Inc.")
                .build();

        List<Instrument> instrumentList = new ArrayList<>();
        instrumentList.add(instrument1);
        instrumentList.add(instrument1);

        // Act
        List<InstrumentDto> instrumentDtoList = Util.mapInstrumentEntityListToDtoList(instrumentList);

        // Assert
        assertEquals(2, instrumentDtoList.size());

        InstrumentDto dto1 = instrumentDtoList.get(0);
        assertEquals(1, dto1.getId());
        assertEquals("AAPL", dto1.getSymbol());
        assertEquals("Apple", dto1.getSimpleName());
        assertEquals("Apple Inc.", dto1.getFullName());
        assertNull(dto1.getMarketId());
    }

    /**
     * case : convert instrument entity with object to instrument dto
     * condition : function should return instrument which have proper marketId value
     */
    @Test
    public void testConvertInstrumentEntityToDtoWithMarketbject() {
        //Arrange
        Instrument instrument = Instrument.builder()
                .id(1)
                .symbol("AAPL")
                .simpleName("Apple")
                .fullName("Apple Inc.")
                .build();

        Market market = Market.builder()
                .id(1)
                .build();

        instrument.setMarket(market);

        //Act
        InstrumentDto dto = Util.convertInstrumentEntityToDto(instrument);

        //Assert
        assertEquals(1, dto.getId());
        assertEquals("AAPL", dto.getSymbol());
        assertEquals("Apple Inc.", dto.getFullName());
        assertEquals("Apple", dto.getSimpleName());
        assertEquals(1, dto.getMarketId());
    }

    /**
     * case : convert instrument entity without object to instrument dto
     * condition : function should return instrument which have null value for marketId field
     */
    @Test
    public void testConvertInstrumentEntityToDtoWithoutMarketObject() {
        //Arrange
        Instrument instrument = Instrument.builder()
                .id(1)
                .symbol("AAPL")
                .simpleName("Apple")
                .fullName("Apple Inc.")
                .build();

        //Act
        InstrumentDto dto = Util.convertInstrumentEntityToDto(instrument);

        //Assert
        assertEquals(1, dto.getId());
        assertEquals("AAPL", dto.getSymbol());
        assertEquals("Apple Inc.", dto.getFullName());
        assertEquals("Apple", dto.getSimpleName());
        assertNull(dto.getMarketId());
    }

}
