package com.vijay.matching.clients;

import com.vijay.matching.ExecutionDecoder;
import com.vijay.matching.OrderSubmitDecoder;
import com.vijay.matching.config.AeronPubSub;
import com.vijay.matching.model.MsgTypes;
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

@Slf4j
@Service
public class ClientExecListener implements FragmentHandler {

    private final Subscription subscription;
    private final ExecutionDecoder executionDecoder = new ExecutionDecoder();
    private final OrderSubmitDecoder orderSubmitDecoder = new OrderSubmitDecoder();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ClientExecListener(Aeron aeron) {
        this.subscription = aeron.addSubscription("aeron:ipc", AeronPubSub.EXECUTIONS_CHANNEL);
        log.info("subscriber started");
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
            case MsgTypes.EXEC -> {
                executionDecoder.wrap(directBuffer, offset + 1, length - 1, 0);
                log.info("received exec {} qty {}", executionDecoder.id(), executionDecoder.execQty());
            }
            case MsgTypes.AGG -> {
                orderSubmitDecoder.wrap(directBuffer, offset + 1, length - 1, 0);
                log.info("received aggregatedOrder {}", orderSubmitDecoder.id());
            }
            default -> {
                log.error("Unknown msg received");
            }
        }
    }
}
