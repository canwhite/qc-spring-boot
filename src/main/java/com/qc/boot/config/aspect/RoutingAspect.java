package com.qc.boot.config.aspect;

import com.qc.boot.config.annotation.RoutingWithSlave;
import com.qc.boot.config.context.RoutingDataSourceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 功能描述: aspect
 *
 * @author lijinhua
 * @date 2022/8/24 15:50
 */
@Aspect
@Component
public class RoutingAspect {
    @Around("@annotation(routingWithSlave)")
    public Object routingWithDataSource(ProceedingJoinPoint joinPoint, RoutingWithSlave routingWithSlave) throws  Throwable{
        //thread local 存值
        try(RoutingDataSourceContext cx = new RoutingDataSourceContext(RoutingDataSourceContext.SLAVE_DATASOURCE)){
            return  joinPoint.proceed();
        }
    }
}
