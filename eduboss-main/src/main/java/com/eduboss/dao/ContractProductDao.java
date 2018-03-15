package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.eduboss.common.ProductType;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.Course;
import com.eduboss.domain.OtmClassStudentAttendent;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;

public interface ContractProductDao extends GenericDAO<ContractProduct, String>{

	List<ContractProduct> findContractProductByProductAndStudent(String studentId,String productId);

	/**
	 * 获取学生的一对一 的 统一产品列表
	 * @param student
	 * @return
	 */
	List<ContractProduct> getOneContractProducts(Student student);
	
	/**
	 * 根据产品ID查询拟报该产品的合同数
	 * @param productId
	 * @return
	 */
	public int countByProductId(String productId)
	;
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void deleteById(String id);
	
	public void save(ContractProduct contractProduct);

	List<ContractProduct> getEscProductByCampus(String campus,String miniClassId);
	

	List<Map<Object, Object>> getCanUseEcsContractProduct(String campus,String miniClassId);
	
	/**
	 * 根据一对多类型和学生ID查询合同列表
	 * @param otmType
	 * @param studentId
	 * @return
	 */
	public List<ContractProduct> getOtmContractProductByOtmTypeAndStu(Integer otmType, String studentId);
	
	/**
	 * 根据一对多学生考勤记录查询剩余资金
	 * @param otmClassStudentAttendentVo
	 * @return
	 */
	public BigDecimal countOtmStudentRemainAmount(OtmClassStudentAttendent otmClassStudentAttendent);
	
	/**
	 * 分页查询一对一合同产品
	 * @param dataPackage
	 * @param contractVo
	 * @param timeVo
	 * @return
	 */
	public DataPackage getMyOooContractProductList(DataPackage dataPackage, ContractVo contractVo, TimeVo timeVo, String isAllDistributed);
	
	 /**
     * 按照课程拿有效的对应课程科目的合同产品(新加逻辑)
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     * @param course
     * @return
     */
    public List<ContractProduct> getOrderValidSubjectOneOnOneContractProducts(Course course);
    
    /**
     * 计算按照课程有效的对应课程科目的合同产品(新加逻辑)
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     * @param course
     * @return
     */
    public BigDecimal countOrderValidSubjectOneOnOneContractProducts(Course course);
    
    /**
	 * 获取当前用户的课时管理操作权限
	 * @param userId
	 * @return
	 */
    @Deprecated
    List<Map<Object, Object>> getCourseTimeManageAuthTags(String userId);

	List<Map<Object, Object>> getCourseTimeManageAuthTagsNew(String userId);
	
    /**
     * 通过合同ID查找合同产品列表
     * @param contractId
     * @return
     */
    public List<ContractProduct> getContractProductByContractId(String contractId);
    
    /**
	 * 通过合同studentId, 产品类型，最低单价查找合同产品列表
	 * @param studentId
	 * @param prodctType
	 * @param lowestPrice
	 * @return
	 */
    public List<ContractProduct> getContractProductByStudent(String studentId, ProductType prodctType, BigDecimal lowestPrice);
    
    /**
     * 根据关联系统的订单编号查找合同产品列表
     * @param relatedNo
     * @return
     */
    List<ContractProduct> listContractProductByAssocRelatedNo(String relatedNo);
    
}
