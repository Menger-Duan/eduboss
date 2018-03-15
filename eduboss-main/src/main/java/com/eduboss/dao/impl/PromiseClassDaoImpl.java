package com.eduboss.dao.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.PromiseClassDao;
import com.eduboss.domain.PromiseClass;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;


@Repository("PromiseClassDao")
public class PromiseClassDaoImpl extends GenericDaoImpl<PromiseClass, String> implements PromiseClassDao{
	@Autowired
	private UserService userService;
	/**
	 * 目标班列表
	 * */
	public DataPackage getPromiseClassList(PromiseClass pc,DataPackage dataPackage){
		return null;
	}

	/**
	 * 统计学生的成功率
	 * */
	@Override
	public double countStudentSuccessRate(String promiseClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql1 = new StringBuilder();
	    StringBuilder hql2 = new StringBuilder();
		//统计成功的学生数量
		hql1.append("select count(id) from PromiseStudent where resultStatus='1' and  promiseClass.id= :promiseClassId ");
		params.put("promiseClassId", promiseClassId);
		int amount = this.findCountHql(hql1.toString(), params);
		//查询班级的学生数
		hql2.append("select count(id) from PromiseStudent where promiseClass.id = :promiseClassId ");
		int rate = this.findCountHql(hql2.toString(), params);
		if(amount!=0){
			double successRate =amount*1.0/rate*100;
			return successRate;
		}else{
			return 0;
		}
	}

	/**
	 * 对目标班进行结课处理
	 * */
	@Override
	public void endPromiseClass(String promiseClassId,String successRate) {
		Map<String, Object> params = new HashMap<String, Object>();
		String[] userInfo = this.getCurUserAndDate();
		StringBuilder hql = new StringBuilder();
		hql.append("update PromiseClass set success_rate = :successRate, pStatus = '0',endDate ='").append(userInfo[1]).append("', modifyUserId='").append(userInfo[0]).append("'")
		.append(" , modifyDate='").append(userInfo[1]).append("' where ID = :promiseClassId ");
		params.put("successRate", Double.parseDouble(successRate));
		params.put("promiseClassId", promiseClassId);
		this.excuteHql(hql.toString(), params);
		
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

}
