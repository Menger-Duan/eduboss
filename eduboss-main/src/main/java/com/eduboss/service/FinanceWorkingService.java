package com.eduboss.service;

import com.eduboss.jedis.FinanceMessage;


public interface FinanceWorkingService {
	public void saveFinanceByQueue(FinanceMessage messge);
}

