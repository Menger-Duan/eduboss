package com.eduboss.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.common.utils.HttpHeaders;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.DataDictCategory;
import com.eduboss.common.ProductType;
import com.eduboss.common.PromotionType;
import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.CourseProductInfoVo;
import com.eduboss.domainVo.ProductChooseVo;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.domainVo.TextBookVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.Response;
import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.jedis.CleanSmallClass;
import com.eduboss.jedis.RedisDataSource;
import com.eduboss.service.*;

import com.eduboss.service.DataDictService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ProductService;
import com.eduboss.service.UserService;
import com.eduboss.utils.*;
import com.google.common.collect.Maps;









import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;







import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.DigestException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

	private final static Logger log=Logger.getLogger(ProductServiceImpl.class);
	
    @Value("${list.url}")
    private String listUrl;

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Autowired
	private PromotionDao promotionDao;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private ContractProductDao contractProductDao;
    
    @Autowired
    private RedisDataSource redisDataSource;

    @Autowired
	private ProductBranchDao productBranchDao;

    @Autowired
	private OrganizationService organizationService;

    @Autowired
    private ProductPackageDao productPackageDao;

	@Autowired
    private DataDictDao dataDictDao;

	@Autowired
	private RoleQLConfigService roleQLConfigService;

	/**
	 * 返回 所有一对一课程
	 * 
	 * @return
	 */
	public List<Product> getOneOnOneCources() {
		return productDao.findProductByCategory(ProductType.ONE_ON_ONE_COURSE);
	}

	/**
	 * 返回 所有小班课程
	 * 
	 * @return
	 */
	public List<Product> getSmallClassCources() {
		return productDao.findProductByCategory(ProductType.SMALL_CLASS);
	}

	@Override
	public Product getProductById(String id) {
		return productDao.findById(id);
	}

	@Override
	public DataPackage getProductList(Product product, ModelVo modelVo, DataPackage dp) {
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(product);
		if(product!=null && product.getGradeDict()!=null && StringUtils.isNotEmpty(product.getGradeDict().getId()))
			criterionList.add(Expression.eq("gradeDict.id", product.getGradeDict().getId()));
        if(product!=null && product.getProductGroup()!=null && StringUtils.isNotEmpty(product.getProductGroup().getId()))
            criterionList.add(Expression.eq("productGroup.id", product.getProductGroup().getId()));
		if(product!=null && product.getOrganization()!=null && StringUtils.isNotEmpty(product.getOrganization().getId()))
			criterionList.add(Expression.or(Expression.eq("organization.id", product.getOrganization().getId()),
					Expression.sqlRestriction("exists (select 1  from product_branch  where product_id={alias}.id and branch_id='"+product.getOrganization().getId()+"')")
					));
		if(product.getCategory()==null||product.getCategory() == ProductType.LIVE) {
			criterionList.add(Restrictions.ne("category",ProductType.LIVE));
		}

		if(product!=null && product.getProductCategoryId()!=null && StringUtils.isNotEmpty(product.getProductCategoryId().getId()))
			criterionList.add(Expression.eq("productCategoryId.id", product.getProductCategoryId().getId()));
		
		if(product!=null && product.getCourseSeries()!=null && StringUtils.isNotEmpty(product.getCourseSeries().getId()))
			criterionList.add(Expression.eq("courseSeries.id", product.getCourseSeries().getId()));
		if(product!=null && product.getProductVersion()!=null && StringUtils.isNotEmpty(product.getProductVersion().getId()))
			criterionList.add(Expression.eq("productVersion.id", product.getProductVersion().getId()));
		if(product!=null && product.getProductQuarter()!=null && StringUtils.isNotEmpty(product.getProductQuarter().getId()))
			criterionList.add(Expression.eq("productQuarter.id", product.getProductQuarter().getId()));
		if(product!=null && product.getSubject()!=null && StringUtils.isNotEmpty(product.getSubject().getId()))
			criterionList.add(Expression.eq("subject.id", product.getSubject().getId()));
		if(product!=null && product.getClassType()!=null && StringUtils.isNotEmpty(product.getClassType().getId()))
			criterionList.add(Expression.eq("classType.id", product.getClassType().getId()));
		
		if(modelVo!=null && StringUtils.isNotEmpty(modelVo.getModelName1()))
			
			criterionList.add(Expression.le("price", new BigDecimal(modelVo.getModelName1())));
		if(modelVo!=null && StringUtils.isNotEmpty(modelVo.getModelName2()))
			criterionList.add(Expression.ge("price", new BigDecimal(modelVo.getModelName2())));

		if (modelVo!=null && StringUtils.isNotEmpty(modelVo.getStartDate())){
			criterionList.add(Expression.ge("validEndDate",modelVo.getStartDate()));
		}

		if (modelVo!=null && StringUtils.isNotEmpty(modelVo.getEndDate())){
			criterionList.add(Expression.le("validEndDate",modelVo.getEndDate()));
		}
		
		//归属组织架构(归属分公司)
		//Organization campus = userService.getBelongBrench();
		//if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
		//	String orgLevel = campus.getOrgLevel();
		//	criterionList.add(Expression.like("organization.orgLevel",orgLevel,MatchMode.START));
		//}
//		criterionList.add(RoleCodeAuthoritySearchUtil.getOrganizationCriterionByOrgObject(userService.getBelongBrench(),"organization"));
        dp.setRoleQLConfigName("课程产品管理"); // 自动附加权限控制配置
		dp = productDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "id", "asc"), criterionList);

		return getReturnProductDp(dp);
	}

	public DataPackage getReturnProductDp(DataPackage dp){
		List<Product> proList=(List<Product>) dp.getDatas();
		List<ProductVo> proVoList=HibernateUtils.voListMapping(proList, ProductVo.class);
		String promotionIds = "";
		String promotionName = "";
		String pName = "";
		String currentDate = DateTools.getCurrentDate();
		for (ProductVo vo : proVoList) {
			promotionName = "";
			promotionIds = vo.getPromotionId();
			if (StringUtil.isNotBlank(promotionIds)) {
				for (String promotionId : promotionIds.split(",")) {
					if (promotionId.equals("-1")) {
						promotionName += "所有优惠" + ",";
					} else {
						Promotion p = promotionDao.findById(promotionId);
						if (p != null) {
							pName = p.getName();
							pName = DateTools.daysBetween(p.getEndTime(), currentDate) > 0 ? pName + "(过期)" : pName; // 判断是否过期了
							pName = !p.getIsActive().equals("Y") ? pName + "(无效)" : pName; // 判断是否无效
							promotionName += pName + ",";
						}
					}
				}
			}
			promotionName = StringUtil.isNotBlank(promotionName) ? promotionName.substring(0, promotionName.length() -1) : "";
			vo.setPromotionName(promotionName);



			if(vo.getCategory()!=null && vo.getCategory().equals(ProductType.LIVE)) {
				List<ProductBranch> branchList = findBranchByProductId(vo.getId());
				StringBuilder branchName =new StringBuilder();
				for (ProductBranch b : branchList) {
					branchName.append(b.getBranch().getName()+" ");
				}
				if(branchList.size()>0)
					vo.setOrganizationName(branchName.toString());
			}
		}
		dp.setDatas(proVoList);

		return dp;
	}

	@Override
	public DataPackage getProductListNew(Product product, ModelVo modelVo, DataPackage dp) {
		StringBuilder sql = new StringBuilder();
		Map param = new HashMap();
		sql.append(" SELECT p.* FROM product p");
		sql.append(" LEFT OUTER JOIN data_dict classType ON p.CLASS_TYPE_ID = classType.ID");
		sql.append(" LEFT OUTER JOIN data_dict series ON p.COURSE_SERIES_ID = series.ID");
		sql.append(" LEFT OUTER JOIN data_dict grade ON p.GRADE_ID = grade.ID");
		sql.append(" LEFT OUTER JOIN organization o ON p.ORGANIZATION_ID = o.id");
		sql.append(" LEFT OUTER JOIN data_dict gp ON p.PRODUCT_GROUP_ID = gp.ID");
		sql.append(" LEFT OUTER JOIN data_dict qt ON p.PRODUCT_QUARTER_ID = qt.ID");
		sql.append(" LEFT OUTER JOIN data_dict ver ON p.PRODUCT_VERSION_ID = ver.ID");
		sql.append(" LEFT OUTER JOIN data_dict sub ON p.SUBJECT_ID = sub.ID");

		sql.append(" where 1= 1 ");

		if(StringUtil.isNotBlank(product.getName())){
			sql.append(" and p.name like :name");
			param.put("name","%"+product.getName()+"%");
		}

		if(product.getClassTimeLength()!=null){
			sql.append(" AND p.class_time_length =:timelength");
			param.put("timelength",product.getClassTimeLength());
		}

		if(product.getPrice()!=null){
			sql.append(" AND p.price =:price");
			param.put("price",product.getPrice());
		}

		if(StringUtil.isNotBlank(product.getProStatus())){
			sql.append(" AND p.pro_status =:status");
			param.put("status",product.getProStatus());
		}

		if(product!=null && product.getGradeDict()!=null && StringUtils.isNotEmpty(product.getGradeDict().getId())){
			sql.append(" AND grade.ID =:gradeId");
			param.put("gradeId",product.getGradeDict().getId());
		}
		if(product!=null && product.getProductGroup()!=null && StringUtils.isNotEmpty(product.getProductGroup().getId())){
			sql.append(" AND gp.ID =:groupId");
			param.put("groupId",product.getProductGroup().getId());
		}
		if(product!=null && product.getOrganization()!=null && StringUtils.isNotEmpty(product.getOrganization().getId())){
			sql.append(" AND (o.id =:orgId or exists (select 1  from product_branch  where product_id=p.id and branch_id=:orgId))");
			param.put("orgId",product.getOrganization().getId());
		}
		if(product.getCategory()!=null) {
			sql.append(" AND p.CATEGORY =:category");
			param.put("category",product.getCategory());
		}
		sql.append(" AND p.CATEGORY <> 'LIVE'");


		if(product!=null && product.getProductCategoryId()!=null && StringUtils.isNotEmpty(product.getProductCategoryId().getId())){
			sql.append(" AND p.CATEGORY =:categoryId");
			param.put("categoryId",product.getProductCategoryId().getId());
		}

		if(product!=null && product.getCourseSeries()!=null && StringUtils.isNotEmpty(product.getCourseSeries().getId())){
			sql.append(" AND series.ID =:seriesId");
			param.put("seriesId",product.getCourseSeries().getId());
		}
		if(product!=null && product.getProductVersion()!=null && StringUtils.isNotEmpty(product.getProductVersion().getId())){
			sql.append(" AND ver.ID =:versionId");
			param.put("versionId",product.getProductVersion().getId());
		}
		if(product!=null && product.getProductQuarter()!=null && StringUtils.isNotEmpty(product.getProductQuarter().getId())){
			sql.append(" AND qt.ID =:qtId");
			param.put("qtId",product.getProductQuarter().getId());
		}
		if(product!=null && product.getSubject()!=null && StringUtils.isNotEmpty(product.getSubject().getId())){
			sql.append(" AND sub.ID =:subjectId");
			param.put("subjectId",product.getSubject().getId());
		}
		if(product!=null && product.getClassType()!=null && StringUtils.isNotEmpty(product.getClassType().getId())){
			sql.append(" AND classType.ID =:classType");
			param.put("classType",product.getClassType().getId());
		}

		if(modelVo!=null && StringUtils.isNotEmpty(modelVo.getModelName1())){
			sql.append(" AND p.price <=:price1");
			param.put("price1",modelVo.getModelName1());
		}

		if(modelVo!=null && StringUtils.isNotEmpty(modelVo.getModelName2())){
			sql.append(" AND p.price >=:price2");
			param.put("price2",modelVo.getModelName2());
		}

		if(modelVo!=null && StringUtils.isNotEmpty(modelVo.getStartDate())){
			sql.append(" AND p.VALID_END_DATE >=:date1");
			param.put("date1",modelVo.getStartDate());
		}

		if(modelVo!=null && StringUtils.isNotEmpty(modelVo.getEndDate())){
			sql.append(" AND p.VALID_END_DATE <=:date2");
			param.put("date2",modelVo.getEndDate());
		}

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("课程产品管理","nsql","sql");
		sql.append(roleQLConfigService.findAllByNameAndType(rvo,sqlMap));

		sql.append(" order by p.create_time desc ");


		dp = productDao.findPageBySql(sql.toString(),dp,true,param);
		return getReturnProductDp(dp);
	}

	@Override
	public void deleteProduct(Product product) {
		//删除其他产品关联
		String sql = " SELECT * FROM product_package WHERE parent_product_id=:productId OR son_product_id=:productId  ";
		Map<String, Object> params = new HashMap<>();
		params.put("productId", product.getId());
		List<ProductPackage> productPackageList = productPackageDao.findBySql(sql, params);
		if (productPackageList.size()>0){
			throw new ApplicationException("产品已经关联了其他课程产品，不能删除！");
		}
		

		productDao.delete(product);
		if (product.getCategory() == ProductType.OTHERS) {// 如果是其他产品,则推送消息中心到报读
		    MessageQueueUtils.postToMq(MessageQueueUtils.TopicCode.OTHER_PRODUCT_SYNC, product.getId());
		}
	}

	@Override
	public Response saveOrUpdateProduct(Product product) {
	    boolean isChangedNameOrPrice = false; // 其他产品：是否改动过名称或者单价，用于在线报读同步
		Response res=checkRepeatProduct(product);
		if(res.getResultCode()!=0){
			return res;
		}
		if (StringUtil.isNotBlank(product.getId()) && product.getCategory() == ProductType.OTHERS) {
		    isChangedNameOrPrice = this.isChangedNameOrPrice(product);
		}
		if(StringUtils.isEmpty(product.getProStatus()))
			product.setProStatus("0");
		product.setModifyTime(DateTools.getCurrentDateTime());
		User user = userService.getCurrentLoginUser();
		product.setModifyUserId(user.getUserId());
		if(StringUtils.isEmpty(product.getId())){
			product.setCreateTime(DateTools.getCurrentDateTime());
			product.setCreateUserId(user.getUserId());
		}
		if(StringUtils.isEmpty(product.getId()) 
				&& product.getCategory().equals(ProductType.ONE_ON_ONE_COURSE)
				&& StringUtils.isNotEmpty(product.getSubjectNames()) ){
			String[] productNames=product.getSubjectNames().split(",");
			for(String productName : productNames){
				Product newProduct= new Product();
				HibernateUtils.copyPropertysWithoutNull(newProduct, product);
				newProduct.setId(null);
				newProduct.setName(productName);
				productDao.save(newProduct);
			}
			
		}else if(product.getCategory().equals(ProductType.ECS_CLASS) && product.getPromiseClassDiscount()==null){
			product.setPromiseClassDiscount(0.35);//目标班保证金如果为空默认百分之三十五   ，2016-05-05
			productDao.save(product);
		}else{
			productDao.save(product);
		}

		boolean isChangedSonProduct = this.isChangedSonProduct(product); // 判断是否有修改过关联的子产品
		if (isChangedSonProduct) {
		    deleteProductPackageByParentID(product.getId());
		    List<ProductPackage> productPackageList = product.getProductPackageList();
		    for (ProductPackage otherProductPackage :productPackageList){
		        otherProductPackage.setCreateTime(DateTools.getCurrentDateTime());
		        otherProductPackage.setCreateUserId(userService.getCurrentLoginUser().getUserId());
		        otherProductPackage.setParentProduct(product);
		        productPackageDao.save(otherProductPackage);
		    }
		}

		// 修改前为小班产品，修改子产品逻辑，如果有变动则推送到消息中心 miniclassIds
		if (product.getCategory() == ProductType.SMALL_CLASS && isChangedSonProduct) {
		    String miniClassIds = this.getRelatedMiniClassIds(product.getId()); // 查询关联的小班编号用逗号隔开
		    if (StringUtil.isNotBlank(miniClassIds)) {
		        MessageQueueUtils.postToMq(MessageQueueUtils.TopicCode.MINI_CLASS_OTHER_PRODUCT_SYNC, miniClassIds);
		    }
		}

		// 如果是其他产品,(改动了名称和单价或者无效操作)则推送消息中心到报读 productId
		if (isChangedNameOrPrice) {
		    MessageQueueUtils.postToMq(MessageQueueUtils.TopicCode.OTHER_PRODUCT_SYNC, product.getId());
		}

		return res;
	}

	/**
	 * 查询关联的小班编号用逗号隔开
	 * @param productId
	 * @return
	 */
	private String getRelatedMiniClassIds(String productId) {
	    String result = null;
	    String sql = " select GROUP_CONCAT(MINI_CLASS_ID) miniClassIds from mini_class where PRODUCE_ID = :productId ";
	    Map<String, Object> params = Maps.newHashMap();
	    params.put("productId", productId);
	    List<Map<Object, Object>> list = productDao.findMapBySql(sql, params);
	    if (list != null && !list.isEmpty()) {
	        result = (String) list.get(0).get("miniClassIds");
	    }
	    return result;
	}

	/**
	 * 判断是否有修改过关联的子产品
	 * @param product
	 * @return
	 */
	private boolean isChangedSonProduct(Product product) {
	    List<Product> list = this.listAllSubProductsById(product.getId());
	    List<ProductPackage> productPackageList = product.getProductPackageList();
	    int oldSize = 0;
	    int newSize = 0;
	    if (list != null && !list.isEmpty()) {
	        oldSize = list.size();
	    }
	    if (productPackageList != null && !productPackageList.isEmpty()) {
	        newSize = productPackageList.size();
	    }
	    if (newSize != oldSize) { // 数量不等为修改，返回true
	        return true;
	    } else if (newSize == oldSize && newSize == 0) { // 数量相等，并且等于0为没修改，返回false
	        return false;
	    } else { // 数量相等，并且不等0
	        String productIds = "";
            for (ProductPackage pp : productPackageList) {
                productIds += pp.getSonProduct().getId() + ",";
            }
            for (Product p : list) {
                if (productIds.contains(p.getId())) {
                    productIds = productIds.replace(p.getId() + ",", "");
                } else { // 没包含，修改了返回true
                    return true;
                }
            }
            if ("".equals(productIds)) { // 没修改，返回false
                return false;
            }
	    }
	    return true;

	}


	private boolean isChangedNameOrPrice(Product product) {
	    Map<String, Object> params = Maps.newHashMap();
        String sql = " select name, price from product where id = :id ";
        params.put("id", product.getId());
        List<Map<Object, Object>> list = productDao.findMapBySql(sql, params);
        if (list != null && !list.isEmpty()) {
            String nameInDb = (String) list.get(0).get("name");
            BigDecimal priceInDb = (BigDecimal) list.get(0).get("price");
            if (nameInDb != null && !nameInDb.equals(product.getName())) {
                return true;
            }
            if (priceInDb != null && priceInDb.compareTo(product.getPrice()) !=0) {
                return true;
            }
        }
        return false;
	}

	private void deleteProductPackageByParentID(String productId) {
		if (StringUtil.isNotBlank(productId)){
			String hql ="delete from ProductPackage where parentProduct.id= :productId ";
			Map<String, Object> map = new HashMap<>();
			map.put("productId",productId);
			productPackageDao.excuteHql(hql, map);
		}
	}

	private Response checkRepeatProduct(Product product){
		Response res=new Response();
		List<Criterion> list =new ArrayList<Criterion>();
		if(StringUtils.isEmpty(product.getId()) 
				&& product.getCategory().equals(ProductType.ONE_ON_ONE_COURSE)
				&& StringUtils.isNotEmpty(product.getSubjectNames()) ){
			String[] productNames=product.getSubjectNames().split(",");
			list.add(Expression.in("name", productNames));
		}else{
			list.add(Expression.eq("name", product.getName()));
		}
		
		list.add(Expression.eq("category", product.getCategory()));
		if(product.getGradeDict()!=null && StringUtils.isNotEmpty(product.getGradeDict().getId())){
			list.add(Expression.eq("gradeDict.id", product.getGradeDict().getId()));
		}
		list.add(Expression.eq("organization.id", product.getOrganization().getId()));
		if(StringUtils.isNotEmpty(product.getId())){
			list.add(Expression.ne("id", product.getId()));
		}
		List<Product> proList= productDao.findAllByCriteria(list);
		if(proList!=null && proList.size()>0){
			String names="";
			for(Product pro :proList){
				if("".equals(names)){
					names+=pro.getName();
				}else{
					names+=","+pro.getName();
				}
			}
			res.setResultCode(1);
			res.setResultMessage("产品名称是"+names+"已存在");
		}
		return res;
	}

	/**
	 * 查产品，通过id
	 * @param id
	 * @return
	 */
	@Override
	public ProductVo findProductById(String id) {
		Product product= productDao.findById(id);
		if (product!=null){
			ProductVo productVo = HibernateUtils.voObjectMapping(product, ProductVo.class);
			if(product.getProductCategoryId() != null && StringUtils.isNotBlank(product.getProductCategoryId().getCatLevel())){
				String productCategoryFullName=productCategoryDao.getProductCategoryFullName(product.getProductCategoryId().getCatLevel());
				productVo.setProductCategoryFullName(productCategoryFullName);
			}
			List<ProductBranch> branchList=findBranchByProductId(id);

			List<ProductPackage> list = findProductPackage(id);

			List<ProductVo> result = new ArrayList<>();
			for (ProductPackage p : list){
				result.add(HibernateUtils.voObjectMapping(p.getSonProduct(), ProductVo.class));
			}
			productVo.setSonProduct(result);

			Set<String> branchId=new HashSet<>();
			for (ProductBranch b:branchList){
				branchId.add(b.getBranch().getId());
			}
			productVo.setBranchId(branchId);
			return productVo;
		}else {
			throw new ApplicationException("找不到产品");
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProductVo> getOneOnOneCourcesByGrade(String gradeId,String proStatus) {
		List<Product> list = productDao.getOneOnOneCourcesByGrade(gradeId,proStatus);
		List<ProductVo> voList =  HibernateUtils.voListMapping(list, ProductVo.class);
		return voList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductVo> getSmallClassCourcesByKeyWord(String keyword, String gradeId,String proStatus) {
		List<Product> list = productDao.getSmallClassCourcesByKeyWord(keyword, gradeId,proStatus);
		List<ProductVo> voList =  HibernateUtils.voListMapping(list, ProductVo.class);
		return voList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductVo> getOtherProducts() {
		List<Product> list = productDao.getOtherProducts();
		List<ProductVo> voList =  HibernateUtils.voListMapping(list, ProductVo.class);
		return voList;
	}

	@Override
	public List<ProductVo> getProductByTypeSelection(ProductType productType) {
		List<Criterion> list = new ArrayList<Criterion>();
		if (productType != null) {
			list.add(Expression.eq("category", productType));
		}
		list.add(Expression.ne("proStatus", "1"));
		//归属组织架构(归属分公司)
		Organization campus = userService.getBelongBranch();
		if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
			String orgLevel = campus.getOrgLevel();
			list.add(Expression.like("organization.orgLevel",orgLevel,MatchMode.START));
		}
		
		List<Product> proList = productDao.findAllByCriteria(list);
		return HibernateUtils.voListMapping(proList, ProductVo.class);
	}

	@Override
	public Product getFreeHourProduct(String gradeId) {
		// 暂时没有gradeId对应的 free hour 产品
		Product product = productDao.getFreeHourProduct(gradeId);
		return product;
	}

	@Override
	public Product getOneOnOneNormalProduct(String gradeId) {
		Product product = productDao.getOneOnOneNormalProduct(gradeId);
		return product;
	}

	@Override
	public void updateProStatus(Product product) throws Exception {
		if(StringUtils.isEmpty(product.getId()) || StringUtils.isEmpty(product.getProStatus())){
			throw new ApplicationException("操作异常，请联系管理员！");
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("proStatus", product.getProStatus());
		params.put("id", product.getId());
		productDao.excuteHql("update Product set proStatus= :proStatus where id=:id ",params);
	}

	public List<ProductCategory> getProductCategoryTree(){
		return productCategoryDao.getProductCategoryTree();
	}
	
	/**s
	 * 获取第一级的产品类别
	 */
	@Override
	public List<ProductCategory>  getProductCategoryFirstFloor(String isEcsProductCategory) {
		return productCategoryDao.getProductCategoryFirstFloor(isEcsProductCategory);
	}

	@Override
	public void deleteProductCategory(ProductCategory productCategory) throws Exception {
		productCategory = productCategoryDao.findById(productCategory.getId());
		Map<String, Object> params = Maps.newHashMap();
		params.put("catLevel", productCategory.getCatLevel()+"%");
		productCategoryDao.excuteHql("delete ProductCategory where catLevel like :catLevel ",params);
	}

	@Override
	public ProductCategory saveOrUpdateProductCategory(ProductCategory productCategory) {
		User user = userService.getCurrentLoginUser();
		ProductCategory productCategoryDb = null;
		if(productCategory.getId()!= null){
			productCategoryDb = productCategoryDao.findById(productCategory.getId());
			String oldCatLevel=productCategoryDb.getCatLevel();
			productCategoryDb.setCatLevel(productCategoryDao.getProductCategoryLevelString(productCategory));
			if(!productCategoryDb.getCatLevel().equals(oldCatLevel)){
				productCategoryDao.updateChildrenCatLevel(oldCatLevel, productCategoryDb.getCatLevel(),productCategory.getProductType());
			}
			if(productCategory.getProductType() != null)
				productCategoryDb.setProductType(productCategory.getProductType());
			productCategoryDb.setModifyTime(DateTools.getCurrentDateTime());
			productCategoryDb.setModifyUserId(user.getUserId());
			productCategoryDb.setName(productCategory.getName());
			productCategoryDb.setParentId(productCategory.getParentId());
			productCategoryDb.setCatOrder(productCategory.getCatOrder());
			productCategoryDao.save(productCategoryDb);
			return productCategoryDb;
		} else {
			productCategory.setModifyTime(DateTools.getCurrentDateTime());
			productCategory.setModifyUserId(user.getUserId());
			productCategory.setCreateTime(DateTools.getCurrentDateTime());
			productCategory.setCreateUserId(user.getUserId());
			//设置组织架构层级
			productCategory.setCatLevel(productCategoryDao.getProductCategoryLevelString(productCategory));
			productCategoryDao.save(productCategory);
			return productCategory;
		}
	
	}

    /**
     * 根据学生获取可消费产品
     *
     * @param studentId
     * @return
     */
    @Override
    public List<ProductVo> getCanConsumeProductsByStudent(String studentId) {
    	List<ProductVo> list = new ArrayList<ProductVo>();
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("contract.student.id",studentId));
        criterions.add(Restrictions.eq("type", ProductType.ONE_ON_ONE_COURSE));
        criterions.add(Restrictions.not(Restrictions.in("status",new Object[]{ContractProductStatus.ENDED,ContractProductStatus.CLOSE_PRODUCT})));
        List<Order> recordList=new ArrayList<Order>();
		recordList.add(Order.desc("product.createTime"));
        List<ContractProduct> contractProducts = contractProductDao.findAllByCriteria(criterions, recordList);
        for(ContractProduct cp : contractProducts){
        	list.add(HibernateUtils.voObjectMapping(cp.getProduct(),ProductVo.class));
        }
        return list;
    }
	
	/* *//**
     * 根据学生获取可消费产品
     *
     * @param studentId
     * @return
     *//*
    @Override
    public List<ProductVo> getCanConsumeProductsByStudent(String studentId) {
        return productDao.getCanConsumeProductsByStudent(studentId);
    }*/
    
    /**
     * 根据老师获取可教产品
     * 获取产品中有关联该老师可教科目的产品
     *
     * @param teacherId
     * @return
     */
    @Override
    public List<ProductVo> getCanTeachProductsByTeacher(String teacherId) {
        List<ProductVo> products = new ArrayList<ProductVo>();
        Set<DataDict> subjects = dataDictService.getCanTeachSubjectByTeacher(teacherId);
        if(subjects.size() > 0){
            List<Criterion> productCriterions = new ArrayList<Criterion>();
            Disjunction orLike = Restrictions.disjunction();
            for(DataDict subject : subjects){
                orLike.add(Restrictions.like("subjectIds",subject.getId(),MatchMode.ANYWHERE));
            }
            productCriterions.add(orLike);
            productCriterions.add(Restrictions.eq("category", ProductType.ONE_ON_ONE_COURSE));
            productCriterions.add(Restrictions.eq("proStatus" ,"0"));
            List<Order> recordList=new ArrayList<Order>();
    		recordList.add(Order.desc("createTime"));
            List<Product> productsList = productDao.findAllByCriteria(productCriterions, recordList);
            for (int i = 0; i < productsList.size(); i++) {
                products.add(HibernateUtils.voObjectMapping(productsList.get(i),ProductVo.class));
            }
        }
        return products;
    }
    
    @Override
    public DataPackage findPage(DataPackage dp, ProductChooseVo productChooseVo, String productIdArray,String selectProductType, String mainProductId) {
        String currentDate = DateTools.getCurrentDate();
        List<Criterion> criterions = new ArrayList<Criterion>();
        if(productChooseVo.getExpressions() != null){
            for(String expression : productChooseVo.getExpressions()){
                criterions.add(productDao.buildCriterion(expression));
            }
        }
        // 产品类别
        if(productChooseVo.getProduct() != null){
        	if(productChooseVo.getProduct().getCategory() != null){
                criterions.add(Restrictions.eq("category",productChooseVo.getProduct().getCategory()));
            }
//        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductCategoryId())) {
//        		criterions.add(Restrictions.eq("product.productCategoryId.id",productChooseVo.getProduct().getProductCategoryId()));
//        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getCourseSeriesId())) {
        		criterions.add(Restrictions.eq("courseSeries.id",productChooseVo.getProduct().getCourseSeriesId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductVersionId())) {
        		criterions.add(Restrictions.eq("productVersion.id",productChooseVo.getProduct().getProductVersionId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductQuarterId())) {
        		criterions.add(Restrictions.eq("productQuarter.id",productChooseVo.getProduct().getProductQuarterId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getSubjectId())) {
        		criterions.add(Restrictions.eq("subject.id",productChooseVo.getProduct().getSubjectId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getClassTypeId())) {
        		criterions.add(Restrictions.eq("classType.id",productChooseVo.getProduct().getClassTypeId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductGroupId())) {
        		criterions.add(Restrictions.eq("productGroup.id",productChooseVo.getProduct().getProductGroupId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getGradeId())) {
        		criterions.add(Restrictions.eq("gradeDict.id",productChooseVo.getProduct().getGradeId()));
        	}
			if (StringUtils.isNotBlank(productChooseVo.getProduct().getGradeSegmentId())) {
				criterions.add(Restrictions.eq("gradeSegmentDict.id",productChooseVo.getProduct().getGradeSegmentId()));
			}
        }
        if (StringUtils.isNotBlank(productIdArray)) {
        	String[] idArray = productIdArray.split(",");
        	for (String id : idArray) {
        		criterions.add(Restrictions.ne("id", id));
        	}
        }

        if (!"main".equals(selectProductType)){
			if (StringUtils.isNotBlank(mainProductId)){
				Product mainProduct = productDao.findById(mainProductId);//主产品选择后，副产品可以看到主产品的相同年份，季度，年级与同产品组的并集。
				if (mainProduct!=null){
					if (mainProduct.getProductVersion()==null||mainProduct.getProductQuarter()==null||mainProduct.getGradeDict()==null){
						throw new ApplicationException("主产品必须要有年份，季度，年级，请修改主产品！");
					}
					if (mainProduct.getProductGroup()!=null){
						criterions.add(
								Restrictions.or(
										//相同年份，季度，年级
										Restrictions.and(
												Restrictions.and(
														Restrictions.eq("productVersion.id", mainProduct.getProductVersion().getId()) // 相同年份
														,Restrictions.eq("productQuarter.id", mainProduct.getProductQuarter().getId())//相同季度
												)
												,Restrictions.eq("gradeDict.id", mainProduct.getGradeDict().getId())//相同年级
										)
										//或者相同产品组
										,Restrictions.eq("productGroup.id", mainProduct.getProductGroup().getId())
								)
						);
					}else {
						//没有产品组
						criterions.add(
								//相同年份，季度，年级
								Restrictions.and(
										Restrictions.and(
												Restrictions.eq("productVersion.id", mainProduct.getProductVersion().getId()) // 相同年份
												,Restrictions.eq("productQuarter.id", mainProduct.getProductQuarter().getId())//相同季度
										)
										,Restrictions.eq("gradeDict.id", mainProduct.getGradeDict().getId())//相同年级
								)
						);
					}
				}
			}
		}

        
        
       //#4951
        criterions.add(Restrictions.ge("validEndDate", currentDate));
        criterions.add(Restrictions.le("validStartDate", currentDate));
//        if(selectProductType!=null && !selectProductType.equals("add")){
        	//添加辅助产品不需要限制
        	

            // 排除不在有效期内的产品 #3812 小班建立关联的主次产品都需要是有效的产品，但不需要时间有效区间限制


			//#3812 主产品关联需要产品年份是当前年份以后的年份（比如选择是2016年5月，主产品只能选择产品年份为2016年或以后的产品）。

//			try {
//				criterions.add(Restrictions.ge("productVersion.name",DateTools.getCurrentDate("yyyy")));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

//		}

		// 排除已禁用的产品
		criterions.add(Restrictions.ne("proStatus","1"));

        // 本公司的产品和本校区的小班
        criterions.add(Restrictions.eq("organization.id", userService.getBelongBranch().getId()));
        // 产品组织架构向上兼容
//        criterions.add(Restrictions.sqlRestriction("(select orgLevel from organization where id = '"+userService.getCurrentLoginUser().getOrganizationId()+"') like concat((select orgLevel from organization where id = (select organization_id from product where id = product_id)),'%')"));
//        criterions.add(Restrictions.or(Restrictions.eq("miniClass.blCampus.id", userService.getBelongCampus().getId()),Restrictions.isNull("miniClass.blCampus.id")));

        List<Order> orders = new ArrayList<Order>();
        dp = productDao.findPageByCriteria(dp,orders,criterions);



        dp.setDatas(HibernateUtils.voListMapping((List) dp.getDatas(),ProductVo.class));
        return dp;
    }
    
    
    @Override
    public DataPackage findProductByCategoryAndSomeParam(DataPackage dp,
    		ProductVo product) {
    	dp=productDao.findProductByCategoryAndSomeParam(dp,product);
    	dp.setDatas(HibernateUtils.voListMapping((List)dp.getDatas(), ProductVo.class));
    	return dp;
    }
    
    /**
     * 过季小班清理
     */
    @Override
    public Response cleanOutExpiredSmallClass(String productVersion, String productQuarter, String submitParams){
    	if(submitParams.equals("search")){
    		return productDao.cleanOutExpiredSmallClass(productVersion, productQuarter);
    	}else{
    		//执行存储过程
    		this.pushCleanSmallclassToQueue(new CleanSmallClass(productVersion, productQuarter));
    		return new Response();
    	}
    	
    }
    
    /**
     * 过季小班清理，添加到队列
     */
    public void pushCleanSmallclassToQueue(CleanSmallClass cleanSmallclass){
		try {// 加入队列
			JedisUtil.lpush(
					ObjectUtil.objectToBytes("clean"),
					ObjectUtil.objectToBytes(cleanSmallclass));
		} catch (Exception e) {
			// 错了				
		}
		
		
	}
    
	@Override
	public Map<String, Object> getCourseProductInfo(String courseId) {

		Map<String, Object> map = new HashMap<String, Object>();
		CourseProductInfoVo result = new CourseProductInfoVo();
		// 根据课程Id查询product 只需要查course 和mini_class 一对多没有产品 概念
		StringBuffer query = new StringBuffer();
		// 一对一
		Product product = null;
		Map<String, Object> params = Maps.newHashMap();
		params.put("courseId", courseId);
		query.append(" select * from product p where p.ID in (select c.PRODUCT_ID from course c WHERE c.COURSE_ID = :courseId ) ");
		List<Product> courselist = productDao.findBySql(query.toString(),params);
		if (courselist != null && courselist.size() > 0) {
			product = courselist.get(0);
		} else {
			query.delete(0, query.length());
			query.append(" select * from product p where p.ID in ");
			query.append(" (select mc.PRODUCE_ID from mini_class mc where mc.MINI_CLASS_ID in ");
			query.append(" (select MINI_CLASS_ID from mini_class_course mcc where mcc.MINI_CLASS_COURSE_ID = :courseId ) ) ");
			List<Product> minilist = productDao.findBySql(query.toString(),params);
			if (minilist != null && minilist.size() > 0) {
				product = minilist.get(0);
			}
		}
		if (product != null) {
			result.setId(product.getId());
			result.setName(product.getName());
			result.setCategory(product.getCategory()!=null?product.getCategory()
					.getValue():"null");
			result.setCategoryName(product.getCategory()!=null?product.getCategory()
					.getName():"null");
			result.setClassTypeId(product.getClassType()!=null?product.getClassType().getValue():"null");
			result.setClassTypeName(product.getClassType()!=null?product.getClassType().getName():"null");
			result.setCourseSeriesId(product.getCourseSeries()!=null?product.getCourseSeries().getValue():"null");
			result.setCourseSeriesName(product.getCourseSeries()!=null?product.getCourseSeries().getName():"null");
			result.setProductVersionId(product.getProductVersion()!=null?product.getProductVersion().getValue():"null");
			result.setProductVersionName(product.getProductVersion()!=null?product.getProductVersion().getName():"null");
			result.setProductQuarterId(product.getProductQuarter()!=null?product.getProductQuarter().getValue():"null");
			result.setProductQuarterName(product.getProductQuarter()!=null?product.getProductQuarter().getName():"null");
			result.setSubjectId(product.getSubject()!=null?product.getSubject().getValue():"null");
			result.setSubjectName(product.getSubject()!=null?product.getSubject().getName():"null");
			result.setProductGroupId(product.getProductGroup()!=null?product.getProductGroup().getValue():"null");
			result.setProductGroupName(product.getProductGroup()!=null?product.getProductGroup().getName():"null");
			result.setOrganizationId(product.getOrganization()!=null?product.getOrganization().getId():"null");
			result.setOrganizationName(product.getOrganization()!=null?product.getOrganization().getName():"null");
			result.setGradeSegmentDictId(product.getGradeSegmentDict()!=null?product.getGradeSegmentDict().getValue():"null");
			result.setGradeSegmentDictName(product.getGradeSegmentDict()!=null?product.getGradeSegmentDict().getName():"null");
			result.setGradeId(product.getGradeDict()!=null?product.getGradeDict().getValue():"null");
			result.setGradeName(product.getGradeDict()!=null?product.getGradeDict().getName():"null");
			result.setPrice(String.valueOf(product.getPrice()));
			result.setValidStartDate(product.getValidStartDate());
			result.setValidEndDate(product.getValidEndDate());
			result.setMiniClassTotalhours(String.valueOf(product.getMiniClassTotalhours()));
			result.setClassTimeLength(
					product.getClassTimeLength() == null ? product.getClassTimeLength().toString() : "0");
		}else{
			result = null;
		}
		query.delete(0, query.length());

		map.put("resultStatus", 200);
		map.put("resultMessage", "产品信息");
		map.put("result", result);
		return map;
	}

	@Override
	public DataPackage findProductForTwoTeacherChoose(DataPackage dp, ProductChooseVo productChooseVo) {
		Map<String,Object> pmap= new HashMap();
		StringBuffer hql= new StringBuffer();
		hql.append(" from Product where 1=1 ");
		if(productChooseVo.getProduct() != null){

			if (StringUtils.isNotBlank(productChooseVo.getProduct().getName())){
				hql.append(" and name like :productName ");
				pmap.put("productName", "%"+productChooseVo.getProduct().getName()+"%");
			}

			if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductQuarterId())) {
				hql.append(" and productQuarter.id= :productQuarterId ");
				pmap.put("productQuarterId", productChooseVo.getProduct().getProductQuarterId());
			}
			if (StringUtils.isNotBlank(productChooseVo.getProduct().getCourseSeriesId())) {
				hql.append(" and courseSeries.id=:courseSeriesId ");
				pmap.put("courseSeriesId", productChooseVo.getProduct().getCourseSeriesId());
			}
			if (StringUtils.isNotBlank(productChooseVo.getProduct().getClassTypeId())) {
				hql.append(" and classType.id=:classTypeId ");
				pmap.put("classTypeId", productChooseVo.getProduct().getClassTypeId());
			}
			if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductGroupId())) {
				hql.append(" and productGroup.id= :productGroupId ");
				pmap.put("productGroupId", productChooseVo.getProduct().getProductGroupId());
			}
			if (StringUtils.isNotBlank(productChooseVo.getProduct().getGradeId())) {
				hql.append(" and gradeDict.id= :gradeDictId ");
				pmap.put("gradeDictId", productChooseVo.getProduct().getGradeId());
			}
			if (StringUtils.isNotBlank(productChooseVo.getProduct().getGradeSegmentId())) {
				hql.append(" and gradeSegmentDict.id= :gradeSegmentDictId ");
				pmap.put("gradeSegmentDictId", productChooseVo.getProduct().getGradeSegmentId());
			}
			if (StringUtils.isNotBlank(productChooseVo.getProduct().getPhaseId())) {
				hql.append(" and phase.id= :phaseId ");
				pmap.put("phaseId", productChooseVo.getProduct().getPhaseId());
			}
		}


		hql.append(" and  validStartDate <='"+DateTools.getCurrentDate()+"'");
		hql.append(" and  validEndDate >='"+DateTools.getCurrentDate()+"'");
		// 排除已禁用的产品
		hql.append(" and proStatus<>'1' and category='"+ProductType.TWO_TEACHER+"'");

		dp=productDao.findPageByHQL(hql.toString(),dp,true,pmap);

		dp.setDatas(HibernateUtils.voListMapping((List) dp.getDatas(),ProductVo.class));
		return dp;
	}

	public Product findProductByLiveId(String liveId){
    	String hql="from Product where liveId='"+liveId+"'";

    	return productDao.findOneByHQL(hql,null);
	}

	@Override
	public void saveProductByLive(JSONObject proMap) {
    	Product product;

		if(proMap.get("curriculum_id")!=null) {
			product= findProductByLiveId(proMap.getString("curriculum_id"));
			if(product==null){
				product=new Product();
				product.setCreateTime(DateTools.getCurrentDateTime());
				product.setModifyTime(DateTools.getCurrentDateTime());
				product.setCreateUserId("112233");
				product.setModifyUserId("112233");
				product.setDiscount(BigDecimal.ZERO);
				ProductCategory productCategory =productCategoryDao.findById(ProductType.LIVE.getValue());
				if(productCategory!=null)
				product.setProductCategoryId(productCategory);
				Organization organization=userService.getBelongGroupUserId("112233");
				product.setOrganization(organization);
			}else{
				product.setModifyTime(DateTools.getCurrentDateTime());
			}
			product.setLiveId(proMap.getString("curriculum_id"));
		}else{
			throw new ApplicationException("产品ID不能为空");
		}
		product.setCategory(ProductType.LIVE);

    	if(proMap.get("lesson_time_in_min")!=null) {
			product.setClassTimeLength(proMap.getInt("lesson_time_in_min"));
		}
		if(proMap.get("curriculum_title")!=null)
			product.setName(proMap.getString("curriculum_title"));

		if(proMap.get("teacher_account")!=null) {
			User user=userService.findUserByAccount(proMap.getString("teacher_account"));
			if(user!=null) {
				product.setTeacher(user);
			}else{
				throw new ApplicationException("老师没有同步过来");
			}
		}
		if(proMap.get("grade")!=null) {
			DataDict grade=dataDictService.findDataDictByNameAndCateGory(proMap.getString("grade"), DataDictCategory.STUDENT_GRADE.getValue());
			if(grade!=null) {
				product.setGradeDict(grade);
				if (grade.getParentDataDict() != null) {
					product.setGradeSegmentDict(grade.getParentDataDict());
				}
			}
		}

		if(proMap.get("subject")!=null) {
			net.sf.json.JSONArray subjecs=proMap.getJSONArray("subject");
			String subj="";
			for(Object sub:subjecs) {
				DataDict subject = dataDictService.findDataDictByNameAndCateGory(sub.toString(), DataDictCategory.SUBJECT.getValue());
				if(subject!=null){
					subj+=subject.getId()+",";
				}
			}
			if(subj.length()>0)
			product.setSubjectIds(subj.substring(0,subj.lastIndexOf(",")));
		}
		if(proMap.get("class_type")!=null) {
			DataDict classType=dataDictService.findDataDictByNameAndCateGory(proMap.getString("class_type"), DataDictCategory.CLASS_TYPE.getValue());
			product.setClassType(classType);
		}
		if(proMap.get("lesson_price")!=null)
			product.setPrice(BigDecimal.valueOf(proMap.getDouble("lesson_price")));
		if(proMap.get("lesson_num")!=null)
			product.setMiniClassTotalhours(BigDecimal.valueOf(( proMap.getDouble("lesson_num"))));
		if(proMap.get("price")!=null)
			product.setTotalAmount(BigDecimal.valueOf((proMap.getDouble("price"))));

		if(proMap.get("start_time")!=null) {
			String startTime=DateTools.TimeStamp2Date(proMap.getString("start_time"),"yyyy-MM-dd HH:mm");
			product.setValidStartDate(startTime.substring(0,10));
			product.setStartTime(startTime.substring(11));
		}
		if(proMap.get("end_time")!=null) {
			String endTime=DateTools.TimeStamp2Date(proMap.getString("end_time"),"yyyy-MM-dd");
			product.setValidEndDate(endTime);
		}

		if(proMap.get("off_price")!=null) {
			BigDecimal promotionAmount=BigDecimal.valueOf(proMap.getDouble("off_price"));
			if(promotionAmount.compareTo(BigDecimal.ZERO)>0) {
				product.setPromotionAmount(promotionAmount);
				Promotion promotion = promotionDao.findOneByHQL(" from Promotion where endTime>='"+DateTools.getCurrentDate()+"' and organization.id='000001' and promotionType='REDUCTION' and isActive='Y' and promotionValue=" + promotionAmount, null);
				if (promotion != null) {
					product.setPromotionId(promotion.getId());
					product.setPromotionName(promotion.getName());
				} else {//如果系统中没有，就创建新的优惠
					Promotion p=saveNewPromotion(promotionAmount);
					product.setPromotionId(p.getId());
					product.setPromotionName(p.getName());
				}
			}else{
				product.setPromotionId(null);
				product.setPromotionName(null);
			}
		}else{
			product.setPromotionId(null);
			product.setPromotionName(null);
		}
		product.setMaxPromotionDiscount(Double.parseDouble("99.99"));
		product.setProStatus("0");
		if(!proMap.getBoolean("is_on_shelf")){
			product.setProStatus("1");
		}

		productDao.save(product);
	}

	/**
	 * @param liveId
	 * @return
	 */
	@Override
	public String getLiveProductIdByLiveId(String liveId) {
		Product product = findProductByLiveId(liveId);
		if (product==null){
			updateLiveProduct(liveId);
			product = findProductByLiveId(liveId);
			if (product == null){
				throw new ApplicationException("直播产品不存在,创建产品失败");
			}else {
				return product.getId();
			}
		}else {
			return product.getId();
		}
	}

	/**
	 * 根据金额创建新的优惠
	 * @param promotionAmount
	 * @return
	 */
	@Override
	public Promotion saveNewPromotion(BigDecimal promotionAmount){
    	User user = userService.findUserById("112233");
    	Promotion promotion=new Promotion();
    	promotion.setAccessRoles("");
    	promotion.setCreateTime(DateTools.getCurrentDateTime());
    	promotion.setEndTime("2099-12-31");
		promotion.setIsActive("Y");
		promotion.setModifyTime(DateTools.getCurrentDateTime());
		promotion.setData("LIVE");
		if(user!=null) {
			promotion.setCreateUser(user);
			promotion.setModifyUser(user);
			Organization org=organizationService.findById("000001");
			if(org!=null){
				promotion.setOrganization(org);
			}else {
				promotion.setOrganization(userService.getBelongGroupUserId(user.getUserId()));
			}
		}
		promotion.setName("减"+promotionAmount+"元");
    	promotion.setPromotionType(PromotionType.REDUCTION);
    	promotion.setPromotionValue(promotionAmount);
    	promotion.setStartTime(DateTools.getCurrentDate());
    	promotionDao.save(promotion);
    	return promotion;
	}
	/**
	 * 修改直播产品分公司
	 * @param id
	 * @param branchId
	 */
	@Override
    public void saveLiveProduct(String id, String[] branchId) {
        Product product=productDao.findById(id);
        if(product!=null){
        	product.setModifyTime(DateTools.getCurrentDateTime());
        	if(userService.getCurrentLoginUser()!=null)
        	product.setModifyUserId(userService.getCurrentLoginUser().getUserId());
			deleteProductBranch(id);
			saveProductBranch(branchId,product);
		}else{
        	throw new ApplicationException("未找到数据");
		}

    }

	/**
	 * 根据产品查询分公司list
	 * @param productId
	 * @return
	 */
	public List<ProductBranch>  findBranchByProductId(String productId){
		Map<String,Object> pmap= new HashMap();
		String hql = "from ProductBranch where product.id = '"+productId+"'";
		return productBranchDao.findAllByHQL(hql,pmap);
	}


	private List<ProductPackage> findProductPackage(String id) {
		Map<String,Object> pmap= new HashMap();
		String hql = "from ProductPackage where parentProduct.id = :id";
		pmap.put("id", id);
		return productPackageDao.findAllByHQL(hql, pmap);
	}


	/**
	 * 根据产品删除关联分公司
	 * @param productId
	 */
	public void deleteProductBranch(String productId){
		List<ProductBranch> brench= findBranchByProductId(productId);
		productBranchDao.deleteAll(brench);
		productBranchDao.flush();
	}

	/**
	 * 保存产品分公司关系表
	 * @param brenchId
	 * @param product
	 */
	public void saveProductBranch(String[] brenchId,Product product){
		for(String brench:brenchId) {
			ProductBranch pb=new ProductBranch();
			pb.setBranch(new Organization(brench));
			pb.setProduct(product);
			productBranchDao.save(pb);
		}
	}


	@Override
	public void updateLiveProduct(String productId) {
		String url = PropertiesUtils.getStringValue("LIVE_URL")+"internal/v1.5/curriculum/details?curriculum_id="+productId;
		HttpClient client = wrapHttpClient();
		HttpGet getrequest = new HttpGet(url);
		setRequestHeader(getrequest);
		HttpResponse getResponse =null;
		try {
			getResponse =client.execute(getrequest);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("访问直播系统接口出错2"+e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			throw new ApplicationException("访问直播系统接口出错3"+e.getMessage()+":地址"+url);
		}

		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				String str =  EntityUtils.toString(getResponse.getEntity(),"utf-8");
				JSONObject j=JSONObject.fromObject(str);
				if(j!=null){
					saveProductByLive(j);
				}
				log.info(str);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApplicationException("处理数据异常"+getResponse.getStatusLine().getStatusCode());
			}
		}else{
			throw new ApplicationException("访问直播系统接口出错1"+getResponse.getStatusLine().getStatusCode());
		}
	}

	@Override
	public void transferProductToLivePlatform(String productId) {
			String url = PropertiesUtils.getStringValue("LIVE_URL") + "/internal/v1.5/pay/order/new";
			HttpClient client = wrapHttpClient();
			HttpPost post = new HttpPost(url);
			log.info("URL:"+url);
			setRequestHeader(post);
			try {
				post.addHeader(HTTP.CONTENT_TYPE, "application/json");
				StringEntity se = new StringEntity(JSONObject.fromObject(productId).toString(), "utf-8");
				se.setContentType("text/json");
				post.setEntity(se);
				HttpResponse getResponse = client.execute(post);
				if (getResponse != null && getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					try {
						String str = EntityUtils.toString(getResponse.getEntity(), "utf-8");
						JSONObject j = JSONObject.fromObject(str);
						if(j.get("status")!=null && "2".equals(j.get("status").toString())){

						}
					} catch (Exception e) {
						JedisUtil.lpush("liveProductErro",productId);
						e.printStackTrace();
						throw new ApplicationException("处理数据异常" + getResponse.getStatusLine().getStatusCode());
					}
				} else {
					JedisUtil.lpush("liveProductErro",productId);
					throw new ApplicationException("访问直播系统接口出错" + getResponse.getStatusLine().getStatusCode());
				}
			} catch (UnsupportedEncodingException e) {
				JedisUtil.lpush("liveProductErro",productId);
				throw new ApplicationException("编码出错" + e.getMessage());
			} catch (IOException e) {
				JedisUtil.lpush("liveProductErro",productId);
				throw new ApplicationException("访问直播系统接口出错" + e.getMessage());
			}
	}

	@Override
	public List<ProductVo> getOtherProductForEcs(String organizationId, String category) {

		String hql = "from Product p where (p.organization.id = :organizationId or p.organization.orgType='GROUNP') and category = 'OTHERS' and other_of_type.id =:otherTypeId and p.proStatus=0 and p.validEndDate >=:day and p.validStartDate <= :day ";

		Map<String, Object> params = new HashMap<>();
		params.put("organizationId", organizationId);
		if (StringUtil.isNotBlank(category)){
			String categoryName = ProductType.valueOf(category).getName();
			String dataHql = "from DataDict where name=:name and category ='OTHER_OF_CATEGORY' ";
			Map<String, Object> map = new HashMap<>();
			map.put("name", categoryName);
			DataDict dataDict = dataDictDao.findOneByHQL(dataHql, map);
			if (dataDict==null){
				List<ProductVo> nullResult = new ArrayList<>();
				return nullResult;
			}
			params.put("otherTypeId", dataDict.getId());
		}
		params.put("day", DateTools.getCurrentDate());
		List<Product> productList = productDao.findAllByHQL(hql, params);
		List<ProductVo> productVos = HibernateUtils.voListMapping(productList, ProductVo.class);

		return productVos;
	}

	private static HttpClient wrapHttpClient()
	{
		HttpClient client = HttpClientUtils.getHttpClient();
		client = WebClientDevWrapper.wrapClient(client);
		return client;
	}

	private static void setRequestHeader(HttpRequestBase getrequest){
		Random random=new Random();
		Long ran=random.nextLong();

		Date date=new Date();
		date=DateTools.add(date,Calendar.MONTH,1);
		Map<String,Object> param=new HashMap<>();

		param.put("1",PropertiesUtils.getStringValue("LIVE_SECRET"));
		param.put("2",ran.toString());
		param.put("3",String.valueOf(date.getTime()));

		getrequest.setHeader("Xiao-User-Agent", PropertiesUtils.getStringValue("LIVE_USER"));
		getrequest.setHeader("X-App-Id", PropertiesUtils.getStringValue("LIVE_APP_ID"));
		getrequest.setHeader("X-Nonce", ran.toString());
		getrequest.setHeader("X-Expiration-At", String.valueOf(date.getTime()));
		getrequest.setHeader(HttpHeaders.CONTENT_TYPE, "Content-Type");
		try {

			getrequest.setHeader("X-Signature", Sha1Utils.SHA1(param));

			log.info("Xiao-User-Agent"+PropertiesUtils.getStringValue("LIVE_USER"));
			log.info("X-App-Id"+PropertiesUtils.getStringValue("LIVE_APP_ID"));
			log.info("X-Nonce"+ran.toString());
			log.info("X-Expiration-At"+String.valueOf(date.getTime()));
			log.info("X-Signature"+Sha1Utils.SHA1(param));
		} catch (DigestException e) {
			e.printStackTrace();
			throw new ApplicationException("加密SHA1失败");
		}
	}
	
	   @Override
	    public DataPackage getTextBookInfoList(TextBookVo textBookVo, DataPackage dataPackage) {
	        // 使用 httpClient获取数据
	        // 根据textBookVo封装参数
	        Map<String, String> params = new HashMap<>();
	        params.put("pageNum", String.valueOf(dataPackage.getPageNo()+1));
	        params.put("pageSize", String.valueOf(dataPackage.getPageSize()));
	        if (textBookVo != null) {
	            if (StringUtils.isNotBlank(textBookVo.getId())) {
	                params.put("id", textBookVo.getId());
	            }
	            if(textBookVo.getState()!=null){
	                params.put("state",textBookVo.getState().toString());
	            }
//				if (textBookVo.getOwnedGroup() != null) {
//					params.put("ownedGroup", textBookVo.getOwnedGroup().toString());
//				}
	            if (StringUtils.isNotBlank(textBookVo.getCategory())) {
	                params.put("category", textBookVo.getCategory());
	            }
	            if (StringUtils.isNotBlank(textBookVo.getProductVersionId())) {
	                params.put("productVersionId", textBookVo.getProductVersionId());
	            }
	            if (StringUtils.isNotBlank(textBookVo.getProductQuarterId())) {
	                params.put("productQuarterId", textBookVo.getProductQuarterId());
	            }
	            if (StringUtils.isNotBlank(textBookVo.getCourseSeriesId())) {
	                params.put("courseSeriesId", textBookVo.getCourseSeriesId());
	            }
	            if (StringUtils.isNotBlank(textBookVo.getGradeId())) {
	                params.put("gradeId", textBookVo.getGradeId());
	            }
	            if (StringUtils.isNotBlank(textBookVo.getSubjectId())) {
	                params.put("subjectId", textBookVo.getSubjectId());
	            }

	            if (StringUtils.isNotBlank(textBookVo.getClassTypeId())) {
	                params.put("classTypeId", textBookVo.getClassTypeId());
	            }
	            if (StringUtils.isNotBlank(textBookVo.getName())) {
	                params.put("name", textBookVo.getName());
	            }
	            if (StringUtils.isNotBlank(textBookVo.getOrganizationId())) {
	                params.put("organizationId", textBookVo.getOrganizationId());
	            }
	            if(StringUtils.isNotBlank(textBookVo.getWareType())){
	                params.put("wareType", textBookVo.getWareType());
	            }
	        }
	        String json = null;
	        try {
	        	log.info("listUrl:"+listUrl);
	        	log.info("params:"+params);
	        	long timeBegin = System.currentTimeMillis();
	            json = HttpClientUtil.doGet(listUrl, params);
	            long timeEnd = System.currentTimeMillis();
	            log.info("getTextBookInfoList-costtime:"+(timeEnd-timeBegin)+"ms");
	            // 解析结果
	            com.alibaba.fastjson.JSONObject object = JSON.parseObject(json);
	            if(object==null)return dataPackage;

	            Integer code = (Integer) object.get("code");
	            log.info("code:"+code);
	            if (code!=200) {
	                return dataPackage;
	            }
	            com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject) object.get("data");
	            com.alibaba.fastjson.JSONArray jsonArray = data.getJSONArray("result");
	            Integer totalCount = (Integer) data.get("totalCount");
	            List<TextBookVo> result = null;
	            if(jsonArray!=null){
	                result = JSON.parseArray(jsonArray.toJSONString(), TextBookVo.class);
	            }
	            dataPackage.setDatas(result);
	            dataPackage.setRowCount(totalCount);
	            log.info("result:"+result);
	            return dataPackage;
	        } catch (Exception e) {
	            // 请求失败 则抛出返回 null
	            e.printStackTrace();
//				return dataPackage;
	            throw new ApplicationException("请求教材数据失败");
	        }
	    }

    @Override
    public List<Product> listAllSubProductsById(String productId) {
        String sql = " select p.* from product_package pp "
                + " left join product p on pp.son_product_id = p.id where pp.parent_product_id = :productId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("productId", productId);
        return productDao.findBySql(sql, params);
    }

    @Override
    public List<Product> listAllSubProductsByMiniClassId(String miniClassId) {
        String sql = " select p.* from  product_package pp "
                + " left join product p on pp.son_product_id = p.id "
                + " left join mini_class mc on pp.parent_product_id = mc.PRODUCE_ID where mc.MINI_CLASS_ID = :miniClassId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("miniClassId", miniClassId);
        return productDao.findBySql(sql, params);
    }

    @Override
    public boolean checkRelatedOnLineSaleMiniClassByProductId(String productId) {
        String sql = " select count(1) from  product_package pp "
                + " left join product p on pp.son_product_id = p.id "
                + " left join mini_class mc on pp.parent_product_id = mc.PRODUCE_ID where p.id = :productId and mc.ONLINE_SALE = 0 and on_shelves = 0 ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("productId", productId);
        int reslut = productDao.findCountSql(sql, params);
        if (reslut > 0) {
            return false;
        }
        return true;
    }


}
