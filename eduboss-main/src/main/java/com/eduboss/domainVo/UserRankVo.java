package com.eduboss.domainVo;

public class UserRankVo {

	private Double money;
	private int rank;
	private int upOrDown;
	public UserRankVo(Double money, int rank, int upOrDown) {
		this.money = money;
		this.rank = rank;
		this.upOrDown = upOrDown;
	}
	
	public UserRankVo() {

	}

	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getUpOrDown() {
		return upOrDown;
	}
	public void setUpOrDown(int upOrDown) {
		this.upOrDown = upOrDown;
	}
	
}
