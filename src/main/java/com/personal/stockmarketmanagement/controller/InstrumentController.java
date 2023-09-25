package com.personal.stockmarketmanagement.controller;

import com.personal.stockmarketmanagement.model.constant.ResponseStatus;
import com.personal.stockmarketmanagement.model.contracts.controller.ControllerResponse;
import com.personal.stockmarketmanagement.model.dto.InstrumentDto;
import com.personal.stockmarketmanagement.service.InstrumentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.*;

@RestController
public class InstrumentController {
    @Autowired
    InstrumentService instrumentService;

    @GetMapping("/api/instruments/sync")
    @Operation(summary = "Sync instrument data in database with instrument data in data provider")
    public ResponseEntity<ControllerResponse> syncMarketData() {
        try {
            instrumentService.syncAllInstrumentData();
            ControllerResponse controllerResponse = ControllerResponse.builder()
                    .status(ResponseStatus.SUCCESS)
                    .message(INSTRUMENT_SYNC_SUCCESS)
                    .build();

            return new ResponseEntity<>(controllerResponse, HttpStatus.OK);
        } catch (Exception ex) {
            ControllerResponse controllerResponse = ControllerResponse.builder()
                    .status(ResponseStatus.ERROR)
                    .message(INSTRUMENT_SYNC_ERROR)
                    .additionalInfo(ex.getMessage())
                    .build();
            return new ResponseEntity<>(controllerResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/instruments")
    @Operation(summary = "Retrieves all instrument data from database")
    public ResponseEntity<ControllerResponse> getAllInstruments() {
        try {
            List<InstrumentDto> instrumentDtoList = instrumentService.getAllInstruments();

            ControllerResponse controllerResponse = ControllerResponse.builder()
                    .status(ResponseStatus.SUCCESS)
                    .message(GET_ALL_INSTRUMENTS_SUCCESS)
                    .initializeDataAndAdd(instrumentDtoList)
                    .build();

            return new ResponseEntity<>(controllerResponse, HttpStatus.OK);
        } catch (Exception ex) {
            ControllerResponse controllerResponse = ControllerResponse.builder()
                    .status(ResponseStatus.ERROR)
                    .message(GET_ALL_INSTRUMENTS_ERROR)
                    .additionalInfo(ex.getMessage())
                    .build();
            return new ResponseEntity<>(controllerResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/instruments/{symbol}")
    @Operation(summary = "Retrieve an instrument data by its symbol")
    public ResponseEntity<ControllerResponse> getInstrumentBySymbol(@PathVariable String symbol) {
        try {
            InstrumentDto instrumentDto = instrumentService.getInstrumentBySymbol(symbol);

            ControllerResponse controllerResponse = ControllerResponse.builder()
                    .status(ResponseStatus.SUCCESS)
                    .message(GET_INSTRUMENT_BY_SYMBOL_SUCCESS)
                    .initializeDataAndAdd(instrumentDto)
                    .build();

            return new ResponseEntity<>(controllerResponse, HttpStatus.OK);
        } catch (Exception ex) {
            ControllerResponse controllerResponse = ControllerResponse.builder()
                    .status(ResponseStatus.ERROR)
                    .message(GET_INSTRUMENT_BY_SYMBOL_ERROR)
                    .additionalInfo(ex.getMessage())
                    .build();
            return new ResponseEntity<>(controllerResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
