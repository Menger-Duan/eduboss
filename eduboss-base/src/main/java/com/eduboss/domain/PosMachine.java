package com.eduboss.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.PosMachineStatus;

/**
 * POS终端机使用日期
 * @author lixuejun
 *
 */
@Entity
@Table(name = "pos_machine")
public class PosMachine implements Serializable {

    private static final long serialVersionUID = -2585489662953058486L;
    
    private int id;
	private String posNumber;
	private String posName;
	private DataDict type;
	private Organization brench;
	private Organization campus;
	private String startTime;
	private String endTime;
	private PosMachineStatus status;
	private String createTime;
	private User createUser;
	private String modifyTime;
	private User modifyUser;
	private DataDict posType;
	
	public PosMachine() {
		super();
	}

	public PosMachine(int id, String posNumber, String posName, DataDict type,
			Organization brench, Organization campus, String startTime,
			String endTime, PosMachineStatus status, String createTime,
			User createUser, String modifyTime, User modifyUser) {
		super();
		this.id = id;
		this.posNumber = posNumber;
		this.posName = posName;
		this.type = type;
		this.brench = brench;
		this.campus = campus;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.createTime = createTime;
		this.createUser = createUser;
		this.modifyTime = modifyTime;
		this.modifyUser = modifyUser;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "POS_NUMBER", length = 20)
	public String getPosNumber() {
		return posNumber;
	}

	public void setPosNumber(String posNumber) {
		this.posNumber = posNumber;
	}

	@Column(name = "POS_NAME", length = 50)
	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	/**
	 * pos机类型
	 * @return
	 */
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TYPE")
	public DataDict getType() {
        return type;
    }

    public void setType(DataDict type) {
        this.type = type;
    }

    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BRENCH_ID")
	public Organization getBrench() {
		return brench;
	}

	public void setBrench(Organization brench) {
		this.brench = brench;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CAMPUS_ID")
	public Organization getCampus() {
		return campus;
	}

	public void setCampus(Organization campus) {
		this.campus = campus;
	}

	@Column(name = "START_TIME", length = 20)
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", length = 20)
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 32)
	public PosMachineStatus getStatus() {
		return status;
	}

	public void setStatus(PosMachineStatus status) {
		this.status = status;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CREATE_USER")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MODIFY_USER")
	public User getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	/**
	 * 终端渠道
	 * @return
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="POS_TYPE")
	public DataDict getPosType() {
		return posType;
	}

	public void setPosType(DataDict posType) {
		this.posType = posType;
	}

    @Override
    public String toString() {
        return "PosMachine [id=" + id + ", posNumber=" + posNumber
                + ", posName=" + posName + ", type=" + type + ", brench="
                + brench + ", campus=" + campus + ", startTime=" + startTime
                + ", endTime=" + endTime + ", status=" + status
                + ", createTime=" + createTime + ", createUser=" + createUser
                + ", modifyTime=" + modifyTime + ", modifyUser=" + modifyUser
                + ", posType=" + posType + "]";
    }
	
}
