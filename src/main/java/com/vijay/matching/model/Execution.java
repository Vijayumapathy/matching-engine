package com.vijay.matching.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class Execution {
    private long execId;
    private long id;
    private double execPrice;
    private long qty;
    private boolean ack;
}
