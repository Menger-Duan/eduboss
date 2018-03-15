package com.pad.dto;

import java.util.List;

/**
 *批量保存标签对象
 *
 * @author Spark
 * @since 2017-11-30
 */
public class CmsSignMultiVo {

	private String type;
	private List<CmsSignVo> signVos;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<CmsSignVo> getSignVos() {
		return signVos;
	}

	public void setSignVos(List<CmsSignVo> signVos) {
		this.signVos = signVos;
	}
}
