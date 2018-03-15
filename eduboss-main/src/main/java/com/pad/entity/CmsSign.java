package com.pad.entity;

import com.eduboss.domain.DataDict;
import com.pad.common.CmsUseType;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Entity
@Table(name= "cms_sign")
public class CmsSign implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private String value;
	private DataDict type;
	private String status;

	private Date createTime;

	private String createUser;

	private CmsUseType useType;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "value",length = 10)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "type")
	public DataDict getType() {
		return type;
	}

	public void setType(DataDict type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name ="create_time",insertable = false,updatable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name ="create_user",updatable = false)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "use_type")
	public CmsUseType getUseType() {
		return useType;
	}

	public void setUseType(CmsUseType useType) {
		this.useType = useType;
	}

	@Override
	public String toString() {
		return "CmsSignVo{" +
			", id=" + id +
			", value=" + value +
			", type=" + type +
			", status=" + status +
			", createTime=" + createTime +
			", createUser=" + createUser +
			"}";
	}
}
