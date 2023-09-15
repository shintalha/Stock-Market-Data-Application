package com.personal.stockmarketmanagement.model.contracts.robinhood;

import com.personal.stockmarketmanagement.model.dto.RobinhoodMarketDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketsApiResponse {
    private int previous;
    private int next;
    private List<RobinhoodMarketDto> results;
}
