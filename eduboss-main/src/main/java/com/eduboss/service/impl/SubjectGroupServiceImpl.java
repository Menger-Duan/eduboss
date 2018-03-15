package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.SubjectGroupDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.RefSubjectGroup;
import com.eduboss.domain.SubjectGroup;
import com.eduboss.domain.TeacherVersion;
import com.eduboss.domainVo.RefSubjectGroupVo;
import com.eduboss.domainVo.SubjectGroupSummaryVo;
import com.eduboss.domainVo.SubjectGroupVo;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.OperationCountService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.RefSubjectGroupService;
import com.eduboss.service.SubjectGroupService;
import com.eduboss.service.TeacherSubjectVersionService;
import com.eduboss.service.TeacherVersionService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

/**
 * 2016-12-17
 * @author lixuejun
 *
 */
@Service
public class SubjectGroupServiceImpl implements SubjectGroupService {
	
	@Autowired
	private RefSubjectGroupService refSubjectGroupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TeacherSubjectVersionService teacherSubjectVersionService;
	
	@Autowired
	private OperationCountService operationCountService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private SubjectGroupDao subjectGroupDao;
	
	@Autowired
	private TeacherVersionService teacherVersionService;

	/**
	 * 保存或修改科组
	 */
	@Override
	public void editSubjectGroup(SubjectGroupVo subjectGroupVo) {
		// 先判断名字是否重复
		if (subjectGroupVo.getId() <= 0) {
			int isexit = subjectGroupDao.countSubjectGroupList(subjectGroupVo.getBlBrenchId(), subjectGroupVo.getBlCampusId(), subjectGroupVo.getVersion(), subjectGroupVo.getNameId());
			if (isexit > 0) {
				throw new ApplicationException("科组不能重名");
			}
			
		}
		
		String currentMonth = DateTools.getCurrentDate().substring(0, 4) + DateTools.getCurrentDate().substring(5,7);
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		String currentTime = DateTools.getCurrentDateTime();
		SubjectGroup subjectGroup = HibernateUtils.voObjectMapping(subjectGroupVo, SubjectGroup.class);
		Set<RefSubjectGroupVo> refSubjectGroupVos = subjectGroupVo.getMoveRefSubjectGroups();
//		Set<RefSubjectGroupVo> refSubjectGroupVos = subjectGroupVo.getRefSubjectGroups();
//		Set<RefSubjectGroup> refSubjectGroups = HibernateUtils.voSetMapping(refSubjectGroupVos, RefSubjectGroup.class);
		if (subjectGroupVo.getVersion() < Integer.parseInt(currentMonth)) {
			throw new ApplicationException("不能修改本月前的科组");
		}
		String subjectDes = "";
//		subjectGroup.setSubjectDes(subjectDes);
		subjectGroup.setModifyTime(currentTime);
		subjectGroup.setModifyUserId(currentUserId);
	
		if (subjectGroup.getId() > 0) {// 修改
			SubjectGroup sgInDb = subjectGroupDao.findById(subjectGroup.getId());
			// 影响到的科组
			String affectedGroupIds = "";
			for(RefSubjectGroupVo vo : refSubjectGroupVos) {
				if (!affectedGroupIds.contains(""+ vo.getSubjectGroupId())) {
					affectedGroupIds += "'" + vo.getSubjectGroupId() + "',";
				}
				if (!affectedGroupIds.contains(""+ vo.getMoveToSubjectGroupId())) {
					affectedGroupIds += "'" + vo.getMoveToSubjectGroupId() + "',";
				}
				RefSubjectGroup rsg = HibernateUtils.voObjectMapping(vo, RefSubjectGroup.class);
				rsg.setTeacherDes(null);
				rsg.setSubjectGroup(new SubjectGroup(vo.getMoveToSubjectGroupId()));
				refSubjectGroupService.editRefSubjectGroup(rsg);
			}
			if (StringUtil.isNotBlank(affectedGroupIds)) {
				affectedGroupIds = affectedGroupIds.substring(0, affectedGroupIds.length() - 1);
				// 清空影响到的科组的科目描述
				subjectGroupDao.updateSubjectGroupSubjectDesByIds(affectedGroupIds.split(","));
			}
			subjectGroup.setCreateTime(sgInDb.getCreateTime());
			subjectGroup.setCreateUserId(sgInDb.getCreateUserId());
			subjectGroupDao.merge(subjectGroup);
		} else {
			// 新增的设置排序
			int size = subjectGroupDao.countSubjectGroupList(subjectGroupVo.getBlBrenchId(), subjectGroupVo.getBlCampusId(), subjectGroupVo.getVersion(), null);
			subjectGroup.setRorder(size + 1);
			subjectGroup.setCreateTime(currentTime);
			subjectGroup.setCreateUserId(currentUserId);
			SubjectGroup otherSubjectGroup = null;
			String otherSubjectDes = "";
			for(RefSubjectGroupVo vo : refSubjectGroupVos) {
				if (otherSubjectGroup == null) {
					otherSubjectGroup = subjectGroupDao.findById(vo.getSubjectGroupId());
					otherSubjectDes = otherSubjectGroup.getSubjectDes();
				}
				if (otherSubjectDes.contains(vo.getSubjectId() + ",")) {
					otherSubjectDes = otherSubjectDes.replace(vo.getSubjectId() + ",", "");
				} else {
					otherSubjectDes = otherSubjectDes.replace("," + vo.getSubjectId(), "");
				}
				subjectDes += vo.getSubjectId() + ",";
			}
			// 更新其他科组的科目描述
			if (otherSubjectGroup != null) {
				otherSubjectGroup.setSubjectDes(otherSubjectDes);
				subjectGroupDao.merge(otherSubjectGroup);
			}
			if (StringUtil.isNotBlank(subjectDes)) {
				subjectDes = subjectDes.substring(0, subjectDes.length() - 1);
				subjectGroup.setSubjectDes(subjectDes);
			}
			subjectGroupDao.save(subjectGroup);
			subjectGroupDao.flush();
			for(RefSubjectGroupVo vo : refSubjectGroupVos) {
				RefSubjectGroup rsg = HibernateUtils.voObjectMapping(vo, RefSubjectGroup.class);
				rsg.setTeacherDes(null);
				rsg.setSubjectGroup(subjectGroup);
				refSubjectGroupService.editRefSubjectGroup(rsg);
			}
			
		}
		/*if (subjectGroupVo.getVersion() == Integer.parseInt(currentMonth)) {
			if (subjectGroupVo.getIsUpToNextMonth() == 1) {
				String nextMonth = "";
				try {
					nextMonth = DateTools.getNextMonth(DateTools.getCurrentDate()).substring(0, 4) 
							+  DateTools.getNextMonth(DateTools.getCurrentDate()).substring(5,7);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 执行存储过程，批量全部更新下月这个校区的科组
				operationCountService.updateToNextVersion(subjectGroupVo.getBlBrenchId(), subjectGroupVo.getBlCampusId(), subjectGroup.getVersion(), Integer.parseInt(nextMonth));
			}
		}*/
	}
	
	
	/**
	 * 删除科组
	 */
	@Override
	public void deleteSubjectGroup(SubjectGroupVo subjectGroupVo) {
		SubjectGroup sgInDb = subjectGroupDao.findById(subjectGroupVo.getId());
		if (sgInDb.getName().equals("其他")) {
			throw new ApplicationException("其他科组不能删除");
		}
		List<SubjectGroup> sgList = subjectGroupDao.getSubjectGroupList(sgInDb.getBlBrench().getId(), sgInDb.getBlCampus().getId(), sgInDb.getVersion(), null);
		// 更新排序
		for (SubjectGroup sg : sgList) {
			if (sg.getId() != sgInDb.getId() && sg.getRorder() > sgInDb.getRorder()) {
				int rorder = sg.getRorder() - 1 > 0 ? sg.getRorder() - 1 : 0;
				sg.setRorder(rorder);
				subjectGroupDao.merge(sg);
			}
		}
		// 转移科组下的科目到其他科组
		Set<RefSubjectGroup> rsgSet = sgInDb.getRefSubjectGroups();
		if (rsgSet.size() > 0) {
			List<SubjectGroup> otherSgList = subjectGroupDao.getSubjectGroupList(sgInDb.getBlBrench().getId(), sgInDb.getBlCampus().getId(), sgInDb.getVersion(), "其他");
			SubjectGroup otherSg =  otherSgList.get(0);
			String subjectDes = "";
			for (RefSubjectGroup rsg : rsgSet) {
				rsg.setSubjectGroup(otherSg);
				subjectDes += rsg.getSubject().getId() + ",";
				refSubjectGroupService.editRefSubjectGroup(rsg);
			}
			subjectDes = subjectDes.substring(0, subjectDes.length() - 1);
			if (StringUtils.isNotBlank(otherSg.getSubjectDes())) {
				otherSg.setSubjectDes(otherSg.getSubjectDes() + "," + subjectDes);
			} else {
				otherSg.setSubjectDes(subjectDes);
			}
		}
		subjectGroupDao.flush();
		sgInDb.setRefSubjectGroups(null);
		subjectGroupDao.delete(sgInDb);
	}
	
