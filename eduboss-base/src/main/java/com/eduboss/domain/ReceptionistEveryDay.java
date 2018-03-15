package com.eduboss.domain;


import java.math.BigDecimal;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="receptionist")
public class ReceptionistEveryDay {
	private String id;
	private String loginDate;
	private BigDecimal telNum;
	private BigDecimal telNumw;
	private BigDecimal visitNumz;
	private BigDecimal visitNumy;
	private String writeTime;
	private User user;
	private Organization organization;
	


	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "rec_id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="login_date", length = 20)
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	
	@Column(name="tel_num",  precision = 11)
	public BigDecimal getTelNum() {
		return telNum;
	}
	public void setTelNum(BigDecimal telNum) {
		this.telNum = telNum;
	}
	
	@Column(name="tel_numw",  precision = 11)
	public BigDecimal getTelNumw() {
		return telNumw;
	}
	public void setTelNumw(BigDecimal telNumw) {
		this.telNumw = telNumw;
	}
	
	@Column(name="visit_numz",  precision = 11)
	public BigDecimal getVisitNumz() {
		return visitNumz;
	}
	public void setVisitNumz(BigDecimal visitNumz) {
		this.visitNumz = visitNumz;
	}
	
	@Column(name="visit_numy",  precision = 11)
	public BigDecimal getVisitNumy() {
		return visitNumy;
	}
	public void setVisitNumy(BigDecimal visitNumy) {
		this.visitNumy = visitNumy;
	}
	
	
	
	@Column(name="write_time", length = 20)
	public String getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(String writeTime) {
		this.writeTime = writeTime;
	}

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@JoinColumn(name = "org_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	

		
	

}
