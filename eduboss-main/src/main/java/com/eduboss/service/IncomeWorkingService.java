package com.eduboss.service;

import com.eduboss.jedis.IncomeMessage;


public interface IncomeWorkingService {
	public void saveIncomeByQueue(IncomeMessage messge);
}

