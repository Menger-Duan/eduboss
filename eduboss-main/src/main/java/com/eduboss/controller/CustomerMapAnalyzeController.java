package com.eduboss.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eduboss.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.dao.CustomerMapAnalyzeDao;
import com.eduboss.domain.CustomerMapAnalyze;
import com.eduboss.service.CustomerMapAnalyzeService;
import com.eduboss.utils.ReadExcel;
import com.eduboss.dto.SelectOptionResponse.NameValue;

@Controller
@RequestMapping(value = "/CustomerMapController")
public class CustomerMapAnalyzeController {
	
	@Autowired
	private CustomerMapAnalyzeService customerMapAnalyzeService;
	
	@Autowired
	private CustomerMapAnalyzeDao customerMapAnalyzeDao;
	
	@RequestMapping(value = "/transationAddressToLatitude")
	@ResponseBody
	public Response transactionAddressToLatitude(){
//	public List<CustomerMapAnalyzeVo> transactionAddressToLatitude(HttpServletRequest request){
//		String tableName = "Student_parent_info_01";
//		if(StringUtil.isNotBlank(request.getParameter("tableName"))){
//			tableName = request.getParameter("tableName");
//		}
		customerMapAnalyzeService.TransactionAddressToLatitude();
		return new Response(); 
	}
	
	@RequestMapping(value = "/saveCustomerLatitude")
	@ResponseBody
	public Response saveCustomerLatitude(String latitudes){
		//JSONObject obj = new JSONObject();
		//JSONObject jsonObject = JSONObject.fromObject(obj);
		
		
		customerMapAnalyzeService.saveCustomerLatitude(latitudes);
		return new Response();
	}
	
	@RequestMapping(value = "/findCustomerForMapView")
	@ResponseBody
	public DataPackageForJqGrid getCustomerLatitude(GridRequest gridRequest,int pageNo, int pageSize,String grade,String city, String blCampusName){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = customerMapAnalyzeService.getCustomerLatitude(dataPackage,city,pageNo,pageSize,grade, blCampusName);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}

	@RequestMapping(value = "/getSchoolSelectionByCity",method = RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getSchoolSelectionByCity(@RequestParam String city){
		List<NameValue> nvs= new ArrayList<NameValue>();
		List<String> schools= customerMapAnalyzeService.getSchoolSelectionByCity(city);
		if(schools!=null){
			for(String school: schools){
				nvs.add(SelectOptionResponse.buildNameValue(school,school));
			}
		}
		SelectOptionResponse selectOptionResponse= new SelectOptionResponse(nvs);
//		selectOptionResponse.getValue().put("","请选择");
		return  selectOptionResponse;
	}
	
	
	/**
	 * 导入
	 * @throws IOException 
	 * @file 文件路径
	 * @sheetAt 工作表数
	 * */
	@RequestMapping(value = "/importExcelData")
	@ResponseBody
	public List importExcelData(@RequestParam String file, @RequestParam int sheetAt) throws IOException{
		List<List<Object>> list=ReadExcel.readExcel(new File(file),sheetAt);
		for(List<Object> obj : list){
			String[] arr = obj.toString().split(",");
			CustomerMapAnalyze customer = new CustomerMapAnalyze();
			customer.setId(null);
			customer.setSchoolName(arr[0]);
			customer.setStudentName(arr[1]);
			customer.setClassName(arr[2]);
			customer.setContact(arr[3]);
			customer.setFather(arr[4]);
			customer.setFatherContact(arr[5]);
			customer.setFatherAddress(arr[6]);
			customer.setMother(arr[7]);
			customer.setMotherContact(arr[8]);
			customer.setMotherAddress(arr[9]);
			customerMapAnalyzeDao.save(customer);
			//System.out.println(customer);
		}
		return list;
	}
	
	
//	public static void main(String[] args) throws IOException {
//		//File file = new File("F:/studentParentInfo.xls.xlsx");
//		List<List<Object>> list=ReadExcel.readExcel(new File("F:/studentParentInfo.xls.xlsx"),2);
//		for(List<Object> l : list){
//			System.out.println(l.toString());
//		}
//	} 
	
	@RequestMapping(value = "/updateStudentGrade")
	@ResponseBody
	public Response updateStudentGrade(){
		customerMapAnalyzeService.updateStudentGrade();
		return new Response();
	}
}
