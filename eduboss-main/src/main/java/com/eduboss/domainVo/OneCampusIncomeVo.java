package com.eduboss.domainVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Yao
 * 分校收入
 * 2015-04-13
 */
public class OneCampusIncomeVo {

	private String campusId;
	private String campusName;
	private String total;
	private String quantity;
	
	List<Map<Object, Object>> income=new ArrayList<Map<Object, Object>>();//金额
	List<Map<Object, Object>> newStudent=new ArrayList<Map<Object, Object>>();//新增学生
	List<Map<Object, Object>> quantitys=new ArrayList<Map<Object, Object>>();//课消
	
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public List<Map<Object, Object>> getIncome() {
		return income;
	}
	public void setIncome(List<Map<Object, Object>> income) {
		this.income = income;
	}
	public List<Map<Object, Object>> getNewStudent() {
		return newStudent;
	}
	public void setNewStudent(List<Map<Object, Object>> newStudent) {
		this.newStudent = newStudent;
	}
	public List<Map<Object, Object>> getQuantitys() {
		return quantitys;
	}
	public void setQuantitys(List<Map<Object, Object>> quantitys) {
		this.quantitys = quantitys;
	}
	
	
}
