package com.vijay.matching.service;

import com.vijay.matching.Boolean;
import com.vijay.matching.ExecutionEncoder;
import com.vijay.matching.config.AeronPubSub;
import com.vijay.matching.model.Execution;
import com.vijay.matching.model.MsgTypes;
import io.aeron.Aeron;
import io.aeron.Publication;
import lombok.extern.slf4j.Slf4j;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@DependsOn("aeron")
public class ExecutionsPublisher {

    private ResourceLoader resourceLoader;
    private final Aeron aeron;
    private Publication execPublication;
    private final UnsafeBuffer unsafeBuffer;
    private final ExecutionEncoder executionEncoder = new ExecutionEncoder();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final String ordersFile;

    public ExecutionsPublisher(Aeron aeron, @Value("${file.orders}") String ordersFile,
                               ResourceLoader resourceLoader) {
        this.aeron = aeron;
        this.ordersFile = ordersFile;
        this.resourceLoader = resourceLoader;
        this.unsafeBuffer = new UnsafeBuffer(BufferUtil.allocateDirectAligned(124, 64));
        this.execPublication = aeron.addPublication("aeron:ipc", AeronPubSub.EXECUTIONS_CHANNEL);
//        while (!orderPublication.isConnected()) {
//            //log.info("Waiting for subscriber to connect...");
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
        log.info("publisher started");
    }

    public void publish(List<Execution> executionList) {
            executionList.forEach(execution -> {
                unsafeBuffer.putByte(0, MsgTypes.EXEC);
                executionEncoder.wrap(unsafeBuffer, 1)
                        .id(execution.id())
                        .execQty(execution.qty())
                        .execPrice((float) execution.execPrice())
                        .ack(execution.ack() ? Boolean.T:Boolean.F);
                long status;
                do {
                    status = execPublication.offer(unsafeBuffer, 0,
                            executionEncoder.encodedLength() + 1);
                    if (status < 0) {
                        log.info("Message not published, retrying..." + status);
                        AeronPubSub.getIdleStrategy().idle();
                    }

                } while (status < 0);
                log.info("published exec {}", execution.id());
            });
    }
}
