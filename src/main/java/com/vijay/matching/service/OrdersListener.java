package com.vijay.matching.service;

import com.vijay.matching.ExecutionDecoder;
import com.vijay.matching.Side;
import com.vijay.matching.config.AeronPubSub;
import com.vijay.matching.OrderCancelDecoder;
import com.vijay.matching.OrderSubmitDecoder;
import com.vijay.matching.model.MsgTypes;
import com.vijay.matching.model.Order;
import com.vijay.matching.model.OrderType;
import io.aeron.Aeron;
import io.aeron.FragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class OrdersListener implements FragmentHandler {

    private final Subscription subscription;
    private final OrderSubmitDecoder orderSubmitDecoder = new OrderSubmitDecoder();
    private final OrderCancelDecoder orderCancelDecoder = new OrderCancelDecoder();
    private final ExecutionDecoder executionDecoder = new ExecutionDecoder();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final OrderBookManager orderBookManager;
    private final AtomicLong eventId = new AtomicLong(10000);

    public OrdersListener(Aeron aeron, OrderBookManager orderBookManager) {
        this.subscription = aeron.addSubscription("aeron:ipc", AeronPubSub.ORDERS_CHANNEL);
        log.info("subscriber started");
        this.orderBookManager = orderBookManager;
    }

    @PostConstruct
    public void init() {
        executorService.submit(() -> {
            log.info("subscriber listening..");
            FragmentHandler handler = new FragmentAssembler(this);
            int msgCnt;
            while (true) {
                msgCnt = subscription.poll(handler, 5);
                if (msgCnt <= 0) {
                    AeronPubSub.getIdleStrategy().idle();
                }
            }
        });
    }

    @Override
    public void onFragment(DirectBuffer directBuffer, int offset, int length, Header header) {

        byte messageType = directBuffer.getByte(offset);
        switch (messageType) {
            case MsgTypes.SUBMIT -> {
                orderSubmitDecoder.wrap(directBuffer, offset + 1, length -1, 0);
                log.info("received {}", orderSubmitDecoder.id());
                orderBookManager.submitOrder(new Order().id(orderSubmitDecoder.id())
                        .symbol(orderSubmitDecoder.symbol())
                        .isBuy(orderSubmitDecoder.side() == Side.Buy)
                        .qty(orderSubmitDecoder.qty())
                        .price(orderSubmitDecoder.price())
                        .orderType(OrderType.get(orderSubmitDecoder.orderType().value())));
            }
            case MsgTypes.CANCEL -> {
                orderCancelDecoder.wrap(directBuffer, offset + 1, length -1, 0);
                log.info("received {}", orderCancelDecoder.id());
                orderBookManager.cancelOrder(new Order().id(orderCancelDecoder.originalId())
                        .symbol(orderCancelDecoder.symbol())
                        .isBuy(orderCancelDecoder.side() == Side.Buy));
            }
            case MsgTypes.EXEC -> {
                executionDecoder.wrap(directBuffer, offset +1, length -1, 0);
                log.info("received exec {}", executionDecoder);
            }
            default -> {
                log.error("Unknown msg received");
            }
        }

    }
}
