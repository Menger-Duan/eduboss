package com.eduboss.utils;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/3/16.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmitAnnotation {
    int time() default 10;
}
