package com.eduboss.common;


public enum DataDictCategory {
	
	RES_TYPE("RES_TYPE", "资源类型"),//资源类型 ENUM
	CUS_ORG("CUS_ORG", "市场来源"),//市场来源
	//CUS_STATUS("CUS_STATUS", "客户状态"),//客户状态
	STUDENT_GRADE("STUDENT_GRADE", "学生年级"),//学生年级
	STUDENT_GRADE_SEGMENT("STUDENT_GRADE_SEGMENT", "学生年级段"),//新加的学生年级段
	//STUDENT_SCHOOL("STUDENT_SCHOOL", "学生学校"),//学生学校
	STAFF_GRADUATE_SCHOOL("STAFF_GRADUATE_SCHOOL", "员工毕业学校"),//员工毕业学校
	REFUND_RESULT_CATEGORY("REFUND_RESULT_CATEGORY", "退费原因"), //退费原因
	SUBJECT("SUBJECT", "科目"), //科目
	//CON_TYPE("CON_TYPE", "合同类型"), //合同类型 ENUM
	COURSE_TIME("COURSE_TIME", "上课时间"),// 上课时间
	MINI_CLASS_TYPE("MINI_CLASS_TYPE", "小班类型"),
	MINI_CLASS_STATUS("MINI_CLASS_STATUS","小班状态"),
	CONTRACT_REFUN_RESON_CAETEGORY("CONTRACT_REFUN_RESON_CAETEGORY", "退费原因类型"),//退费原因类型
	DISCOUNT_RATE("DISCOUNT_RATE", "折扣率"),
	PROVINCE("PROVINCE" , "省份"),//省份
	CITY("CITY" , "城市"), //城市
	YEAR("YEAR" , "年份"), //年份
	PLAN_TARGET_TYPE("PLAN_TARGET_TYPE","计划指标类型"),//计划指标类型
	EXAM_TYPE("EXAM_TYPE","考试类型"),//考试类型
	SCHOOL_LEVEL("SCHOOL_LEVEL","学校级别"),
	PRODUCT_GROUP("PRODUCT_GROUP","产品组"),
	REPERTORY_PRODUCT_UNIT("REPERTORY_PRODUCT_UNIT","库存单位"),
	REPERTORY_PRODUCT_TYPE("REPERTORY_PRODUCT_TYPE","库存类别"),
	EXPENDITURE_TYPE("EXPENDITURE_TYPE","支出类型"),
	CLASSROOM_TYPE("CLASSROOM_TYPE","教室类型") ,
	CLASSROOM_EQUITMENT("CLASSROOM_EQUITMENT","教室设备"), 
	CUSTOMER_SATISFICING("CUSTOMER_SATISFICING","客户意向度"),
	DOCUMENT_TYPE("DOCUMENT_TYPE","学生档案类型"),//学生档案类型   add  by yao  2015-04-09
	NOTICE_TYPE("NOTICE_TYPE","公告类型"),//学生档案类型   add  by yao  2015-04-20
	SUMMARY_TYPE("SUMMARY_TYPE","计划总结类型"),
	INTENTION_OF_THE_CUSTOMER("INTENTION_OF_THE_CUSTOMER","客户意向度分级"),
	INTENTION_SUBJECT("INTENTION_SUBJECT","意向科目"),
	PURCHASING_POWER("PURCHASING_POWER","购买力分级"),
	COURSE_SERIES("COURSE_SERIES", "课程系列"),
	PRODUCT_VERSION("PRODUCT_VERSION", "产品版本"),
	PRODUCT_QUARTER("PRODUCT_QUARTER", "产品季度"),
	CLASS_TYPE("CLASS_TYPE", "班级类型"),
	RECRUIT_STUDENT_STATUS("RECRUIT_STUDENT_STATUS", "招生状态"),
	REFUND_TYPE("REFUND_TYPE", "退费原因"),
	
