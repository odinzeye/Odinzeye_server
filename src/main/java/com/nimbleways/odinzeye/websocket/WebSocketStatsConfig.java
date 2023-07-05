package com.nimbleways.odinzeye.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

@Configuration
public class WebSocketStatsConfig {
    @Autowired
    public void configureWebSocketStats(WebSocketMessageBrokerStats stats) {
        stats.setLoggingPeriod(0);
    }
}