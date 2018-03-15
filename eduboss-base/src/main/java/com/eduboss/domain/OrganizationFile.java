package com.eduboss.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *组织架构图片
 * @author yao
 * @since 2018-01-15
 */
@Entity
@Table(name= "organization_file")
public class OrganizationFile implements Serializable{

    private static final long serialVersionUID = 1L;

	private Integer id;

	private String organizationId;


	private Date createTime;

	private String createUser;

	private String filePath;

	private String fileName;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name ="organization_id")
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}


    @Column(name ="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

    @Column(name ="create_user")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	@Column(name = "file_path")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "file_name")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
