package com.personal.stockmarketmanagement.dataprovider;

import com.personal.stockmarketmanagement.model.contracts.robinhood.GetInstrumentBySymbolApiResponse;
import com.personal.stockmarketmanagement.model.contracts.robinhood.MarketsApiResponse;
import com.personal.stockmarketmanagement.model.entity.Instrument;
import com.personal.stockmarketmanagement.model.entity.Market;
import com.personal.stockmarketmanagement.utility.RobinhoodUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class RobinhoodProvider implements DataProvider {
    @Value("${robinhoodMarketsApiUrl}")
    private String robinhoodMarketsApiUrl;
    @Value("${robinhoodGetInstrumentBySymbolApiUrl}")
    private String robinhoodGetInstrumentBySymbolApiUrl;

    public Optional<Instrument> getInstrumentBySymbol(String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GetInstrumentBySymbolApiResponse> response =
                restTemplate.getForEntity(
                        robinhoodGetInstrumentBySymbolApiUrl,
                        GetInstrumentBySymbolApiResponse.class,
                        symbol);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody().getResults() != null && !response.getBody().getResults().isEmpty()) {
            return Optional.of(
                    RobinhoodUtil.convertRobinhoodInstrumentDtoToInstrument(
                            response.getBody().getResults().get(0)
                    ));
        }

        return Optional.empty();
    }

    public Optional<List<Market>> getMarkets() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MarketsApiResponse> response =
                restTemplate.getForEntity(
                        robinhoodMarketsApiUrl,
                        MarketsApiResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody().getResults() != null && !response.getBody().getResults().isEmpty()) {
            return Optional.of(
                    RobinhoodUtil.mapRobinhoodMarketDtoListToMarketList(
                            response.getBody().getResults()
                    ));
        }

        return Optional.empty();
    }
}
