package com.eduboss.common;

public enum CustomerEventType {
	
	NEW("NEW","新增客户"),//新增客户事件  是
	DELIVER("DELIVER", "客户资源调配"),//客户资源分配事件 是
	FOLLOWING("FOLLOWING", "跟进"),//跟进事件 是
	APPOINTMENT("APPOINTMENT", "预约上门"),//预约事件 是
	APPOINTMENT_ONLINE("APPOINTMENT_ONLINE","网络分配校区营运主任"),//网络分配校区营运主任  是
	APPOINTMENT_ONLINEDELIVER("APPOINTMENT_ONLINEDELIVER","网络分配校区营运主任更换跟进对象"),//网络分配校区营运主任更换跟进对象
	APPOINTMENT_ONLINE_OPERATION("APPOINTMENT_ONLINE_OPERATION","营运经理分配校区"),//营运经理分配校区
	APPOINTMENT_CONFORM("APPOINTMENT_CONFORM", "预约到访"),//预约到访 
	CONTRACT_SIGN("CONTRACT_SIGN", "签合同"),//签合同事件  客户动态事件  是
	CHARGER("CHARGER", "收款"),//收款    
	BACK("BACK","资源回流"),//系统回收  是
	INVALID("INVALID", "无效"),   //是
	GAINCUSTOMER("GAINCUSTOMER","抢客"), //是
	TRANSFERCUSTOMEROUT("TRANSFERCUSTOMEROUT","转出校区"),//转校-转出 是
	TRANSFERCUSTOMERIN("TRANSFERCUSTOMERIN","转入校区"),//转校-转入  是
	RESOURCES("RESOURCES","资源量"),//用于查询客户概览  统计各个入口的资源量 否
	VISITCOME("VISITCOME","上门数"),//用于统计客户概览  统计各个入口的上门数量 否
	ACTIVECUSTOMER("ACTIVECUSTOMER","激活老客户"),//是
	//新增多种客户事件类型 20170419 xiaojinwang	
	IMPORTCUSTOMER("IMPORTCUSTOMER","导入客户"),//是
	PHONECALL("PHONECALL","留电量"), //否
	RELEASE("RELEASE","释放客户"),//是
	//RECEPTION_DELIVER("RECEPTION_DELIVER", "前台分配客户"),//前台分配客户事件
	UPDATE_STUDENTINFO("UPDATE_STUDENTINFO","修改潜在学生信息"), //是
	GET("GET","获取客户"),//是
	GET_RESOURCEPOOL("RESOURCEPOOL","从公共资源池获取客户"), //是
	CHANGE_OTHER("CHANGE_OTHER","更换其他人跟进客户"),//是
	VISITCOME_SET("VISITCOME_SET","标记已上门"),//是
	VISITCOME_CUSTOMER("VISITCOME_CUSTOMER","客户已上门"),//否  xiaojinwang 20170524 统计关联上门咨询师
	APPOINTMENT_SIGN("APPOINTMENT_SIGN","上门登记"),//是
	CHANGE_COUNSELOR("CHANGE_COUNSELOR","更换咨询师"),// 前台分配咨询师更换咨询师 调配抢客更换咨询师 是
	TRANSFER_SIGN("TRANSFER_SIGN","转介绍登记"),// 是
	TRANSFER_AUDIT("TRANSFER_AUDIT","转介绍审核"),//是
	INVALIDCUSTOMER_AUDIT("INVALIDCUSTOMER_AUDIT","无效客户审核"),//是
	CHANGE_USERROLE("CHANGE_USERROLE","切换前台模式"),//否
	INVALIDUSER_RELEASE("INVALIDUSER_RELEASE","无效用户释放客户"),//是
	CHANGECAMPUS_RELEASE("CHANGECAMPUS_RELEASE","用户更换校区释放客户"),//是
	CHANGECAMPUS_APPLY("CHANGECAMPUS_APPLY","咨询师申请客户转校"),//是
	COUNSELOR_OUTCALL("COUNSELOR_OUTCALL","咨询师获取外呼客户"),//否
	COUNSELOR_NETWORK("COUNSELOR_NETWORK","咨询师获取网络客户"),//否
	COUNSELOR_OTHER("COUNSELOR_OTHER","咨询师获取其他客户"),//否
	DELETESTUDENT("DELETESTUDENT","删除学生"),//是
	RELEASECUSTOMER("RELEASECUSTOMER","释放客户");//否
	
	
	
	private String value;
	private String name;
	
	private CustomerEventType(String value, String name) {
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