	/**
	 * 移动科组
	 */
	@Override
	public void moveSubjectGroup(int id, int type) {
		SubjectGroup sgInDb = subjectGroupDao.findById(id);
		List<SubjectGroup> sgList = subjectGroupDao.getSubjectGroupList(sgInDb.getBlBrench().getId(), sgInDb.getBlCampus().getId(), sgInDb.getVersion(), null);
		// 未移动前的排序
		int rorder = sgInDb.getRorder();
		for (SubjectGroup sg : sgList) {
			// 当前科组的排序修改
			if (sg.getId() == sgInDb.getId()) {
				sg.setRorder(sg.getRorder() + type);
				subjectGroupDao.merge(sg);
			}
			// 对调科组的排序修改
			if (sg.getRorder() ==  rorder + type) {
				sg.setRorder(rorder - type);
				subjectGroupDao.merge(sg);
			}
		}
		subjectGroupDao.flush();
	}

	/**
	 * 按校区版本查询科组列表概要
	 */
	@Override
	public SubjectGroupSummaryVo updateAndgetSubjectGroupSummary(String brenchId,
			String campusId, int version) {
		SubjectGroupSummaryVo returnVo = new SubjectGroupSummaryVo();
		List<SubjectGroup> list = subjectGroupDao.getSubjectGroupList(brenchId, campusId, version, null);
		Map<Integer, SubjectGroup> refMap = new HashMap<Integer, SubjectGroup>();
		for (SubjectGroup sg : list) {
			refMap.put(sg.getId(), sg);
		}
		List<SubjectGroupVo> voList = HibernateUtils.voListMapping(list, SubjectGroupVo.class);
		int groupSum = voList.size() > 0 ? voList.size() -1 : 0;
		returnVo.setGroupSum(groupSum);
		int subjectSum = 0;
		int teahcerSum = 0;
		String teacherDes = "";
		String allTeacherDes = "";
		String subjectDes = "";
		for (SubjectGroupVo vo : voList) {
			teacherDes = "";
			subjectDes = vo.getSubjectDes();
			if (StringUtils.isBlank(subjectDes)) { // 如果没有科目描述，查找出来并更新到数据库去
				SubjectGroup sg = refMap.get(vo.getId());
				subjectDes = this.getSubjectDes(sg.getRefSubjectGroups());
				sg.setSubjectDes(subjectDes);
				subjectGroupDao.merge(sg);
				vo.setSubjectDes(subjectDes);
			}
			String[] desArr = subjectDes.split(",");
			if (!vo.getNameName().equals("其他")) {
				subjectSum += desArr.length;
			}
			// 没有老师描述
			if (StringUtils.isBlank(vo.getTeacherDes()) && StringUtils.isNotBlank(subjectDes)) {
				for (String des : desArr) {
					String tmpDes = teacherSubjectVersionService.findAssociatedTeacherNameDes(vo.getBlCampusId(), des, vo.getVersion());
					if (StringUtils.isNotBlank(tmpDes)) {
						String[] tmpDesArr = tmpDes.split(",");
						for (String tmp : tmpDesArr) {
							if (!teacherDes.contains(tmp)) {
								teacherDes += tmp + ",";
							}
							if (!vo.getNameName().equals("其他") && !allTeacherDes.contains(tmp)) {
								allTeacherDes += tmp + ",";
							}
						}
					}
				}
				if (StringUtils.isNotBlank(teacherDes)) {
					teacherDes = teacherDes.substring(0, teacherDes.length() - 1);
				}
			} else if (StringUtils.isNotBlank(vo.getTeacherDes())) { // 有老师描述
				String[] teacherArr = vo.getTeacherDes().split(",");
				teacherDes += userService.getUserNamesByUserIds(teacherArr);
				if (!vo.getNameName().equals("其他") && StringUtil.isNotBlank(teacherDes)) {
					String[] teacherDesArr = teacherDes.split(",");
					for (String tmp : teacherDesArr) {
						if (!allTeacherDes.contains(tmp)) {
							allTeacherDes += tmp + ",";
						}
					}
				}
				if (StringUtils.isNotBlank(teacherDes)) {
					teacherDes = teacherDes.substring(0, teacherDes.length() - 1);
				}
			}
			vo.setTeacherDes(teacherDes);
			vo.setRefSubjectGroups(null);
		}
		if (StringUtils.isNotBlank(allTeacherDes)) {
			allTeacherDes = allTeacherDes.substring(0, allTeacherDes.length() - 1);
			String[] tNameArr = allTeacherDes.split(",");
			teahcerSum += tNameArr.length;
		}
		returnVo.setSubjectSum(subjectSum);
		returnVo.setTeahcerSum(teahcerSum);
		returnVo.setSubjectGroups(voList);
		return returnVo;
	}
	
