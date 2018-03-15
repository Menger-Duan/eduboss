package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * SysemConfig entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sysem_config")
public class SysemConfig implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String tag;
	private String value;
	private String remark;
	private String createTime;
	private String creaetUserId;
	private String modifyTime;
	private String modifyUserId;

	// Constructors

	/** default constructor */
	public SysemConfig() {
	}

	/** minimal constructor */
	public SysemConfig(String id) {
		this.id = id;
	}

	/** full constructor */
	public SysemConfig(String id, String name, String tag, String value,
			String remark, String createTime, String creaetUserId,
			String modifyTime, String modifyUserId) {
		this.id = id;
		this.name = name;
		this.tag = tag;
		this.value = value;
		this.remark = remark;
		this.createTime = createTime;
		this.creaetUserId = creaetUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TAG", length = 32)
	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Column(name = "VALUE", length = 512)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "REMARK", length = 256)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREAET_USER_ID", length = 32)
	public String getCreaetUserId() {
		return this.creaetUserId;
	}

	public void setCreaetUserId(String creaetUserId) {
		this.creaetUserId = creaetUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

}