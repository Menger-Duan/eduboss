package com.eduboss.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ApplicationContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext context;//用spring注入的一个context
	
	public void setApplicationContext(ApplicationContext contex) throws BeansException {
		ApplicationContextUtil.context=contex;
	}
	
	public static ApplicationContext getContext(){
	  return context;
	}
}
