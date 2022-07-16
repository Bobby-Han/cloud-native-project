package com.nju15.cloudnativeproject.aop;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.nju15.cloudnativeproject.annotation.Limit;
import com.nju15.cloudnativeproject.exception.LimitException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
@Aspect
@Component
public class LimitAOP{
//    private static final Logger logger = LoggerFactory.getLogger("LimitLogger");

    private final Map<String, RateLimiter> limitMap= Maps.newConcurrentMap();

    @Pointcut("@annotation(com.nju15.cloudnativeproject.annotation.Limit)")
    public void RequestLimit(){

    }
    @Around("RequestLimit()")
    public Object RequestLimit(ProceedingJoinPoint joinPoint)throws Throwable{
        Limit limit=getRequestLimit(joinPoint);
        if(limit!=null){
            String key=limit.key();
            RateLimiter rateLimiter=null;
            if(!limitMap.containsKey(key)){
                // 还没有对应key的令牌桶
                rateLimiter=RateLimiter.create(limit.permitsPerSecond());
                limitMap.put(key,rateLimiter);
            }
            rateLimiter=limitMap.get(key);
            // 尝试拿令牌
            boolean acquire=rateLimiter.tryAcquire(limit.timeout(),limit.timeunit());
            // 没有获取
            if(!acquire){
                return new LimitException(limit.msg());
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 得到具体的limit注解信息
     */
    private Limit getRequestLimit(final JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        String name = joinPoint.getSignature().getName();
        if (!StringUtils.isEmpty(name)) {
            for (Method method : methods) {
                Limit annotation = method.getAnnotation(Limit.class);
                if (!Objects.isNull(annotation) && name.equals(method.getName())) {
                    return annotation;
                }
            }
        }
        return null;
    }
}
