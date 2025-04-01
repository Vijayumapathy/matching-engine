package com.vijay.matching.service;

import com.vijay.matching.model.Order;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class OrderBookManager implements IOrderBookManager{

    private final ExecutionsPublisher executionsPublisher;
    private final Map<String, OrderBook> orderBookMap = new ConcurrentHashMap<>();

    public OrderBookManager(ExecutionsPublisher executionsPublisher) {
        this.executionsPublisher = executionsPublisher;
        Stream.of("EURUSD", "USDCAD").forEach(ccy -> {
            orderBookMap.put(ccy, new OrderBook(ccy, executionsPublisher));
        });
    }

    @Override
    public boolean submitOrder(Order order) {
        return orderBookMap.get(order.symbol()).addOrder(order);
    }

    @Override
    public boolean cancelOrder(Order order) {
        return orderBookMap.get(order.symbol()).cancelOrder(order);
    }

    @Override
    public void match(String user, String symbol) {
        orderBookMap.get(symbol).marketMatch(user);
    }

}
