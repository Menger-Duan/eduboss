package com.eduboss.domainVo;


 
public class CustomerFollowUpRecrodsVo {	 
	
	private String remark;//备注
	private String createUserName;//跟进人
	private String planTime;  //预约或下次跟进时间
	private String type;  //类型
	private String schoolName;  //校区	
	private String createTime;  //创建时间
	private String satisficingName; //意向度
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}	
	public String getPlanTime() {
		return planTime;
	}
	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSatisficingName() {
		return satisficingName;
	}

	public void setSatisficingName(String satisficingName) {
		this.satisficingName = satisficingName;
	}
}
