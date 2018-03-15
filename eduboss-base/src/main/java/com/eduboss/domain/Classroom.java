package com.eduboss.domain;

/**
 *  教室
 * @author xuwen
 * @date 2014年10月26日
 */
public class Classroom {

	private String id; // ID
	private String name; // 教室名称
	private String type; // 教室类型
	private int area; // 教室面积（平方米）
	private String hire; // 可否外租
	private int hourRent; // 时租金
	private String status; // 状态
	private String remark; // 备注

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public String getHire() {
		return hire;
	}

	public void setHire(String hire) {
		this.hire = hire;
	}

	public int getHourRent() {
		return hourRent;
	}

	public void setHourRent(int hourRent) {
		this.hourRent = hourRent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
