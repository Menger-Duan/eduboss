package com.eduboss.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eduboss.dto.DataPackage;

@Service
public interface CustomerMapAnalyzeService {
	
	/**
	 * 将地址转换成经纬度保存到库
	 * */
	public List<Map<String,Object>> TransactionAddressToLatitude();
	
	/**
	 * 保存客户经纬度
	 * */
	public void saveCustomerLatitude(String latitude);
	
	/**
	 * 查询客户经纬度
	 * */
	public DataPackage getCustomerLatitude(DataPackage dataPackage,String city, int pageNo, int pageSize,String grade, String blCampusName);
	
	/**
	 * 更新年级
	 * */
	public void updateStudentGrade();


	List<String> getSchoolSelectionByCity(String city);
}
