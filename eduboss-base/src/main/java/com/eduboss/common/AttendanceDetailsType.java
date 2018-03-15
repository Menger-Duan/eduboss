package com.eduboss.common;
/**
 * 考勤明细 类型
 * @author xiaojinwang
 *
 */
public enum AttendanceDetailsType {


	ADT_ONE("ADT_ONE", "识别为实际=计划，未改课时"),
	ADT_TWO("ADT_TWO", "识别为实际=计划，更改课时"),
	ADT_THREE("ADT_THREE", "识别为实际≠计划，填写课时"),
	ADT_FOUR("ADT_FOUR", "在扫一扫考勤详情页点击“扫一扫考勤”按钮后识别超时，手动考勤"),
	ADT_FIVE("ADT_FIVE","在手动考勤详情页点击拍照，手动考勤"),
	ADT_SIX("ADT_SIX","首页扫一扫超时后点击“手动考勤”按钮后进入课程列表页选择课程，手动考勤");

	
	private String value;
	private String name;
	
	private AttendanceDetailsType(String value, String name) {
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
