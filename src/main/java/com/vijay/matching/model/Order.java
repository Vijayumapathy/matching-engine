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
    private boolean isBuy;
    private double price;
    private long qty;
    private OrderType orderType;
    private long eventId;

}
