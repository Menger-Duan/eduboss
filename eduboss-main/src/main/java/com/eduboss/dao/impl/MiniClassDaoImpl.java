package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.MiniClassDao;
import com.eduboss.domain.MiniClass;
import com.eduboss.service.RoleQLConfigService;

@Repository("MiniClassDao")
public class MiniClassDaoImpl extends GenericDaoImpl<MiniClass, String> implements MiniClassDao {
	
	private static final Logger log = LoggerFactory.getLogger(MiniClassDaoImpl.class);
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	@Override
	public int countByProductId(String productId) {
		List<Criterion> criterion=new ArrayList<Criterion>();
		criterion.add(Restrictions.eq("product.id", productId));
		return findCountByCriteria(criterion);
	}

	/**
	 * 更新小班销售渠道为线上
	 */
	@Override
	public void onLineMiniclass(String miniClassIds) {
		String sql = " update mini_class set channel_type = 'ON_LINE' where mini_class_id in (:miniClassIds) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("miniClassIds", miniClassIds.split(","));
		super.excuteSql(sql, params);
	}

	/**
	 * 计算小班销售渠道为线上的数量，用于判断能否更新小班销售渠道
	 */
	@Override
	public int countOnlineMiniClass(String miniClassIds) {
		String sql = " select count(*) from mini_class where mini_class_id in (:miniClassIds) and channel_type = 'ON_LINE' ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("miniClassIds", miniClassIds.split(","));
		return super.findCountSql(sql, params);
	}

	/**
	 * 根据小班ids获取小班列表
	 */
	@Override
	public List<MiniClass> getMiniClassListByIds(String miniClassIds) {
		String sql = " select * from mini_class where mini_class_id in (:miniClassIds) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("miniClassIds", miniClassIds.split(","));
		return super.findBySql(sql, params);
	}

	/**
	 * 计算已完成的小班数量，用于判断能否更新小班销售渠道
	 */
	@Override
	public int countConpleteMiniClass(String miniClassIds) {
		String sql = " select count(*) from mini_class where mini_class_id in (:miniClassIds) and status = 'CONPELETE' ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("miniClassIds", miniClassIds.split(","));
		return super.findCountSql(sql, params);
	}

	@Override
	public List<MiniClass> getNoTeacherMiniClassListByIds(String miniClassIds,Boolean isNoTeacher) {
		String sql = " select * from mini_class where mini_class_id in (:miniClassIds) ";
		if(!isNoTeacher){
		    sql+="and teacher_id is not null ";
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("miniClassIds", miniClassIds.split(","));
		return super.findBySql(sql, params);
	}

    @Override
    public void updateMiniClassSaleType(String miniClassIds, int onlineSale) {
        String sql = " update mini_class set on_shelves = :onlineSale "
                + " where mini_class_id in (:miniClassIds) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("onlineSale", onlineSale);
        params.put("miniClassIds", miniClassIds.split(","));
        super.excuteSql(sql, params);
    }

    @Override
    public Map<Object, Object> findCourseStatusNumVoByOrgId(String orgId) {
        String sql = " select count(*) totalNum, sum(case when on_shelves = 0 then 1 else 0 end) notOnNum, "
                + " sum(case when on_shelves = 1 then 1 else 0 end) offNum, sum(case when on_shelves = 2 then 1 else 0 end) onNum "
                + " from mini_class where BL_CAMPUS_ID in (select id from organization where parentID = :orgId) and ONLINE_SALE = 0 ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orgId", orgId);
        List<Map<Object, Object>> list = super.findMapBySql(sql, params);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
	
	

}
