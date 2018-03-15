package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.MiniClass;




@Repository
public interface MiniClassDao extends GenericDAO<MiniClass, String> {

	int countByProductId(String productId);
	
	/**
	 * 更新小班销售渠道为线上
	 * @param miniClassIds
	 */
	void onLineMiniclass(String miniClassIds);
	
	/**
	 * 计算小班销售渠道为线上的数量，用于判断能否更新小班销售渠道
	 * @param miniClassIds
	 * @return
	 */
	int countOnlineMiniClass(String miniClassIds);
	
	/**
	 * 根据小班ids获取小班列表
	 * @param miniClassIds
	 * @return
	 */
	List<MiniClass> getMiniClassListByIds(String miniClassIds);
	
	/**
	 * 计算已完成的小班数量，用于判断能否更新小班销售渠道
	 * @param miniClassIds
	 * @return
	 */
	int countConpleteMiniClass(String miniClassIds);
	
	/**
	 * 获取没有老师小班列表
	 * @param miniClassIds
	 * @return
	 */
	List<MiniClass> getNoTeacherMiniClassListByIds(String miniClassIds,Boolean isNoTeacher);
	
	void updateMiniClassSaleType(String miniClassIds, int onlineSale);
	
	Map<Object, Object> findCourseStatusNumVoByOrgId(String orgId);
	
}
