package com.personal.stockmarketmanagement.model.contracts.robinhood;

import com.personal.stockmarketmanagement.model.dto.RobinhoodInstrumentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetInstrumentBySymbolApiResponse {
    private int next;
    private int prev;
    private List<RobinhoodInstrumentDto> results;
}
