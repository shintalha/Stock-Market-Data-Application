package com.personal.stockmarketmanagement.service;

import com.personal.stockmarketmanagement.dataprovider.DataProvider;
import com.personal.stockmarketmanagement.model.dto.InstrumentDto;
import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.model.entity.Market;
import com.personal.stockmarketmanagement.model.exception.DatabaseConnectionException;
import com.personal.stockmarketmanagement.repository.InstrumentRepository;
import com.personal.stockmarketmanagement.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class InstrumentService {
    @Autowired
    DataProvider dataProvider;

    @Autowired
    InstrumentRepository instrumentRepository;

    @Autowired
    MarketService marketService;

    /**
     * get all instrument data from db
     * @return instrument list
     * @throws DatabaseConnectionException if there is an error during db connection
     */
    public List<InstrumentDto> getAllInstruments() throws Exception {
        try {
            List<Instrument> instrumentList = instrumentRepository.findAll();
            return Util.mapInstrumentEntityListToDtoList(instrumentList);
        } catch (Exception ex) {
            throw new DatabaseConnectionException(ex.getMessage());
        }
    }

    @CacheEvict(value = "instrument", allEntries = true)
    public void syncAllInstrumentData() throws Exception {
        List<Instrument> instrumentList = instrumentRepository.findAll();
        List<CompletableFuture<Void>> updatedInstrumentFutureList =
                this.getInstrumentAndUpdateAsync(instrumentList);

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                updatedInstrumentFutureList.toArray(new CompletableFuture[updatedInstrumentFutureList.size()])
        );

        allFutures.join();
    }

    /**
     * This method takes an instrument entity list; make api call and database update asynchronously for entities on the list
     * @param instrumentList
     * @return compatibleFuture list
     */
    public List<CompletableFuture<Void>> getInstrumentAndUpdateAsync(List<Instrument> instrumentList) {
        return instrumentList.stream()
                .map(instrument ->
                        CompletableFuture.runAsync(() -> {
                            Optional<Instrument> optionalInstrument = dataProvider.getInstrumentBySymbol(instrument.getSymbol());
                            optionalInstrument.ifPresent( // If instrument data provided by provider
                                    providerInstrument -> {
                                        Optional<Market> optionalMarket = marketService.getMarketByCode(providerInstrument.getMarket().getCode());
                                        optionalMarket.ifPresent(market -> { // If market of instrument have record in db
                                            providerInstrument.setId(instrument.getId());
                                            providerInstrument.setMarket(market);
                                            this.updateInstrumentData(providerInstrument);
                                        });
                                    });
                        })
                ).toList();
    }

    /**
     * This method update instrument data on db
     * Calls a function which get id of market of instrument from db
     * if marketId is not null(if there is a record of the market of the instrument), instrument data is updated.
     * @param instrument use name, customName during updating, using market(url) to get marketId
     */
    public void updateInstrumentData(Instrument instrument) {
        instrumentRepository.updateInstrumentData(
                instrument.getFullName(),
                instrument.getSimpleName(),
                instrument.getMarket().getId(),
                instrument.getId()
        );
    }
}
