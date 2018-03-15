package com.eduboss.domainVo.EduPlatform;

import java.io.Serializable;

public class BaseRelateDataVo implements Serializable{
   

	/**
	 * 
	 */
	private static final long serialVersionUID = -5780891017627458033L;
	private Integer type; //类型：1同步 2专题
	private Integer section; //学段id
	private Integer subject; //科目id
	private Integer publishVersion;//版本id
	private Integer bookVersion;//书本id
	private Integer isStorage;//是否入库的知识点数据，1 是入库的 0 不是入库的 默认0
	
	public BaseRelateDataVo() {
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public Integer getSubject() {
		return subject;
	}

	public void setSubject(Integer subject) {
		this.subject = subject;
	}

	public Integer getPublishVersion() {
		return publishVersion;
	}

	public void setPublishVersion(Integer publishVersion) {
		this.publishVersion = publishVersion;
	}

	public Integer getBookVersion() {
		return bookVersion;
	}

	public void setBookVersion(Integer bookVersion) {
		this.bookVersion = bookVersion;
	}

	public Integer getIsStorage() {
		return isStorage;
	}

	public void setIsStorage(Integer isStorage) {
		this.isStorage = isStorage;
	}

	
	
}
