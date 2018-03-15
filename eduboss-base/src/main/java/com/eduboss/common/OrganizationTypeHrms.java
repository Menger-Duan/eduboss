package com.eduboss.common;


public enum OrganizationTypeHrms {
	
	// GROUP -集团， HIERARCHY -体系， REGION -省区， CENTER, -集团中心, BRANCH -分公司, GROUP_DEPARTMENT -集团职能部门, GROUP_TEAM -集团科/组, DEPARTMENT -校区/分公司职能部门, BRANCH_TEAM -分公司科/组
	GROUP("GROUP", "集团"),
	HIERARCHY("HIERARCHY", "体系"), 
	REGION("REGION", "省区"),
	REGION_DEPT("REGION_DEPT","省区职能部门"),
	CENTER("CENTER", "集团中心"),
	BRANCH("BRANCH", "分公司"),
	CAMPUS("CAMPUS", "校区"),
	CAMPUS_TEAM("CAMPUS_TEAM","校区科组"),
	SUB_CAMPUS("SUB_CAMPUS","分校"),
	SUB_CAMPUS_DEPT("SUB_CAMPUS_DEPT","分校职能部门"),
	SUB_CAMPUS_TEAM("SUB_CAMPUS_TEAM","分校科/组"),
	SERVICE_CENTER("SERVICE_CENTER","服务中心"),
	TEACHING_VENUE("TEACHING_VENUE","教学点"),
	GROUP_DEPARTMENT("GROUP_DEPARTMENT", "集团职能部门"),
	GROUP_TEAM("GROUP_TEAM","集团科/组"),
	BRANCH_DEPT("BRANCH_DEPT","分公司职能部门"),
	BRANCH_TEAM("BRANCH_TEAM", "分公司科/组");
	
	private String value;
	private String name;
	
	private OrganizationTypeHrms(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
