package com.vijay.matching.clients;

public interface IClientOrderPublisher {
    boolean submit(long clientOrderId, String symbol, String dealt, String side,
                   long qty, String valueDate, String user);

    boolean cancel(long clientOrderId, long originalId, String symbol, String side);
}
