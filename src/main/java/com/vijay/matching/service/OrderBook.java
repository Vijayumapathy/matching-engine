package com.vijay.matching.service;

import com.vijay.matching.model.Execution;
import com.vijay.matching.model.Order;
import com.vijay.matching.model.OrderType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class OrderBook implements IOrderBook{

    private final String symbol;
    private double lastExecPrice;
    private final ExecutionsPublisher executionsPublisher;
    private final ConcurrentSkipListMap<Double, ConcurrentLinkedDeque<Order>> buyBook = new ConcurrentSkipListMap<>(Collections.reverseOrder());
    private final ConcurrentSkipListMap<Double, ConcurrentLinkedDeque<Order>> sellBook = new ConcurrentSkipListMap<>();
    private final Map<Long, Long> clientOrderIdToEventIdMap = new ConcurrentHashMap<>();
    private final Map<Long, Double> clientOrderIdToPriceMap = new ConcurrentHashMap<>();
    private final ExecutorService depthPrinter = Executors.newSingleThreadExecutor();
    private final ExecutorService matchExecutor = Executors.newSingleThreadExecutor();

    public OrderBook(String symbol, ExecutionsPublisher executionsPublisher) {
        this.symbol = symbol;
        this.executionsPublisher = executionsPublisher;
    }

    @Override
    public boolean addOrder(Order order) {
        clientOrderIdToPriceMap.put(order.id(), order.price());
        clientOrderIdToEventIdMap.put(order.id(), order.eventId());
        boolean result = (order.isBuy() ? buyBook : sellBook)
                .computeIfAbsent(order.price(), orderList -> new ConcurrentLinkedDeque<>())
                .offer(order);
        if(!result) {
            return false;
        }
        depthPrinter.submit(this::depth);
        switch (order.orderType()) {
            case MKT -> {
                matchExecutor.submit(this::marketMatch);
            }
            case LMT -> {
                matchExecutor.submit(this::limitMatch);
            }
            case FOK -> {
                matchExecutor.submit(this::fokMatch);
            }
            case IOC -> {
                matchExecutor.submit(this::iocMatch);
            }
        }
        return true;
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
            depthPrinter.submit(this::depth);
            return result;
        }
        return false;
    }
    @Override
    public void limitMatch() {
        List<Execution> executions;
        Order bestBid, bestAsk;
        while (!buyBook.isEmpty() && !sellBook.isEmpty()){
            Map.Entry<Double, ConcurrentLinkedDeque<Order>> topBuy = buyBook.firstEntry();
            Map.Entry<Double, ConcurrentLinkedDeque<Order>> topSell = sellBook.firstEntry();
            if(topBuy.getKey() < topSell.getKey()){
                break;
            }

            ConcurrentLinkedDeque<Order> buyOrders = topBuy.getValue();
            ConcurrentLinkedDeque<Order> sellOrders = topSell.getValue();

            while(!buyOrders.isEmpty() && !sellOrders.isEmpty()){
                executions = new ArrayList<>();
                bestBid = buyOrders.peek();
                bestAsk = sellOrders.peek();
                long execQty = Math.min(bestBid.qty(), bestAsk.qty());
                bestBid.qty(bestBid.qty()-execQty);
                bestAsk.qty(bestAsk.qty()-execQty);

                executions.add(new Execution()
                        .id(bestBid.id())
                        .qty(execQty)
                        .execPrice(bestBid.price())
                        .ack(true));
                executions.add(new Execution()
                        .id(bestAsk.id())
                        .qty(execQty)
                        .execPrice(bestAsk.price())
                        .ack(true));
                executionsPublisher.publish(executions);
                if(bestBid.qty() == 0){
                    buyOrders.poll();
                }
                if(bestAsk.qty() == 0){
                    sellOrders.poll();
                }
                if(buyOrders.isEmpty()){
                    buyBook.remove(bestBid.price());
                }
                if(sellOrders.isEmpty()) {
                    sellBook.remove(bestBid.price());
                }
            }
            depthPrinter.submit(this::depth);
        }
    }

    @Override
    public void marketMatch() {
        List<Execution> executions;
        Order bestBid, bestAsk;
        while (!buyBook.isEmpty() && !sellBook.isEmpty()){
            Map.Entry<Double, ConcurrentLinkedDeque<Order>> topBuy = buyBook.firstEntry();
            Map.Entry<Double, ConcurrentLinkedDeque<Order>> topSell = sellBook.firstEntry();
            if(topBuy.getKey() < topSell.getKey()){
                break;
            }

            ConcurrentLinkedDeque<Order> buyOrders = topBuy.getValue();
            ConcurrentLinkedDeque<Order> sellOrders = topSell.getValue();

            while(!buyOrders.isEmpty() && !sellOrders.isEmpty()){
                executions = new ArrayList<>();
                bestBid = buyOrders.peek();
                bestAsk = sellOrders.peek();
                long execQty = Math.min(bestBid.qty(), bestAsk.qty());
                bestBid.qty(bestBid.qty()-execQty);
                bestAsk.qty(bestAsk.qty()-execQty);

                executions.add(new Execution()
                        .id(bestBid.id())
                        .qty(execQty)
                        .execPrice(bestBid.price())
                        .ack(true));
                executions.add(new Execution()
                        .id(bestAsk.id())
                        .qty(execQty)
                        .execPrice(bestAsk.price())
                        .ack(true));
                executionsPublisher.publish(executions);
                if(bestBid.qty() == 0){
                    buyOrders.poll();
                }
                if(bestAsk.qty() == 0){
                    sellOrders.poll();
                }
                if(buyOrders.isEmpty()){
                    buyBook.remove(bestBid.price());
                }
                if(sellOrders.isEmpty()) {
                    sellBook.remove(bestBid.price());
                }
            }
            depthPrinter.submit(this::depth);
        }
    }

    @Override
    public void iocMatch() {

    }

    @Override
    public void fokMatch() {

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
