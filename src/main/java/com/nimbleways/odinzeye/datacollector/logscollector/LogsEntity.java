package com.nimbleways.odinzeye.datacollector.logscollector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogsEntity {
    private String level;
    private String log;
}
