package com.personal.stockmarketmanagement.model.exception;

import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.INSTRUMENT_BY_SYMBOL_NOT_FOUND_ERROR;

public class InstrumentNotFoundException extends Exception {
    public InstrumentNotFoundException(String symbol) {
        super(INSTRUMENT_BY_SYMBOL_NOT_FOUND_ERROR + " Symbol: " + symbol);
    }

}
