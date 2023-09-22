package com.personal.stockmarketmanagement.controller;

import com.personal.stockmarketmanagement.model.constant.ResponseStatus;
import com.personal.stockmarketmanagement.model.contracts.controller.ControllerResponse;
import com.personal.stockmarketmanagement.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.INSTRUMENT_SYNC_ERROR;
import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.INSTRUMENT_SYNC_SUCCESS;

@RestController
public class InstrumentController {
    @Autowired
    InstrumentService instrumentService;

    @GetMapping("/api/instruments/sync")
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
}
