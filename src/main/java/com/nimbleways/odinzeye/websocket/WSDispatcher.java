package com.nimbleways.odinzeye.websocket;

import com.nimbleways.odinzeye.datacollector.databasequerycollector.DataBaseQueryEntity;
import com.nimbleways.odinzeye.datacollector.exceptionscollector.ExceptionsCollectorEntity;
import com.nimbleways.odinzeye.datacollector.logscollector.LogsEntity;
import com.nimbleways.odinzeye.datacollector.metricscollector.CPUEntity;
import com.nimbleways.odinzeye.datacollector.metricscollector.HeapMemoryEntity;
import com.nimbleways.odinzeye.datacollector.services.CurrentRequestIDUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WSDispatcher implements IWSDispatcher {
    private final SimpMessagingTemplate template;

    private static final String BASE_PATH = "/collectors";

    private static final String DB_QUERY_PATH = "/db-queries";
    private static final String EXCEPTIONS_PATH = "/exceptions";
    private static final String HEAP_MEMORY_PATH = "/metrics/heap";
    private static final String CPU_PATH = "/metrics/cpu";
    private static final String LOGS_PATH = "/backend-logs";
    public WSDispatcher(final SimpMessagingTemplate template)
    {
        this.template = template;
    }
    @Override
    public void sendCollectedDBQueries(final DataBaseQueryEntity dataBaseQueryEntity){
        String CLIENT_ID = CurrentRequestIDUtils.getCurrentClientID();
        template.convertAndSend(BASE_PATH + DB_QUERY_PATH +  "/" + CLIENT_ID, dataBaseQueryEntity);
    }

    @Override
    public void sendCollectedExceptions(final ExceptionsCollectorEntity exceptionsCollector) {
        String CLIENT_ID = CurrentRequestIDUtils.getCurrentClientID();
        template.convertAndSend( BASE_PATH + EXCEPTIONS_PATH +  "/" + CLIENT_ID, exceptionsCollector);
    }

    @Override
    public void sendCollectedMemoryHeap(HeapMemoryEntity heapMemoryEntity) {
        template.convertAndSend(BASE_PATH + HEAP_MEMORY_PATH, heapMemoryEntity);
    }

    @Override
    public void sendCollectedCPUUsage(CPUEntity cpuEntity) {
        template.convertAndSend(BASE_PATH + CPU_PATH, cpuEntity);
    }

    @Override
    public void sendCollectedLogs(LogsEntity logsEntity) {
        template.convertAndSend(BASE_PATH + LOGS_PATH, logsEntity);
    }
}
