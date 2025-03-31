package com.vijay.matching.service;

import com.vijay.matching.OrderSubmitEncoder;
import com.vijay.matching.OrderType;
import com.vijay.matching.Side;
import com.vijay.matching.config.AeronPubSub;
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
public class ExecutionsPublisher {

    private ResourceLoader resourceLoader;
    private final Aeron aeron;
    private Publication orderPublication;
    private final UnsafeBuffer unsafeBuffer;
    private final OrderSubmitEncoder orderSubmitEncoder = new OrderSubmitEncoder();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final String ordersFile;

    public ExecutionsPublisher(Aeron aeron, @Value("${file.orders}") String ordersFile,
                               ResourceLoader resourceLoader) {
        this.aeron = aeron;
        this.ordersFile = ordersFile;
        this.resourceLoader = resourceLoader;
        this.unsafeBuffer = new UnsafeBuffer(BufferUtil.allocateDirectAligned(124, 64));
        this.orderPublication = aeron.addPublication("aeron:ipc", AeronPubSub.EXECUTIONS_CHANNEL);
        while (!orderPublication.isConnected()) {
            //log.info("Waiting for subscriber to connect...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.info("publisher started");
    }

    @PostConstruct
    public void init() {

    }
}
