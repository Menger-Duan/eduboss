package com.pad.entity;

import javax.persistence.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Entity@Table(name= "cms_menu_auth")
public class CmsMenuAuth implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer cmsMenuId;

	private String orgId;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name ="cms_menu_id")
	public Integer getCmsMenuId() {
		return cmsMenuId;
	}

	public void setCmsMenuId(Integer cmsMenuId) {
		this.cmsMenuId = cmsMenuId;
	}

	@Column(name ="org_id")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return "CmsMenuAuthVo{" +
			", id=" + id +
			", cmsMenuId=" + cmsMenuId +
			", orgId=" + orgId +
			"}";
	}
}
