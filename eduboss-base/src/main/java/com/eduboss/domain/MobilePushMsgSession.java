package com.eduboss.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.eduboss.common.MsgStatus;
import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.SessionType;



/**
 * MessageDeliverRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MOBILE_PUSH_MSG_SESSION")
public class MobilePushMsgSession implements java.io.Serializable {

	// Fields
	private String id;
	private SessionType sessionType;
	
	private String remark;
	
	private String createTime;
	private MobileUser createMobileUser; 
	private String modifyTime;
	private MobileUser modifyMobileUser; 
	
	private Set<MobileUser> mobileUsers = new HashSet<MobileUser>();
    private MsgStatus msgStatus;
    
    private Set<MobilePushMsgRecord> records =  new HashSet<MobilePushMsgRecord>();
    
    // Constructors

	/** default constructor */
	public MobilePushMsgSession() {
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "SESSION_TYPE", length = 20)
	public SessionType getSessionType() {
		return sessionType;
	}
	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREAET_MOBILE_USER_ID")
	public MobileUser getCreateMobileUser() {
		return createMobileUser;
	}
	public void setCreateMobileUser(MobileUser createMobileUser) {
		this.createMobileUser = createMobileUser;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_MOBILE_USER_ID")
	public MobileUser getModifyMobileUser() {
		return modifyMobileUser;
	}
	public void setModifyMobileUser(MobileUser modifyMobileUser) {
		this.modifyMobileUser = modifyMobileUser;
	}

	@ManyToMany
	@JoinTable(name="MOBILE_PUSH_MSG_SESSION_USER",
		joinColumns = {@JoinColumn(name = "SESSION_ID")},
		inverseJoinColumns = {@JoinColumn(name = "MOBILE_USER_ID")}
	)
	public Set<MobileUser> getMobileUsers() {
		return mobileUsers;
	}
	public void setMobileUsers(Set<MobileUser> mobileUsers) {
		this.mobileUsers = mobileUsers;
	}

	@Column(name = "REMARK", length = 50)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

    public void setMsgStatus(MsgStatus msgStatus) {
        this.msgStatus = msgStatus;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "MSG_STATUS", length = 20)
    public MsgStatus getMsgStatus() {
        return msgStatus;
    }

    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "session")
	public Set<MobilePushMsgRecord> getRecords() {
		return records;
	}
	public void setRecords(Set<MobilePushMsgRecord> records) {
		this.records = records;
	}
}