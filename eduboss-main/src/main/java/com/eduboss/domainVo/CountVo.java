package com.eduboss.domainVo;

public class CountVo { 
	private Integer count=0; //个人工作日程数
	private Integer systemNotReadNum=0;//系统消息未读数
	private Integer courseUntreatedCount=0; // 课程管理未处理数
	private Integer customerUntreatedCount=0;//客户管理未处理数

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getSystemNotReadNum() {
		return systemNotReadNum;
	}

	public void setSystemNotReadNum(Integer systemNotReadNum) {
		this.systemNotReadNum = systemNotReadNum;
	}

	public Integer getCourseUntreatedCount() {
		return courseUntreatedCount;
	}

	public void setCourseUntreatedCount(Integer courseUntreatedCount) {
		this.courseUntreatedCount = courseUntreatedCount;
	}

	public Integer getCustomerUntreatedCount() {
		return customerUntreatedCount;
	}

	public void setCustomerUntreatedCount(Integer customerUntreatedCount) {
		this.customerUntreatedCount = customerUntreatedCount;
	}

}
