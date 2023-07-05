package com.nimbleways.odinzeye.datacollector.databasequerycollector.invocationhandlers;


import com.nimbleways.odinzeye.datacollector.databasequerycollector.DataBaseQueriesEntityHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.util.List;

public class PreparedStmtInvocHandler implements InvocationHandler {
    private final PreparedStatement preparedStatement;
    private final List<String> boundParams;
    private final List<String> paramsDbTypes;
    private final List<String> paramsJavaTypes;
    public PreparedStmtInvocHandler(PreparedStatement preparedStatement, List<String> boundParams, List<String> paramsDbTypes, List<String> paramsJavaTypes)
    {
        this.preparedStatement = preparedStatement;
        this.boundParams = boundParams;
        this.paramsDbTypes = paramsDbTypes;
        this.paramsJavaTypes = paramsJavaTypes;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ParameterMetaData paramMetaData = preparedStatement.getParameterMetaData();
        if (method.getName().startsWith("set")) {
            DataBaseQueriesEntityHelper.AssignTypesToSqlValues(args, paramMetaData, boundParams, paramsDbTypes, paramsJavaTypes);
        }
        return method.invoke(preparedStatement, args);
    }
}
