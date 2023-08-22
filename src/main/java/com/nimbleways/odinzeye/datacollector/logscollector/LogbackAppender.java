package com.nimbleways.odinzeye.datacollector.logscollector;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.nimbleways.odinzeye.websocket.IWSDispatcher;

import java.nio.charset.StandardCharsets;

public class LogbackAppender extends AppenderBase<ILoggingEvent>{

    private final PatternLayoutEncoder encoder;
    private final IWSDispatcher wsDispatcher;

    public LogbackAppender(final PatternLayoutEncoder encoder, final IWSDispatcher wsDispatcher)
    {
        this.encoder = encoder;
        this.wsDispatcher = wsDispatcher;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        String data = this.encoder.getLayout().doLayout(eventObject);
        wsDispatcher.sendCollectedLogs(new LogsEntity(eventObject.getLevel().levelStr, data));
    }
}
