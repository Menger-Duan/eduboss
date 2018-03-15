package com.eduboss.service;

import com.eduboss.common.ProductType;
import com.eduboss.domain.Product;
import com.eduboss.domain.ProductCategory;
import com.eduboss.domain.Promotion;
import com.eduboss.domainVo.ProductChooseVo;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.domainVo.TextBookVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.Response;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService {
	/**
	 * 返回 所有一对一课程
	 * 
	 * @return
	 */
	public List<Product> getOneOnOneCources();

	/**
	 * 返回 所有小班课程
	 * 
	 * @return
	 */
	public List<Product> getSmallClassCources();
	
	public Product getProductById(String id);

	public DataPackage getProductList(Product product, ModelVo modelVo, DataPackage dataPackage);

	/**
	 * 获取产品列表，新
	 * 20180119
	 * @param product
	 * @param modelVo
	 * @param dataPackage
	 * @return
	 */
	public DataPackage getProductListNew(Product product, ModelVo modelVo, DataPackage dataPackage);

	public void deleteProduct(Product product);

	public Response saveOrUpdateProduct(Product product);

	public ProductVo findProductById(String id);

	
	/**
	 * 根据年级 返回他的一对一的课程
	 * @param stuGrade
	 * @return
	 */
	public List<ProductVo> getOneOnOneCourcesByGrade(String stuGrade,String proStatus);

	/**
	 * 根据 关键字 返回不同的小班
	 * @param keyword
	 * @param gradeId 
	 * @return
	 */
	public List<ProductVo> getSmallClassCourcesByKeyWord(String keyword, String gradeId,String proStatus);

	
	/**
	 * 读取不同的其他收费项目
	 * @return
	 */
	public List<ProductVo> getOtherProducts();

	public List<ProductVo> getProductByTypeSelection(ProductType productType);

	/**
	 * 获取 赠送课时的 product
	 * @return
	 */
	public Product getFreeHourProduct(String gradeId);
	
	
	/**
	 * 获取 一对一的统一价格的产品
	 * @param gradeId 
	 * @return
	 */
	public Product getOneOnOneNormalProduct(String gradeId);

	public void updateProStatus(Product product) throws Exception ;
	
	public List<ProductCategory> getProductCategoryTree();
	
	/**
	 * 获取第一级的产品类别
	 */
	public List<ProductCategory>  getProductCategoryFirstFloor(String isEcsProductCategory);

	public void deleteProductCategory(ProductCategory productCategory) throws Exception;

	public ProductCategory saveOrUpdateProductCategory(ProductCategory productCategory);

    /**
     * 根据老师获取可教产品
     * 获取产品中有关联该老师可教科目的产品
     * @param teacherId
     * @return
     */
    public List<ProductVo> getCanTeachProductsByTeacher(String teacherId);
    
    
	/**
	 * 根据类型查找产品列表，以及其他参数    Yao
	 * @param dp
	 * @param product
	 * @return
	 */
	public DataPackage findProductByCategoryAndSomeParam(DataPackage dp,
			ProductVo product);

    /**
     * 根据学生获取可消费产品
     * @param studentId
     * @return
     */
    public List<ProductVo> getCanConsumeProductsByStudent(String studentId);
    
    public DataPackage findPage(DataPackage dp, ProductChooseVo productChooseVo, String productIdArray,String selectProductType, String mainProductId);

    /**
     * 过季小班清理
     */
    public Response cleanOutExpiredSmallClass(String productVersion, String productQuarter, String searchClean);
    
    /**
     * 开放给教育平台接口
     * 根据课程编号查询产品信息
     */
    public Map<String, Object> getCourseProductInfo(String courseId);

	DataPackage findProductForTwoTeacherChoose(DataPackage dp, ProductChooseVo productChooseVo);

	void saveProductByLive(JSONObject proMap);

    void saveLiveProduct(String id, String[] branchId);

    void updateLiveProduct(String productId);

	void transferProductToLivePlatform(String productId);

    List<ProductVo> getOtherProductForEcs(String organizationId, String productId);
    
    public DataPackage getTextBookInfoList(TextBookVo textBookVo, DataPackage dataPackage);

	Promotion saveNewPromotion(BigDecimal promotionAmount);

	/**
	 *
	 * @param liveId
	 * @return
	 */
    String getLiveProductIdByLiveId(String liveId);
    
    /**
     * 根据产品编号获取所有子产品
     * @param productId
     * @return
     */
    List<Product> listAllSubProductsById(String productId);
    
    /**
     * 根据小班编号获取关联的所有其他产品
     * @param productId
     * @return
     */
    List<Product> listAllSubProductsByMiniClassId(String miniClassId);
    
    /**
     * 检查是否关联了在线销售的小班
     * @param productId
     * @return
     */
    boolean checkRelatedOnLineSaleMiniClassByProductId(String productId);
    
}