	// 获取科目描述
	private String getSubjectDes(Set<RefSubjectGroup> refSet) {
		String subjectDes = "";
		if (refSet.size() > 0) {
			for (RefSubjectGroup ref : refSet) {
				subjectDes += ref.getSubject().getId() + ",";
			}
			subjectDes = subjectDes.substring(0, subjectDes.length() - 1);
		}
		return subjectDes;
	}

	/**
	 * 根据id查找科组
	 */
	@Override
	public SubjectGroupVo findSubjectGroup(int subjectGroupId) {
		SubjectGroup subjectGroup = subjectGroupDao.findById(subjectGroupId);
		SubjectGroupVo subjectGroupVo = HibernateUtils.voObjectMapping(subjectGroup, SubjectGroupVo.class);
		Set<RefSubjectGroup> rsgs = subjectGroup.getRefSubjectGroups();
		Set<RefSubjectGroupVo> rsgVos = HibernateUtils.voSetMapping(rsgs, RefSubjectGroupVo.class);
		for (RefSubjectGroupVo rsgVo: rsgVos) {
			String teacherDes = "";
			if (StringUtils.isNotBlank(rsgVo.getTeacherDes())) {
				String[] teacherArr = rsgVo.getTeacherDes().split(",");
				teacherDes += userService.getUserNamesByUserIds(teacherArr);
			} else {
				teacherDes += teacherSubjectVersionService.findAssociatedTeacherNameDes(subjectGroupVo.getBlCampusId(), rsgVo.getSubjectId(), rsgVo.getVersion()) + ","; 
			}
			if (StringUtils.isNotBlank(teacherDes) && teacherDes.contains(",")) {
				teacherDes = teacherDes.substring(0, teacherDes.length() - 1);
			}
			rsgVo.setTeacherDes(teacherDes);
		}
		subjectGroupVo.setRefSubjectGroups(rsgVos);
		return subjectGroupVo;
	}

