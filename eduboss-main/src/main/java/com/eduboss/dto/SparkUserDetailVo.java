package com.eduboss.dto;

/**
 * 用户数据同步
 */
public class SparkUserDetailVo {

	private String account;
	private String name;
	private String work_type;
	private String employee_no;
	private String sex;
	private String phone;
	private String job;
	private String id;
	private String password;
	private Boolean is_disabled;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWork_type() {
		return work_type;
	}
	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}
	public String getEmployee_no() {
		return employee_no;
	}
	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getIs_disabled() {
		return is_disabled;
	}
	public void setIs_disabled(Boolean is_disabled) {
		this.is_disabled = is_disabled;
	}
    @Override
    public String toString() {
        return "SparkUserDetailVo [account=" + account + ", name=" + name
                + ", work_type=" + work_type + ", employee_no=" + employee_no
                + ", sex=" + sex + ", phone=" + phone + ", job=" + job
                + ", id=" + id + ", password=" + password + ", is_disabled="
                + is_disabled + "]";
    }
}
