package com.eduboss.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设定接口不能重复提交， 可以设定时间
 * @author sk
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForbiddenRecommit {

    /**
     * @return 重复提交限制间隔时间
     */
    int value() default 60;
    
}
