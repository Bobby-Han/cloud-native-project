package com.nju15.cloudnativeproject.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

// 使用令牌桶算法的限流方式
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Limit {
    /**
     * 实现不同的接口不同的流量控制
     * key是方法的名字
     */
    String key() default "";

    /**
     * 每秒最多的访问限制次数
     */
    double permitsPerSecond () ;

    /**
     * 获取令牌最大等待时间
     */
    long timeout() default 0;

    /**
     * 获取令牌最大等待时间,单位(例:分钟/秒/毫秒) 默认:毫秒
     */
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    /**
     * 得不到令牌的提示语
     */
    String msg() default "系统繁忙,请稍后再试.";
}
