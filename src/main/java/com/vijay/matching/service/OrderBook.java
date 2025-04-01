package com.vijay.matching.service;

import com.vijay.matching.model.Execution;
import com.vijay.matching.model.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
public class OrderBook implements IOrderBook {

    private final String symbol;
    private double lastExecPrice;
    private final ExecutionsPublisher executionsPublisher;
    private final ConcurrentSkipListMap<String, ConcurrentLinkedDeque<Order>> buyBook = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<String, ConcurrentLinkedDeque<Order>> sellBook = new ConcurrentSkipListMap<>();
    private final ExecutorService depthPrinter = Executors.newSingleThreadExecutor();
    private final ExecutorService matchExecutor = Executors.newSingleThreadExecutor();
    private final Map<String, HashSet<String>> userValueDateMap = new ConcurrentHashMap<>();


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

        if (similarOrder.isPresent()) {
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
            userValueDateMap.computeIfAbsent(newOrder.user(), k -> new HashSet<>()).add(newOrder.valueDate());
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
                    orderList.offer(newOrder);
                    userValueDateMap.computeIfAbsent(newOrder.user(), k -> new HashSet<>()).add(newOrder.valueDate());
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

//                executions.add(new Execution()
//                        .execId(bestBid.id())
//                        .execQty(execQty));
//                executions.add(new Execution()
//                        .execId(bestAsk.id())
//                        .execQty(execQty));
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
    public void marketMatch(String user) {
        List<Execution> executions = new ArrayList<>();
        if (buyBook.isEmpty() || sellBook.isEmpty()) {
            log.info("nothing to match for user {}", user);
            return;
        }
        //get list of valueDate order for user
        Optional<String> valuedateO = userValueDateMap.get(user).stream().findFirst();
        if (valuedateO.isEmpty()) {
            log.info("no valuedates for user {}", user);
            return;
        }
        String valueDate = valuedateO.get();
        matchBooks(user, valueDate, executions, buyBook, sellBook);
        matchBooks(user, valueDate, executions, sellBook, buyBook);
    }

    private void matchBooks(String user, String valueDate, List<Execution> executions,
                            ConcurrentSkipListMap<String, ConcurrentLinkedDeque<Order>> buyBook,
                            ConcurrentSkipListMap<String, ConcurrentLinkedDeque<Order>> sellBook) {
        //find the buy order for the user with valuedate
        ConcurrentLinkedDeque<Order> buyOrderQueue = buyBook
                .computeIfAbsent(valueDate, list -> new ConcurrentLinkedDeque<>());
        Optional<Order> buyOrderO = buyOrderQueue.stream()
                .filter(order -> order.user().equalsIgnoreCase(user)).findFirst();
        if (buyOrderO.isEmpty()) {
            log.info("no buyOrder for user {}", user);
            return;
        }
        Order buyOrder = buyOrderO.get();

        //find list of sell orders
        ConcurrentLinkedDeque<Order> sellOrderQueue = sellBook
                .computeIfAbsent(valueDate, list -> new ConcurrentLinkedDeque<>());
        List<Order> sellOrderList = sellOrderQueue.stream()
                .filter(ord -> ord.dealt().equalsIgnoreCase(buyOrder.dealt())).toList();

        //match buy qty until zero or all sell orders are exhausted
        Iterator<Order> iterator = sellOrderList.iterator();
        while (iterator.hasNext() && buyOrder.qty() > 0) {
            Order sellOrder = iterator.next();
            if(sellOrder.qty() == 0){
                continue;
            }
            long tradeQty = Math.min(buyOrder.qty(), sellOrder.qty());
            long remainQty = Math.abs(buyOrder.qty() - sellOrder.qty());

            if (remainQty == 0) {
                //both orders fully reduced,add both to executions
                executions.add(new Execution(buyOrder, tradeQty).matchPercentage(100));
                executions.add(new Execution(sellOrder, tradeQty).matchPercentage(100));
                sellOrderQueue.remove(sellOrder);
                buyOrderQueue.remove(buyOrder);
            } else {
                long sellQty = sellOrder.qty();
                long buyQty = buyOrder.qty();
                buyOrder.qty(buyOrder.qty() - tradeQty);
                sellOrder.qty(sellOrder.qty() - tradeQty);
                if (sellOrder.qty() == 0) {
                    Execution sellexec = new Execution(sellOrder, tradeQty);
                    sellexec.matchPercentage((tradeQty/sellQty)*100);
                    sellexec.qty(sellQty);
                    executions.add(sellexec);
                    sellOrderQueue.remove(sellOrder);

                    Execution buyExec =  new Execution(buyOrder, tradeQty);
                    buyExec.matchPercentage(((float)tradeQty/buyQty)*100);
                    buyExec.qty(buyQty);
                    executions.add(buyExec);
                }
                if (buyOrder.qty() == 0) {
                    Execution buyExec =  new Execution(buyOrder, tradeQty);
                    buyExec.matchPercentage((tradeQty/buyQty)*100);
                    buyExec.qty(buyQty);
                    executions.add(buyExec);
                    buyOrderQueue.remove(buyOrder);

                }
            }
        }
        executionsPublisher.publish(executions);
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
