package com.vijay.matching.config;

import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import lombok.extern.slf4j.Slf4j;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Slf4j
@Configuration
public class AeronPubSub {

    private static final IdleStrategy IDLE_STRATEGY = new SleepingIdleStrategy();
    public static final int ORDERS_CHANNEL = 4;
    public static final int EXECUTIONS_CHANNEL = 5;

    private final MediaDriver.Context ctx = new MediaDriver.Context()
            .conductorIdleStrategy(IDLE_STRATEGY)
            .threadingMode(ThreadingMode.SHARED)
            .receiverIdleStrategy(IDLE_STRATEGY)
            .senderIdleStrategy(IDLE_STRATEGY)
            .dirDeleteOnShutdown(true)
            .dirDeleteOnStart(true)
            .aeronDirectoryName(Paths.get(System.getProperty("java.io.tmpdir"),
                    "aeron-matchingEngine-ipc").toString());

    public AeronPubSub() {

    }

    @Bean
    public Aeron aeron() {
        MediaDriver mediaDriver = MediaDriver.launch(ctx);
        Aeron aeron = Aeron.connect(new Aeron.Context().aeronDirectoryName(mediaDriver.aeronDirectoryName()));
        log.info("Aeron context initialized {}", aeron);
        return aeron;
    }

    public static IdleStrategy getIdleStrategy() {
        return IDLE_STRATEGY;
    }
}
