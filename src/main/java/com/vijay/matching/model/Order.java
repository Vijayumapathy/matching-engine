package com.vijay.matching.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class Order {
    private long id;
    private String symbol;
    private String dealt;
    private boolean isBuy;
    private long qty;
    private String valueDate;
    private String user;

}
