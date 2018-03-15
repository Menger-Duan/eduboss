package com.eduboss.datasource;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <pre>
 * 读/写动态数据库 决策者
 * 根据DataSourceType是write/read 来决定是使用读/写数据库
 * 通过ThreadLocal绑定实现选择功能
 * </pre>
 * @author Zhang Kaitao
 *
 */
public class ReadWriteDataSourceDecision  {
    
    private static final Logger logger = LoggerFactory.getLogger(ReadWriteDataSourceDecision.class);
    
	private static final ThreadLocal<Stack<DataSourceType>> contextHolder = new ThreadLocal<Stack<DataSourceType>>();
	//嵌套中有一个写，则全部操作到写库
	private static final ThreadLocal<Boolean> nestWrite = new ThreadLocal<Boolean>(){
		public Boolean initialValue(){
			return false;
		}
	};
	
	private static final ThreadLocal<Stack<String>> routeHolder = new ThreadLocal<Stack<String>>(){
		public Stack<String> initialValue() {
			return new Stack<String>();
		}
	};
	
    public enum DataSourceType {
        write, read;
    }
    
    /**
     * 记录方法调用栈
     * @param method
     */
    public static void markWrite(String method) {
    	routeHolder.get().push(method);
    	markWrite();
    }
    
    /**
     * 记录方法调用栈
     * @param method
     */
    public static void markRead(String method) {
    	routeHolder.get().push(method);
    	markRead();
    }
	
	public static void markWrite() {
		nestWrite.set(true);
		setDbType(DataSourceType.write);
    }
    
    public static void markRead() {
    	setDbType(DataSourceType.read);
    }
    
    
    public static boolean isChoiceNone() {
        return null == getDbType(); 
    }
    
    public static boolean isChoiceWrite() {
    	
    	//嵌套中有一个写，则全部操作到写库
    	DataSourceType dst = getDbType();
    	if(nestWrite.get()) {
    		if(dst == DataSourceType.read) {
    		    logger.warn("read method contains write operation! switch write datasource!");
    		}
    		return true;
    	}else{
    		return DataSourceType.write == dst;
    	}

    }
    
    public static boolean isChoiceRead() {
        return DataSourceType.read == getDbType();
    }
    
    public static void reset() {
    	popDbType();
    }
    
    public static void setDbType(DataSourceType dbType) {  
		Stack<DataSourceType> stack = getStack();
		if(stack == null){
			stack = new Stack<DataSourceType>();
		}
		stack.push(dbType);
		contextHolder.set(stack);
	}
	
	private static Stack<DataSourceType> getStack(){
		return contextHolder.get();
	}

	public static DataSourceType getDbType() {  
		Stack<DataSourceType> stack = getStack();
		if(stack != null && !stack.isEmpty()){
			DataSourceType dataSourceType = stack.firstElement();
			if(dataSourceType != null){
				return dataSourceType;
			}
			return null;
		}
		return null;
	}
	
	public static DataSourceType popDbType() {  
		Stack<DataSourceType> stack = getStack();
		if(stack != null && !stack.isEmpty()){
			DataSourceType dataSourceType = stack.pop();
			if(stack.isEmpty()){
				clearDbType();
			}
			return dataSourceType;
		}
		return null;
	}

	public static void clearDbType() {
	    logger.debug("MethodStack: {}", routeHolder.get());
	    logger.debug("NestWrite: {}", nestWrite.get());
		routeHolder.remove();
		nestWrite.set(false);//回收状态
		contextHolder.remove(); 
		//System.out.println("=================clear stack===================");
	}
	
    
    

}
