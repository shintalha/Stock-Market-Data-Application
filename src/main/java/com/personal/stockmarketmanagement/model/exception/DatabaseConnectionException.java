package com.personal.stockmarketmanagement.model.exception;

import com.personal.stockmarketmanagement.model.constant.ConstantParameter;

public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException(String exceptionMessage) {

        super(ConstantParameter.DATABASE_ERROR + " : " + exceptionMessage);
    }

    public DatabaseConnectionException() {

        super(ConstantParameter.DATABASE_ERROR);
    }
}
