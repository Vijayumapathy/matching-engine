package com.vijay.matching.service;

import com.vijay.matching.model.Order;
import com.vijay.matching.model.OrderType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class OrderBook implements IOrderBook{

    private final String symbol;
    private double lastExecPrice;
    private final ConcurrentSkipListMap<Double, ConcurrentLinkedDeque<Order>> buyBook = new ConcurrentSkipListMap<>(Collections.reverseOrder());
    private final ConcurrentSkipListMap<Double, ConcurrentLinkedDeque<Order>> sellBook = new ConcurrentSkipListMap<>();
    private final Map<Long, Long> clientOrderIdToEventIdMap = new ConcurrentHashMap<>();
    private final Map<Long, Double> clientOrderIdToPriceMap = new ConcurrentHashMap<>();
    private final ExecutorService depthPrinter = Executors.newSingleThreadExecutor();

    public OrderBook(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean addOrder(Order order) {
        if(order.orderType() == OrderType.LMT) {
            clientOrderIdToPriceMap.put(order.id(), order.price());
            clientOrderIdToEventIdMap.put(order.id(), order.eventId());
            boolean result = (order.isBuy() ? buyBook : sellBook)
                    .computeIfAbsent(order.price(), orderList -> new ConcurrentLinkedDeque<>())
                    .offer(order);
            depthPrinter.submit(this::depth);
            return result;
        } else {
            match();
            return true;
        }
    }

    @Override
    public boolean cancelOrder(Order order) {
      return order.isBuy() ? cancelOrder(order, buyBook) : cancelOrder(order, sellBook);
    }

    private boolean cancelOrder(Order cancelOrder, ConcurrentSkipListMap<Double, ConcurrentLinkedDeque<Order>> book) {
        Double price = clientOrderIdToPriceMap.remove(cancelOrder.id());
        if(price != null) {
            Long eventId = clientOrderIdToEventIdMap.remove(cancelOrder.id());
            ConcurrentLinkedDeque<Order> orders = book.get(price);
            if(orders.isEmpty()){
                book.remove(price);
                return false;
            }
            boolean result = orders.removeIf(order -> order.id() == cancelOrder.id());
            if(orders.isEmpty()){
                book.remove(price);
            }
            return result;
        }
        return false;
    }
    @Override
    public void match() {

    }

    @Override
    public void topOfBook() {

    }

    @Override
    public void depth() {
        StringBuilder builder = new StringBuilder("\n");
        sellBook.descendingMap().forEach((key, value) -> {
            builder.append('S').append(',');
            builder.append((double)key).append(',');
            builder.append(value.stream()
                    .mapToLong(Order::qty).sum()).append('\n');
        });
        buyBook.forEach((key, value) -> {
            builder.append('B').append(',');
            builder.append((double)key).append(',');
            builder.append(value.stream()
                    .mapToLong(Order::qty).sum()).append('\n');
        });
        log.info(builder.toString());
    }

    @Override
    public boolean pause(boolean shouldPause) {
        return false;
    }
}
