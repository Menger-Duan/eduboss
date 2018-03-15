package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.dto.RoleQLConfigSearchVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.RoleDao;
import com.eduboss.dao.SalaryDetailDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.SalaryDetail;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AutoCompleteOptionVo;
import com.eduboss.domainVo.SalaryDetailVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.SalaryDetailService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Service("SalaryDetailService")
public class SalaryDetailServiceImpl implements SalaryDetailService{

	@Autowired
	private SalaryDetailDao salaryDetailDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
    @Autowired
	private RoleQLConfigService roleQLConfigService;
    
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public DataPackage getsalaryDetailVoList(DataPackage dataPackage,
			SalaryDetailVo salaryDetailVo, Map<String, Object> params) {
		
		params.put("currentUser", userService.getCurrentLoginUser());
		params.put("organizationDao", organizationDao);
		dataPackage= salaryDetailDao.getsalaryDetailList(dataPackage, salaryDetailVo,params);		
		List<SalaryDetail> list = (List<SalaryDetail>) dataPackage.getDatas();
		List<SalaryDetailVo> voList = new ArrayList<SalaryDetailVo>();
		for(SalaryDetail con : list){
			SalaryDetailVo vo = HibernateUtils.voObjectMapping(con, SalaryDetailVo.class);
			vo.setSalaryTotal(vo.getSalaryBase().add(vo.getSalaryBonus().add(vo.getSalaryOther())));
			voList.add(vo);
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}


	/**
	 * 根据userid获取所属组织机构信息
	 */
	@Override
	public Organization getOrganizationByUserId(String userId) {
		
		return userService.getBelongCampusByUserId(userId);
	}


	/**
	 * 新增信息
	 */
	@Override
	public void saveSalaryDetailNew(SalaryDetailVo salaryDetailVo) {
		SalaryDetail salaryDetail = HibernateUtils.voObjectMapping(salaryDetailVo, SalaryDetail.class);
		salaryDetail.setModifier(userService.getCurrentLoginUser());
		salaryDetail.setModifyTime(DateTools.getCurrentDateTime());
		salaryDetailDao.save(salaryDetail);
	}


	/**
	 * 查找1条
	 */
	@Override
	public SalaryDetailVo findSalaryDetailById(String id) {
		SalaryDetail salaryDetail = salaryDetailDao.findById(id);
		return HibernateUtils.voObjectMapping(salaryDetail, SalaryDetailVo.class);
	}
	
	/**
	 * 删除1条
	 */
	@Override
	public void deleteSalaryDetail(String id) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		hql.append(" delete from SalaryDetail where id = :id ");
		salaryDetailDao.excuteHql(hql.toString(),params);
	}


	/**
	 * 归档
	 */
	@Override
	public void fileSalaryDetail(String id) {
		StringBuilder hql = new StringBuilder();
		String modifierId = userService.getCurrentLoginUser().getUserId();
		String date = DateTools.getCurrentDateTime();
		Map<String, Object> params = Maps.newHashMap();
		params.put("modifierId", modifierId);
		params.put("date", date);
		params.put("id", id);
		hql.append(" update SalaryDetail set completeFile=1,modifier= :modifierId ").append("',modifyTime= :date ").append("' where id = :id ");
		salaryDetailDao.excuteHql(hql.toString(),params);		
	}


	@Override
	public List<AutoCompleteOptionVo> getLimitUserAutoComplate(String term) {
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","u.organizationId");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("员工工资明细","nsql","hql");
		String extraHql =roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap);
		List<User> resList =salaryDetailDao.getLimitUserAutoComplate(term, extraHql);
		List<AutoCompleteOptionVo> voList = new ArrayList<AutoCompleteOptionVo>();
		
		for(User user : resList){
			String label = "";
			AutoCompleteOptionVo userPer  = new AutoCompleteOptionVo();
			userPer.setValue(user.getUserId());
			label += user.getName();
			label += "("+userService.getBelongCampusByUserId(user.getUserId()).getName()+",";
			if(StringUtils.isNotBlank(user.getRoleId())){
				Role role=roleDao.findById(user.getRoleId());
				if(role !=null )
					label += role.getName()+")";
			}
			userPer.setLabel(label);
			voList.add(userPer);
		}
		return voList;
	}

}
