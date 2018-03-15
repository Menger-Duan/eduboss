package com.pad.dto;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
public class CmsMenuAuthVo implements java.io.Serializable {

	private Integer id;

	private Integer cmsMenuId;

	private String orgId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCmsMenuId() {
		return cmsMenuId;
	}

	public void setCmsMenuId(Integer cmsMenuId) {
		this.cmsMenuId = cmsMenuId;
	}

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
