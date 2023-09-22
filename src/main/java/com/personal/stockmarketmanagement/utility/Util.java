package com.personal.stockmarketmanagement.utility;

import com.personal.stockmarketmanagement.model.dto.InstrumentDto;
import com.personal.stockmarketmanagement.model.entity.Instrument;

import java.util.List;

public final class Util {

    /**
     * This method takes instrument list with datas have market object and converts it to the instrument dto list
     * @param instrumentList
     * @return instrumentDto list without having market object
     */
    public static List<InstrumentDto> mapInstrumentEntityListToDtoList(List<Instrument> instrumentList) {
        return instrumentList.stream().map(instrument ->
                convertInstrumentEntityToDto(instrument)
        ).toList();
    }

    public static InstrumentDto convertInstrumentEntityToDto(Instrument instrument) {
        Integer marketId = null;
        if (instrument.getMarket() != null)
            marketId = instrument.getMarket().getId();
        return InstrumentDto.builder()
                .id(instrument.getId())
                .symbol(instrument.getSymbol())
                .simpleName(instrument.getSimpleName())
                .fullName(instrument.getFullName())
                .marketId(marketId)
                .build();
    }

}
