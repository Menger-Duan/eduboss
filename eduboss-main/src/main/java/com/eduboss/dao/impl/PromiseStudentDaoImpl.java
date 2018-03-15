package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.utils.DateTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.ContractProductDao;
import com.eduboss.dao.PromiseClassRecordDao;
import com.eduboss.dao.PromiseStudentDao;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.PromiseStudent;
import com.eduboss.domainVo.PromiseClassApplyVo;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;


@Repository("PromiseStudentDao")
public class PromiseStudentDaoImpl extends GenericDaoImpl<PromiseStudent, String> implements PromiseStudentDao {
	
	@Autowired
	private ContractProductDao contractProductDao;
	@Autowired
	private UserService userService;
	@Autowired
	private PromiseClassRecordDao promiseClassRecordDao;
	

	
	/**
	 * 班主任学生管理界面点详情按钮，查出该学生的合同信息
	 * @param studentId 学生ID
	 * */
	@Override
	public List<PromiseClassApplyVo> findStudentContractInfo(String studentId, String contractProductId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from ContractProduct where id = :contractProductId and contract.student.id = :studentId ");
		params.put("contractProductId", contractProductId);
		params.put("studentId", studentId);
		List<ContractProduct> list = contractProductDao.findAllByHQL(hql.toString(), params);
		List<PromiseClassApplyVo> voList = new ArrayList<PromiseClassApplyVo>();
		for( ContractProduct con : list){
			//System.out.println(con.getRemainingAmount()+"@@@@@@@@@@@@@@@@@@@@@@@@");
			PromiseClassApplyVo vo = HibernateUtils.voObjectMapping(con, PromiseClassApplyVo.class);
			vo.setRemainningAmount(con.getRemainingAmount());
			if(vo!=null){
				BigDecimal chargeHours = promiseClassRecordDao.countStudentPromiseChargeHoursByStudentId(vo.getStudentId());
				vo.setMonthHours(chargeHours);
			}
			voList.add(vo);
		}
		return voList;
	}
	
	/**
	 * 根据班级ID和学生ID查询合同产品ID
	 * */
	@Override
	public PromiseStudent getPromiseClassContractProId(String promiseClassId,String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PromiseStudent where student.id= :studentId and promiseClass.id = :promiseClassId");
		params.put("studentId", studentId);
		params.put("promiseClassId", promiseClassId);
		List<PromiseStudent> list = this.findAllByHQL(hql.toString(), params);
		PromiseStudent promise = new PromiseStudent();
		if(!list.isEmpty()){
			promise = list.get(0);
		}
		return promise;
	}
	
	/**
	 * 目标班报名，如果之前报过此班则update，否则SAVA
	 * */
	@Override
	public void updateOrSavePromiseStudent(PromiseStudent student){
		Map<String, Object> params = new HashMap<String, Object>();
		String[] userInfo = this.getCurUserAndDate();
		StringBuilder countHql = new StringBuilder();
    	countHql.append(" from PromiseStudent where student.id = :studentId and promiseClass.id = :promiseClassId and contractProduct.id = :contractProductId");
    	params.put("studentId", student.getStudent().getId());
    	params.put("promiseClassId", student.getPromiseClass().getId());
		params.put("contractProductId", student.getContractProduct().getId());
    	List<PromiseStudent> list = this.findAllByHQL(countHql.toString(), params);
    	if(!list.isEmpty() && list.size()>0){//学生已经在该班上，之前退过班
			PromiseStudent stu=list.get(0);
			student.setId(stu.getId());
    		StringBuilder updateHql = new StringBuilder();
    		Map<String, Object> updateParams = new HashMap<String, Object>();
    		updateHql.append(" update PromiseStudent set abortClass = null, modifyUserId='")
    		.append(userInfo[0])
    		.append("', modifyTime='").append(userInfo[1])
    		.append("', contractProduct.id = :contractProductId where student.id = :studentId and promiseClass.id = :promiseClassId");
    		updateParams.put("contractProductId", student.getContractProduct().getId());
    		updateParams.put("studentId", student.getStudent().getId());
    		updateParams.put("promiseClassId", student.getPromiseClass().getId());
    		this.excuteHql(updateHql.toString(), updateParams);
    	}else{
    		student.setCreateUserId(userInfo[0]);
    		student.setCreateTime(DateTools.getCurrentDateTime());
    		this.save(student);
    	}
	}
	
	
	/**
	 * 获取当前登录用户ID和当前系统时间
	 * */
	public String[] getCurUserAndDate(){
		//获取当前登录用户ID
		String userId = userService.getCurrentLoginUser().getUserId();
		//获取当前系统时间设置创建时间
		Long cur = System.currentTimeMillis();
		Date date = new Date(cur);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String curDate = sdf.format(date);
		return new String[]{userId,curDate};
	}

	@Override
	public List<PromiseClassApplyVo> getPromiseStudentInfo(PromiseStudent promiseStudent) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PromiseStudent where promiseClass.id = :promiseClassId  and abortClass is null ");
		params.put("promiseClassId", promiseStudent.getPromiseClass().getId());
		if(promiseStudent.getCourseStatus()!=null){
			hql.append(" and courseStatus = :courseStatus ");
			params.put("courseStatus", promiseStudent.getCourseStatus());
		}
		List<PromiseStudent> list = this.findAllByHQL(hql.toString(), params);
		List<PromiseClassApplyVo> voList = new ArrayList<PromiseClassApplyVo>();
		for(PromiseStudent student : list){
			PromiseClassApplyVo vo = HibernateUtils.voObjectMapping(student, PromiseClassApplyVo.class);
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public List<PromiseStudent> getPromiseStudentByContractPro(
			String contractProductId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PromiseStudent where contractProduct.id = :contractProductId and abortClass is null ");
		params.put("contractProductId", contractProductId);
		return  this.findAllByHQL(hql.toString(), params);
	}

}
