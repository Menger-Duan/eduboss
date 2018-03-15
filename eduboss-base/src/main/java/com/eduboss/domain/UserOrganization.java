package com.eduboss.domain;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * UserOrganization entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user_organization")
public class UserOrganization implements java.io.Serializable {

	// Fields
	private int id ;

	private User user;
	private Organization organization;

	// Constructors

	/** default constructor */
	public UserOrganization() {
	}

	@Id
	@Column(name = "id", unique = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGANIZATIONID")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}