package com.nimbleways.odinzeye.datacollector;


import com.nimbleways.odinzeye.datacollector.logscollector.LogsCollector;
import com.nimbleways.odinzeye.websocket.IWSDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugBarConfig {

    @Bean
    public LogsCollector logsConfiguration(final IWSDispatcher wsDispatcher)
    {
        LogsCollector config = new LogsCollector(wsDispatcher);
        config.configure();
        return config;
    }

}