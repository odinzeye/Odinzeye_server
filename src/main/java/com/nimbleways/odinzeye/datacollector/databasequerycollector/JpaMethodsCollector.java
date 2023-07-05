package com.nimbleways.odinzeye.datacollector.databasequerycollector;


import com.nimbleways.odinzeye.websocket.IWSDispatcher;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.jpa.repository.JpaRepository;

@Aspect
public class JpaMethodsCollector {

    private final DataBaseQueryEntity dataBaseQueryEntity;

    private final IWSDispatcher wsDispatcher;
    public JpaMethodsCollector(final DataBaseQueryEntity dataBaseQueryEntity, final IWSDispatcher wsDispatcher){

        this.dataBaseQueryEntity = dataBaseQueryEntity;
        this.wsDispatcher = wsDispatcher;
    }

    @Around("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))  && target(repository)")
    public Object interceptJpaRepositoryMethods(ProceedingJoinPoint joinPoint, JpaRepository<?,?> repository) throws Throwable {
        dataBaseQueryEntity.setDispatchedFromJPA(true);

        Object result = joinPoint.proceed();

        String methodName = joinPoint.getSignature().getName();
        String className = repository.getClass().getInterfaces()[0].getSimpleName();
        dataBaseQueryEntity.setMethodName(methodName);
        dataBaseQueryEntity.setClassName(className);
        dataBaseQueryEntity.setQueryResult(result);

        wsDispatcher.sendCollectedDBQueries(dataBaseQueryEntity);
        dataBaseQueryEntity.setSql(null);

        return result;
    }

}
