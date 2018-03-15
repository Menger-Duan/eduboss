package com.eduboss.domainVo.EduPlatform;

/**
 * 
 * @author xiaojinwang
 * 未结课的班级列表
 */
public class UnChargedClassInfoVo {
  
	private String classId;//班级编号  双师的情况下是返回主班的id
	private String className;//班级名称
	private String gradeId;//年级编号
	private String gradeName;//年级名称
	private String subjectId;//学科编号
	private String subjectName;//学科名称
	private String type;//课程类型
	private String classIdTwo;//副班编号
	private String classNameTwo;//副班名称
	
	public UnChargedClassInfoVo(){
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassIdTwo() {
		return classIdTwo;
	}

	public void setClassIdTwo(String classIdTwo) {
		this.classIdTwo = classIdTwo;
	}

	public String getClassNameTwo() {
		return classNameTwo;
	}

	public void setClassNameTwo(String classNameTwo) {
		this.classNameTwo = classNameTwo;
	}
	
	
}
