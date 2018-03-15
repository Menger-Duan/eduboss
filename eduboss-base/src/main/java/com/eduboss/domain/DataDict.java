package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.DataDictCategory;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DataDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "data_dict")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler",
        "fieldHandler" })
public class DataDict implements java.io.Serializable, NameValue {

	// Fields
	private String id;
	private String name;
	private String value;
	private Integer dictOrder;
	private DataDict parentDataDict;
	private DataDictCategory category;
	private String icon;
	private String data;
	private String remark;
	private String createTime;
	private String createUserId;
	private String state;
	private String isSystem;

	// Constructors

	/** default constructor */
	public DataDict() {
	}
	
	/** minimal constructor */
	public DataDict(String id) {
		this.id = id;
	}

	/** minimal constructor */
	public DataDict(String id, DataDictCategory category) {
		this.id = id;
		this.category = category;
	}

	public DataDict(DataDictCategory category, DataDict parentDataDict){
		this.category = category;
		this.parentDataDict = parentDataDict;
	}

	/** full constructor */
	public DataDict(String id, String name, String value, Integer dictOrder,
			DataDict parentDataDict, DataDictCategory category, String icon, String data,
			String remark, String createTime, String createUserId,String state) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.dictOrder = dictOrder;
		this.parentDataDict = parentDataDict;
		this.category = category;
		this.icon = icon;
		this.data = data;
		this.remark = remark;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.state=state;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
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

	@Column(name = "VALUE", length = 128)
	public String getValue() {
		return this.id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "DICT_ORDER")
	public Integer getDictOrder() {
		return this.dictOrder;
	}

	public void setDictOrder(Integer dictOrder) {
		this.dictOrder = dictOrder;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	public DataDict getParentDataDict() {
		return parentDataDict;
	}
	
	public void setParentDataDict(DataDict parentDataDict) {
		this.parentDataDict = parentDataDict;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "CATEGORY", length = 32)
	public DataDictCategory getCategory() {
		return this.category;
	}

	public void setCategory(DataDictCategory category) {
		this.category = category;
	}

	@Column(name = "ICON", length = 512)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "DATA", length = 2048)
	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Column(name = "REMARK", length = 1024)
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

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "STATE", length = 1)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	@Column(name = "IS_SYSTEM")
	public String getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataDict dataDict = (DataDict) o;

        if (category != dataDict.category) return false;
        if (createTime != null ? !createTime.equals(dataDict.createTime) : dataDict.createTime != null) return false;
        if (createUserId != null ? !createUserId.equals(dataDict.createUserId) : dataDict.createUserId != null)
            return false;
        if (data != null ? !data.equals(dataDict.data) : dataDict.data != null) return false;
        if (dictOrder != null ? !dictOrder.equals(dataDict.dictOrder) : dataDict.dictOrder != null) return false;
        if (icon != null ? !icon.equals(dataDict.icon) : dataDict.icon != null) return false;
        if (id != null ? !id.equals(dataDict.id) : dataDict.id != null) return false;
        if (isSystem != null ? !isSystem.equals(dataDict.isSystem) : dataDict.isSystem != null) return false;
        if (name != null ? !name.equals(dataDict.name) : dataDict.name != null) return false;
        if (parentDataDict != dataDict.parentDataDict) return false;
//        if (parentId != null ? !parentId.equals(dataDict.parentId) : dataDict.parentId != null) return false;
        if (remark != null ? !remark.equals(dataDict.remark) : dataDict.remark != null) return false;
        if (state != null ? !state.equals(dataDict.state) : dataDict.state != null) return false;
        if (value != null ? !value.equals(dataDict.value) : dataDict.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (dictOrder != null ? dictOrder.hashCode() : 0);
//        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (parentDataDict != null ? parentDataDict.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (createUserId != null ? createUserId.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (isSystem != null ? isSystem.hashCode() : 0);
        return result;
    }
}