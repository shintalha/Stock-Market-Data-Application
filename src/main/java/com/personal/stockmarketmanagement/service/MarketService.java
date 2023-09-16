package com.personal.stockmarketmanagement.service;

import com.personal.stockmarketmanagement.dataprovider.DataProvider;
import com.personal.stockmarketmanagement.model.entity.Market;
import com.personal.stockmarketmanagement.model.exception.MarketDataFetchException;
import com.personal.stockmarketmanagement.model.exception.MarketDataSaveException;
import com.personal.stockmarketmanagement.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class MarketService {
    @Autowired
    MarketRepository marketRepository;
    @Autowired
    DataProvider dataProvider;

    public void syncMarketData() throws Exception {
        Optional<List<Market>> optionalMarketList = dataProvider.getMarkets();
        if (optionalMarketList.isEmpty())
            throw new MarketDataFetchException();

        this.saveAndUpdateMarketData(optionalMarketList.get());
    }

    /**
     * This method saves market data into database, update existing
     * Catches exception and throws again with custom error message
     * @param marketList market entity list to be saved to database
     * @throws MarketDataSaveException if save operation failed
     */
    public void saveAndUpdateMarketData(List<Market> marketList) throws Exception {
        try {
            List<CompletableFuture<Void>> marketDataFutureList = marketList.stream().map(
                    market -> CompletableFuture.runAsync(() -> {
                        Optional<Integer> marketId = marketRepository.getMarketIdByCode(market.getCode());
                        if (marketId.isPresent())
                            market.setId(marketId.get());

                        marketRepository.save(market);
                    })
            ).toList();

            marketDataFutureList.forEach(CompletableFuture::join);
        } catch (Exception ex) {
            throw new MarketDataSaveException(ex.getMessage());
        }
    }
}
