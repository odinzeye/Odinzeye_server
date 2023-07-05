package com.nimbleways.odinzeye.datacollector.logscollector;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.nimbleways.odinzeye.websocket.IWSDispatcher;

import java.nio.charset.StandardCharsets;

public class LogbackAppender extends AppenderBase<ILoggingEvent>{

    final private PatternLayoutEncoder encoder;
    final private IWSDispatcher wsDispatcher;

    public LogbackAppender(final PatternLayoutEncoder encoder, final IWSDispatcher wsDispatcher)
    {
        this.encoder = encoder;
        this.wsDispatcher = wsDispatcher;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        byte[] data = this.encoder.encode(eventObject);
        wsDispatcher.sendCollectedLogs(new LogsEntity(eventObject.getLevel().levelStr, new String(data, StandardCharsets.UTF_8)));
    }
}