	@Override
	public List<SubjectGroupVo> getTransferGroupSubjects(int groupId) {
		SubjectGroup sgInDb = subjectGroupDao.findById(groupId);
		List<SubjectGroupVo> returnVoList = new ArrayList<SubjectGroupVo>();
		List<SubjectGroup> list = subjectGroupDao
					.getSubjectGroupList(sgInDb.getBlBrench().getId(), sgInDb.getBlCampus().getId(), sgInDb.getVersion(), null);
		for (SubjectGroup sg : list) {
			if (!sg.getName().equals("其他") && sg.getId() != groupId) {
				SubjectGroupVo vo = HibernateUtils.voObjectMapping(sg, SubjectGroupVo.class);
				vo.setRefSubjectGroups(null);
				returnVoList.add(vo);
			}
		}
		return returnVoList;
	}

	/**
	 * 根据科目ID，版本查询科组名称
	 */
	@Override
	public String findSubjectGroupName(String subjectId, String campusId, String versionDate, String teacherId) {
		String subjectGroupName = "";
		int searchVersion = 0;
		if (StringUtil.isNotBlank(versionDate)) {
			List<TeacherVersion> list = teacherVersionService.getTeacherVersionList(teacherId);
			if (list.size() <= 1) {
				searchVersion = Integer.parseInt(versionDate.substring(0, 4) + versionDate.substring(5,7));
			} else {
				String searchVersionDate = "";
				for (TeacherVersion tv : list) {
					if (DateTools.daysBetween(versionDate, tv.getVersionDate()) > 0) {
						searchVersionDate = tv.getVersionDate();
						break;
					}
				}
				if (StringUtil.isNotBlank(searchVersionDate)) {
					if (DateTools.daysBetween(searchVersionDate, DateTools.getDateToString(DateTools.getFirstDayOfMonth(searchVersionDate))) == 0) {
						try {
							searchVersionDate = DateTools.getDateToString(DateTools.getLastMonthStart(searchVersionDate));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					searchVersion = Integer.parseInt(searchVersionDate.substring(0, 4) + searchVersionDate.substring(5,7));
				} else {
					searchVersion = Integer.parseInt(versionDate.substring(0, 4) + versionDate.substring(5,7));
				}
			}
			
		} else {
			String currentMonth = DateTools.getCurrentDate().substring(0, 4) + DateTools.getCurrentDate().substring(5,7);
			searchVersion = Integer.parseInt(currentMonth);
		}
		RefSubjectGroupVo vo = new RefSubjectGroupVo();
		vo.setSubjectId(subjectId);
		vo.setBlCampusId(campusId);
		vo.setVersion(searchVersion);
		
		List<RefSubjectGroup> list = refSubjectGroupService.getRefSubjectGroupList(vo);
		if (list.size() > 0) {
			subjectGroupName = list.get(0).getSubjectGroup().getName().getName();
		}
		return subjectGroupName;
	}

	/**
	 * 根据校区ID查询科组下拉选择
	 */
	@Override
	public SelectOptionResponse getSubjectGroupForSelection(String campusId) {
		Organization organization = organizationService.findById(campusId);
		int versionMonthInt = Integer.parseInt(DateTools.getCurrentDate().substring(0, 4) 
				+ DateTools.getCurrentDate().substring(5,7));
		List<SubjectGroup> list = subjectGroupDao.getSubjectGroupList(organization.getParentId(), campusId, versionMonthInt, null);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for (SubjectGroup sg : list) {
			nvs.add(SelectOptionResponse.buildNameValue(sg.getName().getName(), sg.getId() + ""));
		}
		return new SelectOptionResponse(nvs);
	}
	
}
