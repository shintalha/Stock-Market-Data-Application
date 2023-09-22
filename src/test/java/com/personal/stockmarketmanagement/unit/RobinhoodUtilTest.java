package com.personal.stockmarketmanagement.unit;

import com.personal.stockmarketmanagement.model.dto.RobinhoodInstrumentDto;
import com.personal.stockmarketmanagement.model.dto.RobinhoodMarketDto;
import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.model.entity.Market;
import com.personal.stockmarketmanagement.utility.RobinhoodUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobinhoodUtilTest {

    /**
     * case : convert robinhoodInstrumentDto to instrument with market object
     * condition : if method succeed, Instrument object have market object and other properties
     */
    @Test
    public void testConvertRobinhoodInstrumentDtoToInstrumentWithValidRobinhoodInstrumentDto() {
        //Arrange
        RobinhoodInstrumentDto robinhoodInstrumentDto = RobinhoodInstrumentDto.builder()
                .name("Apple Inc.")
                .symbol("AAPL")
                .simple_name("Apple")
                .market("https://api.robinhood.com/markets/XNAS/") // Assuming it's a valid market URL
                .build();

        //Act
        Instrument instrument = RobinhoodUtil.convertRobinhoodInstrumentDtoToInstrument(robinhoodInstrumentDto);

        // Assert
        assertEquals("Apple Inc.", instrument.getFullName());
        assertEquals("AAPL", instrument.getSymbol());
        assertEquals("Apple", instrument.getSimpleName());
        assertEquals("XNAS", instrument.getMarket().getCode());
    }

    /**
     * case : map robinhoodMarketDto list with 2 marketDto objects to market list
     * condition : if method succeed, market object have proper data according to robinhoodMarketDto
     */
    @Test
    public void testMapRobinhoodMarketDtoListToMarketListWithValidRobinhoodMarketDtoList() {
        //Arrange
        RobinhoodMarketDto robinhoodMarketDto1 = RobinhoodMarketDto.builder()
                .name("New York Stock Exchange")
                .mic("XNYS")
                .acronym("NYSE")
                .country("USA")
                .website("www.nyse.com")
                .build();

        RobinhoodMarketDto robinhoodMarketDto2 = RobinhoodMarketDto.builder()
                .name("London Stock Exchange")
                .mic("XLON")
                .acronym("LSE")
                .country("UK")
                .website("www.londonstockexchange.com")
                .build();

        List<RobinhoodMarketDto> robinhoodMarketDtoList = new ArrayList<>();
        robinhoodMarketDtoList.add(robinhoodMarketDto1);
        robinhoodMarketDtoList.add(robinhoodMarketDto2);

        //Act
        List<Market> marketList = RobinhoodUtil.mapRobinhoodMarketDtoListToMarketList(robinhoodMarketDtoList);

        //Assert
        assertEquals(2, marketList.size());

        Market market1 = marketList.get(0);
        assertEquals("New York Stock Exchange", market1.getName());
        assertEquals("XNYS", market1.getCode());
        assertEquals("NYSE", market1.getSymbol());
        assertEquals("USA", market1.getCountry());
        assertEquals("www.nyse.com", market1.getWebsite());

        Market market2 = marketList.get(1);
        assertEquals("London Stock Exchange", market2.getName());
        assertEquals("XLON", market2.getCode());
        assertEquals("LSE", market2.getSymbol());
        assertEquals("UK", market2.getCountry());
        assertEquals("www.londonstockexchange.com", market2.getWebsite());
    }

}
