package com.eduboss.service;

import java.util.List;
import java.util.Map;

public interface ImportContractStuAndCusService {
	public List saveImportContractStuAndCusData(List<List<Object>> list);

	public List<Map<String, String>> saveImportMiniClass(List<List<Object>> list);
}
