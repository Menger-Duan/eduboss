package com.eduboss.domainVo;

import com.eduboss.common.FunctionsLevel;
import com.eduboss.common.TeacherFunctions;

public class TeacherFunctionVersionVo {

	private int id; 
	private int teacherVersionId; // 授课老师版本Id
	private TeacherFunctions teacherFunctions; // 老师职能
	private FunctionsLevel functionsLevel; // 职能级别
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTeacherVersionId() {
		return teacherVersionId;
	}
	public void setTeacherVersionId(int teacherVersionId) {
		this.teacherVersionId = teacherVersionId;
	}
	public TeacherFunctions getTeacherFunctions() {
		return teacherFunctions;
	}
	public void setTeacherFunctions(TeacherFunctions teacherFunctions) {
		this.teacherFunctions = teacherFunctions;
	}
	public FunctionsLevel getFunctionsLevel() {
		return functionsLevel;
	}
	public void setFunctionsLevel(FunctionsLevel functionsLevel) {
		this.functionsLevel = functionsLevel;
	}
}
