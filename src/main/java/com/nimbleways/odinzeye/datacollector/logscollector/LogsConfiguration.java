package com.nimbleways.odinzeye.datacollector.logscollector;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

import com.nimbleways.odinzeye.websocket.IWSDispatcher;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@AllArgsConstructor
public class LogsConfiguration {
    private final IWSDispatcher wsDispatcher;

    public void configure() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%date %-5level [%thread] %logger{36} - %msg%n");
        encoder.setContext(loggerContext);
        encoder.start();

        LogbackAppender appender = new LogbackAppender(encoder, wsDispatcher);
        appender.setContext(loggerContext);
        appender.start();

        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(appender);

        loggerContext.start();
    }
}
