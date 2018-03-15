package com.eduboss.utils;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/3/16.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GrayMethodAnnotation {
    String id() default "没有定义";
    String name() default "没有定义";
}
