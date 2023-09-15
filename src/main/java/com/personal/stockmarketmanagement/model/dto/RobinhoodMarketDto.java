package com.personal.stockmarketmanagement.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RobinhoodMarketDto implements Serializable {
    private String name;
    private String mic;
    private String acronym;
    private String country;
    private String website;
}