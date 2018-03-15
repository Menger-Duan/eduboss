package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.LiveTransferPayDao;
import com.eduboss.domain.LiveTransferPay;

@Repository("liveTransferPayDao")
public class LiveTransferPayDaoImpl extends GenericDaoImpl<LiveTransferPay,String> implements LiveTransferPayDao {

}
