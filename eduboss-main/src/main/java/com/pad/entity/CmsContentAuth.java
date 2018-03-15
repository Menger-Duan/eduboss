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
@Entity
@Table(name= "cms_content_auth")
public class CmsContentAuth implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer cmsContentId;

	private String orgId;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name ="cms_content_id")
	public Integer getCmsContentId() {
		return cmsContentId;
	}

	public void setCmsContentId(Integer cmsContentId) {
		this.cmsContentId = cmsContentId;
	}


	@Column(name ="org_id")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
