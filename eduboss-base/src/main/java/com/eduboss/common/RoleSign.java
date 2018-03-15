package com.eduboss.common;

public enum RoleSign {
	WLZY("WLZY","网络专员"),
	WLZG("WLZG","网络主管"),
	XGZG("XGZG","学管主管"),
	LS("LS","老师"),
//	TMKZG("TMKZG","TMK主管"),//TMK主管改为外呼主管
//	TMKZY("TMKZY", "TMK专员"),//TMK专员 改为外呼专员
	WHZG("WHZG","外呼主管"),//TMK主管改为外呼主管
	WHZY("WHZY", "外呼专员"),//TMK专员 改为外呼专员
	ZXS("ZXS", "咨询师"),//咨询师
	XQZR("XQZR","校区主任"),
	QT("QT","前台"),
	YYJL("YYJL","营运经理"),//新增营运经理  20170220
	YYZJ("YYZJ","营运总监"),
	XQYYFZR("XQYYFZR","校区营运主任"),
	ZXZG("ZXZG","咨询主管"),
	WLYXZY("WLYXZY","网络营销专员"),
	XGS("XGS","学管师"),
	SCJL("SCJL","市场经理"),
	YYZR("YYZR","运营主任");
	private String value;
	private String name;
	
	private RoleSign(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
