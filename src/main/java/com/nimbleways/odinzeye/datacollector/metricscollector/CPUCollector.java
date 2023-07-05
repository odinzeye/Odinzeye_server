package com.nimbleways.odinzeye.datacollector.metricscollector;

import com.nimbleways.odinzeye.websocket.IWSDispatcher;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CPUCollector {
  private final OperatingSystemMXBean mxBean;
  private final ScheduledExecutorService executorService;
  private final IWSDispatcher collectorWebSocketService;

  public CPUCollector(IWSDispatcher collectorWebSocketService) {
    this.collectorWebSocketService = collectorWebSocketService;
    mxBean = ManagementFactory.getOperatingSystemMXBean();
    executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.scheduleAtFixedRate(this::getCpuTimeDetails, 0, 1, TimeUnit.SECONDS);
  }

  public void getCpuTimeDetails() {
    // getProcessCpuTime: Returns the CPU time used by the process on which the Java virtual machine is running in nanoseconds.

    long processCpuTime = ((com.sun.management.OperatingSystemMXBean) mxBean).getProcessCpuTime();
    double systemLoad = ((com.sun.management.OperatingSystemMXBean) mxBean).getCpuLoad();
    double processLoad = ((com.sun.management.OperatingSystemMXBean) mxBean).getProcessCpuLoad();

    CPUEntity cpuEntity = new CPUEntity(processCpuTime, systemLoad, processLoad);
    this.collectorWebSocketService.sendCollectedCPUUsage(cpuEntity);
  }
}
