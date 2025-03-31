package com.vijay.matching.clients;

public interface IClientOrderPublisher {
    boolean submit(long clientOrderId, String symbol, String side,
                   long qty, float price, int orderType);

    boolean cancel(long clientOrderId, long originalId, String symbol, String side);
}
