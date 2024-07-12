package com.example.finalProject_synrgy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfig {
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // Jumlah minimum thread yang akan tetap ada dalam pool
        executor.setMaxPoolSize(10);  // Jumlah maksimum thread dalam pool
        executor.setQueueCapacity(250);  // Kapasitas antrian untuk tugas yang menunggu diproses
        executor.setThreadNamePrefix("custom_task_executor_thread");  // Nama thread
        executor.initialize();
        return executor;
    }
}

