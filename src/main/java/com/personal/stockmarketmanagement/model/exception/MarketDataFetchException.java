package com.personal.stockmarketmanagement.model.exception;

import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.MARKET_DATA_FETCH_ERROR;

public class MarketDataFetchException extends Exception {
    public MarketDataFetchException() {
        super(MARKET_DATA_FETCH_ERROR);
    }
}
