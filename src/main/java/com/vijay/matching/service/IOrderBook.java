package com.vijay.matching.service;

import com.vijay.matching.model.Order;

public interface IOrderBook {
    boolean addOrder(Order order);
    boolean cancelOrder(Order order);
    void limitMatch();
    void marketMatch();
    void iocMatch();
    void fokMatch();
    void topOfBook();
    void depth();
    boolean pause(boolean shouldPause);
}
