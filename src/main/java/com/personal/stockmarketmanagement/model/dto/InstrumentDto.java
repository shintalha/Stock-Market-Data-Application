package com.personal.stockmarketmanagement.model.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InstrumentDto implements Serializable {
    private long id;
    private String symbol;
    private String fullName;
    private String simpleName;
    private Integer marketId;
}
