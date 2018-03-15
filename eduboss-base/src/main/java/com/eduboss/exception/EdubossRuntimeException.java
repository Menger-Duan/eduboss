/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.exception;

/**
 * 系统通用运行时异常
 *
 * @author <a href="xiangshaoxu@wxchina.com">xiangshaoxu</a>
 * @version 1.0.0
 * @date 2015年11月17日
 */
public class EdubossRuntimeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 5448767889546896548L;

    /**
     *
     */
    public EdubossRuntimeException() {}

    /**
     * @param message
     */
    public EdubossRuntimeException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public EdubossRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public EdubossRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public EdubossRuntimeException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
