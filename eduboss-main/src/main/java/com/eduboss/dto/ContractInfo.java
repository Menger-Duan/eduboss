package com.eduboss.dto;

import java.util.List;

import com.eduboss.common.ContractType;
import com.eduboss.domain.CourseVoForContract;
import com.eduboss.domain.Student;

public class ContractInfo {
	private List<CourseVoForContract> oneOnOneCourseList;
	private List<CourseVoForContract> smallClassCourseList;
	private List<CourseVoForContract> otherCourseList;
	private Student student;//学院编号
	private ContractType contractType;//合同类型
	private String customerId;//客户编号
	private Double totalAmount;//合同总额
	private Double depositAmount;//订金总额
	private Double paidAmount;//本次实收
	
	private Double cashAmount;//现金
	private Double slotCardAmount;//刷卡
	private Double transferAccountsAmount;//转账
	
	public List<CourseVoForContract> getOneOnOneCourseList() {
		return oneOnOneCourseList;
	}
	public void setOneOnOneCourseList(List<CourseVoForContract> oneOnOneCourseList) {
		this.oneOnOneCourseList = oneOnOneCourseList;
	}
	public List<CourseVoForContract> getSmallClassCourseList() {
		return smallClassCourseList;
	}
	public void setSmallClassCourseList(List<CourseVoForContract> smallClassCourseList) {
		this.smallClassCourseList = smallClassCourseList;
	}
	public List<CourseVoForContract> getOtherCourseList() {
		return otherCourseList;
	}
	public void setOtherCourseList(List<CourseVoForContract> otherCourseList) {
		this.otherCourseList = otherCourseList;
	}
	
	
	
}
