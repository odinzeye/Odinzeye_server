package com.nimbleways.odinzeye.datacollector.databasequerycollector.invocationhandlers;

import com.nimbleways.odinzeye.datacollector.databasequerycollector.DataBaseQueriesEntityHelper;
import com.nimbleways.odinzeye.datacollector.databasequerycollector.DataBaseQueryEntity;
import com.nimbleways.odinzeye.websocket.IWSDispatcher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConnectionInvocHandler implements InvocationHandler {

    private DataBaseQueryEntity dataBaseQueryEntity;
    final private Connection connection;
    final private IWSDispatcher wsDispatcher;

    public ConnectionInvocHandler (Connection connection, DataBaseQueryEntity dataBaseQueryEntity, IWSDispatcher wsDispatcher)
    {
        this.dataBaseQueryEntity = dataBaseQueryEntity;
        this.connection = connection;
        this.wsDispatcher = wsDispatcher;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String methodName = method.getName();
        if ("prepareStatement".equals(methodName) || "prepareCall".equals(methodName)) {
            List<String> boundParams = new ArrayList<>();
            List<String> paramsDbTypes = new ArrayList<>();
            List<String> paramsJavaTypes = new ArrayList<>();
            final LocalDateTime timeStamp = LocalDateTime.now();

            final long startTime = System.nanoTime();
            final PreparedStatement preparedStatement = (PreparedStatement) method.invoke(connection, args);
            final long endTime = System.nanoTime();

            final long executionDuration = endTime - startTime;

            final PreparedStatement proxyStatement = (PreparedStatement) Proxy.newProxyInstance(
                    preparedStatement.getClass().getClassLoader(),
                    preparedStatement.getClass().getInterfaces(),
                    new PreparedStmtInvocHandler(preparedStatement, boundParams, paramsDbTypes, paramsJavaTypes)
            );

            if(dataBaseQueryEntity.getSql() == null){
                final DataBaseQueryEntity jdbcData =
                        DataBaseQueriesEntityHelper
                                .build(
                                        args,
                                        methodName,
                                        executionDuration,
                                        timeStamp,
                                        boundParams,
                                        paramsDbTypes,
                                        paramsJavaTypes,
                                        dataBaseQueryEntity.isDispatchedFromJPA()
                                );
                dataBaseQueryEntity.mapper(jdbcData);
            }
            if(!dataBaseQueryEntity.isDispatchedFromJPA())
            {
                wsDispatcher.sendCollectedDBQueries(dataBaseQueryEntity);
                dataBaseQueryEntity.setSql(null);
            }
            return proxyStatement;
        }
        return method.invoke(connection, args);
    }

}
