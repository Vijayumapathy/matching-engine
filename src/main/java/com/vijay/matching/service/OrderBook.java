package com.vijay.matching.service;

import com.vijay.matching.model.Execution;
import com.vijay.matching.model.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class OrderBook implements IOrderBook {

    private final String symbol;
    private double lastExecPrice;
    private final ExecutionsPublisher executionsPublisher;
    private final ConcurrentSkipListMap<String, ConcurrentLinkedDeque<Order>> buyBook = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<String, ConcurrentLinkedDeque<Order>> sellBook = new ConcurrentSkipListMap<>();
    private final ExecutorService depthPrinter = Executors.newSingleThreadExecutor();
    private final ExecutorService matchExecutor = Executors.newSingleThreadExecutor();

    public OrderBook(String symbol, ExecutionsPublisher executionsPublisher) {
        this.symbol = symbol;
        this.executionsPublisher = executionsPublisher;
    }

    @Override
    public boolean addOrder(Order newOrder) {
        boolean result = false;
        Order aggregatedOrder = null;
        //find the book for the new order
        ConcurrentLinkedDeque<Order> orderList = (newOrder.isBuy() ? buyBook : sellBook)
                .computeIfAbsent(newOrder.valueDate(), list -> new ConcurrentLinkedDeque<>());

        //if similar order is found aggregate to the existing order, else look to opposite book
        Optional<Order> similarOrder = orderList.stream()
                .filter(order1 -> order1.user().equalsIgnoreCase(newOrder.user()) && order1.dealt().equalsIgnoreCase(newOrder.dealt()))
                .findFirst();

        if(similarOrder.isPresent()) {
            aggregatedOrder = similarOrder.get();
            aggregatedOrder.qty(aggregatedOrder.qty() + newOrder.qty());
            depthPrinter.submit(this::depth);
            executionsPublisher.publish(similarOrder.get());
            return true;
        }

        //find the opposite book for the new order
        ConcurrentLinkedDeque<Order> oppositeOrderList = (newOrder.isBuy() ? sellBook : buyBook)
                .computeIfAbsent(newOrder.valueDate(), list -> new ConcurrentLinkedDeque<>());


        //from the opposite orderlist filter for order with same user and dealtccy
        Optional<Order> oppositeOrder = oppositeOrderList.stream()
                .filter(order1 -> order1.user().equalsIgnoreCase(newOrder.user()) && order1.dealt().equalsIgnoreCase(newOrder.dealt()))
                .findFirst();

        if (oppositeOrder.isEmpty()) {
            //opposite order not found, add the neworder to the book
            orderList.offer(newOrder);
            aggregatedOrder = newOrder;
            result = true;
        } else {
            //opposite order found: try aggregate neworder the opposite order
            Order oppOrder = oppositeOrder.get();
            long aggregatedQty = Math.min(oppOrder.qty(), newOrder.qty());
            long remainQty = Math.abs(oppOrder.qty() - newOrder.qty());
            oppOrder.qty(oppOrder.qty() - aggregatedQty);
            newOrder.qty(newOrder.qty() - aggregatedQty);

            if (remainQty == 0) {
                //both orders fully reduced,remove opposite order
                oppositeOrderList.poll();
                aggregatedOrder = oppOrder;
            } else {
                if (oppOrder.qty() == 0) {
                    //existing order is fully reduced,so remove existing.
                    oppositeOrderList.poll();
                    // add new order with remain amt to orderList,
                    orderList.add(newOrder);
                    aggregatedOrder = newOrder;
                }
                //current order is fully reduced, do nothing;
                if (newOrder.qty() == 0) {
                    aggregatedOrder = oppOrder;
                }
            }
            result = true;
        }
        depthPrinter.submit(this::depth);
        executionsPublisher.publish(aggregatedOrder);
        return result;
    }

    @Override
    public boolean cancelOrder(Order order) {
        return order.isBuy() ? cancelOrder(order, buyBook) : cancelOrder(order, sellBook);
    }

    private boolean cancelOrder(Order cancelOrder, ConcurrentSkipListMap<String, ConcurrentLinkedDeque<Order>> book) {

        return false;
    }

    @Override
    public void match() {
        List<Execution> executions;
        Order bestBid, bestAsk;
        while (!buyBook.isEmpty() && !sellBook.isEmpty()) {
            Map.Entry<String, ConcurrentLinkedDeque<Order>> topBuy = buyBook.firstEntry();
            Map.Entry<String, ConcurrentLinkedDeque<Order>> topSell = sellBook.firstEntry();

            ConcurrentLinkedDeque<Order> buyOrders = topBuy.getValue();
            ConcurrentLinkedDeque<Order> sellOrders = topSell.getValue();

            while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
                executions = new ArrayList<>();
                bestBid = buyOrders.peek();
                bestAsk = sellOrders.peek();
                long execQty = Math.min(bestBid.qty(), bestAsk.qty());
                bestBid.qty(bestBid.qty() - execQty);
                bestAsk.qty(bestAsk.qty() - execQty);

                executions.add(new Execution()
                        .id(bestBid.id())
                        .qty(execQty)
                        //.execPrice(bestBid.price())
                        .ack(true));
                executions.add(new Execution()
                        .id(bestAsk.id())
                        .qty(execQty)
                        //.execPrice(bestAsk.price())
                        .ack(true));
                executionsPublisher.publish(executions);
                if (bestBid.qty() == 0) {
                    buyOrders.poll();
                }
                if (bestAsk.qty() == 0) {
                    sellOrders.poll();
                }
                if (buyOrders.isEmpty()) {
                    buyBook.remove(bestBid.valueDate());
                }
                if (sellOrders.isEmpty()) {
                    sellBook.remove(bestBid.valueDate());
                }
            }
            depthPrinter.submit(this::depth);
        }
    }

    @Override
    public void marketMatch() {
        depthPrinter.submit(this::depth);
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
        StringBuilder builder = new StringBuilder("\nDepth: \n");
        sellBook.forEach((valueDate, value) -> {
            value.forEach(order -> {
                builder.append(symbol).append(" - ").
                        append('S').append(',').
                        append(valueDate).append(',').
                        append(order.dealt()).append(',').
                        append(order.qty()).append(',').
                        append(order.user()).append('\n');
            });
        });
        buyBook.forEach((valueDate, value) -> {
            value.forEach(order -> {
                builder.append(symbol).append(" - ").
                        append('B').append(',').
                        append(valueDate).append(',').
                        append(order.dealt()).append(',').
                        append(order.qty()).append(',').
                        append(order.user()).append('\n');
            });

        });
        log.info(builder.toString());
    }

    @Override
    public boolean pause(boolean shouldPause) {
        return false;
    }
}
