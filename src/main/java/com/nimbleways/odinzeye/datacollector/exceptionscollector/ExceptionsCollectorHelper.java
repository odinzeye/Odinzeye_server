package com.nimbleways.odinzeye.datacollector.exceptionscollector;

import com.nimbleways.odinzeye.datacollector.services.CurrentRequestIDUtils;
import com.nimbleways.odinzeye.websocket.IWSDispatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class ExceptionsCollectorHelper implements IExceptionsCollectorHelper {
    private final IWSDispatcher collectorWebSocketService;

    public ExceptionsCollectorHelper(final IWSDispatcher collectorWebSocketService) {
        this.collectorWebSocketService = collectorWebSocketService;
    }

    public void processException(final Exception exception) {
        final String simpleName = exception.getClass().getSimpleName();
        final String message = exception.getMessage();
        final String stackTrace = Arrays.toString(exception.getStackTrace());
        final String packageName = exception.getClass().getPackage().getName();

        final String requestID = CurrentRequestIDUtils.getCurrentRequestID();

        final StackTraceElement rootCause = exception.getStackTrace()[0];
        final String fileName = rootCause.getFileName();
        final String className = rootCause.getClassName();
        final String methodName = rootCause.getMethodName();
        final int line = rootCause.getLineNumber();
        final LocalDateTime timeStamp = LocalDateTime.now();
        ExceptionsCollectorEntity exceptionEntity = new ExceptionsCollectorEntity(simpleName, message, stackTrace, packageName, requestID, fileName, className, methodName, line, timeStamp);
        collectorWebSocketService.sendCollectedExceptions(exceptionEntity);
    }
}
