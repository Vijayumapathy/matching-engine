package com.vijay.matching.clients;

import com.vijay.matching.OrderSubmitEncoder;
import com.vijay.matching.OrderType;
import com.vijay.matching.Side;
import com.vijay.matching.config.AeronPubSub;
import com.vijay.matching.model.MsgTypes;
import io.aeron.Aeron;
import io.aeron.Publication;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@DependsOn("aeron")
public class ClientOrderPublisher {

    private ResourceLoader resourceLoader;
    private final Aeron aeron;
    private Publication orderPublication;
    private final UnsafeBuffer unsafeBuffer;
    private final OrderSubmitEncoder orderSubmitEncoder = new OrderSubmitEncoder();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final String ordersFile;

    public ClientOrderPublisher(Aeron aeron, @Value("${file.orders}") String ordersFile,
                                ResourceLoader resourceLoader) {
        this.aeron = aeron;
        this.ordersFile = ordersFile;
        this.resourceLoader = resourceLoader;
        this.unsafeBuffer = new UnsafeBuffer(BufferUtil.allocateDirectAligned(124, 64));
        this.orderPublication = aeron.addPublication("aeron:ipc", AeronPubSub.ORDERS_CHANNEL);

    }

    @PostConstruct
    public void init() {

        executorService.submit(() -> {
            while (!orderPublication.isConnected()) {
                log.info("Waiting for subscriber to connect...");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            log.info("publisher started");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                Files.lines(Path.of(resourceLoader.getResource(ordersFile).getURI()))
                        .skip(1)
                        .forEach(line -> {
                            String[] params = line.split(",");
                            unsafeBuffer.putByte(0, MsgTypes.SUBMIT);
                            orderSubmitEncoder.wrap(unsafeBuffer, 1)
                                    .id(Integer.parseInt(params[0]))
                                    .symbol(params[1])
                                    .side(Side.get((byte)params[2].charAt(0)))
                                    .qty(Integer.parseInt(params[3]))
                                    .price(Float.parseFloat(params[4]))
                                    .orderType(OrderType.get(Integer.parseInt(params[5])));
                            long status;
                            do {
                                status = orderPublication.offer(unsafeBuffer, 0,
                                        orderSubmitEncoder.encodedLength() + 1);
                                if (status < 0) {
                                    log.info("Message not published, retrying..." + status);
                                    AeronPubSub.getIdleStrategy().idle();
                                }

                            } while (status < 0);
                            log.info("published order {}", params[0]);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
