package com.vijay.matching.service;

import com.vijay.matching.model.Order;

public interface IOrderBook {
    boolean addOrder(Order order);
    boolean cancelOrder(Order order);
    void match();
    void topOfBook();
    void depth();
    boolean pause(boolean shouldPause);
}
