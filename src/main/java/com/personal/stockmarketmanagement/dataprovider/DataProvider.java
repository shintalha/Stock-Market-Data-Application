package com.personal.stockmarketmanagement.dataprovider;

import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.model.entity.Market;

import java.util.List;
import java.util.Optional;

public interface DataProvider {
    public Optional<Instrument> getInstrumentBySymbol(String symbol);
    public Optional<List<Market>> getMarkets();
}
