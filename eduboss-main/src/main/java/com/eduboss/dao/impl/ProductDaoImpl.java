package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.ProductType;
import com.eduboss.dao.ProductDao;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Product;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;

@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product, String> implements ProductDao {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 根据产品类别查询
	 * 
	 * @param category
	 *            产品类别
	 * @return
	 */
	public List<Product> findProductByCategory(ProductType category) {
		return super.findByCriteria(Expression.eq("category", category));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getOneOnOneCourcesByGrade(String gradeId,String proStatus) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		// 加入不同分公司的限制
		Organization org = userService.getBelongBranch();
		
		Criteria criteria = session.createCriteria(Product.class)
			    .add( Restrictions.eq("gradeDict.id", gradeId))
			    .add( Restrictions.eq("category", ProductType.ONE_ON_ONE_COURSE))
			    .add(Restrictions.eq("organization", org));
		if(StringUtils.isNotBlank(proStatus)){
			// 这里的值很有意思，参照product.js的editoptions:{value:"0:有效;1:无效;null:有效;:有效"}
			if("1".equals(proStatus)){ // 有效
				criteria.add(Restrictions.or(Restrictions.eq("proStatus", "0"),Restrictions.isNull("proStatus")));
			}else{ // 无效
				criteria.add(Restrictions.eq("proStatus", "1"));
			}
		}
		List<Product> list = criteria .list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getSmallClassCourcesByKeyWord(String keyword, String gradeId,String proStatus) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Organization org = userService.getBelongBranch();
		Criteria criteria = session.createCriteria(Product.class)
			    .add( Restrictions.like("name", keyword, MatchMode.ANYWHERE))
			    .add( Restrictions.eq("category", ProductType.SMALL_CLASS))
			    .add(Restrictions.eq("organization", org))
			    .add( Restrictions.eq("gradeDict.id", gradeId));
		if(StringUtils.isNotBlank(proStatus)){
			// 这里的值很有意思，参照product.js的editoptions:{value:"0:有效;1:无效;null:有效;:有效"}
			if("1".equals(proStatus)){ // 有效
				criteria.add(Restrictions.ne("proStatus", "1"));
			}else{ // 无效
				criteria.add(Restrictions.eq("proStatus", "1"));
			}
		}
		List<Product> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getOtherProducts() {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Organization org = userService.getBelongBranch();
		List<Product> list = session.createCriteria(Product.class)
			    .add( Restrictions.eq("category", ProductType.OTHERS))
			    .add( Restrictions.eq("organization", org))
			    .list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal getOneOnOnePrice(String gradeId) {
		Product oneNormalPrd = this.getOneOnOneNormalProduct(gradeId);
		if(oneNormalPrd.getPrice()!=null) {
			return oneNormalPrd.getPrice(); 
		} else {
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Product getFreeHourProduct(String gradeId) {
		// 暂时没有年级对应的免费课时产品， 都是统一的一个没有价钱的free hour 产品
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		List<Product> list = session.createCriteria(Product.class)
			    .add( Restrictions.eq("category", ProductType.ONE_ON_ONE_COURSE_FREE))
			    .setFirstResult(0)
			    .setMaxResults(1)
			    .list();
		return list.get(0);
	}

	@Override
	public String getProductIdByNameAndGrade(String productName, String gradeId,String brenchId) {
		List<Product> list= findByCriteria(Expression.eq("name", productName),Expression.eq("gradeDict.id", gradeId),Expression.eq("organization.id", brenchId));
		if(list!=null && list.size()>0){
			return list.get(0).getId();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Product getOneOnOneNormalProduct(String gradeId) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		// 加入不同分公司的限制
		Organization org = userService.getBelongBranch();
		List<Product> list = session.createCriteria(Product.class)
			    .add( Restrictions.eq("category", ProductType.ONE_ON_ONE_COURSE_NORMAL))
			    .add( Restrictions.eq("gradeDict", new DataDict(gradeId)))
			    .add(Restrictions.eq("organization", org))
			    .setFirstResult(0)
			    .setMaxResults(1)
			    .list();
		return list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public Product getOneOnOneNormalProduct(String gradeId,Organization org) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		List<Product> list = session.createCriteria(Product.class)
			    .add( Restrictions.eq("category", ProductType.ONE_ON_ONE_COURSE_NORMAL))
			    .add( Restrictions.eq("gradeDict", new DataDict(gradeId)))
			    .add(Restrictions.eq("organization", org))
			    .setFirstResult(0)
			    .setMaxResults(1)
			    .list();
		return list.get(0);
	}
	
	/**
	 * 根据年级产品名称查询小班产品
	 * @param gradeId
	 * @param productName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Product getMiniProductByNameAndGradeId(String gradeId,String productName,Organization org) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		List<Product> list = session.createCriteria(Product.class)
			    .add( Restrictions.eq("category", ProductType.SMALL_CLASS))
			    .add( Restrictions.eq("gradeDict.id", gradeId))
			    .add(Restrictions.eq("organization", org))
			    .add(Restrictions.eq("name", productName))
			    .setFirstResult(0)
			    .setMaxResults(1)
			    .list();
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}

	
	public Product getProductIdByNameAndType(String productName, ProductType type,String brenchId) {
		List<Product> list= findByCriteria(Expression.eq("name", productName),Expression.eq("category", type),Expression.eq("organization.id", brenchId));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public DataPackage findProductByCategoryAndSomeParam(DataPackage dp,ProductVo product) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql=new StringBuilder();
		hql.append(" select p.* from product p left join ref_user_org ref on p.ORGANIZATION_ID=ref.BRANCH_ID where 1=1 ");
		hql.append(" and ref.USER_ID='"+userService.getCurrentLoginUser().getUserId()+"'");
		if(product.getCategory()!=null){
			hql.append(" and p.category = :category ");
			params.put("category", product.getCategory());
		}
		if(StringUtils.isNotEmpty(product.getName())){
			hql.append(" and p.name like :productName ");
			params.put("productName", "%" + product.getName() + "%");
		}
		return this.findPageBySql(hql.toString(), dp, true, params);
	}
	
	/**
	 * @author tangyuping
	 * 过季小班清理
	 */
	@Override
	public Response cleanOutExpiredSmallClass(String productVersion, String productQuarter){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select COUNT(1) as productCount,SUM(surplusAmount) allSurplusAmount from ( ");
		sql.append(" select cp.PRODUCT_ID,SUM(cp.PAID_AMOUNT - cp.CONSUME_AMOUNT) as surplusAmount ");
		sql.append(" from contract_product cp,product p ");
		sql.append(" WHERE cp.PRODUCT_ID = p.id AND  cp.`STATUS` = 'NORMAL'  ");
		sql.append(" AND cp.PAID_STATUS = 'PAID' AND cp.TYPE = 'SMALL_CLASS' ");
		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion and p.PRODUCT_QUARTER_ID = :productQuarter ");
		sql.append(" GROUP BY cp.PRODUCT_ID) c ");
		params.put("productVersion", productVersion);
		params.put("productQuarter", productQuarter);
		Response res = new Response();
		List<Map<Object, Object>> maps = this.findMapBySql(sql.toString(), params);
		Map<Object, Object> map = maps.get(0);			
		Integer code = Integer.valueOf(map.get("productCount").toString()) ;
		if(code > 0){
			BigDecimal surplusAmount = new BigDecimal(map.get("allSurplusAmount").toString()) ;
			res.setResultCode(-1);
			String str = "以上清理涉及小班产品"+code+"个，累计处理小班实收剩余资金"+surplusAmount+"元将回退到所属学生的电子账户";
			res.setResultMessage(str);
			return res;
		}else{
			//没有需要处理的小班产品
			res.setResultCode(0);
			String str = "以上清理涉及小班产品"+0+"个，累计处理小班实收剩余资金"+0.00+"元将回退到所属学生的电子账户";
			res.setResultMessage(str);
			return res;
		}						
		
	}
	
	/**
     * 根据学生获取可消费产品
     */
	public List<ProductVo> getCanConsumeProductsByStudent(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select * from product where ID in "
				+ "(select PRODUCT_ID from contract_product where CONTRACT_ID in (select ID from contract WHERE STUDENT_ID = :studentId) "
				+ "and TYPE = 'ONE_ON_ONE_COURSE' and STATUS != 'ENDED' and STATUS != 'CLOSE_PRODUCT' ) ORDER BY CREATE_TIME ";
		params.put("studentId", studentId);
		List<Product> list = super.findBySql(sql, params);
		return HibernateUtils.voListMapping(list, ProductVo.class);
	}

}
