package com.personal.stockmarketmanagement.model.exception;

import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.MARKET_DATA_SAVE_ERROR;

public class MarketDataSaveException extends Exception {
    public MarketDataSaveException(String exceptionMessage) {
        super(MARKET_DATA_SAVE_ERROR + " : " + exceptionMessage);
    }
}
