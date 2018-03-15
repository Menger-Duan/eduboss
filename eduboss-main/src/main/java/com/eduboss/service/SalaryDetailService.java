package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.Organization;
import com.eduboss.domainVo.AutoCompleteOptionVo;
import com.eduboss.domainVo.SalaryDetailVo;
import com.eduboss.dto.DataPackage;

public interface SalaryDetailService {

	/**
	 * 查找表单
	 * @param dataPackage
	 * @param salaryDetailVo
	 * @param params
	 * @return
	 */
	DataPackage getsalaryDetailVoList(DataPackage dataPackage,
			SalaryDetailVo salaryDetailVo, Map<String, Object> params);

	/**
	 * 根据用户id获取所属组织机构信息
	 * @param userId
	 * @return
	 */
	Organization getOrganizationByUserId(String userId);

	/**
	 * 新增信息
	 * @param salaryDetailVo
	 */
	void saveSalaryDetailNew(SalaryDetailVo salaryDetailVo);

	/**
	 * 查找1条信息
	 * @param id
	 * @return
	 */
	SalaryDetailVo findSalaryDetailById(String id);

	/**
	 * 删除1条信息
	 * @param id
	 */
	void deleteSalaryDetail(String id);

	/**
	 * 归档
	 * @param id
	 */
	void fileSalaryDetail(String id);

	/**
	 * 查找用户组织架构、主职位（数据权限限定）
	 * @param term
	 * @return
	 */
	List<AutoCompleteOptionVo> getLimitUserAutoComplate(String term);

}
