package com.personal.stockmarketmanagement.utility;

import com.personal.stockmarketmanagement.model.dto.RobinhoodInstrumentDto;
import com.personal.stockmarketmanagement.model.dto.RobinhoodMarketDto;
import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.model.entity.Market;

import java.util.List;

public final class RobinhoodUtil {

    /**
     * This method takes robinhood instrument dto list with datas have market object and converts it to the instrument entity list
     * @param robinhoodInstrumentDto
     * @return instrument entity list
     */
    public static Instrument convertRobinhoodInstrumentDtoToInstrument(RobinhoodInstrumentDto robinhoodInstrumentDto) {
        return Instrument.builder()
                .simpleName(robinhoodInstrumentDto.getSimple_name())
                .fullName(robinhoodInstrumentDto.getName())
                .symbol(robinhoodInstrumentDto.getSymbol())
                .market(
                        Market.builder()
                                .code(RobinhoodUtil.getMarketCodeFromMarketUrl(
                                        robinhoodInstrumentDto.getMarket()
                                ))
                                .build()
                )
                .build();
    }

    /**
     * This method takes robinhood market dto list and converts it to the market entity list
     * @param robinhoodMarketDtoList
     * @return market entity list
     */

    public static List<Market> mapRobinhoodMarketDtoListToMarketList(List<RobinhoodMarketDto> robinhoodMarketDtoList) {
        return robinhoodMarketDtoList.stream().map(robinhoodMarketDto ->
                Market.builder()
                        .name(robinhoodMarketDto.getName())
                        .symbol(robinhoodMarketDto.getAcronym())
                        .code(robinhoodMarketDto.getMic())
                        .country(robinhoodMarketDto.getCountry())
                        .website(robinhoodMarketDto.getWebsite())
                        .build()
        ).toList();
    }

    /**
     * * pick market code from marketUrl
     * @param url https://api.robinhood.com/markets/{marketCode}/
     * @return marketCode
     */
    public static String getMarketCodeFromMarketUrl(String url) {
        return url.substring(url.length() - 5, url.length() - 1);
    }
}
