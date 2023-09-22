package com.personal.stockmarketmanagement.model.constant;

public final class ConstantParameter {
    public static final String MARKET_DATA_FETCH_ERROR = "An error occurred while retrieving market data from Robinhood.";
    public static final String MARKET_DATA_SAVE_ERROR = "Service failed to save market data to database.";
    public static final String MARKET_SYNC_SUCCESS = "Market data has been successfully updated.";
    public static final String MARKET_SYNC_ERROR = "Market data has been successfully updated.";
    public static final String INSTRUMENT_SYNC_SUCCESS = "Instrument data has been successfully updated.";
    public static final String INSTRUMENT_SYNC_ERROR = "Failed to update instrument.";
    public static final String DATABASE_ERROR = "There is a problem accessing the database for the relevant data.";
    public static final String GET_ALL_INSTRUMENTS_SUCCESS = "All instruments retrieved successfully";
    public static final String GET_ALL_INSTRUMENTS_ERROR = "Failed to retrieve instruments.";
    public static final String INSTRUMENT_BY_SYMBOL_NOT_FOUND_ERROR = "There is no record of instrument with the symbol given.";
    public static final String GET_INSTRUMENT_BY_SYMBOL_SUCCESS = "Instrument by given symbol retrieved successfully";
    public static final String GET_INSTRUMENT_BY_SYMBOL_ERROR = "Failed to retrieve instrument by given symbol.";
}
