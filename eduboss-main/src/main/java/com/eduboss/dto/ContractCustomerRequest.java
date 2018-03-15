package com.eduboss.dto;

import java.util.List;
import com.eduboss.domain.Student;

/**
 * 这个是VOJO 从contract界面传过来的学员信息
 * @author robinzhang
 *
 */
public class ContractCustomerRequest {
	
	private String customerId;
	private String cus_name;
	private String contact;
	private String consultant;
	//这里是一对多个关系。
	private List<Student> stuList;
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public ContractCustomerRequest(String cus_name, String contact,
			String consultant, List<Student> stuList) {
		super();
		this.cus_name = cus_name;
		this.contact = contact;
		this.consultant = consultant;
		this.stuList = stuList;
	}
	
	public ContractCustomerRequest() {}
	
	public String getCus_name() {
		return cus_name;
	}
	public void setCus_name(String cus_name) {
		this.cus_name = cus_name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getConsultant() {
		return consultant;
	}
	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}
	public List<Student> getStuList() {
		return stuList;
	}

	public void setStuList(List<Student> stuList) {
		this.stuList = stuList;
	}
	
	
	
}
