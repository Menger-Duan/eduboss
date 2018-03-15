package com.eduboss.service;

/**
 * 订单service
 * @author lixuejun
 *
 */
public interface OrderService {

    boolean storageOrder(String orderNo);
    
    void surrenderOrder(String orderNo);
    
}
