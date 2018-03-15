package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.SystemNoticeDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.SystemNotice;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.domainVo.WelcomeNoticeVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

@Repository
public class SystemNoticeDaoImpl extends GenericDaoImpl<SystemNotice, String> implements SystemNoticeDao{

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Override
	@SuppressWarnings("unchecked")
	public DataPackage getSystemNoticeList(SystemNoticeVo systemNoticeVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from SystemNotice where 1=1");
		if(StringUtils.isNotBlank(systemNoticeVo.getTitle())){
			hql.append(" and title like :title ");
			params.put("title", "%" + systemNoticeVo.getTitle() + "%");
		}
		if(StringUtils.isNotBlank(systemNoticeVo.getCreateUserName())){
			hql.append(" and createUser.name like :createUserName ");
			params.put("createUserName", "%" + systemNoticeVo.getCreateUserName() + "%");
		}
		if(StringUtils.isNotBlank(systemNoticeVo.getNoticeType())){//  add by Yao 2015-04-21  公告类型
			hql.append(" and noticeType.id like :noticeTypeId ");
			params.put("noticeTypeId", "%" + systemNoticeVo.getNoticeType() + "%");
		}
		hql.append(" order by createTime desc");
		dp = super.findPageByHQL(hql.toString(), dp, true, params);
		List<SystemNotice> list = (List<SystemNotice>)dp.getDatas() ;
		List<SystemNoticeVo> voList = new ArrayList<SystemNoticeVo>();
		voList = HibernateUtils.voListMapping(list, SystemNoticeVo.class);
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public void modifySystemNotice(SystemNotice systemNotice) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE SYSTEM_NOTICE SET TITLE = :title, CONTENT = :content, MODIFY_TIME = :modifyTime, MODIFY_USER = :modifyUserId WHERE ID = :id ");
		params.put("title", systemNotice.getTitle());
		params.put("content", systemNotice.getContent());
		params.put("modifyTime", systemNotice.getModyfyTime());
		params.put("modifyUserId", systemNotice.getModifyUser().getUserId());
		params.put("id", systemNotice.getId());
		super.excuteSql(sql.toString(), params);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DataPackage getSystemNoticeListByRoles(
			SystemNoticeVo systemNoticeVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from SystemNotice where 1=1 ";
		if(StringUtils.isNotBlank(systemNoticeVo.getTitle())){
			hql += " and title like :title ";
			params.put("title", "%" + systemNoticeVo.getTitle() + "%");
		}
		if(StringUtils.isNotBlank(systemNoticeVo.getCreateUserName())){
			hql += " and createUser.name like :createUserName ";
			params.put("createUserName", "%" + systemNoticeVo.getCreateUserName() + "%");
		}
		hql += " order by createTime desc ";
		DataPackage dataResponse =  super.findPageByHQL(hql, dp, true, params);
		List<SystemNotice> list = (List<SystemNotice>) dataResponse.getDatas();
		List<SystemNoticeVo> voList = new ArrayList<SystemNoticeVo>();
		voList = HibernateUtils.voListMapping(list, SystemNoticeVo.class);
		dp.setDatas(voList);
		return dp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public DataPackage getSystemNoticeListByRolesFuckCriteria(
			SystemNoticeVo systemNoticeVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		User user = userService.getCurrentLoginUser();
		List<Organization> userOrgList=user.getOrganization();
		List<Role> userRoleList=user.getRole();
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" select distinct sys.* from SYSTEM_NOTICE sys,SYSTEM_NOTICE_ORG syso,organization org,SYSTEM_NOTICE_ROLE sysr ");
		sbSql.append(" where sys.id = syso.notice_id and org.id = syso.org_id and sys.id = sysr.notice_id ");
		if (userOrgList.size() > 0) {
			String orgArr[] = new String[userOrgList.size()];
			for(int j=0;j<userOrgList.size();j++){
				orgArr[j] = userOrgList.get(j).getId();
			}
			sbSql.append(" and org.id in (:orgIds)");
			params.put("orgIds", orgArr);
		}
		if(userRoleList.size() > 0){
			String roleArr[] = new String[userRoleList.size()];
			for(int i=0;i<userRoleList.size();i++){
				roleArr[i] = userRoleList.get(i).getRoleId();
			}
			sbSql.append(" and sysr.role_id in (:roleIds)");
			params.put("roleIds", roleArr);
		}
		
		if(StringUtils.isNotBlank(systemNoticeVo.getTitle())){
			sbSql.append(" and sys.title like :title ");
			params.put("title", "%" + systemNoticeVo.getTitle() + "%");
		}
		sbSql.append(" order by CREATE_TIME desc ");
		
		
		DataPackage listData = findPageBySql(sbSql.toString(), dp, true, params);
		List<SystemNotice> list = (List<SystemNotice>) listData.getDatas();
		List<SystemNoticeVo> voList = new ArrayList<SystemNoticeVo>();
		for(SystemNotice notice : list){
			SystemNoticeVo vo = HibernateUtils.voObjectMapping(notice, SystemNoticeVo.class);
			Organization organization = organizationDao.findById(vo.getCreateUserOrgId());
			vo.setCreateUserOrg(organization.getName());
			if(StringUtil.isNotEmpty(vo.getModifyUserOrgId())){//新增的 公告没有modifyId 会报空指针
				organization = organizationDao.findById(vo.getModifyUserOrgId());
				vo.setModifyUserOrg(organization.getName());
			}
			voList.add(vo);
		}
		dp.setDatas(voList);
		return dp;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public DataPackage getSystemNoticeListByRolesFuckCriteriaWelCome(
			SystemNoticeVo systemNoticeVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		User user = userService.findUserById("112233");
//		User user = userService.getCurrentLoginUser();
		List<Organization> userOrgList=user.getOrganization();
		List<Role> userRoleList=user.getRole();
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" select distinct sys.* from SYSTEM_NOTICE sys,SYSTEM_NOTICE_ORG syso,organization org,SYSTEM_NOTICE_ROLE sysr ");
		sbSql.append(" where sys.id = syso.notice_id and org.id = syso.org_id and sys.id = sysr.notice_id ");
		if (userOrgList.size() > 0) {
			String orgArr[] = new String[userOrgList.size()];
			for(int j=0;j<userOrgList.size();j++){
				orgArr[j] = userOrgList.get(j).getId();
			}
			sbSql.append(" and org.id in (:orgIds)");
			params.put("orgIds", orgArr);
		}
		if(userRoleList.size() > 0){
			String roleArr[] = new String[userRoleList.size()];
			for(int i=0;i<userRoleList.size();i++){
				roleArr[i] = userRoleList.get(i).getRoleId();
			}
			sbSql.append(" and sysr.role_id in (:roleIds)");
			params.put("roleIds", roleArr);
		}
		sbSql.append(" and sys.notice_type<>'"+"DAT0000000344"+"'");
		if(StringUtils.isNotBlank(systemNoticeVo.getTitle())){
			sbSql.append(" sys.title like :title ");
			params.put("title", "%" + systemNoticeVo.getTitle() + "%");
		}
		sbSql.append(" order by sys.CREATE_TIME desc ");
		DataPackage listData = findPageBySql(sbSql.toString(), dp, true, params);
		List<WelcomeNoticeVo> voList =  HibernateUtils.voListMapping((List<SystemNotice>)listData.getDatas(), WelcomeNoticeVo.class);
		dp.setDatas(voList);
		return dp;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SystemNoticeVo> getSystemNoticeByTypeTopNum(String recordId, String noticeType,String num){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from SystemNotice where 1=1");
		
		hql.append(" and noticeType.id = :noticeType");
		params.put("noticeType", noticeType);
		if (StringUtil.isNotBlank(recordId)) {
			hql.append(" and id != :id ");
			params.put("id", recordId);
		}
		hql.append(" order by createTime desc");
		DataPackage dp=new DataPackage(0,Integer.parseInt(num));
		dp=super.findPageByHQL(hql.toString(), dp, true, params);
		List<SystemNotice> lsv=(List<SystemNotice>) dp.getDatas();
		List<SystemNoticeVo> lsnv=HibernateUtils.voListMapping(lsv, SystemNoticeVo.class); 
		List<SystemNoticeVo> ls=new ArrayList<SystemNoticeVo>();
		for(SystemNoticeVo systemNoticeVo:lsnv){
			Organization organization = organizationDao.findById(systemNoticeVo.getCreateUserOrgId());
			systemNoticeVo.setCreateUserOrg(organization.getName());
			if(StringUtil.isNotEmpty(systemNoticeVo.getModifyUserOrgId())){//新增的 公告没有modifyId 会报空指针
				organization = organizationDao.findById(systemNoticeVo.getModifyUserOrgId());
				systemNoticeVo.setModifyUserOrg(organization.getName());
			}
			ls.add(systemNoticeVo);
		}
		return ls;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SystemNoticeVo> getSystemNoticeOneAndMore(String recordId,String noticeType,String num){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from SystemNotice where ");
		hql.append(" and noticeType.id = :noticeType");
		params.put("noticeType", noticeType);
		hql.append(" order by createTime desc");
		DataPackage dp=new DataPackage(0,Integer.parseInt(num));
		dp=super.findPageByHQL(hql.toString(), dp, true, params);
		List<SystemNotice> lsn=(List<SystemNotice>) dp.getDatas();
		List<SystemNoticeVo> ls=new ArrayList<SystemNoticeVo>();
		SystemNotice sn = findById(recordId); 
		ls.add(HibernateUtils.voObjectMapping(sn, SystemNoticeVo.class));
		System.arraycopy(HibernateUtils.voListMapping(lsn, SystemNoticeVo.class), 0, ls, 1, lsn.size());
		List<SystemNoticeVo> lstt=new ArrayList<SystemNoticeVo>();
		for(SystemNoticeVo systemNoticeVo:ls){
			Organization organization = organizationDao.findById(systemNoticeVo.getCreateUserOrgId());
			systemNoticeVo.setCreateUserOrg(organization.getName());
			if(StringUtil.isNotEmpty(systemNoticeVo.getModifyUserOrgId())){//新增的 公告没有modifyId 会报空指针
				organization = organizationDao.findById(systemNoticeVo.getModifyUserOrgId());
				systemNoticeVo.setModifyUserOrg(organization.getName());
			}
			ls.add(systemNoticeVo);
		}
		return lstt;
	}
	
	
}
