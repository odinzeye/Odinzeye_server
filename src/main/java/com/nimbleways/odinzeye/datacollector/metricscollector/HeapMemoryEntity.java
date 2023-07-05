package com.nimbleways.odinzeye.datacollector.metricscollector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeapMemoryEntity {
    // Initial: Initial memory the JVM requests from the OS during startup xms
    // Used: The current amount of memory used by the JVM
    // Max: The maximum memory available to the JVM. If this limit is reached an OutOfMemoryException may be thrown xmx
    // Committed: The amount of memory guaranteed to be available to the JVM
    private double initialMemory;
    private double usedMemory;
    private double maxHeapMemory;
    private double committedMemory;
    private LocalTime timestamp;
}
