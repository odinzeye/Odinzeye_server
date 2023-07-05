package com.nimbleways.odinzeye.datacollector.databasequerycollector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class DataBaseQueryEntity {
    private String sql;

    private String type;

    private String methodName;

    private String operation;

    private double executionDuration;

    private LocalDateTime timeStamp;

    private List<String> boundParams;

    private List<String> paramsDbTypes;

    private List<String> paramJavaTypes;

    private boolean isDispatchedFromJPA;

    private String requestID;

    private String className;

    private Object queryResult;

    public void mapper(DataBaseQueryEntity dataBaseQueryEntity){
        this.sql = dataBaseQueryEntity.getSql();
        this.type = dataBaseQueryEntity.getType();
        this.operation = dataBaseQueryEntity.getOperation();
        this.executionDuration = dataBaseQueryEntity.getExecutionDuration();
        this.timeStamp = dataBaseQueryEntity.getTimeStamp();
        this.boundParams = dataBaseQueryEntity.getBoundParams();
        this.paramsDbTypes = dataBaseQueryEntity.getParamsDbTypes();
        this.paramJavaTypes = dataBaseQueryEntity.getParamJavaTypes();
        this.isDispatchedFromJPA = dataBaseQueryEntity.isDispatchedFromJPA();
        this.requestID = dataBaseQueryEntity.requestID;
    }
}
