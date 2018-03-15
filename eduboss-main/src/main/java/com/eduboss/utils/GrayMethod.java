package com.eduboss.utils;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/3/16.
 */
public class GrayMethod implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger = LoggerFactory.getLogger(GrayMethod.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.debug("------初始化执行----");
        try {
            // 获取上下文
            ApplicationContext context = event.getApplicationContext();
            // 获取所有beanNames
            String[] beanNames = context.getBeanNamesForType(Object.class);
            for (String beanName : beanNames) {

                GrayClassAnnotation GrayClassAnnotation = context.findAnnotationOnBean(beanName,
                        GrayClassAnnotation.class);
                //判断该类是否含有GrayClassAnnotation注解
                if (GrayClassAnnotation != null) {
                    Method[] methods = context.getBean(beanName).getClass()
                            .getDeclaredMethods();
                    for (Method method : methods) {
                        //判断该方法是否有HelloMethod注解
                        if (method.isAnnotationPresent(GrayMethodAnnotation.class)) {
                            GrayMethodAnnotation annMethod = method
                                    .getAnnotation(GrayMethodAnnotation.class);
                            String id = annMethod.id();
                            String name = annMethod.name();
                            // do something
                            logger.debug("注解方法：" + method.getName() + "," + id
                                    + "," + name);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
