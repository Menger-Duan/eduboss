package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * UserOrganizationId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class UserOrganizationId implements java.io.Serializable {

	// Fields

	private String userId;
	private String organizationId;

	// Constructors

	/** default constructor */
	public UserOrganizationId() {
	}

	/** full constructor */
	public UserOrganizationId(String userId, String organizationId) {
		this.userId = userId;
		this.organizationId = organizationId;
	}

	// Property accessors

	@Column(name = "userID", nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "organizationID", nullable = false, length = 32)
	public String getOrganizationId() {
		return this.organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserOrganizationId))
			return false;
		UserOrganizationId castOther = (UserOrganizationId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null && castOther.getUserId() != null && this
				.getUserId().equals(castOther.getUserId())))
				&& ((this.getOrganizationId() == castOther.getOrganizationId()) || (this
						.getOrganizationId() != null
						&& castOther.getOrganizationId() != null && this
						.getOrganizationId().equals(
								castOther.getOrganizationId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37
				* result
				+ (getOrganizationId() == null ? 0 : this.getOrganizationId()
						.hashCode());
		return result;
	}

}