package com.eduboss.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.eduboss.common.Constants;
import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.dao.OperationCountDao;
import com.eduboss.dao.ProcedureDao;
import com.eduboss.domain.OdsMonthIncomeCampus;
import com.eduboss.domain.OdsMonthRemainAmountCampus;
import com.eduboss.domain.PosPayData;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.OdsMonthIncomeCampusService;
import com.eduboss.service.OdsMonthRemainAmountCampusService;
import com.eduboss.service.OperationCountService;
import com.eduboss.service.PosMachineService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.FtpApche;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;


@Service
public class OperationCountServiceImpl implements OperationCountService {
	
	private final static Logger log = Logger.getLogger(OperationCountServiceImpl.class);
	
	@Autowired
	private OdsMonthIncomeCampusService odsMonthIncomeCampusService;
	
	@Autowired
	private OdsMonthRemainAmountCampusService odsMonthRemainAmountCampusService;
	
	@Autowired
	private PosMachineService posMachineService;
	
	@Autowired
	private OperationCountDao operationCountDao;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	public void updateOdsDayCustypeTotal(String targetDayStart,
			String targetDayEnd) throws SQLException {
		
		log.info("########## proc_ods_day_CUSTYPE_TOTAL ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_ods_day_CUSTYPE_TOTAL(?,?)}";
		List listParam = new ArrayList<Object>();
		List<String> dates =  DateTools.getDates(targetDayStart, targetDayEnd);
		for (String date : dates) {
			listParam.clear();
			listParam.add(date);
			listParam.add(date);
			procedureDao.executeProc(sql, listParam);
		}
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## proc_ods_day_CUSTYPE_TOTAL ########## " + "end" );
	}
	
	@Override
	public void updateCashTotal(String targetDayStart,
			String targetDayEnd) throws SQLException {
		
		log.info("########## proc_ods_day_FINANCE_MOBILE ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		
		String sql = "{call proc_ods_day_FINANCE_MOBILE(?,?)}";
		
		List listParam = new ArrayList<Object>();
		List<String> dates =  DateTools.getDates(targetDayStart, targetDayEnd);
		for (String date : dates) {
			listParam.clear();
			listParam.add(date);
			listParam.add(date);
			procedureDao.executeProc(sql, listParam);
		}
		
		log.info("########## proc_ods_day_FINANCE_MOBILE ########## " + "end" );
	}
	
	@Override
	public void updateAllTotal(String targetDayStart,
			String targetDayEnd) throws SQLException {
		
		log.info("########## proc_ods_day_ALL_TOTAL_MOBILE ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_ods_day_ALL_TOTAL_MOBILE(?,?)}";
		List listParam = new ArrayList<Object>();
		List<String> dates =  DateTools.getDates(targetDayStart, targetDayEnd);
		for (String date : dates) {
			listParam.clear();
			listParam.add(date);
			listParam.add(date);
			procedureDao.executeProc(sql, listParam);
		}
		
		log.info("########## proc_ods_day_ALL_TOTAL_MOBILE ########## " + "end" );
	}
	
	@Override
	public void updateMobileOranization() throws SQLException {
		log.info("########## proc_update_mobile_org ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_mobile_org()}";
		procedureDao.executeProc(sql);
		log.info("########## proc_update_mobile_org ########## " + "end" );
	}
	/**
	 * 更新用户组织架构报表数据 (跟进人事部门)
	 */
	@Override
	public void updateUserOrg() throws SQLException {
		log.info("########## updateUserOrg ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_user_dept()}";
		procedureDao.executeProc(sql);
		
		log.info("########## updateUserOrg ########## " + "end" );
	}
	
	/**
	 * 更新系统监控报表数据
	 */
	@Override
	public void updateSystemMonitor() throws SQLException {
		log.info("########## updateSystemMonitor ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_COUNT_USER_OPERATION_AllSubject()}";
		procedureDao.executeProc(sql);
		
		log.info("########## updateSystemMonitor ########## " + "end" );
	}
	
	/**
	 * 更新现金流报表数据
	 */
	@Override
	public void updateOdsDayFinance(String targetDay) throws SQLException {
		log.info("########## updateOdsDayFinance ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
//		String sql = "{call proc_update_ODS_DAY_FINANCE(?)}";
		String sql = "{call proc_update_ODS_DAY_FINANCE_NEW(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsDayFinance ########## " + "end" );
	}

    /**
     * 更新现金流报表数据
     */
    @Override
    public void updateOdsDayFinanceContract(String targetDay) throws SQLException {
        log.info("########## updateOdsDayFinanceContract ########## " + "begin" );

        ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
        String sql = "{call proc_update_ODS_DAY_FINANCE_CONTRACT(?)}";
        List listParam = new ArrayList<Object>();
        listParam.add(targetDay);
        procedureDao.executeProc(sql, listParam);

        log.info("########## updateOdsDayFinanceContract ########## " + "end" );
    }
	
	/**
	 * 更新课消报表数据
	 */
	@Override
	public void updateOdsDayCourseConsume(String targetDay) throws SQLException {
		log.info("########## updateOdsDayCourseConsume ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_COURSE_CONSUME(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsDayCourseConsume ########## " + "end" );
	}

    /**
     * 更新退费统计数据
     * @param targetDay
     * @throws SQLException
     */
    @Override
    public void updateOdsDayRefundAnalyze(String targetDay) throws SQLException {
        log.info("########## updateRefundAnalyze ########## " + "begin" );

        ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
        String sql = "{call proc_update_ODS_REFUND_STUDENT(?)}";
        List listParam = new ArrayList<Object>();
        listParam.add(targetDay);
        procedureDao.executeProc(sql, listParam);

        log.info("########## updateRefundAnalyze ########## " + "end" );
    }

    /**
     * 更新收入统计数据
     * @param targetDay
     * @throws SQLException
     */
    @Override
    public void updateOdsDayIncomingAnalyze(String targetDay) throws SQLException {
        log.info("########## IncomingAnalyze ########## " + "begin" );

        ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
        String sql = "{call proc_update_ODS_DAY_INCOMING_CAMPUS(?)}";
        List listParam = new ArrayList<Object>();
        listParam.add(targetDay);
        procedureDao.executeProc(sql, listParam);

        log.info("########## IncomingAnalyze ########## " + "end" );
    }
	
	/**
	 * 更新一对一剩余资金报表数据
	 */
	@Override
	public void updateOdsRealStudentRemaining(String targetDay) throws SQLException {
		log.info("########## updateOdsRealStudentRemaining ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
//		String sql = "{call proc_update_ODS_REAL_STUDENT_REMAINING(?)}";
		String sql = "{call proc_update_ODS_REAL_STUDENT_REMAINING_NEW(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsRealStudentRemaining ########## " + "end" );
	}
	
	/**
	 * 更新一对一学生人数报表数据
	 */
	@Override
	public void updateOdsRealStudentCount(String targetDay) throws SQLException {
		log.info("########## updateOdsRealStudentCount ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_REAL_STUDENT_COUNT(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsRealStudentCount ########## " + "end" );
	}
	
	/**
	 * 更新一对一学生状态人数报表数据
	 */
	@Override
	public void updateOdsRealStudentOOOCount(String targetDay) throws SQLException {
		log.info("########## updateOdsRealStudentOOOCount ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		
		String sql = "{call proc_update_ODS_REAL_STUDENT_OOO_COUNT(?,?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		if(DateTools.getLastDayofMonth().equals(targetDay) || DateTools.getFistDayofMonth().equals(targetDay) ){//这个月的第一天和最后一天就要统计到学生 
			listParam.add(1);
		}else{
			listParam.add(0);
		}
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsRealStudentOOOCount ########## " + "end" );
	}
	
	
	@Override
	public void updateOdsRealStudentOTMCount(String targetDay) throws SQLException {
		log.info("########## updateOdsRealStudentOTMCount ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		
		String sql = "{call proc_update_ODS_REAL_STUDENT_OTM_COUNT(?,?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		if(DateTools.getLastDayofMonth().equals(targetDay) || DateTools.getFistDayofMonth().equals(targetDay) ){//这个月的第一天和最后一天就要统计到学生 
			listParam.add(1);
		}else{
			listParam.add(0);
		}
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsRealStudentOTMCount ########## " + "end" );
	}
	
	/**
	 * 更新老师课消报表数据
	 */
	@Override
	public void updateOdsDayCourseConsumeTeacherSubject(String targetDay) throws SQLException {
		log.info("########## updateOdsDayCourseConsumeTeacherSubject ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_COURSE_CONSUME_TEACHER(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsDayCourseConsumeTeacherSubject ########## " + "end" );
	}


	/**
	 * 更新老师课消(小时)报表数据
	 *
	 * @param targetDay
	 * @throws SQLException
	 */
	@Override
	public void updateOdsDayCourseConsumeTeacherXiaoShi(String targetDay) throws SQLException {
		log.info("########## updateOdsDayCourseConsumeTeacherSubject ########## " + "begin" );

		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_COURSE_CONSUME_TEACHER_xiaoshi(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);

		log.info("########## updateOdsDayCourseConsumeTeacherSubject ########## " + "end" );
	}

	/**
     * 更新老师课消报表数据（学生维度）
     */
    @Override
    public void updateOdsDayCourseConsumeStudentSubject(String targetDay) throws SQLException {
        log.info("########## updateOdsDayCourseConsumeStudentSubject ########## " + "begin" );

        ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
        String sql = "{call proc_update_ODS_DAY_COURSE_CONSUME_STUDENT_SUBJECT(?)}";
        List listParam = new ArrayList<Object>();
        listParam.add(targetDay);
        procedureDao.executeProc(sql, listParam);

        log.info("########## updateOdsDayCourseConsumeStudentSubject ########## " + "end" );
    }
	
	/**
	 * 更新小班剩余资金报表数据
	 */
	@Override
	public void updateOdsDayMiniClassSurplusMoney(String targetDay) throws SQLException {
		log.info("########## updateOdsDayMiniClassSurplusMoney ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_MINI_CLASS_SURPLUS_MONEY(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsDayMiniClassSurplusMoney ########## " + "end" );
	}

	/**
	 * 更新 - 学生剩余资金 - 报表数据
	 */
	@Override
	public void updateStudentSurplusFunding(String targetDay) throws SQLException {
		log.info("########## updateStudentSurplusFunding ########## " + "begin" );

		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_REPORT_STUDENT_SURPLUS_FUNDING(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);

		log.info("########## updateStudentSurplusFunding ########## " + "end" );
	}
	
	/**
	 * 更新小班人数报表数据
	 */
	@Override
	public void updateOdsDayMiniClassStudentCount(String targetDay) throws SQLException {
		log.info("########## updateOdsDayMiniClassStudentCount ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_MINI_CLASS_STUDENT_COUNT(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsDayMiniClassStudentCount ########## " + "end" );
	}
	
	/**
	 * 更新小班课消报表数据
	 */
	@Override
	public void updateOdsDayMiniClassConsume(String targetDay) throws SQLException {
		log.info("########## updateOdsDayMiniClassConsume ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_MINI_CLASS_CONSUME(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsDayMiniClassConsume ########## " + "end" );
	}
	
	/**
	 * 更新老师未考勤课时报表数据
	 */
	@Override
	public void updateTeacherNotAttendanceCourseHour() throws SQLException {
		log.info("########## updateTeacherNotAttendanceCourseHour ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_COUNT_USER_OPERATION_TeacherNotAttHour()}";
		procedureDao.executeProc(sql);
		
		log.info("########## updateTeacherNotAttendanceCourseHour ########## " + "end" );
	}
	
	/**
	 * 更新学管未绑定考勤编号的学生数量报表数据
	 */
	@Override
	public void updateUnboundAttendanceNumber() throws SQLException {
		log.info("########## updateUnboundAttendanceNumber ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_COUNT_UNBOUND_ATTENDANCE_NUMBER()}";
		procedureDao.executeProc(sql);
		
		log.info("########## updateUnboundAttendanceNumber ########## " + "end" );
	}
	
	/**
	 * 待收金额
	 * @throws SQLException
	 */
	@Override
	public void updateStudentPendingMoney(String targetDay) throws SQLException {

		log.info("########## updateOdsDayMiniClassConsume ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_OSD_REAL_STUDENT_PENDING_MONEY(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsDayMiniClassConsume ########## " + "end" );
	}

    /**
     * 小班产品人数统计
     * @throws SQLException
     */
    public void updateMiniClassPeople(String targetDay) throws SQLException{

        log.info("########## updateMiniClassPeople ########## " + "begin" );

        ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
        String sql = "{call proc_update_ODS_DAY_SMALL_CLASS_PEOPLE(?)}";
        List listParam = new ArrayList<Object>();
        listParam.add(targetDay);
        procedureDao.executeProc(sql, listParam);

        log.info("########## updateMiniClassPeople ########## " + "end" );
    }
	
	/**
	 * 购买课时
	 * @throws SQLException
	 */
	@Override
	public void updatePayClassHour(String targetDay) throws SQLException {


		log.info("########## updateOdsDayMiniClassConsume ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_OSD_REAL_PAY_CLASS_HOUR(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
	}
	
	/**
	 * 购买课时(每天)
	 * @throws SQLException
	 */
	@Override
	public void updatePayClassHourEveryDay(String targetDay) throws SQLException {


		log.info("########## updateOdsDayMiniClassConsume ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_OSD_REAL_PAY_CLASS_HOUR_EVERY_DAY(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
	}
	
	/**
	 * 购买课时(校区维度)
	 * @throws SQLException
	 */
	@Override
	public void updatePayClassHourCampus(String targetDay) throws SQLException {


		log.info("########## updateOdsDayMiniClassConsume ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_OSD_REAL_PAY_CLASS_HOUR_CAMPUS(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
	}
	
	/**
	 * 测试有返回值的存储过程
	 */
	public void testProcHasOut () {
		log.info("########## testProcHasOut ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_getContractCntById(?,?)}";
		List listParam = new ArrayList<Object>();
		listParam.add("CON0000000024");
		List listOutParamType = new ArrayList<Integer>();
		listOutParamType.add(java.sql.Types.VARCHAR);
		List outData = procedureDao.executeProcHaveOutParam(sql, listParam, listOutParamType);
		for (Object obj:outData) {
			System.out.println(obj.toString());
		}
		
		log.info("########## testProcHasOut ########## " + "end" );
	}
	
	/**
     * 更新收入统计数据
     * @param targetDay
     * @throws SQLException
     */
    @Override
    public void updateMiniClassAttendsAnalyze(String targetDay) throws SQLException {
        log.info("########## IncomingAnalyze ########## " + "begin" );

        ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
        String sql = "{call proc_update_ODS_DAY_MINI_CLASS_ATTENDS(?)}";
        List listParam = new ArrayList<Object>();
        listParam.add(targetDay);
        procedureDao.executeProc(sql, listParam);

        log.info("########## IncomingAnalyze ########## " + "end" );
    }
	
	
	protected Session getSession() {
		Session session = hibernateTemplate.getSessionFactory()
				.openSession();
		return session;
	}
	
	protected String getYesterday() {
		return DateTools.addDateToString( DateTools.getCurrentDate(), -1);
	}

	@Override
	public void updateMoneyArrangeRecord(String targetDay) throws SQLException {
		log.info("########## proc_update_ODS_MONEY_ARRANGE_RECORD ########## " + "begin" );

        ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
        String sql = "{call proc_update_ODS_MONEY_ARRANGE_RECORD(?)}";
        List listParam = new ArrayList<Object>();
        listParam.add(targetDay);
        procedureDao.executeProc(sql, listParam);

        log.info("########## proc_update_ODS_MONEY_ARRANGE_RECORD ########## " + "end" );
		
	}

	/**
	 * 更新学生的课程状态（在读、停课、结课）
	 * @throws SQLException
	 */
	@Override
	public void updateStudentOneOnOneClassStatus() throws SQLException {
		log.info("########## proc_update_STUDENT_ONEONONE_CLASS_STATUS ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_STUDENT_ONEONONE_CLASS_STATUS()}";
		procedureDao.executeProc(sql);
		
		log.info("########## proc_update_STUDENT_ONEONONE_CLASS_STATUS ########## " + "end" );
		
	}
	
	public void updateOdsDayContractBonusOrganization(String targetDay) throws SQLException {
		log.info("########## updateOdsDayContractBonusOrganization ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_OSD_CONTRACT_BONUS_ORGANIZATION(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsDayContractBonusOrganization ########## " + "end" );
	}
	
	public void updateOdsDayContractBonusStaff(String targetDay) throws SQLException {
		log.info("########## updateOdsDayContractBonusStaff ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_OSD_CONTRACT_BONUS_STAFF(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsDayContractBonusStaff ########## " + "end" );
	}

	
	/**
	 * 更新学生状态
	 * @throws SQLException
	 */
	@Override
	public void updateStudentStatus() throws SQLException{
			log.info("########## proc_update_student_status ########## " + "begin" );
			
			ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
			String sql = "{call proc_update_student_status()}";
			procedureDao.executeProc(sql);
			
			log.info("########## proc_update_student_status ########## " + "end" );
			
	}
	
	/**
	 * 更新一对一学生状态
	 * @throws SQLException
	 */
	@Override
	public void updateStudentOOOStatus() throws SQLException {
			log.info("########## proc_update_student_ooo_status ########## " + "begin" );
			
			ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
			String sql = "{call proc_update_student_ooo_status()}";
			procedureDao.executeProc(sql);
			
			log.info("########## proc_update_student_ooo_status ########## " + "end" );
			
	}
	
	/**
	 * 更新一对多学生状态
	 * @throws SQLException
	 */
	@Override
	public void updateStudentOTMStatus() throws SQLException {
			log.info("########## proc_update_student_otm_status ########## " + "begin" );
			
			ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
			String sql = "{call proc_update_student_otm_status()}";
			procedureDao.executeProc(sql);
			
			log.info("########## proc_update_student_otm_status ########## " + "end" );
			
	}
	
	/**
	 * 更新实际小班人数报表数据
	 */
	@Override
	public void updateOdsDayMiniClassStudentRealCount(String targetDay) throws SQLException {
		log.info("########## proc_update_ODS_DAY_MINI_CLASS_STUDENT_REAL_COUNT ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_MINI_CLASS_STUDENT_REAL_COUNT(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## proc_update_ODS_DAY_MINI_CLASS_STUDENT_REAL_COUNT ########## " + "end" );
	}

	
	@Override
	public void updateStudentAccMvRemainAmount() throws SQLException {
		log.info("########## proc_update_student_acc_for_each ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_student_acc_for_each()}";
		procedureDao.executeProc(sql);
		
		log.info("########## proc_update_student_acc_for_each ########## " + "end" );
		
	}
	
	/**
	 * 更新营收统计表
	 * @param targetStartDay
	 * @param targetEndDay
	 * @throws SQLException
	 */
	@Override
    public void updateOdsDayIncomingQuantityAnalyze(String targetStartDay, String targetEndDay) throws SQLException {
		log.info("########## proc_update_ODS_DAY_INCOMING_QUANITY ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_INCOMING_QUANITY(?, ?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetStartDay);
		listParam.add(targetEndDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## proc_update_ODS_DAY_INCOMING_QUANITY ########## " + "end" );
	}
	
	/**
	 * 更新产品销售分析报表数据
	 * @param targetStartDay
	 * @param targetEndDay
	 * @throws SQLException
	 */
	@Override
	public void updateOdsDayProductMarket(String targetStartDay, String targetEndDay) throws SQLException{
        log.info("########## proc_update_ODS_DAY_PRODUCT_MARKET ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_PRODUCT_MARKET(?, ?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetStartDay);
		listParam.add(targetEndDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## proc_update_ODS_DAY_PRODUCT_MARKET ########## " + "end" );
		
	}
	
	/**
	 * 更新产品消耗分析报表数据
	 * @param targetStartDay
	 * @param targetEndDay
	 * @throws SQLException
	 */
	@Override
	public void updateOdsDayProductConsume(String targetStartDay, String targetEndDay) throws SQLException {
        log.info("########## proc_update_ODS_DAY_PRODUCT_CONSUME ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_update_ODS_DAY_PRODUCT_CONSUME(?, ?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetStartDay);
		listParam.add(targetEndDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## proc_update_ODS_DAY_PRODUCT_CONSUME ########## " + "end" );
		
	}
	
	/**
	 * 更新一对多剩余资金报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	@Override
	public void updateOdsOtmRealStudentRemaining(String targetDay) throws SQLException {
		log.info("########## updateOdsOtmRealStudentRemaining ########## " + "begin" );
		
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
//		String sql = "{call proc_update_ODS_REAL_STUDENT_REMAINING(?)}";
		String sql = "{call proc_update_ODS_REAL_OTM_STUDENT_REMAINING(?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## updateOdsOtmRealStudentRemaining ########## " + "end" );
	}
	
	
	@Override
	public void autoCompleteEcsClass(int year, int month,String yearMonth,String lastDay)
			throws SQLException {
		log.info("########## proc_autoTotal_ECS_CLASS ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_autoTotal_ECS_CLASS(?,?,?,?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(year);
		listParam.add(month);
		listParam.add(yearMonth);
		listParam.add(lastDay);
		procedureDao.executeProc(sql, listParam);
		
		log.info("########## proc_autoTotal_ECS_CLASS ########## " + "end" );
		
	}
	
	
	@Override
	public void autoArrangeArea(String get2NextMonthStart,String name) throws SQLException {
		log.info("########## autoArrangeArea ########## " + "begin" );
			ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
			String sql = "{call autoArrangeArea(?,?)}";
			List listParam = new ArrayList<Object>();
			listParam.add(get2NextMonthStart);
			listParam.add(name);
			procedureDao.executeProc(sql, listParam);
		log.info("########## get2NextMonthStart ########## " + "end" );
	}
	
	public void updateOdsDayMonthlyBalance(String targetDay, String mappingDate, String lastMonthDay) throws SQLException {
		log.info("########## updateOdsDayMonthlyBalance ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_ods_day_monthly_balance(?,?,?)}";
		List listParam = new ArrayList<Object>();
		listParam.add(targetDay);
		listParam.add(mappingDate);
		listParam.add(lastMonthDay);
		procedureDao.executeProc(sql, listParam);
		log.info("########## updateOdsDayMonthlyBalance ########## " + "end" );
	}
	
	public void updateOdsDayMonthlyBalance(String targetDay) throws SQLException {
		log.info("########## updateOdsDayMonthlyBalance hand movement ########## " + "begin" );
		if (DateTools.daysBetween(targetDay, "2016-05-31") > 0) {
			String currentDate = DateTools.getCurrentDate();
			Date date = DateTools.addDay(new Date(),-1);
			String mappingDate = DateTools.getDateToString(date);
			ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
			String sql = "{call proc_ods_day_monthly_balance(?,?,?)}";
			boolean isNoLog = false;
			try {
				String lastMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(currentDate));
				if (DateTools.daysBetween(targetDay, lastMonthEndDate) >= 0) {
					while (DateTools.daysBetween(targetDay, lastMonthEndDate) >= 0) {
						List listParam = new ArrayList<Object>();
						listParam.add(targetDay);
						listParam.add(mappingDate);
						String lastMonthDay = DateTools.getDateToString(DateTools.getLastMonthEnd(targetDay));
						listParam.add(lastMonthDay);
						if (isNoLog) {
							procedureDao.executeProcNoLog(sql, listParam);
						} else {
							procedureDao.executeProc(sql, listParam);
							isNoLog = true;
						}
						targetDay = DateTools.getDateToString(DateTools.getLastDayOfMonth(DateTools.addDateToString(targetDay, 1)));
					}
				} else {
					lastMonthEndDate = DateTools.getDateToString(DateTools.getLastDayOfMonth(currentDate));
					List listParam = new ArrayList<Object>();
					listParam.add(targetDay);
					listParam.add(mappingDate);
					String lastMonthDay = DateTools.getDateToString(DateTools.getLastMonthEnd(targetDay));
					listParam.add(lastMonthDay);
					procedureDao.executeProc(sql, listParam);
				}
			} catch (Exception e) {
				
			}
			log.info("########## updateOdsDayMonthlyBalance hand movement ########## " + "end" );
		}
	}
	
	/**
	 * 刷新财务现金流凭证
	 */
	public void updateOdsFinanceMonthlyEvidence(String targetDay, String mappingDate) throws SQLException {
		log.info("########## updateOdsFinanceMonthlyEvidence hand movement ########## " + "begin" );
		String currentDate = DateTools.getCurrentDate();
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_ods_month_finance_evidence(?,?,?,?)}";
		boolean isNoLog = false;
		try {
			String lastMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(currentDate));
			if (DateTools.daysBetween(targetDay, lastMonthEndDate) >= 0) {
				boolean isUsedMappingDate = false;
				while (DateTools.daysBetween(targetDay, lastMonthEndDate) >= 0) {
					String startDate = DateTools.getDateToString(DateTools.getFirstDayOfMonth(targetDay));
					List listParam = new ArrayList<Object>();
					listParam.add(startDate);
					listParam.add(targetDay);
					if (!isUsedMappingDate) {
						listParam.add(mappingDate);
						isUsedMappingDate = true;
					} else {
						Map<String, Object> params = new HashMap<String, Object>();
						String mappingDateSql = "select * from ods_month_finance_evidence where COUNT_DATE = :targetDay limit 1";
						params.put("targetDay", targetDay);
						List<Map<Object, Object>> mappingList = operationCountDao.findMapBySql(mappingDateSql, params);
						if (mappingList != null && mappingList.size() > 0 && StringUtil.isNotBlank((String)mappingList.get(0).get("MAPPING_DATE"))) {
							listParam.add((String)mappingList.get(0).get("MAPPING_DATE"));
						} else {
							listParam.add(DateTools.addDateToString(targetDay, 1));
						}
					}
					String lastMonthDay = DateTools.getDateToString(DateTools.getLastMonthEnd(targetDay));
					Map<String, Object> params = new HashMap<String, Object>();
					String lastMonthMappingSql = "select * from ods_month_finance_evidence where COUNT_DATE = '" + lastMonthDay + "' limit 1";
					params.put("lastMonthDay", lastMonthDay);
					List<Map<Object, Object>> list = operationCountDao.findMapBySql(lastMonthMappingSql, params);
					if (list != null && list.size() > 0 && StringUtil.isNotBlank((String)list.get(0).get("MAPPING_DATE"))) {
						listParam.add((String)list.get(0).get("MAPPING_DATE"));
					} else {
						listParam.add(lastMonthDay);
					}
					if (isNoLog) {
						procedureDao.executeProcNoLog(sql, listParam);
					} else {
						procedureDao.executeProc(sql, listParam);
						isNoLog = true;
					}
					targetDay = DateTools.getDateToString(DateTools.getLastDayOfMonth(DateTools.addDateToString(targetDay, 1)));
				}
			} else {
				List listParam = new ArrayList<Object>();
				String startDate = DateTools.getDateToString(DateTools.getFirstDayOfMonth(targetDay));
				listParam.add(startDate);
				listParam.add(targetDay);
				listParam.add(mappingDate);
				String lastMonthDay = DateTools.getDateToString(DateTools.getLastMonthEnd(targetDay));
				String lastMonthMappingSql = "select * from ods_month_finance_evidence where COUNT_DATE = :lastMonthDay limit 1";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("lastMonthDay", lastMonthDay);
				List<Map<Object, Object>> list = operationCountDao.findMapBySql(lastMonthMappingSql, params);
				if (list != null && list.size() > 0 && StringUtil.isNotBlank((String)list.get(0).get("MAPPING_DATE"))) {
					listParam.add((String)list.get(0).get("MAPPING_DATE"));
				} else {
					listParam.add(lastMonthDay);
				}
				procedureDao.executeProc(sql, listParam);
			}
		} catch (Exception e) {
			
		}
		log.info("########## updateOdsDayMonthlyBalance hand movement ########## " + "end" );
	}
	
	/**
	 * 刷新财务扣费凭证
	 */
	public void updateOdsIncomeMonthlyEvidence(String targetDay, String mappingDate) throws SQLException {
		log.info("########## updateOdsIncomeMonthlyEvidence hand movement ########## " + "begin" );
		String currentDate = DateTools.getCurrentDate();
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_ods_month_income_evidence(?,?,?,?)}";
		boolean isNoLog = false;
		try {
			String lastMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(currentDate));
			if (DateTools.daysBetween(targetDay, lastMonthEndDate) >= 0) {
				boolean isUsedMappingDate = false;
				while (DateTools.daysBetween(targetDay, lastMonthEndDate) >= 0) {
					String startDate = DateTools.getDateToString(DateTools.getFirstDayOfMonth(targetDay));
					List listParam = new ArrayList<Object>();
					listParam.add(startDate);
					listParam.add(targetDay);
					if (!isUsedMappingDate) {
						listParam.add(mappingDate);
						isUsedMappingDate = true;
					} else {
						Map<String, Object> params = new HashMap<String, Object>();
						String mappingDateSql = "select * from ods_month_income_evidence where COUNT_DATE = :targetDay limit 1";
						params.put("targetDay", targetDay);
						List<Map<Object, Object>> mappingList = operationCountDao.findMapBySql(mappingDateSql, params);
						if (mappingList != null && mappingList.size() > 0 && StringUtil.isNotBlank((String)mappingList.get(0).get("MAPPING_DATE"))) {
							listParam.add((String)mappingList.get(0).get("MAPPING_DATE"));
						} else {
							listParam.add(DateTools.addDateToString(targetDay, 1));
						}
					}
					String lastMonthDay = DateTools.getDateToString(DateTools.getLastMonthEnd(targetDay));
					String lastMonthMappingSql = "select * from ods_month_income_evidence where COUNT_DATE = :lastMonthDay limit 1";
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("lastMonthDay", lastMonthDay);
					List<Map<Object, Object>> list = operationCountDao.findMapBySql(lastMonthMappingSql, params);
					if (list != null && list.size() > 0 && StringUtil.isNotBlank((String)list.get(0).get("MAPPING_DATE"))) {
						listParam.add((String)list.get(0).get("MAPPING_DATE"));
					} else {
						listParam.add(lastMonthDay);
					}
					if (isNoLog) {
						procedureDao.executeProcNoLog(sql, listParam);
					} else {
						procedureDao.executeProc(sql, listParam);
						isNoLog = true;
					}
					targetDay = DateTools.getDateToString(DateTools.getLastDayOfMonth(DateTools.addDateToString(targetDay, 1)));
				}
			} else {
				List listParam = new ArrayList<Object>();
				String startDate = DateTools.getDateToString(DateTools.getFirstDayOfMonth(targetDay));
				listParam.add(startDate);
				listParam.add(targetDay);
				listParam.add(mappingDate);
				String lastMonthDay = DateTools.getDateToString(DateTools.getLastMonthEnd(targetDay));
				String lastMonthMappingSql = "select * from ods_month_income_evidence where COUNT_DATE = '" + lastMonthDay + "' limit 1";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("lastMonthDay", lastMonthDay);
				List<Map<Object, Object>> list = operationCountDao.findMapBySql(lastMonthMappingSql, params);
				if (list != null && list.size() > 0 && StringUtil.isNotBlank((String)list.get(0).get("MAPPING_DATE"))) {
					listParam.add((String)list.get(0).get("MAPPING_DATE"));
				} else {
					listParam.add(lastMonthDay);
				}
				procedureDao.executeProc(sql, listParam);
			}
		} catch (Exception e) {
			
		}
		log.info("########## updateOdsIncomeMonthlyEvidence hand movement ########## " + "end" );
	}
	
	/**
	 * 刷新营收凭证
	 */
	public void updateOdsIncomeMonthlyStudentCampus(String targetDay, String mappingDate) throws SQLException {
		log.info("########## updateOdsIncomeMonthlyStudentCampus hand movement ########## " + "begin" );
		String currentDate = DateTools.getCurrentDate();
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		//营收
		String incomeStudentSql = "{call proc_ods_month_income_student(?,?,?)}";
		String incomeCampusSql = "{call proc_ods_month_income_campus(?)}";
		//剩余资金
//		String remainStudentSql = "{call proc_ods_month_remain_amount_student(?,?,?,?)}";
//		String remainCampusSql = "{call proc_ods_month_remain_amount_campus(?,?)}";
		//现金流
		String paymentMain = "{call proc_update_ods_month_payment_receipt_main(?,?)}"; 
		String paymentDetail = "{call proc_ods_month_payment_receipt(?,?)}";
		String paymentMainStudent = "{call proc_update_ods_month_payment_receipt_main_student(?,?)}";
		
		//校区业绩凭证
		String campusAchievement = "{call proc_update_ods_month_campus_achievement_main(?,?)}";
		String studentAchievement = "{call proc_ods_month_campus_achievement_main_student(?,?)}";
		boolean isNoLog = false;
		try {
			String lastMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(currentDate));
			List incomeStudentListParam = new ArrayList<Object>();
//			List remainStudentListParam = new ArrayList<Object>();
			List campusListParam = new ArrayList<Object>();
			List paymentParam = new ArrayList<Object>();
			String startDate = DateTools.getDateToString(DateTools.getFirstDayOfMonth(targetDay));
			incomeStudentListParam.add(startDate);
			incomeStudentListParam.add(targetDay);
			incomeStudentListParam.add(mappingDate + " 23:59:59");
//			remainStudentListParam.add(startDate);
//			remainStudentListParam.add(targetDay);
//			remainStudentListParam.add(mappingDate + " 23:59:59");
//			remainStudentListParam.add(null);
			campusListParam.add(targetDay);
			log.info("########## 营收凭证跑批开始 ##########");
			procedureDao.executeProc(incomeStudentSql, incomeStudentListParam);
			procedureDao.executeProc(incomeCampusSql, campusListParam);
			log.info("########## 营收凭证跑批结束 ##########");
			log.info("########## 剩余资金凭证跑批开始 ##########");
//			procedureDao.executeProc(remainStudentSql, remainStudentListParam);
//			procedureDao.executeProc(remainCampusSql, campusListParam);
			log.info("########## 剩余资金凭证跑批结束##########");
			paymentParam.add(targetDay.substring(0,7));
			paymentParam.add("");
			log.info("########## 现金流凭证跑批开始 ##########");
			procedureDao.executeProc(paymentMain, paymentParam);
			procedureDao.executeProc(paymentDetail, paymentParam);
			procedureDao.executeProc(paymentMainStudent, paymentParam);
			log.info("########## 现金流凭证跑批结束 ##########");
			
			List achievementParam = new ArrayList<Object>();
			achievementParam.add(targetDay.substring(0,7));
			achievementParam.add("");
			log.info("########## 校区业绩凭证跑批开始 ##########");
			procedureDao.executeProc(campusAchievement, achievementParam);
			procedureDao.executeProc(studentAchievement, achievementParam);
			log.info("########## 校区业绩凭证跑批结束 ##########");
		} catch (Exception e) {
		    e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("########## updateOdsIncomeMonthlyStudentCampus hand movement ########## " + "end" );
	}
	
	/**
	 * 刷新营收凭证 校区
	 */
	@Override
	public void updateOdsIncomeMonthlyStudentCampusByCampus(String targetDay, String mappingDate, String campusId) throws SQLException {
		log.info("########## updateOdsIncomeMonthlyStudentCampusByCampus hand movement ########## " + "begin" );
		String currentDate = DateTools.getCurrentDate();
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String studentSql = "{call proc_ods_month_income_student_by_campus(?,?,?,?)}";
		String campusSql = "{call proc_ods_month_income_campus_by_campus(?,?)}";
		boolean isNoLog = false;
		try {
			String lastMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(currentDate));
			String lastTargeMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(targetDay));
			String lastEvidenceId = campusId + "_" + lastTargeMonthEndDate;
			String currentEvidenceId = campusId + "_" + targetDay;
			OdsMonthIncomeCampus evidence = odsMonthIncomeCampusService.findOdsMonthIncomeCampusById(currentEvidenceId);
			/*if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.FINANCE_END_AUDITED) {
				 throw new ApplicationException("该校区的营收凭证已终审，不可以再次刷新");
			}*/
			if(!odsMonthIncomeCampusService.checkCanAuditOrFlush(lastEvidenceId)){
				throw new ApplicationException("该校区的营收凭证尚有历史月份未终审，请终审后才对新月份凭证进行刷新与审核");
			}
			List studentListParam = new ArrayList<Object>();
			List campusListParam = new ArrayList<Object>();
			String startDate = DateTools.getDateToString(DateTools.getFirstDayOfMonth(targetDay));
			studentListParam.add(startDate);
			studentListParam.add(targetDay);
			campusListParam.add(targetDay);
			if (mappingDate.equals(DateTools.getCurrentDate())) {
				mappingDate = DateTools.getCurrentDateTime();
			} else {
				mappingDate = mappingDate + " 23:59:59";
			}
			studentListParam.add(mappingDate);
			studentListParam.add(campusId);
			campusListParam.add(campusId);
			procedureDao.executeProc(studentSql, studentListParam);
			procedureDao.executeProc(campusSql, campusListParam);
			
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("########## updateOdsIncomeMonthlyStudentCampusByCampus hand movement ########## " + "end" );
	}
	
	/**
	 * 刷新剩余资金凭证
	 */
	public void updateOdsRemainMonthlyStudentCampus(String targetDay, String mappingDate) throws SQLException {
		log.info("########## updateOdsRemainMonthlyStudentCampus hand movement ########## " + "begin" );
		String currentDate = DateTools.getCurrentDate();
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String studentSql = "{call proc_ods_month_remain_amount_student(?,?,?,?)}";
		String campusSql = "{call proc_ods_month_remain_amount_campus(?, ?)}";
		boolean isNoLog = false;
		try {
			String lastMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(currentDate));
			List studentListParam = new ArrayList<Object>();
			List campusListParam = new ArrayList<Object>();
			String startDate = DateTools.getDateToString(DateTools.getFirstDayOfMonth(targetDay));
			studentListParam.add(startDate);
			studentListParam.add(targetDay);
			campusListParam.add(targetDay);
			if (mappingDate.equals(DateTools.getCurrentDate())) {
                mappingDate = DateTools.getCurrentDateTime();
            } else {
                mappingDate = mappingDate + " 23:59:59";
            }
			studentListParam.add(mappingDate);
			studentListParam.add(null);
			campusListParam.add(null);
			procedureDao.executeProc(studentSql, studentListParam);
			procedureDao.executeProc(campusSql, campusListParam);
		} catch (Exception e) {
			
		}
		log.info("########## updateOdsRemainMonthlyStudentCampus hand movement ########## " + "end" );
	}

	@Override
	public void updateOdsPaymentReciept(String targetDay, String campusId)
			throws SQLException {
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String studentSql = "{call proc_update_ods_month_payment_receipt_main(?,?)}";
		String campusSql = "{call proc_ods_month_payment_receipt(?,?)}";
		String campusStudentSql = "{call proc_update_ods_month_payment_receipt_main_student(?,?)}"; 
		List param = new ArrayList<Object>();
		param.add(targetDay);
		param.add(campusId);
		procedureDao.executeProc(studentSql, param);
		procedureDao.executeProc(campusSql, param);
		procedureDao.executeProc(campusStudentSql, param);
	}
	
	@Override
	public void updateOdsPaymentReciept(String targetDay)
			throws SQLException {
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String studentSql = "{call proc_update_ods_month_payment_receipt_main_flush(?)}";
		String campusSql = "{call proc_ods_month_payment_receipt_flush(?)}";
		String campusStudentSql = "{call proc_update_ods_month_payment_receipt_main_student_flush(?)}"; 
		List param = new ArrayList<Object>();
		param.add(targetDay);
		procedureDao.executeProc(studentSql, param);
		procedureDao.executeProc(campusSql, param);
		procedureDao.executeProc(campusStudentSql, param);
	}
	
	/**
	 * 执行系统审批收款记录
	 */
	@Override
	public void auditFundsChangeHistory() throws SQLException {
		log.info("########## auditFundsChangeHistory hand movement ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String excuteSql = "{call proc_audit_funds_change_history()}";
		procedureDao.executeProc(excuteSql);
		log.info("########## auditFundsChangeHistory hand movement ########## " + "end" );
	}
	
	/**
	 * 更新导入收款数据的校区
	 */
	@Override
	public void updatePosPayDataBlCampusId() {
		log.info("########## updatePosPayDataBlCampusId hand movement ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String excuteSql = "{call proc_update_pos_pay_data_blcampus()}";
		try {
			procedureDao.executeProc(excuteSql);
		} catch (SQLException e) {
			log.error("存储过程updatePosPayDataBlCampusId出错：" + e.getMessage());
			e.printStackTrace();
		}
		log.info("########## updatePosPayDataBlCampusId hand movement ########## " + "end" );
	}
	
	@Override
	public void flushStudentSubjectStatus(String currentDate) {
		log.info("########## flushStudentSubjectStatus ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String excuteSql = "{call proc_ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS(?)}";
		try {
			List param = new ArrayList<Object>();
			param.add(currentDate);
			procedureDao.executeProc(excuteSql,param);
		} catch (SQLException e) {
			log.error("存储过程flushStudentSubjectStatus出错：" + e.getMessage());
			e.printStackTrace();
		}
		log.info("########## flushStudentSubjectStatus ########## " + "end" );
	}
	
	/**
	 * 更新下个月校区的科组
	 */
	@Override
	public void updateToNextVersion(String blBrenchId, String blCampusId, int verison, int nextVersion) {
		log.info("########## updateToNextVersion hand movement ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String excuteSql = "{call proc_update_to_next_subject_group(?,?,?,?)}";
		try {
			List param = new ArrayList<Object>();
			param.add(blBrenchId);
			param.add(blCampusId);
			param.add(verison);
			param.add(nextVersion);
			procedureDao.executeProc(excuteSql,param);
		} catch (SQLException e) {
			log.error("存储过程updaeToNextVersion出错：" + e.getMessage());
			e.printStackTrace();
		}
		log.info("########## updateToNextVersion hand movement ########## " + "end" );
	}
	
	/**
	 *  初始化新建的校区科组
	 */
	@Override
	public void createCampusSubejctGroup(String blBrenchId, String blCampusId, int verison, int nextVersion) {
		log.info("########## createCampusSubejctGroup hand movement ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String excuteSql = "{call proc_create_campus_subject_group(?,?,?,?)}";
		try {
			List param = new ArrayList<Object>();
			param.add(blBrenchId);
			param.add(blCampusId);
			param.add(verison);
			param.add(nextVersion);
			procedureDao.executeProc(excuteSql,param);
		} catch (SQLException e) {
			log.error("存储过程proc_create_campus_subject_group出错：" + e.getMessage());
			e.printStackTrace();
		}
		log.info("########## createCampusSubejctGroup hand movement ########## " + "end" );
	}
	
	/**
	 * 更新老师版本
	 */
	@Override
	public void updateTeacherVersion() {
		log.info("########## updateTeacherVersion ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String excuteSql = "{call proc_updat_is_month_version(?,?)}";
		String currentDate = DateTools.getCurrentDate();
		int versionMonth = Integer.parseInt(currentDate.substring(0, 4) + currentDate.substring(5,7));
		try {
			List param = new ArrayList<Object>();
			param.add(currentDate);
			param.add(versionMonth);
			procedureDao.executeProc(excuteSql,param);
		} catch (SQLException e) {
			log.error("存储过程updateTeacherVersion出错：" + e.getMessage());
			e.printStackTrace();
		}
		log.info("########## updateTeacherVersion ########## " + "end" );
	}

	/**
	 * 每月初更新科组
	 */
	@Override
	public void updateSubjectGroup(int preVersion, int currentVersion,
			int nextVersion) {
		log.info("########## updateSubjectGroup ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String nextSql = "{call proc_update_all_to_nex_subject_group(?,?)}";
		String preSql = "{call proc_update_pre_subject_group(?)}";
		try {
			List nextParam = new ArrayList<Object>();
			nextParam.add(currentVersion);
			nextParam.add(nextVersion);
			procedureDao.executeProc(nextSql,nextParam);
			
			List preParam = new ArrayList<Object>();
			preParam.add(preVersion);
			procedureDao.executeProc(preSql,preParam);
		} catch (SQLException e) {
			log.error("存储过程updateSubjectGroup出错：" + e.getMessage());
			e.printStackTrace();
		}
		log.info("########## updateSubjectGroup ########## " + "end" );
	}
	
	/**
	 * 下载并导入银联支付数据检查30天没下载的重复下载
	 */
	@Override
	public void downLoadAndImportPosDataReview(String targetDate) {
	    int days = PropertiesUtils.getIntValue("ftp.review.days");
	    int redisSeconds = PropertiesUtils.getIntValue("ftp.review.redis.seconds");
	    if (!JedisUtil.exists((Constants.FTP_SHOUFU_CURRENT_DATE_ + targetDate).getBytes())) { //当天没下载过，下载当天的ftp
	        if (this.downLoadAndImportPosData(targetDate)) { // 当天的ftp下载成功,redis标注当天下载成功，删掉30内重复下载的redis标识
	            JedisUtil.set((Constants.FTP_SHOUFU_CURRENT_DATE_ + targetDate).getBytes(), 
	                    (Constants.FTP_SHOUFU_CURRENT_DATE_ + targetDate).getBytes(), redisSeconds);
	            JedisUtil.del((Constants.FTP_SHOUFU_ + targetDate).getBytes());
	        } else { // 当天的ftp下载不成功，增加30内重复下载的redis标识
	            JedisUtil.set((Constants.FTP_SHOUFU_ + targetDate).getBytes(), 
	                    (Constants.FTP_SHOUFU_ + targetDate).getBytes(), redisSeconds);
	        }
	    }
	    for (int i=1; i<=days; i++) { // 循环过去30天，没下载成功的重新下载
	        try {
	            targetDate = DateTools.getDateToString(DateTools.getPre(targetDate)); // 获取上一天
	            if (JedisUtil.exists(Constants.FTP_SHOUFU_ + targetDate)) { // 如果redis存在没下载的标识则重新下载
	                if (this.downLoadAndImportPosData(targetDate)) { // ftp下载成功，删掉30内重复下载的redis标识
	                    JedisUtil.del((Constants.FTP_SHOUFU_ + targetDate).getBytes());
	                } else {
	                    if (i < days) { // ftp下载失败，增加30内重复下载的redis标识
	                        JedisUtil.set((Constants.FTP_SHOUFU_ + targetDate).getBytes(), 
	                                (Constants.FTP_SHOUFU_ + targetDate).getBytes(), redisSeconds);
	                    }
	                }
	            }
            } catch (Exception e) {
                e.printStackTrace();
            }
	    }
	}
	
	/**
	 * 下载并导入银联支付数据
	 */
	@Override
	public boolean downLoadAndImportPosData(String targetDate) {
	    boolean result = false;
	    String fileName = "SFS_034_" + targetDate.replaceAll("-", "") + ".txt";
        log.info("开始导入：" + fileName);
        String rootPath=getClass().getResource("/").getFile().toString();
        String localPath=rootPath.substring(0,rootPath.lastIndexOf("WEB-INF"))+"uploadfile/";
        if (FtpApche.downFile(PropertiesUtils.getStringValue("ftp.yinlian.url"), PropertiesUtils.getStringValue("ftp.yinlian.username"), 
                PropertiesUtils.getStringValue("ftp.yinlian.password"), fileName, localPath)) {
            String downloadFile = localPath + fileName;
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(downloadFile), FtpApche.getCharset(downloadFile));
                BufferedReader br = new BufferedReader(isr);
                String str = null;  
                String[] tmps= null;
                
                String regebDate8 = "^\\d{8}$";
                String regebDate14 = "^\\d{14}$";
                String regebDate = "(19|20)[0-9][0-9]-[0-9][0-9]-[0-9][0-9]";
                String posId = "";
                String posTime = "";
                String amount = "";
                String poundage = "";
                PosPayData dto = null;
                boolean canAdd = true;
                
                Map<Integer, PosPayData> loadIntoDBMap = new HashMap<Integer, PosPayData>();
//              List<PosPayData> loadIntoDB = new ArrayList<>();
                int lineNum = 0;
                while((str = br.readLine()) != null) {
                    lineNum++;
                    dto = new PosPayData();
                    if (str.contains("999999999999999|")) { // 最后一行
                        break;
                    }
                    tmps = str.split("\\|");
                    canAdd = true;
                    for (int i = 0; i < tmps.length ; i++) {
                        switch (i) {
                        case 1:
                            dto.setPosNumber(tmps[i].trim());
                            break;
                        case 3:
                            posTime = tmps[i].trim();
                            if (posTime.matches(regebDate8) || posTime.matches(regebDate14)) {
                                posTime = posTime.substring(0, 8);
                            } else if (posTime.matches(regebDate)) {
                                posTime = posTime.replaceAll("-", "");
                            } else {
                                canAdd = false;
                                log.info("导入" + fileName + "第" + lineNum + "行，交易时间格式不正确");
                                break;
                            }
                            dto.setPosTime(posTime);
                            break;
                        case 7:
                            dto.setPosAccount(tmps[i].trim());
                            break;
                        case 12:
                            amount = tmps[i].trim().replaceAll(",", "");
                            try {
                                dto.setAmount(new BigDecimal(amount));
                            } catch(Exception e) {
                                canAdd = false;
                                log.info("导入" + fileName + "第" + lineNum + "行，金额不符合格式");
                            }
                            break;
                        case 15:
                            poundage = tmps[i].trim().replaceAll(",", "");
                            try {
                                dto.setPoundage(new BigDecimal(poundage));
                            } catch(Exception e) {
                                canAdd = false;
                                log.info("导入" + fileName + "第" + lineNum + "行，手续费不符合格式");
                            }
                            break;
                        case 19:
                            posId = tmps[i].trim();
                            posId = posId.replaceFirst("^0*", "");
                            dto.setPosId(posId);
                            break;
                        case 20:
                            dto.setMerchantName(tmps[i].trim());
                            break;
                        default:
                            break;
                        }
                    }
                    if (canAdd) {
                        loadIntoDBMap.put(lineNum, dto);
                    }
                }
                posMachineService.importPosPayDataFromList(loadIntoDBMap, fileName);
                br.close();
                result = true;
            } catch(FileNotFoundException e) {
                log.info(e.getMessage());
                e.printStackTrace();
            } catch(IOException e) {
                log.info(e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
	}
	
	/**
     * 下载并导入快钱支付数据检查30天没下载的重复下载
     */
    @Override
    public void downLoadAndImportPosDataKuaiQianReview(String targetDate) {
        int days = PropertiesUtils.getIntValue("ftp.review.days");
        int redisSeconds = PropertiesUtils.getIntValue("ftp.review.redis.seconds");
        if (!JedisUtil.exists((Constants.FTP_KUAIQIAN_CURRENT_DATE_ + targetDate).getBytes())) { //当天没下载过，下载当天的ftp
            if (this.downLoadAndImportPosDataKuaiQian(targetDate)) { // 当天的ftp下载成功,redis标注当天下载成功，删掉30内重复下载的redis标识
                JedisUtil.set((Constants.FTP_KUAIQIAN_CURRENT_DATE_ + targetDate).getBytes(), 
                        (Constants.FTP_KUAIQIAN_CURRENT_DATE_ + targetDate).getBytes(), redisSeconds);
                JedisUtil.del((Constants.FTP_KUAIQIAN_ + targetDate).getBytes());
            } else { // 当天的ftp下载不成功，增加30内重复下载的redis标识
                JedisUtil.set((Constants.FTP_KUAIQIAN_ + targetDate).getBytes(), 
                        (Constants.FTP_KUAIQIAN_ + targetDate).getBytes(), redisSeconds);
            }
        }
        for (int i=1; i<=days; i++) { // 循环过去30天，没下载成功的重新下载
            try {
                targetDate = DateTools.getDateToString(DateTools.getPre(targetDate)); // 获取上一天
                if (JedisUtil.exists(Constants.FTP_KUAIQIAN_ + targetDate)) { // 如果redis存在没下载的标识则重新下载
                    if (this.downLoadAndImportPosDataKuaiQian(targetDate)) { // ftp下载成功，删掉30内重复下载的redis标识
                        JedisUtil.del((Constants.FTP_KUAIQIAN_ + targetDate).getBytes());
                    } else {
                        if (i < days) { // ftp下载失败，增加30内重复下载的redis标识
                            JedisUtil.set((Constants.FTP_KUAIQIAN_ + targetDate).getBytes(), 
                                    (Constants.FTP_KUAIQIAN_ + targetDate).getBytes(), redisSeconds);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
    /**
     * 下载并导入快钱支付数据
     */
	@Override
	public boolean downLoadAndImportPosDataKuaiQian(String targetDate) {
	    boolean result = false;
		String fileName = "spark_" + targetDate + ".txt";
		log.info("开始导入：" + fileName);
		String rootPath=getClass().getResource("/").getFile().toString();
		String localPath=rootPath.substring(0,rootPath.lastIndexOf("WEB-INF"))+"uploadfile/";
		if (FtpApche.downFile(PropertiesUtils.getStringValue("ftp.kuaiqian.url"), PropertiesUtils.getStringValue("ftp.kuaiqian.username"), 
				PropertiesUtils.getStringValue("ftp.kuaiqian.password"), fileName, localPath)) {
		    String downloadFile = localPath + fileName;
		    try {
		        InputStreamReader isr = new InputStreamReader(new FileInputStream(downloadFile), FtpApche.getCharset(downloadFile));
		        BufferedReader br = new BufferedReader(isr);
		        String str = null;  
		        String[] tmps= null;
		        
		        String regebDate8 = "^\\d{8}$";
		        String regebDate14 = "^\\d{14}$";
		        String regebDate = "(19|20)[0-9][0-9]-[0-9][0-9]-[0-9][0-9]";
		        String posId = "";
		        String posTime = "";
		        String amount = "";
		        String poundage = "";
		        PosPayData dto = null;
		        boolean canAdd = true;
		        
		        Map<Integer, PosPayData> loadIntoDBMap = new HashMap<Integer, PosPayData>();
//				List<PosPayData> loadIntoDB = new ArrayList<>();
		        int lineNum = 0;
		        while((str = br.readLine()) != null) {
		            if (StringUtil.isBlank(str) || !str.substring(0,2).equals(" |")) {
		                continue;
		            } else {
		                lineNum++;
		            }
		            dto = new PosPayData();
		            tmps = str.split("\\|");
		            canAdd = true;
		            for (int i = 0; i < tmps.length ; i++) {
		                switch (i) {
		                case 91:
		                    dto.setPosNumber(tmps[i].trim());
		                    break;
		                case 19:
		                    posTime = tmps[i].trim();
		                    if (posTime.matches(regebDate8) || posTime.matches(regebDate14)) {
		                        posTime = posTime.substring(0, 8);
		                    } else if (posTime.matches(regebDate)) {
		                        posTime = posTime.replaceAll("-", "");
		                    } else if (posTime.matches("^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30))) (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$")) {
		                        posTime = posTime.replaceAll("-", "");
		                        posTime = posTime.substring(0, 8);
		                    } else {
		                        canAdd = false;
		                        log.info("导入" + fileName + "第" + lineNum + "行，交易时间格式不正确");
		                        break;
		                    }
		                    dto.setPosTime(posTime);
		                    break;
		                case 16:
		                    dto.setPosAccount(tmps[i].trim());
		                    break;
		                case 31:
		                    amount = tmps[i].trim().replaceAll(",", "");
		                    try {
		                        dto.setAmount(new BigDecimal(amount));
		                    } catch(Exception e) {
		                        canAdd = false;
		                        log.info("导入" + fileName + "第" + lineNum + "行，金额不符合格式");
		                    }
		                    break;
		                case 57:
		                    poundage = tmps[i].trim().replaceAll(",", "");
		                    try {
		                        dto.setPoundage(new BigDecimal(poundage));
		                    } catch(Exception e) {
//								canAdd = false;
		                        log.info("导入" + fileName + "第" + lineNum + "行，手续费不符合格式");
		                    }
		                    break;
		                case 9:
		                    posId = tmps[i].trim();
		                    posId = posId.replaceFirst("^0*", "");
		                    dto.setPosId(posId);
		                    break;
		                case 48:
		                    dto.setMerchantName(new String(tmps[i].getBytes("GBK")).trim());
		                    break;
		                default:
		                    break;
		                }
		            }
		            if (canAdd) {
		                loadIntoDBMap.put(lineNum, dto);
		            }
		        }
		        posMachineService.importPosPayDataFromList(loadIntoDBMap, fileName);
		        br.close();
		        result = true;
		    } catch(FileNotFoundException e) {
		        e.printStackTrace();
		    } catch(IOException e) {
		        e.printStackTrace();
		    }
		}
		return result;
	}

	/**
	 * 更新成绩模板版本
	 */
    @Override
    public void updateAchievementTemplateVesion() {
        log.info("########## updateAchievementTemplateVesion ########## " + "begin" );
        ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
        String excuteSql = "{call proc_updat_current_ach_template(?)}";
        String currentDate = DateTools.getCurrentDate();
        try {
            List param = new ArrayList<Object>();
            param.add(currentDate);
            procedureDao.executeProc(excuteSql,param);
        } catch (SQLException e) {
            log.error("存储过程updateAchievementTemplateVesion出错：" + e.getMessage());
            e.printStackTrace();
        }
        log.info("########## updateAchievementTemplateVesion ########## " + "end" );
    }
	
    
    @Override
	public void updateOdsCampusAchievement(String targetDay, String campusId)
			throws SQLException {
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String campusSql = "{call proc_update_ods_month_campus_achievement_main(?,?)}";
		String campusStudentSql = "{call proc_ods_month_campus_achievement_main_student(?,?)}"; 
		List param = new ArrayList<Object>();
		param.add(targetDay);
		param.add(campusId);
		procedureDao.executeProc(campusSql, param);
		procedureDao.executeProc(campusStudentSql, param);
	}
	
	@Override
	public void updateOdsCampusAchievementFlush(String targetDay)
			throws SQLException {
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String campusSql = "{call proc_update_ods_month_campus_achievement_main_flush(?)}";
		String campusStudentSql = "{call proc_ods_month_campus_achievement_main_student_flush(?)}"; 
		List param = new ArrayList<Object>();
		param.add(targetDay);
		procedureDao.executeProc(campusSql, param);
		procedureDao.executeProc(campusStudentSql, param);
	}
}
