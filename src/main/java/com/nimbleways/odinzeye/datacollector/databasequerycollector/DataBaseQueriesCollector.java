package com.nimbleways.odinzeye.datacollector.databasequerycollector;

import com.nimbleways.odinzeye.datacollector.databasequerycollector.invocationhandlers.ConnectionInvocHandler;
import com.nimbleways.odinzeye.datacollector.services.CurrentRequestIDUtils;
import com.nimbleways.odinzeye.websocket.IWSDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Optional;

@Slf4j
@Aspect
@Component
public class DataBaseQueriesCollector {

    private final DataBaseQueryEntity dataBaseQueryEntity;

    private final IWSDispatcher wsDispatcher;

    public DataBaseQueriesCollector(DataBaseQueryEntity dataBaseQueryEntity, final IWSDispatcher wsDispatcher)
    {
        this.dataBaseQueryEntity = dataBaseQueryEntity;
        this.wsDispatcher = wsDispatcher;
    }

    private static final String API_POINTCUT = "execution(* javax.sql.DataSource.getConnection(..))";

    @Pointcut(API_POINTCUT)
    public void apiPointCut(){}

    @Around("apiPointCut()")
    public Object interceptJDBCData(final ProceedingJoinPoint joinPoint) throws Throwable
    {

        boolean isInReq = inTheContextOfARequest();
        if(!inTheContextOfARequest()){
            return joinPoint.proceed();
        }
        final Connection connection = (Connection) joinPoint.proceed();

        return Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                connection.getClass().getInterfaces(),
                new ConnectionInvocHandler(connection, dataBaseQueryEntity, wsDispatcher)
        );
    }

    @Around("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))  && target(repository)")
    public Object interceptJpaRepositoryData(ProceedingJoinPoint joinPoint, JpaRepository<?,?> repository) throws Throwable
    {
        if(!inTheContextOfARequest()){
            return joinPoint.proceed();
        }

        dataBaseQueryEntity.setDispatchedFromJPA(true);

        Object result = joinPoint.proceed();

        String methodName = joinPoint.getSignature().getName();
        String className = repository.getClass().getInterfaces()[0].getSimpleName();
        dataBaseQueryEntity.setMethodName(methodName);
        dataBaseQueryEntity.setClassName(className);
        dataBaseQueryEntity.setQueryResult(result);

        if(result==null || (result instanceof Optional && ((Optional) result).isEmpty())){
            dataBaseQueryEntity.setQueryResult("");
        }

        DataBaseQueryEntity dataBaseQueryEntityCopy = new DataBaseQueryEntity();
        dataBaseQueryEntityCopy.mapper(dataBaseQueryEntity);

        wsDispatcher.sendCollectedDBQueries(dataBaseQueryEntityCopy);
        dataBaseQueryEntity.setSql(null);

        return result;
    }

    public boolean inTheContextOfARequest()
    {
        return CurrentRequestIDUtils.getCurrentRequestID() != null;
    }

}
