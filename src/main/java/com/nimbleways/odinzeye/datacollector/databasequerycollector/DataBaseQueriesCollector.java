package com.nimbleways.odinzeye.datacollector.databasequerycollector;

import com.nimbleways.odinzeye.datacollector.databasequerycollector.invocationhandlers.ConnectionInvocHandler;
import com.nimbleways.odinzeye.websocket.IWSDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;
import java.sql.Connection;

@Slf4j
@Aspect
public class DataBaseQueriesCollector {

    @Autowired
    private DataBaseQueryEntity dataBaseQueryEntity;
    final private IWSDispatcher wsDispatcher;
    public DataBaseQueriesCollector(final IWSDispatcher wsDispatcher)
    {
        this.wsDispatcher = wsDispatcher;
    }
    private final static String API_POINTCUT = "execution(* javax.sql.DataSource.getConnection(..))";
    @Pointcut(API_POINTCUT)
    public void apiPointCut(){};

    @Around("apiPointCut()")
    public Object intercept(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Connection connection = (Connection) joinPoint.proceed();
        return Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                connection.getClass().getInterfaces(),
                new ConnectionInvocHandler(connection, dataBaseQueryEntity, wsDispatcher)
        );
    }
}
