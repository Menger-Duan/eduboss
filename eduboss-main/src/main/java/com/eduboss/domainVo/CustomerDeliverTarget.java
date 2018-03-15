package com.eduboss.domainVo;

import java.util.List;

import com.eduboss.common.CustomerDeliverType;
import com.eduboss.domain.Customer;

public class CustomerDeliverTarget {
	
	private CustomerDeliverType deliveType;
	private String targetId;
	private String targetName;
	private int followingCount;
	private List<Customer> followingCustomers;
	
	/**
	 * @return the deliveType
	 */
	public CustomerDeliverType getDeliveType() {
		return deliveType;
	}
	/**
	 * @param deliveType the deliveType to set
	 */
	public void setDeliveType(CustomerDeliverType deliveType) {
		this.deliveType = deliveType;
	}
	/**
	 * @return the targetId
	 */
	public String getTargetId() {
		return targetId;
	}
	/**
	 * @param targetId the targetId to set
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}
	/**
	 * @param targetName the targetName to set
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	/**
	 * @return the followingCustomers
	 */
	public List<Customer> getFollowingCustomers() {
		return followingCustomers;
	}
	/**
	 * @param followingCustomers the followingCustomers to set
	 */
	public void setFollowingCustomers(List<Customer> followingCustomers) {
		this.followingCustomers = followingCustomers;
	}
	public int getFollowingCount() {
		return followingCount;
	}
	public void setFollowingCount(int followingCount) {
		this.followingCount = followingCount;
	}
}
