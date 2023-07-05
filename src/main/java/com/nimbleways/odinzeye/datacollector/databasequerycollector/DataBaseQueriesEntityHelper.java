package com.nimbleways.odinzeye.datacollector.databasequerycollector;



import com.nimbleways.odinzeye.datacollector.services.CurrentRequestIDUtils;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DataBaseQueriesEntityHelper {
    public static DataBaseQueryEntity build(final Object[] args, final String methodName, long executionTime, LocalDateTime timeStamp, List<String> boundParams, List<String> paramsDbTypes, List<String> paramJavaTypes, boolean isDispatchedFromJPA) {
        final String requestID = CurrentRequestIDUtils.getCurrentRequestID();

        String type = "";
        final String sql = args[0].toString();
        final String operation = Arrays.stream(sql.split("\\s+")).toList().get(0);
        switch (methodName) {
            case "prepareStatement":
                type = "Prepared Statement";
                break;
            case "prepareCall":
                type = "Callable Statement";
                break;
            default:
                type = "Undefined Statement Type";
                break;
        }
        return new DataBaseQueryEntity(sql,type,null, operation, executionTime, timeStamp, boundParams, paramsDbTypes, paramJavaTypes, isDispatchedFromJPA,requestID,null,null);
    }
    public static void AssignTypesToSqlValues(final Object[] stmtArgs, ParameterMetaData paramMetaData, List<String> boundParams, List<String> paramsDbTypes, List<String> paramsJavaTypes) throws SQLException {
        final int paramIndex = Integer.parseInt(stmtArgs[0].toString());
        String paramDbType = paramMetaData.getParameterClassName(paramIndex);
        String paramJavaType = paramMetaData.getParameterTypeName(paramIndex);
        boundParams.add(paramIndex - 1, stmtArgs[1].toString());
        paramsDbTypes.add(paramIndex - 1, paramDbType);
        paramsJavaTypes.add(paramIndex - 1, paramJavaType);
    }
}