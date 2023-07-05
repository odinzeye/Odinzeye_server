package com.nimbleways.odinzeye.websocket;


import com.nimbleways.odinzeye.datacollector.databasequerycollector.DataBaseQueryEntity;
import com.nimbleways.odinzeye.datacollector.exceptionscollector.ExceptionsCollectorEntity;
import com.nimbleways.odinzeye.datacollector.logscollector.LogsEntity;
import com.nimbleways.odinzeye.datacollector.metricscollector.CPUEntity;
import com.nimbleways.odinzeye.datacollector.metricscollector.HeapMemoryEntity;

public interface IWSDispatcher {
    void sendCollectedDBQueries(DataBaseQueryEntity dataBaseQueryEntity);
    void sendCollectedExceptions(ExceptionsCollectorEntity exceptionsCollector);
    void sendCollectedMemoryHeap(HeapMemoryEntity heapMemoryEntity);
    void sendCollectedCPUUsage(CPUEntity cpuEntity);
    void sendCollectedLogs(LogsEntity logsEntity);
}
