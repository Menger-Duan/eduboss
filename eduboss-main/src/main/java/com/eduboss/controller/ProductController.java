package com.eduboss.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.eduboss.common.Constants;
import com.eduboss.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.guice.RequestParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eduboss.domain.Product;
import com.eduboss.domain.ProductCategory;
import com.eduboss.domainVo.ProductChooseVo;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.domainVo.TextBookVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.MiniClassRelationSearchVo;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.Response;
import com.eduboss.service.ContractService;
import com.eduboss.service.ProductChooseViewService;
import com.eduboss.service.ProductService;
import com.eduboss.service.SmallClassService;

/**
 * 产品
 * @author ndd
 * 2014-8-4
 */
@Controller
@RequestMapping(value ="/ProductController")
public class ProductController {

	/** 产品 **/
	@Autowired
	private ProductService productService;

    @Autowired
    private ProductChooseViewService productChooseViewService;
    
    @Autowired
    private ContractService contractService;
    
    @Autowired
    private SmallClassService smallClassService;

	/**
	 * 日志
	 */
	private final static Logger log = Logger.getLogger(ProductController.class);
	
	@RequestMapping(value ="/getProductList")
	@ResponseBody
	public DataPackageForJqGrid getProductList(@ModelAttribute Product product, ModelVo modelVo, @ModelAttribute GridRequest gridRequest) {
		DataPackage dataPackage = new DataPackage(gridRequest);
        if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && Constants.ZERO.equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
            dataPackage = productService.getProductListNew(product, modelVo, dataPackage);
        }else{
            dataPackage = productService.getProductList(product, modelVo, dataPackage);
        }
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value="/editProduct")
	@ResponseBody
	public Response editProduct(@ModelAttribute GridRequest gridRequest, @ModelAttribute Product product) throws Exception{
		Response res = new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			if (contractService.countContractByProductId(product.getId()) > 0 
			        || smallClassService.countByProductId(product.getId()) > 0) {
				res.setResultCode(1);
				res.setResultMessage("产品已经签订合同，不可删除！");
				return res;
			}
			if (!productService.checkRelatedOnLineSaleMiniClassByProductId(product.getId())) {
			    res.setResultCode(1);
                res.setResultMessage("该产品的班课已在报读平台销售，请先在报读平台下架该班课后再删除资料费");
                return res;
			}
			productService.deleteProduct(product);
		}else if("updateProStatus".equalsIgnoreCase(gridRequest.getOper())){
			productService.updateProStatus(product);
		} else {
			return productService.saveOrUpdateProduct(product); 
		}
		return res;
	}

	/**
	 * 查产品，通过id
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findProductById")
	public ProductVo findProductById(@RequestParam String id){
		return productService.findProductById(id);
	}

	@ResponseBody
	@RequestMapping(value="/getProductCategoryTree")
	public List<ProductCategory> getProductCategoryTree(){
		return productService.getProductCategoryTree();
	}
	
	@ResponseBody
	@RequestMapping(value="/getProductCategoryFirstFloor")
	public List<ProductCategory>  getProductCategoryFirstFloor(@RequestParam(required = false, defaultValue = "isNotEcs")String isEcsProductCategory){
		return productService.getProductCategoryFirstFloor(isEcsProductCategory);
	}
	
	@RequestMapping(value="/editProductCategory")
	@ResponseBody
	public Response editProductCategory(@ModelAttribute GridRequest gridRequest, @ModelAttribute ProductCategory productCategory) {
		Response  res = new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			try {
				productService.deleteProductCategory(productCategory);
			} catch (Exception e) {
				res.setResultCode(1);
				res.setResultMessage("产品中有用到该类型或其子类型，不能删除");
			}
		} else {
			ProductCategory productCategoryDb = productService.saveOrUpdateProductCategory(productCategory); 
			res.setResultMessage(productCategoryDb.getId());
		}
		return res;
	}

    @RequestMapping(value ="/findProductForChoose")
    @ResponseBody
    public DataPackageForJqGrid findProductForChoose(@ModelAttribute ProductChooseVo productChooseVo, @ModelAttribute GridRequest gridRequest,@RequestParam(required = false, defaultValue = "isNotEcs")String isEcsProductCategory) {
        DataPackage dp = new DataPackage(gridRequest);
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
        productChooseVo.setExpressions(new ArrayList<String>());
        for(Object paramName : request.getParameterMap().keySet()){
            if(paramName.toString().startsWith("filter_") && StringUtils.isNotBlank(request.getParameter(paramName.toString()))){
                productChooseVo.getExpressions().add(paramName.toString().substring("filter_".length()) + "=" + request.getParameter(paramName.toString()));
            }
        }
        return new DataPackageForJqGrid(productChooseViewService.findPage(dp,productChooseVo,isEcsProductCategory));
    }
    
    /**
     * 小班产品刷选
     */
    @RequestMapping(value ="/findProductForMiniClassChoose")
    @ResponseBody
    public DataPackageForJqGrid findProductForMiniClassChoose(@ModelAttribute ProductChooseVo productChooseVo, String productIdArray, @ModelAttribute GridRequest gridRequest,String selectProductType, String mainProductId) {
        DataPackage dp = new DataPackage(gridRequest);
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
        productChooseVo.setExpressions(new ArrayList<String>());
        for(Object paramName : request.getParameterMap().keySet()){
            if(paramName.toString().startsWith("filter_") && StringUtils.isNotBlank(request.getParameter(paramName.toString()))){
                productChooseVo.getExpressions().add(paramName.toString().substring("filter_".length()) + "=" + request.getParameter(paramName.toString()));
            }
        }
        return new DataPackageForJqGrid(productService.findPage(dp,productChooseVo, productIdArray,selectProductType, mainProductId));
    }


    /**
     * 小班产品刷选
     */
    @RequestMapping(value ="/findProductForTwoTeacherChoose")
    @ResponseBody
    public DataPackageForJqGrid findProductForTwoTeacherChoose(@ModelAttribute ProductChooseVo productChooseVo, @ModelAttribute GridRequest gridRequest) {
        DataPackage dp = new DataPackage(gridRequest);
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
        productChooseVo.setExpressions(new ArrayList<String>());
        for(Object paramName : request.getParameterMap().keySet()){
            if(paramName.toString().startsWith("filter_") && StringUtils.isNotBlank(request.getParameter(paramName.toString()))){
                productChooseVo.getExpressions().add(paramName.toString().substring("filter_".length()) + "=" + request.getParameter(paramName.toString()));
            }
        }
        return new DataPackageForJqGrid(productService.findProductForTwoTeacherChoose(dp,productChooseVo));
    }
    
    
    /**
     * 
     */
    @RequestMapping(value ="/findProductByCategoryAndSomeParam")
    @ResponseBody
    public DataPackageForJqGrid findProductByCategoryAndSomeParam(@ModelAttribute ProductVo product, @ModelAttribute GridRequest gridRequest) {
        DataPackage dp = new DataPackage(gridRequest);
        return new DataPackageForJqGrid(productService.findProductByCategoryAndSomeParam(dp,product));
    }
    
    /**
	 * 根据学生获取已经结束的小班
	 * @param dp
	 * @param miniClassRelationSearchVo
	 * @return
	 */
    @RequestMapping(value ="/findOldMiniClass")
    @ResponseBody
    public DataPackageForJqGrid findOldMiniClass(@ModelAttribute MiniClassRelationSearchVo miniClassRelationSearchVo, @ModelAttribute GridRequest gridRequest) {
        DataPackage dp = new DataPackage(gridRequest);
        return new DataPackageForJqGrid(smallClassService.findOldMiniClassByStudent(dp,miniClassRelationSearchVo));
    }

    /**
	 * 删除小班续班扩科关联记录
	 * @param miniClassRelationSearchVo
	 */
    @RequestMapping(value ="/deleteMiniClassRelation")
    @ResponseBody
    public Response deleteMiniClassRelation(@ModelAttribute MiniClassRelationSearchVo miniClassRelationSearchVo) {
        smallClassService.deleteOldMiniClass(miniClassRelationSearchVo);
        return new Response();
    }
    
    /**
     * 根据老师获取可教产品
     * 获取产品中有关联该老师可教科目的产品
     * @param teacherId
     * @return
     */
    @RequestMapping(value ="/getCanTeachProductsByTeacher")
    @ResponseBody
    public List<ProductVo> getCanTeachProductsByTeacher(@RequestParam String teacherId,String studentId){
        List<ProductVo> canTeachProducts = productService.getCanTeachProductsByTeacher(teacherId);
        if(StringUtils.isNotBlank(studentId)){
            List<ProductVo> canConsumeProducts = productService.getCanConsumeProductsByStudent(studentId);
            if(canTeachProducts != null && canTeachProducts.size() > 0
                    && canConsumeProducts != null && canConsumeProducts.size() > 0){
                if(canTeachProducts.size() > canConsumeProducts.size()){
                    // 以 canTeachProducts 为主取交集
                    canConsumeProducts.retainAll(canTeachProducts);
                    return canConsumeProducts;
                }else{
                    // 以 canConsumeProducts 为主取交集
                    canTeachProducts.retainAll(canConsumeProducts);
                    return canTeachProducts;
                }
            }else{
                return new ArrayList<ProductVo>(0);
            }
        }else{
            return canTeachProducts;
        }
    }

    /**
     * 根据学生获取可消费产品
     * @param studentId
     * @return
     */
    @RequestMapping(value ="/getCanConsumeProductsByStudent")
    @ResponseBody
    public List<ProductVo> getCanConsumeProductsByStudent(@RequestParam String studentId){
        return productService.getCanConsumeProductsByStudent(studentId);
    }



    @RequestMapping(value="/saveLiveProduct",method= RequestMethod.POST)
    @ResponseBody
    public Response saveLiveProduct(@RequestParameters String id, @RequestParam(value="branchId[]") String[] branchId){
        Response res = new Response();
        productService.saveLiveProduct(id,branchId);
        return res;
    }

    @RequestMapping(value = "/getOtherProductForEcs")
    @ResponseBody
    public List<ProductVo> getOtherProductForEcs(@RequestParameters String organizationId, String category){
        return productService.getOtherProductForEcs(organizationId, category);
    }
    
    /**
     * 获取教材信息
     */
    @RequestMapping(value ="/getTextBookInfoList")
    @ResponseBody
    public DataPackageForJqGrid getTextBookInfoList(@ModelAttribute TextBookVo textBookVo, @ModelAttribute GridRequest gridRequest) {
        DataPackage dataPackage = new DataPackage(gridRequest);
        //pageSize 和pageNum必传
        dataPackage = productService.getTextBookInfoList(textBookVo, dataPackage);
        return new DataPackageForJqGrid(dataPackage);
    }
}
