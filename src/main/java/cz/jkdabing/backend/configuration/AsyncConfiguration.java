package cz.jkdabing.backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Value("${async.executor.corePoolSize}")
    private int corePoolSize;

    @Value("${async.executor.maxPoolSize}")
    private int maxPoolSize;

    @Value("${async.executor.queueCapacity}")
    private int queueCapacity;

    @Bean(name = "threadPoolTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("AudiotLog-");
        executor.initialize();

        return executor;
    }
}
