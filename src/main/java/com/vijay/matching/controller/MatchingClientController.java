package com.vijay.matching.controller;

import com.vijay.matching.clients.ClientOrderPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchingClientController {

    private final ClientOrderPublisher orderPublisher;

    public MatchingClientController(ClientOrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @GetMapping(value = "/submitOrder", produces = MediaType.TEXT_PLAIN_VALUE)
    public boolean submitOrder(long clientOrderId, String symbol, String side,
                               long qty, float price, int orderType) {
        return orderPublisher.submit(clientOrderId, symbol, side, qty, price, orderType);
    }

    @GetMapping(value = "/cancelOrder", produces = MediaType.TEXT_PLAIN_VALUE)
    public boolean cancelOrder(long clientOrderId, long originalId, String symbol, String side) {
        return orderPublisher.cancel(clientOrderId, originalId, symbol, side);
    }
}
