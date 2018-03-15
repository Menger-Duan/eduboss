/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.exception;

/**
 * 系统受控异常父类
 * 
 * @author xiangshaoxu 2016年6月6日下午9:37:47
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class EdubossException extends Exception{


    /**
     *
     */
    public EdubossException() {}

    /**
     * @param message
     */
    public EdubossException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public EdubossException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public EdubossException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public EdubossException(String message, Throwable cause, boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
