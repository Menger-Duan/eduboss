package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.ElectronicAccChangeType;
import com.eduboss.dao.ElectronicAccountChangeLogDao;
import com.eduboss.domain.ElectronicAccountChangeLog;
import com.eduboss.domain.StudnetAccMv;
import com.eduboss.domainVo.ElectronicAccountChangeLogVo;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("ElectronicAccountChangeLogDao")
public class ElectronicAccountChangeLogDaoImpl extends GenericDaoImpl<ElectronicAccountChangeLog, String> implements ElectronicAccountChangeLogDao {

	private static final Logger log = LoggerFactory.getLogger(ElectronicAccountChangeLogDaoImpl.class);
	// property constants

	@Autowired
	private UserService userService;
	
	public void saveElecAccChangeLog(String studentAccMvId, ElectronicAccChangeType type, String changeType, BigDecimal changeAmount, BigDecimal afterAmount, String remark) {
		ElectronicAccountChangeLog log = new ElectronicAccountChangeLog();
        StudnetAccMv accMv = new StudnetAccMv();
        accMv.setStudentId(studentAccMvId);
        log.setStudentAccMv(accMv);
        log.setType(type);
		log.setChangeTime(DateTools.getCurrentDateTime());
		log.setChangeAmount(changeAmount);
		log.setAfterAmount(afterAmount);
		log.setChangeUser(userService.getCurrentLoginUser());
		log.setChangeType(changeType);
		log.setRemark(remark);
		super.save(log);
	}

	
	/**
	 * 根据学生ID查询学生电子账户记录
	 * */
	@Override
	public List<ElectronicAccountChangeLogVo> getElectronicAccountChangeLogByStudentId(
			ElectronicAccountChangeLogVo electronicAccountChangeLogVo) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder hql = new StringBuilder();
		hql.append(" from ElectronicAccountChangeLog where studentAccMv.studentId = :studentId ");
		hql.append(" order by changeTime desc ");
		params.put("studentId", electronicAccountChangeLogVo.getStudentId());
		List<ElectronicAccountChangeLog> list = this.findAllByHQL(hql.toString(),params);
		List<ElectronicAccountChangeLogVo> voList = new ArrayList<ElectronicAccountChangeLogVo>();
		for(ElectronicAccountChangeLog log : list){
			ElectronicAccountChangeLogVo vo = HibernateUtils.voObjectMapping(log, ElectronicAccountChangeLogVo.class);
			voList.add(vo);
		}
		return voList;
	}


	@Override
	public DataPackage getStudentRechargeRecord(DataPackage dataPackage, ElectronicAccountChangeLogVo vo, TimeVo timeVo) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("       SELECT                                                                             ");
		sql.append("       eacl.id as id ,                                                                     ");
		sql.append("       s. id as studentId ,                                                          ");
		sql.append("       s. NAME as studentName ,                                                          ");
		sql.append("       		o.`name` AS blcampusName,                                                 ");
		sql.append("       brench.`name` AS brenchName,                                                       ");
		sql.append("       eacl.CHANGE_AMOUNT as amount,                                                ");
		sql.append("       		eacl.TYPE as type ,                                                          ");
		sql.append("       		eacl.CHANGE_TIME as time,                                                 ");
		sql.append("       		u. NAME as  userName ,                                                   ");
		sql.append("       		eacl.REMARK  as remark                                                     ");
		sql.append("       FROM                                                                               ");
		sql.append("       electronic_account_change_log eacl,                                                ");
		sql.append("       student s                                                                          ");
		sql.append("       LEFT JOIN organization o ON s.BL_CAMPUS_ID = o.id, organization brench,`user` u    ");
		sql.append("       		WHERE                                                                     ");
		sql.append("       eacl.STUDENT_MV_ID = s.ID                                                          ");
		sql.append("       AND eacl.CHANGE_USER_ID = u.USER_ID                                                ");
		sql.append("       AND o.parentID = brench.id                                                         ");
		sql.append("       AND TYPE = 'RECHARGE'                                                              ");

		if (StringUtils.isNotBlank(vo.getBranchId())){
			sql.append(" and brench.id = :branchId ");
			params.put("branchId", vo.getBranchId());
		}

		if (StringUtils.isNotBlank(vo.getBlcampusId())){
			sql.append(" and o.id = :blcampusId");
			params.put("blcampusId", vo.getBlcampusId());
		}

		if (StringUtils.isNotBlank(timeVo.getStartDate())){
			sql.append("   and CHANGE_TIME >= :startDate ");
			params.put("startDate",timeVo.getStartDate()+" 00:00:00" );
		}
		if (StringUtils.isNotBlank(timeVo.getEndDate())){
			sql.append(" and CHANGE_TIME  <= :endDate ");
			params.put("endDate",timeVo.getEndDate()+" 23:59:59" );
		}
		if (StringUtils.isNotBlank(vo.getStudentName())){
			sql.append(" and s.name like :studentName ");
			params.put("studentName","%"+vo.getStudentName()+"%" );
		}
		sql.append("       ORDER BY                                                                           ");
		sql.append("       eacl.CHANGE_TIME DESC                                                             ");
		dataPackage.setRowCount(findCountSql("select count(*) " + sql.substring(sql.indexOf("from") > 0 ? sql.indexOf("from") : sql.indexOf("FROM")),params));
		return super.findMapPageBySQL(sql.toString(), dataPackage, false,params);
//		return super.findPageBySql(sql.toString(), dataPackage, true);

	}


}
