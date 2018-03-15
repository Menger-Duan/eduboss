package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.DistributeType;
import com.eduboss.dao.ContractProductSubjectDao;
import com.eduboss.service.ContractProductSubjectService;
import com.eduboss.service.CourseHoursDistributeRecordService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

/**
 * 合同产品科目排课service
 * @author lixuejun
 *
 */
@Service("ContractProductSubjectService")
public class ContractProductSubjectServiceImpl implements
		ContractProductSubjectService {
	
	@Autowired
	private ContractProductSubjectDao contractProductSubjectDao;
	
	@Autowired
	private CourseHoursDistributeRecordService courseHoursDistributeRecordService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationDao organizationDao;
	
	
	
	/**
	 * 根据合同产品id查询科目排课列表
	 */
	@Override
	public List<ContractProductSubject> findContractProductSubjectByCpId(String contractProductId) {
		return contractProductSubjectDao.findContractProductSubjectByCpId(contractProductId);
	}
	
	/**
	 * 删除ContractProductSubject
	 */
	@Override
	public void deleteContractProductSubject(ContractProductSubject contractProductSubject) {
		contractProductSubjectDao.delete(contractProductSubject);
	}
	
	/**
	 * 保存或修改ContractProductSubject
	 */
	@Override
	public void saveOrUpdateContractProductSubject(ContractProductSubject contractProductSubject) {
		if (StringUtils.isNotBlank(contractProductSubject.getId())) {
			contractProductSubjectDao.merge(contractProductSubject);
		} else {
			contractProductSubjectDao.save(contractProductSubject);
		}
	}
	
	/**
	 * 根据合同产品id和科目id查找ContractProductSubject
	 */
	@Override
	public ContractProductSubject findContractProductSubjectByCpIdAndSubjectId(String contractProductId, String subjectId) {
		return contractProductSubjectDao.findContractProductSubjectByCpIdAndSubjectId(contractProductId, subjectId);
	}
	
	/**
	 * 修改合同产品，合同产品总金额小于已分配金额转移所有未消耗课时
	 */
	@Override
	public void transferOutContractProductSubject(ContractProduct contractProduct) {
		User currentUser = userService.getCurrentLoginUser();
		Contract contract = contractProduct.getContract();
		Organization blCampus = null;
		if (contract!=null){
			String blCampusId = contract.getBlCampusId();
			if (StringUtil.isNotBlank(blCampusId)){
				blCampus = organizationDao.findById(blCampusId);
			}
		}
		List<ContractProductSubject> list = this.findContractProductSubjectByCpId(contractProduct.getId());
		BigDecimal totalDistributedHours = BigDecimal.ZERO;
		String distributedHoursStr = "";
		for(ContractProductSubject cps : list) {
			if (cps.getQuantity().compareTo(cps.getConsumeHours()) > 0) {
				CourseHoursDistributeRecord record = new CourseHoursDistributeRecord(contractProduct.getId(), cps.getSubject(), 
						DistributeType.TRANSFER_OUT, cps.getQuantity().subtract(cps.getConsumeHours()), cps.getConsumeHours(), 
						cps.getConsumeHours(), BigDecimal.ZERO, currentUser, DateTools.getCurrentDateTime(), blCampus);
				courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(record);
				cps.setQuantity(cps.getConsumeHours());
				this.saveOrUpdateContractProductSubject(cps);
			}
			totalDistributedHours =totalDistributedHours.add(cps.getQuantity());
			distributedHoursStr += cps.getSubject().getName() + ":" + cps.getQuantity() + ",";
		}
		contractProduct.setOooSubjectDistributedHours(totalDistributedHours);
		if (StringUtil.isNotBlank(distributedHoursStr)) {
			distributedHoursStr = distributedHoursStr.substring(0, distributedHoursStr.length() - 1);
		}
		contractProduct.setOooSubjectDistributedHoursDes(distributedHoursStr);
	}

	/**
	 * 根据学生、产品组(或产品)、科目组计算剩余课时
	 */
	@Override
	public BigDecimal sumRemainHoursByStudentProductSubject(String studentId,
			String productGroupId, String productId, String subjectId) {
		return contractProductSubjectDao.sumRemainHoursByStudentProductSubject(studentId, productGroupId, productId, subjectId);
	}

	/**
	 * 根据id查找ContractProductSubject
	 */
	@Override
	public ContractProductSubject findById(String id) {
		return contractProductSubjectDao.findById(id);
	}
	
	/**
	 * 学生转校区，修改未消耗完的合同产品科目的校区
	 */
	@Override
	public void updateContractSubjectForTurnCampus(String studentId, String campusId) {
		if (StringUtil.isNotBlank(studentId) && StringUtil.isNotBlank(campusId)) {
			contractProductSubjectDao.updateContractSubjectForTurnCampus(studentId, campusId);
		}
	}
	
}
