package com.eduboss.domainVo;

public class ClassStudentInfoVo {
    private String id;
    private String name;
    private String courseStatus;//是否结课
    public ClassStudentInfoVo(){
    	
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
    
}
