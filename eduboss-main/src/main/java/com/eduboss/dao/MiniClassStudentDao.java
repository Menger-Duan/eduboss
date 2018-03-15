package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.domain.Contract;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassStudent;
import com.eduboss.domainVo.MiniClassStudentVo;
import com.eduboss.dto.DataPackage;

public interface MiniClassStudentDao extends GenericDAO<MiniClassStudent, Integer> {
	
	/**
	 * 小班学生考勤列表
	 * @param miniClassCourse
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassStudentList(MiniClassStudentVo miniClassStudentVo,
			DataPackage dp);

	/**
	 * 获取一条小班学生记录
	 * @param miniClassId
	 * @param studentId
	 * @return
	 */
	public MiniClassStudent getOneMiniClassStudent(String miniClassId, String studentId);
	

	public MiniClassStudent getMiniClassStudentByStuIdAndCpId( String studentId,String contractProductId);
	
	
	/**
	 * 获取小班学生记录
	 * @param miniClassId
	 * @return
	 */
	public List<MiniClassStudent> getMiniClassStudent(String miniClassId);
	
	/**
	 * 根据获取学生报名记录
	 * @param contract
	 */
	public List<MiniClassStudent> listMiniClassStudentByContract(Contract contract);

	/**
	 * 小班扣费总人数
	 * @param miniClassId
	 * @return
	 */
	public int getChargedPeopleQuantity(String miniClassId);
	
	/**
	 * 小班扣费总金额
	 * @param miniClassId
	 * @return
	 */
	public BigDecimal getChargedMoneyQuantity(String miniClassId);

	/**
	 * 小班详情-在读学生信息列表
	 * @param miniClassStudentVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassDetailStudentList(
			String miniClassId, DataPackage dp);

	/**
	 * 小班剩余总课时
	 * @param miniClassId
	 * @return
	 */
	public BigDecimal getMiniClassTotalRemainCourseHour(String miniClassId);
	
	/**
	 * 根据和合同产品ID 来查找对应的小班
	 * @param contractProductId
	 * @return
	 */
	public MiniClass findMiniClassByContractProductId(String contractProductId);

	/**
	 * 小班学生考勤列表（未扣费）
	 * @param miniClassStudentVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassStudentListUncharge(
			MiniClassStudentVo miniClassStudentVo, DataPackage dp);
	
	public DataPackage findPageByHQLForAcc(String hql,DataPackage dp,String countHql);
	
	/**
	 * 计算小班人数
	 * @param miniClassId
	 * @return
	 */
	public int countMiniClassPeopleNum(String miniClassId);
	
	/**
	 * 根据学生id 数组获取对应关联的客户的联系方式
	 */
	public String[] getCustomerContactByStuIds(String[] studentIds,String miniClassId);

}
