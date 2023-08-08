package com.nimbleways.odinzeye.datacollector.metricscollector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CPUEntity {
    long processCpuTime;
    double systemCpuLoad;
    double processCpuLoad;
}
