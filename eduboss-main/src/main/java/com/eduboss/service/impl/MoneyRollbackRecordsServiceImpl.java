package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.MoneyRollbackRecordsDao;
import com.eduboss.dao.RollbackBackupRecordsDao;
import com.eduboss.domain.Course;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MoneyRollbackRecords;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.RollbackBackupRecords;
import com.eduboss.domainVo.MoneyRollbackRecordsVo;
import com.eduboss.domainVo.RollbackBackupRecordsVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.MoneyRollbackRecordsService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

@Service
public class MoneyRollbackRecordsServiceImpl implements
		MoneyRollbackRecordsService {
	
	@Autowired
	private MoneyRollbackRecordsDao moneyRollbackRecordsDao;
	
	@Autowired
	private RollbackBackupRecordsDao rollbackBackupRecordsDao;
	
	@Autowired
	private UserService userService;

	/**
	 * 资金回滚记录列表
	 */
	@Override
	public DataPackage getMoneyRollbackRecordsList(DataPackage dataPackage,
			MoneyRollbackRecordsVo moneyRollbackRecordsVo) {
		List<Criterion> requirementList = new ArrayList<Criterion>();
		if (StringUtil.isNotBlank(moneyRollbackRecordsVo.getStartDate())) {
			requirementList.add(Restrictions.ge("rollbackTime",
					moneyRollbackRecordsVo.getStartDate() + " 00:00:00"));
		}
		if (StringUtil.isNotBlank(moneyRollbackRecordsVo.getEndDate())) {
			requirementList.add(Restrictions.le("rollbackTime",
					moneyRollbackRecordsVo.getEndDate() + " 23:59:59"));
		}
		if (StringUtil.isNotBlank(moneyRollbackRecordsVo.getRollbackOperatorName())) {
			requirementList.add(Restrictions.like("rollbackOperator.name",
					moneyRollbackRecordsVo.getRollbackOperatorName()));
		}
		if (StringUtil.isNotBlank(moneyRollbackRecordsVo.getCauseId())) {
			requirementList.add(Restrictions.eq("cause.id",
					moneyRollbackRecordsVo.getCauseId()));
		}
		if (StringUtil.isNotBlank(moneyRollbackRecordsVo.getBlCampusId())) {
			requirementList.add(Restrictions.eq("blCampusId.id",
					moneyRollbackRecordsVo.getBlCampusId()));
		}
		if (StringUtil.isNotBlank(moneyRollbackRecordsVo.getTransactionTime())) {
			String transactionStartTime = moneyRollbackRecordsVo.getTransactionTime() + " 00:00:00";
			String transactionEndTime = moneyRollbackRecordsVo.getTransactionTime() + " 23:59:59";
			requirementList.add(Restrictions.ge("transactionTime", transactionStartTime));
			requirementList.add(Restrictions.le("transactionTime", transactionEndTime));
		}
		if (StringUtil.isNotBlank(moneyRollbackRecordsVo.getStudentName())) {
			requirementList.add(Restrictions.like("student.name",
					moneyRollbackRecordsVo.getStudentName()));
		}
		
		dataPackage = moneyRollbackRecordsDao.findPageByCriteria(dataPackage, HibernateUtils.prepareOrder(dataPackage, "rollbackTime", "desc"), requirementList);
		
		List<MoneyRollbackRecords> moneyRollbackRecordsList = (List<MoneyRollbackRecords>) dataPackage.getDatas();
		
		List<MoneyRollbackRecordsVo> moneyRollbackRecordsVos = HibernateUtils
				.voListMapping(moneyRollbackRecordsList,
						MoneyRollbackRecordsVo.class);
		dataPackage.setDatas(moneyRollbackRecordsVos);
		return dataPackage;
	}
	
	/**
	 * 根据id查询资金回滚记录
	 */
	@Override
	public MoneyRollbackRecordsVo findMoneyRollbackRecordsById(String id) {
		return HibernateUtils.voObjectMapping(moneyRollbackRecordsDao.findById(id), MoneyRollbackRecordsVo.class);
	}
	
	/**
	 * 根据transactionId获取备份的扣费记录 
	 */
	public DataPackage getRollbackBackupRecordsByTransactionId(String transactionId, DataPackage dp) {
		dp = rollbackBackupRecordsDao.getRollbackBackupRecordsByTransactionId(transactionId, dp);
		List<Object[]> list =  (List<Object[]>)  dp.getDatas();
		List<RollbackBackupRecordsVo> voList = new ArrayList<RollbackBackupRecordsVo>();
		for (Object[] obj : list) {
			RollbackBackupRecords rollbackBackupRecords=(RollbackBackupRecords) obj[0];
			RollbackBackupRecordsVo vo = HibernateUtils.voObjectMapping(rollbackBackupRecords, RollbackBackupRecordsVo.class);
			if(rollbackBackupRecords.getCourse()!=null){
				Course course = (Course)obj[1];
				vo.setCourseDate(course.getCourseDate());
				vo.setTeacherName(rollbackBackupRecords.getCourse().getTeacher().getName());
				vo.setPayHour(rollbackBackupRecords.getQuality());
				vo.setCourseTime(rollbackBackupRecords.getCourse().getCourseDate()+" "+rollbackBackupRecords.getCourse().getCourseTime());
				vo.setGrade(course.getGrade().getName());
			}else if(rollbackBackupRecords.getMiniClassCourse()!=null){
				MiniClassCourse miniClassCourse = (MiniClassCourse)obj[3];
				vo.setCourseDate(miniClassCourse.getCourseDate());
				vo.setTeacherName(rollbackBackupRecords.getMiniClassCourse().getTeacher().getName());
				if(rollbackBackupRecords.getMiniClassCourse().getCourseHours()!=null)
					vo.setPayHour(BigDecimal.valueOf(rollbackBackupRecords.getMiniClassCourse().getCourseHours()));
				vo.setCourseTime(rollbackBackupRecords.getMiniClassCourse().getCourseDate()+" "
			+rollbackBackupRecords.getMiniClassCourse().getCourseTime()+"-"+rollbackBackupRecords.getMiniClassCourse().getCourseEndTime());
				if(miniClassCourse.getGrade()!=null){
					vo.setGrade(miniClassCourse.getGrade().getName());
				}else{
					vo.setGrade("");
				}				
			} else if(rollbackBackupRecords.getOtmClassCourse()!=null){
				OtmClassCourse otmClassCourse = (OtmClassCourse)obj[5];
				vo.setCourseDate(otmClassCourse.getCourseDate());
				vo.setTeacherName(rollbackBackupRecords.getOtmClassCourse().getTeacher().getName());
				if(rollbackBackupRecords.getOtmClassCourse().getCourseHours()!=null)
					vo.setPayHour(BigDecimal.valueOf(rollbackBackupRecords.getOtmClassCourse().getCourseHours()));
				vo.setCourseTime(rollbackBackupRecords.getOtmClassCourse().getCourseDate()+" "
			+rollbackBackupRecords.getOtmClassCourse().getCourseTime()+"-"+rollbackBackupRecords.getOtmClassCourse().getCourseEndTime());
				if(otmClassCourse.getGrade()!=null){
					vo.setGrade(otmClassCourse.getGrade().getName());
				}else{
					vo.setGrade("");
				}				
			}
			vo.setChargeTypeName(rollbackBackupRecords.getChargeType().getName());
			vo.setChargeTypeValue(rollbackBackupRecords.getChargeType().getValue());
			voList.add(vo);
		}
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public void editMoneyRollbackRecords(
			MoneyRollbackRecords moneyRollbackRecords) {
		MoneyRollbackRecords moneyRollbackRecordsInDb = moneyRollbackRecordsDao.findById(moneyRollbackRecords.getId());
		moneyRollbackRecordsInDb.setCause(moneyRollbackRecords.getCause());
		moneyRollbackRecordsInDb.setDetailReason(moneyRollbackRecords.getDetailReason());
		moneyRollbackRecordsInDb.setModifyOperator(userService.getCurrentLoginUser());
		moneyRollbackRecordsInDb.setModifyTime(DateTools.getCurrentDateTime());
		moneyRollbackRecordsDao.flush();
	}

}
