package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import tebie.applib.api.E;

import com.eduboss.common.NewsType;
import com.eduboss.dao.AppNewsManageDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.AppNewsManage;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AppNewsManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;

@Repository("AppNewsManageDao")
public class AppNewsManageDaoImpl extends GenericDaoImpl<AppNewsManage, String>  implements AppNewsManageDao {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Override
	public DataPackage getNews(DataPackage dp, AppNewsManageVo vo) {
		StringBuffer sql=new StringBuffer();
		StringBuffer sqlWhere=new StringBuffer();
		User user=userService.getCurrentLoginUser();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select anm.* from app_news_manage anm ");
		sqlWhere.append(" where 1=1  ");
		if(vo.getTopButn()!=null && vo.getTopButn().equals("app")){
			//app只查询有效信息，pc列表显示所有信息
			sqlWhere.append("and VALIDE_STATUS='VALID'  ");
		}	
		if(vo.getType()!=null){
			sqlWhere.append(" and anm.type = :anmType ");
			params.put("anmType", vo.getType());
		}
		if(StringUtils.isNotBlank(vo.getTitle())){
			sqlWhere.append(" and anm.title like :anmTitle ");
			params.put("anmTitle", "%"+vo.getTitle()+"%");
		}
		if(StringUtils.isNotBlank(vo.getCreateUserName())){			
			sql.append(" INNER JOIN `user` u on anm.CREATE_USER=u.USER_ID ");
			sqlWhere.append(" and u.name LIKE :uName ");
			params.put("uName", "%"+vo.getCreateUserName()+"%");
		}
		Organization org=organizationDao.findById(user.getOrganizationId());
		if(NewsType.BRENCHINFO.equals(vo.getType()) && !org.getOrgType().toString().equals("GROUNP")){
			//分公司信息,当前登陆人可看到自己属于的分公司信息
			sql.append(" INNER JOIN app_news_manage_brench ab on anm.id=ab.app_id ");
			sqlWhere.append(" and ab.brench_id in ( select org.id from organization org where org.orgLevel in ");
			sqlWhere.append(" (select substring(o.orgLevel, 1, 8) from organization o INNER JOIN user_organization_role uo on o.id=uo.organization_ID where uo.user_ID= :uoUserID) ) ");
			params.put("uoUserID", user.getUserId());
		}
		if(NewsType.BRENCHINFO.equals(vo.getType()) && !org.getOrgType().toString().equals("GROUNP")){
			sqlWhere.append(" GROUP BY ab.app_id ");
		}
		sqlWhere.append(" order by anm.create_time DESC ");
		
		dp=this.findPageBySql(sql.toString()+sqlWhere.toString(), dp,true, params);
		List<AppNewsManage> list=(List<AppNewsManage>)dp.getDatas();
		List<AppNewsManageVo> voList=new ArrayList<AppNewsManageVo>();
		if(list!=null && list.size()>0){
			voList=HibernateUtils.voListMapping(list, AppNewsManageVo.class);
		}
		getImageUrl();
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public void saveOrEditNewsManage(AppNewsManage appNewsManage) {
//		appNewsManage.setCreateUser(userService.findUserById(appNewsManage.getCreateUser().getUserId()));
		this.save(appNewsManage);
	}

	@Override
	public AppNewsManageVo findAppNewsManageById(String id) {
		String sql="select brench_id from app_news_manage_brench where app_id='"+id+"'";
		List<String> list=new ArrayList<String>();
		String brenchId="";
		AppNewsManage appNewsManage=this.findById(id);
		AppNewsManageVo vo=new AppNewsManageVo();
		if(appNewsManage!=null){
			vo=HibernateUtils.voObjectMapping(appNewsManage, AppNewsManageVo.class);
		}
		if(vo.getType()==NewsType.BRENCHINFO){
			list=(List<String>) this.getCurrentSession().createSQLQuery(sql).list();
			if(list!=null && list.size()>0){
				for(String orgId:list){
					brenchId += orgId+",";
				}
			}
		}
		vo.setBrenchId(brenchId);
		
		return vo;
		
		
	}
	
	//删掉新闻，附件，brenchID
	@Override
	public void delNewsManage(String id) {
		Map<String, Object> params = Maps.newHashMap();
		Map<String, Object> params1 = Maps.newHashMap();

		String sql="delete from app_news_manage where id= :appNewsManageId ";
		String sqlBrench="delete from app_news_manage_brench where brench_id= :brenchId ";
		params.put("appNewsManageId", id);
		params1.put("brenchId", id);
		this.excuteSql(sql, params);
		this.excuteSql(sqlBrench, params1);
		
	}
	
	//获取五大类的最新图片信息
	@Override
	public List<AppNewsManageVo> getImageUrl(){
		String sql="select * from app_news_manage where VALIDE_STATUS='VALID' and TYPE<>'BRENCHINFO' ORDER BY CREATE_TIME DESC ";
		StringBuffer sqlb=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		Map<String, Object> emptyMap = Maps.newHashMap();
		User user=userService.getCurrentLoginUser();
		Organization org=organizationDao.findById(user.getOrganizationId());
		if(org.getOrgType().toString().equals("GROUNP")){
			sqlb.append(" select anm.*  from app_news_manage anm ");
			sqlb.append("  where VALIDE_STATUS='VALID' and anm.type ='BRENCHINFO'  ");

		}else{
			sqlb.append(" select anm.*  from app_news_manage anm   INNER JOIN app_news_manage_brench ab   on anm.id=ab.app_id ");
			sqlb.append("  where VALIDE_STATUS='VALID' and anm.type ='BRENCHINFO'  ");
			sqlb.append(" and ab.brench_id in ( select org.id from organization org where org.orgLevel in  ");
			sqlb.append(" (select substring(o.orgLevel, 1, 8) from organization o INNER JOIN user_organization_role uo on o.id=uo.organization_ID where uo.user_ID= :userID ) )  ");
			params.put("userID", userService.getCurrentLoginUser().getUserId());
		}
		sqlb.append(" order by anm.create_time desc");
		List<AppNewsManage> list=this.findBySql(sql, emptyMap);
		List<AppNewsManageVo> voList=new ArrayList<AppNewsManageVo>();
		if(list!=null){
			for(AppNewsManage app:list){
				boolean type=true;
				if(voList.size()>0 && voList.size()<5){
					for(AppNewsManageVo a:voList){
						if(a.getType()==app.getType()){
							type=false;
							break;
						}
					}
				}
				if(type){
					AppNewsManageVo vo=HibernateUtils.voObjectMapping(app,AppNewsManageVo.class);
					voList.add(vo);
				}								
			}
		}
		List<AppNewsManage> anmList=this.findBySql(sqlb.toString(), params);
		if(anmList!=null && anmList.size()>0){
			AppNewsManageVo anmVo=HibernateUtils.voObjectMapping(anmList.get(0), AppNewsManageVo.class);
			voList.add(anmVo);
		}
		
		
		return voList;
	}

	@Override
	public List<AppNewsManageVo> getNewsBanner(int num) {
		Map<String, Object> params = Maps.newHashMap();
		String sql="select * from app_news_manage where type='NEWSBANNER' and VALIDE_STATUS='VALID' ORDER BY CREATE_TIME DESC limit 1, :num ";
		params.put("num", num);
		List<AppNewsManage> list=this.findBySql(sql, params);
		List<AppNewsManageVo> volist=HibernateUtils.voListMapping(list, AppNewsManageVo.class);
		return volist;
	}
	
	
	
}
