package com.vijay.matching.service;

import com.vijay.matching.model.Order;

public interface IOrderBookManager {
    boolean submitOrder(Order order);
    boolean cancelOrder(Order order);
}
