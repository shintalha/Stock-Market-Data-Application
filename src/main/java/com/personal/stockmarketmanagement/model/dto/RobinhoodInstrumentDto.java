package com.personal.stockmarketmanagement.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RobinhoodInstrumentDto implements Serializable {
    private String name;
    private String symbol;
    private String simple_name;

    /**
     * Content of this field is market URL
     * example = "https://api.robinhood.com/markets/XNAS"
     */
    private String market;

}
