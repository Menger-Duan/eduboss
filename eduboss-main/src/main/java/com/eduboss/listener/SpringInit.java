package com.eduboss.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**@author wmy
 *@date 2015年11月6日上午10:57:00
 *@version 1.0 
 *@description
 */
public class SpringInit implements ServletContextListener {
    

    private static WebApplicationContext springContext;
    
    public SpringInit() {
        super();
    }
    
    public void contextInitialized(ServletContextEvent event) {
        springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
    }
    

    public void contextDestroyed(ServletContextEvent event) {
    }
    
    public static ApplicationContext getApplicationContext() {
        return springContext;
    }

    
}


