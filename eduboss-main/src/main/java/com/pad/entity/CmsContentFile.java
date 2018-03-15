package com.pad.entity;

import javax.persistence.*;

import javax.persistence.Column;
import java.io.Serializable;
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
@Table(name= "cms_content_file")
public class CmsContentFile implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer cmsContentId;

	private Integer fileId;

	private Date createTime;

	private String createUser;

	private String filePath;

	private String fileName;

	private String fileSize;

	private String aliSaveTime;

	private String viewUrl;



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name ="file_id")
	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
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

	@Column(name = "file_size")
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "ali_save_time")
	public String getAliSaveTime() {
		return aliSaveTime;
	}

	public void setAliSaveTime(String aliSaveTime) {
		this.aliSaveTime = aliSaveTime;
	}

	@Column(name = "view_url")
	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
}