	USER_INFO_NATIONALITY("USER_INFO_NATIONALITY", "民族"),
	USER_INFO_POLITICS_STATUS("USER_INFO_POLITICS_STATUS", "政治面貌"),
	USER_INFO_MARITAL_STATUS("USER_INFO_MARITAL_STATUS", "婚姻状态"),
	USER_INFO_NATIVE_PLACE("USER_INFO_NATIVE_PLACE", "籍贯"),
	USER_INFO_HIGHEST_EDUCATION("USER_INFO_HIGHEST_EDUCATION", "最高学历"),
	USER_INFO_WORK_TYPE("USER_INFO_WORK_TYPE", "员工类型"),
	USER_INFO_LEAVE_TYPE("USER_INFO_LEAVE_TYPE", "离职形式"),
	USER_INFO_POSITIONING("USER_INFO_POSITIONING", "人员定位"),
	USER_INFO_TEACHER_STAR("USER_INFO_TEACHER_STAR", "教师星级"),
	USER_INFO_JOB_LEVEL("USER_INFO_JOB_LEVEL", "职级"),
	
	FEEDBACK_TYPE("FEEDBACK_TYPE","反馈类型"), // add by tangyuping 2015-10-21
	FEEDBACK_LEVEL("FEEDBACK_LEVEL","反馈相关"),
	ROLLBACK_CAUSE("ROLLBACK_CAUSE", "资金回滚原因"), // add by lixuejun 2015-10-28
	
	
	CUSTOMER_PROJECT("CUSTOMER_PROJECT","项目"), // add by tangyuping 2015-11-13 
	RES_ENTRANCE("RES_ENTRANCE","资源入口"),

    STATE_TYPE("STATE_TYPE", "状态"),
    
    OTM_TYPE("OTM_TYPE", "一对多类型"),
    SMALL_CLASS_PHASE("SMALL_CLASS_PHASE", "期"),

	POS_TYPE("POS_TYPE", "终端渠道"), // add by yaoyuqi 2017-04-26

    PRODUCT_TYPE("PRODUCT_TYPE", "产品类型"), // add by tangyuping 2016-02-17
    
    MSG_TYPE("MSG_TYPE", "信息类别"), // add by lixuejun 2016-04-14

	OTHER_CATEGORY("OTHER_CATEGORY", "费用类型"), // guoshibo 2016-04-19   Other_Category 是其他产品类型的细分由数据字典维护，“资料费”，“综合服务费”...

	OTHER_OF_CATEGORY("OTHER_OF_CATEGORY","所属产品类型"),  // guoshibo 2016-04-19 Other_Of_Category 是其他产品类型的所属产品的分类，所属产品类型：数据字段维护，“一对一”，“小班”，“一对多” "目标班"
	
	WASH_CAUSE("WASH_CAUSE", "资金冲销原因"), // add by lixuejun 2016-08-10
	
	ADJUST_PROJECT("ADJUST_PROJECT", "调整项目"), // add by lixuejun 2016-09-14
	
	SUBJECT_GROUP("SUBJECT_GROUP", "科组"), // add by lixuejun 2017-03-13
	
	SCHOOL_YEAR("SCHOOL_YEAR", "学年"), // add by lixuejun 2017-11-16
	
	SIGN_TYPE("SIGN_TYPE", "标签类型浏览模式"), // yaoyuqi 20171206

	SIGN_TYPE_WORK("SIGN_TYPE_WORK", "标签类型工作台模式"), // yaoyuqi 20171206

	EXAMINATION_TYPE("EXAMINATION_TYPE", "考试类型"), // add by lixuejun 2017-11-16
	
	POS_MACHINE_TYPE("POS_MACHINE_TYPE", "POS机类型"); // add by lixuejun 2018-03-09; 
    
//    TRANSFER_MODE("TRANSFER_MODE", "流转方式");
	
	private String value;
	private String name;
	
	private DataDictCategory(String value, String name) {
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
