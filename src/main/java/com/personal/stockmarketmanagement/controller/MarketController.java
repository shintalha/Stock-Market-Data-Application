package com.personal.stockmarketmanagement.controller;

import com.personal.stockmarketmanagement.model.constant.ResponseStatus;
import com.personal.stockmarketmanagement.model.contracts.controller.ControllerResponse;
import com.personal.stockmarketmanagement.service.MarketService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.MARKET_SYNC_ERROR;
import static com.personal.stockmarketmanagement.model.constant.ConstantParameter.MARKET_SYNC_SUCCESS;

@RestController
public class MarketController {
    @Autowired
    MarketService marketService;

    @GetMapping("/api/markets/sync")
    @Operation(summary = "Sync market datas in database with market datas in data provider")
    public ResponseEntity<ControllerResponse> syncMarketData() {
        try {
            marketService.syncMarketData();

            ControllerResponse controllerResponse = ControllerResponse.builder()
                    .status(ResponseStatus.SUCCESS)
                    .message(MARKET_SYNC_SUCCESS)
                    .build();

            return new ResponseEntity<>(controllerResponse, HttpStatus.OK);
        } catch (Exception ex) {
            ControllerResponse controllerResponse = ControllerResponse.builder()
                    .status(ResponseStatus.ERROR)
                    .message(MARKET_SYNC_ERROR)
                    .additionalInfo(ex.getMessage())
                    .build();
            return new ResponseEntity<>(controllerResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
