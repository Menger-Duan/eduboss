package com.eduboss.dao;

import com.eduboss.common.ProductType;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Product;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author robinzhang
 *
 */
public interface ProductDao extends GenericDAO<Product, String> {

	public List<Product> findProductByCategory(ProductType oneononecourse);

	/**
	 * 根据年级 拿到所有一对一的课程
	 * @param stuGrade
	 * @return
	 */
	public List<Product> getOneOnOneCourcesByGrade(String stuGrade,String state);

	/**
	 * 根据关键字 和 年级 拿到小班的信息
	 * @param keyword
	 * @param gradeId 
	 * @return
	 */
	public List<Product> getSmallClassCourcesByKeyWord(String keyword, String gradeId,String state);

	/**
	 * 获取其他收费项目
	 * @return
	 */
	public List<Product> getOtherProducts();
	
	
	/**
	 * 获取 一对一价格
	 * @param gradeId
	 * @return
	 */
	public BigDecimal getOneOnOnePrice(String gradeId);

	/**
	 * 获取 赠送课时的product
	 * @param gradeId 
	 * @return
	 */
	public Product getFreeHourProduct(String gradeId);

	public String getProductIdByNameAndGrade(String productName, String gradeId,String brenchId);

	/**
	 * 获取一对一统一价格的product
	 * @param gradeId
	 * @return
	 */
	public Product getOneOnOneNormalProduct(String gradeId);
	
	public Product getOneOnOneNormalProduct(String gradeId,Organization org);
	
	/**
	 * 根据年级产品名称查询小班产品
	 * @param gradeId
	 * @param productName
	 * @return
	 */
	public Product getMiniProductByNameAndGradeId(String gradeId,String productName,Organization org);
	
	public Product getProductIdByNameAndType(String productName, ProductType Type,String brenchId);

	public DataPackage findProductByCategoryAndSomeParam(DataPackage dp,
			ProductVo product);
	
	/**
	 * 过季小班清理
	 */
	public Response cleanOutExpiredSmallClass(String productVersion, String productQuarter);
	
	 /**
     * 根据学生获取可消费产品
     * @param studentId
     * @return
     */
	public List<ProductVo> getCanConsumeProductsByStudent(String studentId);

}
