package com.eduboss.dto;

/**
 * 用户数据同步
 */
public class SparkUserVo {

	private String action;	
	private SparkUserDetailVo origin;
	private SparkUserDetailVo updated;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public SparkUserDetailVo getOrigin() {
		return origin;
	}
	public void setOrigin(SparkUserDetailVo origin) {
		this.origin = origin;
	}
	public SparkUserDetailVo getUpdated() {
		return updated;
	}
	public void setUpdated(SparkUserDetailVo updated) {
		this.updated = updated;
	}
	
    @Override
    public String toString() {
        return "SparkUserVo [action=" + action + ", origin=" + origin
                + ", updated=" + updated + "]";
    }
	
}
