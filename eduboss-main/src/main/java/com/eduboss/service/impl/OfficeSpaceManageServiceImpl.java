package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.OrganizationType;
import com.eduboss.dao.OfficeSpaceManageDao;
import com.eduboss.domain.OfficeSpaceManage;
import com.eduboss.domainVo.OfficeSpaceManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.DataDictService;
import com.eduboss.service.OfficeSpaceManageService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service("OfficeSpaceManageServiceImpl")
public class OfficeSpaceManageServiceImpl implements OfficeSpaceManageService{
	@Autowired
	private OfficeSpaceManageDao officeSpaceManageDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataDictService dataDictService;

	/**
	 * 查询
	 * @param dataPackage
	 * @param classroomManageVo
	 * @param params
	 * @return
	 */
	public DataPackage getOfficeSpaceManageList(DataPackage dataPackage,
			OfficeSpaceManageVo OfficeSpaceManageVo, Map<String, Object> params) {
		dataPackage= officeSpaceManageDao.getOfficeSpaceManageList(dataPackage, OfficeSpaceManageVo,params);		
		List<OfficeSpaceManage> list = (List<OfficeSpaceManage>) dataPackage.getDatas();
		List<OfficeSpaceManageVo> voList = new ArrayList<OfficeSpaceManageVo>();
		for(OfficeSpaceManage con : list){
			OfficeSpaceManageVo vo = HibernateUtils.voObjectMapping(con, OfficeSpaceManageVo.class);
			vo.setStatusName(vo.getStatus() == 1 ? "有效":"无效");
			voList.add(vo);
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}
	
	/**
	 * 新增信息
	 * @param officeSpaceManageVo
	 */
	public void saveOfficeSpaceManage(OfficeSpaceManageVo officeSpaceManageVo) {
		OfficeSpaceManage officeSpaceManage = HibernateUtils.voObjectMapping(officeSpaceManageVo, OfficeSpaceManage.class);
		//checkPlanManagement(officeSpaceManage);//检查组织架构信息是否存在
		officeSpaceManage.setCreator(userService.getCurrentLoginUser());
		officeSpaceManage.setModifier(userService.getCurrentLoginUser());
		officeSpaceManage.setCreateTime(DateTools.getCurrentDateTime());
		officeSpaceManage.setModifyTime(DateTools.getCurrentDateTime());
		officeSpaceManageDao.save(officeSpaceManage);
	}
	
	
	private Response checkPlanManagement(OfficeSpaceManage officeSpaceManage){
		Response res =new Response();
		List<Criterion> criterionList = new ArrayList<Criterion>();
		
		if(StringUtils.isNotEmpty(officeSpaceManage.getId())){
			criterionList.add(Expression.ne("id", officeSpaceManage.getId()));
		}
		
		if(StringUtils.isNotEmpty(officeSpaceManage.getOrganization().getId())){
			criterionList.add(Expression.eq("organization.id", officeSpaceManage.getOrganization().getId()));
		}
		
		List list=officeSpaceManageDao.findAllByCriteria(criterionList);
		if(list!=null && list.size()>0){
			throw new ApplicationException("当前组织架构的信息已经存在");
		}
		return res;
	}
	
	/**
	 * 查找1条信息
	 * @param id
	 * @return
	 */
	@Override
	public OfficeSpaceManageVo findOfficeSpaceById(String id) {
		if(id == null || id.equals("")){
			return new OfficeSpaceManageVo();
		}
		return HibernateUtils.voObjectMapping(officeSpaceManageDao.findById(id), OfficeSpaceManageVo.class);
	}
	
	/**
	 * 修改
	 * @param officeSpaceManageVo
	 */
	@Override
	public void modifyOfficeSpaceManage(OfficeSpaceManageVo officeSpaceManageVo) {
		OfficeSpaceManage officeSpaceManage = HibernateUtils.voObjectMapping(officeSpaceManageVo, OfficeSpaceManage.class);
		officeSpaceManage.setModifier(userService.getCurrentLoginUser());
		officeSpaceManage.setModifyTime(DateTools.getCurrentDateTime());
		officeSpaceManageDao.save(officeSpaceManage);		
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@Override
	public void deleteOfficeSpaceManage(String id) {
		if(id != null && !id.equals("")){
			OfficeSpaceManage officeSpaceManage = officeSpaceManageDao.findById(id);
			officeSpaceManageDao.delete(officeSpaceManage);
		}
	}

	/**
	 * 场地信息统计
	 */
	@Override
	public DataPackage officeSpaceCountList(DataPackage dataPackage,String campusType,String organizationIdFinder,OrganizationType organizationType) {
		return officeSpaceManageDao.officeSpaceCountList(dataPackage,campusType,organizationIdFinder,organizationType);
	}
	
	/**
	 * 添加场地前判断
	 */
	public void beforSaveOfficeSpace( String orgId,String space,String fid) throws Exception{
		int count=officeSpaceManageDao.beforSaveOfficeSpace(orgId, space,fid);
		if(count>0){
			throw new ApplicationException("场地名称重复，请确认后重新填写!");
		}else{
			return;
		}
	}
	
}
