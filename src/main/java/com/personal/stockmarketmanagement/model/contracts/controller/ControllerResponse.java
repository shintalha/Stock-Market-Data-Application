package com.personal.stockmarketmanagement.model.contracts.controller;

import com.personal.stockmarketmanagement.model.constant.ResponseStatus;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControllerResponse implements Serializable {
    private ResponseStatus status;
    private String message;
    private String additionalInfo;
    private List<Object> data;

    public static class ControllerResponseBuilder {
        public ControllerResponseBuilder initializeDataAndAdd(Object data) {
            this.data = new ArrayList<>();
            this.data.add(data);
            return this;
        }

        public ControllerResponseBuilder addData(Object data) {
            this.data.add(data);
            return this;
        }
    }
}
