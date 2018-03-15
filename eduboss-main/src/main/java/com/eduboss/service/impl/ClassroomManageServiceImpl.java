package com.eduboss.service.impl;

import com.eduboss.dao.ClassroomManageDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.ClassroomManage;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.ClassroomManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.ClassroomManageService;
import com.eduboss.service.DataDictService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("ClassroomManageService")
public class ClassroomManageServiceImpl implements ClassroomManageService{
	@Autowired
	private ClassroomManageDao classroomManageDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataDictService dataDictService;

	@Autowired
	private OrganizationDao organizationDao;

	/**
	 * 查询
	 * @param dataPackage
	 * @param classroomManageVo
	 * @param params
	 * @return
	 */
	public DataPackage getClassroomManageList(DataPackage dataPackage,
			ClassroomManageVo classroomManageVo, Map<String, Object> params) {
		Object branchId = params.get("branchId");
		if (branchId!=null){
			Organization branch = organizationDao.findById(branchId.toString());
			if (branch!=null){
				String  branchOrgLevel = branch.getOrgLevel();
				params.put("branchOrgLevel", branchOrgLevel);
			}
		}
		dataPackage= classroomManageDao.getContractPayManageList(dataPackage, classroomManageVo,params);
		List<ClassroomManage> list = (List<ClassroomManage>) dataPackage.getDatas();
		List<ClassroomManageVo> voList = new ArrayList<ClassroomManageVo>();
		for(ClassroomManage con : list){
			ClassroomManageVo vo = HibernateUtils.voObjectMapping(con, ClassroomManageVo.class);
			vo.setStatusName(vo.getStatus() == 1 ? "有效":"无效");
			voList.add(vo);
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}
	
	/**
	 * 新增信息
	 * @param classroomManageVo
	 */
	public void saveClassroomManage(ClassroomManageVo classroomManageVo) {
		ClassroomManage classroomManage = HibernateUtils.voObjectMapping(classroomManageVo, ClassroomManage.class);
		classroomManage.setCreator(userService.getCurrentLoginUser());
		classroomManage.setModifier(userService.getCurrentLoginUser());
		classroomManage.setCreateTime(DateTools.getCurrentDateTime());
		classroomManage.setModifyTime(DateTools.getCurrentDateTime());
		classroomManageDao.save(classroomManage);
	}

	/**
	 * 查找1条信息
	 * @param id
	 * @return
	 */
	@Override
	public ClassroomManageVo findClassroomById(String id) {
		if(id == null || id.equals("")){
			return new ClassroomManageVo();
		}
		return HibernateUtils.voObjectMapping(classroomManageDao.findById(id), ClassroomManageVo.class);
	}

	/**
	 * 修改
	 * @param classroomManageVo
	 */
	@Override
	public void modifyClassroomManage(ClassroomManageVo classroomManageVo) {
		ClassroomManage classroomManage = HibernateUtils.voObjectMapping(classroomManageVo, ClassroomManage.class);
		classroomManage.setModifier(userService.getCurrentLoginUser());
		classroomManage.setModifyTime(DateTools.getCurrentDateTime());
		classroomManageDao.save(classroomManage);		
	}

	/**
	 * 删除
	 * @param id
	 */
	@Override
	public void deleteClassroomManage(String id) {
		StringBuilder sbHql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		sbHql.append(" delete from ClassroomManage where id = :id ");
		classroomManageDao.excuteHql(sbHql.toString(),params);	
	}

	/**
	 * 自动查找教室
	 * @param input
	 * @param checkType
	 * @return
	 */
	@Override
	public List<ClassroomManage> getClassroomAutoComplate(String input,String checkType) {
		StringBuilder sbHql = new StringBuilder();
		sbHql.append(" from ClassroomManage where 1=1 ");
		sbHql.append(" and status = 1 ");
		Map<String, Object> params = Maps.newHashMap();		
		if(StringUtil.isNotBlank(input)){
			sbHql.append(" and classroom like :input ");
			params.put("classroom", "%"+input+"%");
		}
		
		return classroomManageDao.findAllByHQL(sbHql.toString(),params);
	}
	
	public List<ClassroomManage> getClassroomAutoComplate(String checkType) {
		return this.getClassroomAutoComplate("",checkType);
	}
	
	public List<ClassroomManage> getClassroomByCampus(String campusId) {

		Organization org=organizationDao.findById(campusId);


		StringBuilder sbHql = new StringBuilder();
		sbHql.append(" from ClassroomManage where 1=1 ");
		sbHql.append(" and status = 1 ");
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtil.isNotBlank(campusId)){
			sbHql.append(" and organization.id = :organizationId ");
			params.put("organizationId", campusId);
		}
		
		return classroomManageDao.findAllByHQL(sbHql.toString(),params);
	};

	/**
	 * 以教室名称找到教室ID（不安全，如果存在同名教室只获取集合的第一个）
	 * @param classroomName
	 * @return
	 */
	@Override
	public String getClassroomIdByName(String classroomName) {
		List<ClassroomManage> list= classroomManageDao.findByCriteria(Expression.eq("classroom", classroomName));
		if(list!=null && list.size()>0){
			return list.get(0).getId();
		}
		return null;
	}

    @Override
    public List getAllCurrentClassroom(String[] campusId, String brenchId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select cm.id,cm.CLASS_ROOM,cm.CLASS_MEMBER,o.name campusName from classroom_manage cm");
		sql.append(" left join organization o on cm.ORGANIZATION_ID=o.id");
		sql.append("  where cm.status='1' ");


		if(campusId!=null){
			sql.append(" and (1=2 ");
			int i =0;
			for(String campus:campusId) {
				sql.append(" or cm.organization_id =:campusId"+i);
				params.put("campusId"+i, campus);
				i++;
			}
			sql.append(") ");
		}

		if(StringUtils.isNotBlank(brenchId)){
			sql.append(" and o.parentId =:brenchId");
			params.put("brenchId",brenchId);
		}

		sql.append(" and (1=2  ");
		for(Organization o:userService.getCurrentLoginUser().getOrganization()){
			sql.append( " or o.orgLevel like '"+o.getOrgLevel()+"%'");
		}
		sql.append(" )");



		return classroomManageDao.findMapBySql(sql.toString(),params);
    }


}
