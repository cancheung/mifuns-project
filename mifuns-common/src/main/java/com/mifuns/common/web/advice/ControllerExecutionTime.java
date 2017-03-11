package com.mifuns.common.web.advice;

import com.mifuns.common.util.NetworkUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by miguangying on 2017/3/11.
 */
@Aspect
public class ControllerExecutionTime {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExecutionTime.class);

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMappingMethod(){

    }


    @Around("requestMappingMethod()")
    public Object aroundControllerAdvice(ProceedingJoinPoint joinPoint)throws Throwable{
        return executeTimeLog(joinPoint);
    }
    private Object executeTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long begin = System.currentTimeMillis();
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            Signature sig = joinPoint.getSignature();
            String className = sig.getDeclaringType().getName();
            String method = sig.getName();
            float execTime = (end - begin);
            //logger.info(String.format("[%s#%s] Executed %f ms.", className, method, execTime));
            //class_name,method,exectime,server_host,client_host
            HttpServletRequest request = getRequest();
            logger.info("{}|{}|Executed|{}|ms|{}|{}", className, method, execTime, NetworkUtil.getServerHost(request), NetworkUtil.getRemoteIP(request));
        }
    }

    private HttpServletRequest getRequest() {
        return (RequestContextHolder.getRequestAttributes() == null) ? null : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
