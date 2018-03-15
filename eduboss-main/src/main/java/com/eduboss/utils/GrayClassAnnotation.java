package com.eduboss.utils;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/3/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GrayClassAnnotation {
    String value() default "grayClassAnnotation";
}
