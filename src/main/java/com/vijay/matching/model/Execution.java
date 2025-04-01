package com.vijay.matching.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class Execution {
    private long execId;
    private String symbol;
    private String dealt;
    private boolean isBuy;
    private long execQty;
    private long qty;
    private String valueDate;
    private String user;
    private float matchPercentage;

    public Execution(Order order, long execQty) {
        symbol = order.symbol();
        dealt = order.dealt();
        isBuy = order.isBuy();
        valueDate = order.valueDate();
        user = order.user();
        this.execQty = execQty;
    }
}
