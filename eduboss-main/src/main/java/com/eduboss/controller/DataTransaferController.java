package com.eduboss.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;
import com.eduboss.common.LiveFinanceType;
import com.eduboss.domain.LiveTransferPay;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domainVo.ContractLiveVo;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.LiveCapmusVo;
import com.eduboss.domainVo.LivePaymentRecordVo;
import com.eduboss.domainVo.LiveTransferPayVo;
import com.eduboss.domainVo.OranizationRelationVo;
import com.eduboss.domainVo.StudentChangeVo;
import com.eduboss.domainVo.StudentTransaferVo;
import com.eduboss.domainVo.UserForTransaferVo;
import com.eduboss.dto.MessageQueueDataVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.intenetpay.IntenetPayResponseParam;
import com.eduboss.service.ChargeService;
import com.eduboss.service.ContractService;
import com.eduboss.service.LivePaymentRecordService;
import com.eduboss.service.LiveTransferPayService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ProductService;
import com.eduboss.service.StudentService;
import com.eduboss.service.UserService;
import com.eduboss.utils.CommonUtil;
import com.eduboss.utils.PropertiesUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/DataTransaferController")
public class DataTransaferController {
    
    private final static Logger logger = Logger.getLogger(DataTransaferController.class);
	
	@Autowired
	private ProductService productService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private ChargeService chargeService;
	
	@Autowired
	private StudentService studentService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private LiveTransferPayService liveTransferPayService;

	@Autowired
	private LivePaymentRecordService livePaymentRecordService;
	
   /* @RequestMapping(value="/saveOrUpdateLiveProduct")
	@ResponseBody
	public void updateLiveProduct(@RequestParam String productId, HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            if(checkAuthorization(request,response)) {
                try {
                    productService.updateLiveProduct(productId);
                    out.print("操作成功！！");
                }catch (ApplicationException e){
                    response.setStatus(500);
                    out.print(e.getErrorMsg());
                }
            }
        }catch (IOException e){
            response.setStatus(500);
        }
	}*/

    @RequestMapping(value="/getContractCampusInfoByIdList")
    @ResponseBody
	public void getContractCampusInfoByIdList(@RequestParam String contractIds, HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            if (checkAuthorization(request, response)){
                try {
                    Map<Object, Object> list = contractService.getContractCampusInfoByIdList(contractIds);
                    out.print(JSONObject.fromObject(list));
                }catch (ApplicationException e){
                    response.setStatus(500);
                    out.print(e.getErrorMsg());
                }
            }
        }  catch (IOException e){
            response.setStatus(500);
        }

    }

    /**
     * 直播课程扣费
     * @param chargeOrWashCurriculumVo
     * @param request
     * @param response
     */
  /*  @RequestMapping(value="/chargeLiveCurriculum", method = RequestMethod.POST)
    @ResponseBody
    public void chargeLiveCurriculum(@RequestBody ChargeOrWashCurriculumVo chargeOrWashCurriculumVo, HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            if(checkAuthorization(request,response)) {
                try {
                    contractService.chargeLiveOfCurriculum(chargeOrWashCurriculumVo);
                    out.print("操作成功！！");
                }catch (ApplicationException e){
                    response.setStatus(500);
                    e.printStackTrace();
                    out.print(e.getErrorMsg());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            response.setStatus(500);
        }
    }*/

   /* @RequestMapping(value="/washCharge", method = RequestMethod.POST)
    @ResponseBody
    public void washCharge(@RequestBody ChargeOrWashCurriculumVo chargeOrWashCurriculumVo, HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            if(checkAuthorization(request,response)) {
                try {
                    chargeService.washLiveChargeList(chargeOrWashCurriculumVo);
                    out.print("操作成功！！");
                }catch (ApplicationException e){
                    response.setStatus(500);
                    e.printStackTrace();
                    out.print(e.getErrorMsg());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            response.setStatus(500);
        }
    }*/



    /*@RequestMapping(value="/closeContractProductForLive", method = RequestMethod.POST)
    @ResponseBody
    public void closeContractProductForLive(@RequestBody LiveContractProductRefundVo liveContractProductRefundVo, HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            if(checkAuthorization(request,response)) {
                try {
                    String contractId = liveContractProductRefundVo.getContractId();
                    String liveId = liveContractProductRefundVo.getLiveId();
                    contractService.closeContractProductForLive(contractId, liveId, liveContractProductRefundVo);
                    out.print("操作成功！！");
                }catch (ApplicationException e){
                    response.setStatus(500);
                    e.printStackTrace();
                    out.print(e.getErrorMsg());
                }catch (Exception e){
                    response.setStatus(500);
                    e.printStackTrace();
                    out.print(e.getMessage());
                }
            }
        }catch (IOException e){
            response.setStatus(500);
            e.printStackTrace();
        }
    }*/

    /*@RequestMapping(value="/saveContract", method = RequestMethod.POST)
    @ResponseBody
    public void saveContract(@RequestBody ContractLiveVo contractVo, HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            if(checkAuthorization(request,response)){
                try {
                    this.checkNumOfBits(contractVo);
                    String contractId = contractService.saveLiveContract(contractVo);
                    String str = "{\"contractId\":\""+contractId+"\"}";
                    JSONObject json = JSONObject.fromObject(str);
                    out.print(json.toString());
                }catch (ApplicationException e){
                    e.printStackTrace();
                    response.setStatus(500);
                    
                    out.print(e.getErrorMsg());
                }catch (Exception e){
                    e.printStackTrace();
                    response.setStatus(500);
                    out.print(e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
*/
  /*  @RequestMapping(value="/changeCurriculum", method = RequestMethod.POST)
    @ResponseBody
    public void changeCurriculum(@RequestBody LiveContractChangeVo liveContractChangeVo, HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            if(checkAuthorization(request,response)){
                try {
                    this.checkNumOfBits(liveContractChangeVo.getContractLiveVo());
                    String contractId=contractService.changeCurriculum(liveContractChangeVo);
                    String str = "{\"contractId\":\""+contractId+"\"}";
                    JSONObject json = JSONObject.fromObject(str);
                    out.print(json.toString());
                }catch (ApplicationException e){
                    response.setStatus(500);
                    e.printStackTrace();
                    out.print(e.getErrorMsg());
                }catch (Exception e){
                    response.setStatus(500);
                    e.printStackTrace();
                    out.print(e.getMessage());
                }
            }

        } catch (IOException e) {
            response.setStatus(500);
            e.printStackTrace();
        }
    }*/

    /**
     * 修改学生联系方式
     * @param studentChangeVo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/changeStudentContact", method = RequestMethod.POST)
    @ResponseBody
    public void changeStudentContact(@RequestBody StudentChangeVo studentChangeVo, HttpServletRequest request, HttpServletResponse response){
        logger.info("changeStudentContact studentChangeVo:" + studentChangeVo);
        if(checkAuthorization(request,response)) {
            try {
                studentService.updateStudentContact(studentChangeVo, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpStatus.SC_BAD_REQUEST);
            }
        }
    }

    /**
     * 鉴权方法
     * @param request
     * @param response
     * @return
     */
    public Boolean checkAuthorization(HttpServletRequest request,HttpServletResponse response){
        try{
            response.setCharacterEncoding("UTF-8");
            PrintWriter out=response.getWriter();
            String authorization=request.getHeader("authorization");
            if(authorization==null||authorization.equals("")){
                response.setStatus(401);
                response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
                out.print("对不起你没有权限！！");
                return false;
            }
            String userAndPass=new String(new BASE64Decoder().decodeBuffer(authorization.split(" ")[1]));
            if(userAndPass.split(":").length<2){
                response.setStatus(401);
                response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
                out.print("对不起你没有权限！！");
                return false;
            }
            String user=userAndPass.split(":")[0];
            String pass=userAndPass.split(":")[1];
            if(user.equals(PropertiesUtils.getStringValue("LIVE_AUTH_USER"))&&pass.equals(PropertiesUtils.getStringValue("LIVE_AUTH_PWD"))){
                return true;
            }else{
                response.setStatus(401);
                response.setHeader("WWW-authenticate","Basic realm=\"帐号密码不匹配\"");
                out.print("对不起你没有权限！！");
                return false;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            response.setStatus(401);
        }
        return false;
    }
    
    /**
     * 检查同步直播数据,是否超过两位小数
     * @param contractLiveVo
     */
    private void checkNumOfBits(ContractLiveVo contractLiveVo) {
        logger.info("contractLiveVo:" + JSON.toJSONString(contractLiveVo));
        if ((contractLiveVo.getPromotionAmount() != null && CommonUtil.getNumOfBits(contractLiveVo.getPromotionAmount().doubleValue()) > 2)
                || (contractLiveVo.getTransactionAmount() != null && CommonUtil.getNumOfBits(contractLiveVo.getTransactionAmount()) > 2)
                || (contractLiveVo.getContractProductVo() != null 
                    && ((contractLiveVo.getContractProductVo().getChangedAmount() != null &&
                        CommonUtil.getNumOfBits(contractLiveVo.getContractProductVo().getChangedAmount()) > 2)
                        || (contractLiveVo.getContractProductVo().getPromotionAmount() != null && 
                                CommonUtil.getNumOfBits(contractLiveVo.getContractProductVo().getPromotionAmount().doubleValue()) > 2)))) {
            throw new ApplicationException(ErrorCode.LIVE_SYNC_BIT_ERROR);
        }
        Set<ContractProductVo> cpSet = contractLiveVo.getContractProductVos();
        if (cpSet != null) {
            for (ContractProductVo cp : cpSet) {
                if ((cp.getChangedAmount() != null && CommonUtil.getNumOfBits(cp.getChangedAmount()) > 2 )|| 
                       ( cp.getPromotionAmount() != null &&  CommonUtil.getNumOfBits(cp.getPromotionAmount().doubleValue()) > 2)) {
                    throw new ApplicationException(ErrorCode.LIVE_SYNC_BIT_ERROR);
                }
            }
        }
    }
    
    /**
     * 分公司列表
     * @author: duanmenrun
     * @Title: getAllBranch
     * @Description: TODO
     * @throws
     * @param request
     * @param response
     */
    @RequestMapping(value="/getAllBranch")
    @ResponseBody
	public Response getAllBranch(HttpServletRequest request, HttpServletResponse response){
    	Response respon = new Response(200,"");
        if (checkAuthorizationNoWriter(request, response)){
        	response.setStatus(200);
            List<Map<String, Object>> list = organizationService.getAllBranch();
            Map<String, Object> map = new HashMap<String, Object>();
    	 	map.put("list", list);
            respon.setData( map );
        }
        return respon;
    }

   /**
    * 查询分公司(星火用工号,培优用账号和手机号)
    * @author: duanmenrun
    * @Title: getBranchByUserMsg
    * @Description: TODO
    * @throws
    * @param employeeNo 工号
    * @param account 账号
    * @param contact 手机号
    * @param request
    * @param response
    */
    @RequestMapping(value="/getBranchByUserMsg")
    @ResponseBody
	public Response getBranchByUserMsg(@ModelAttribute UserForTransaferVo userForTransaferVo,HttpServletRequest request, HttpServletResponse response){
    	Response respon = new Response(200,"");
        if (checkAuthorizationNoWriter(request, response)){
    		response.setStatus(200);
    		respon = userService.findUserByUserForTransaferVo(userForTransaferVo);
    		if(200 != respon.getResultCode()) {
    			return respon;
    		}
    		User user =  (User) respon.getData();
        	Organization org = organizationService.findBrenchById(user.getOrganizationId());
        	if(org == null) {
        		return new Response(ErrorCode.LIVE_ORGANIZATION_EMPTY.getErrorCode(),ErrorCode.LIVE_ORGANIZATION_EMPTY.getErrorString());
        	}
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("organizationId", org.getId());
        	map.put("organizationName", org.getName());
        	respon.setData(map);
        }
        return respon;
    }
    /**
     * 获取用户组织架构和个人信息
     * @author: duanmenrun
     * @Title: getUserDetail 
     * @Description: TODO 
     * @throws 
     * @param userForTransaferVo
     * @param request
     * @param response
     */
    @RequestMapping(value="/getUserDetail")
    @ResponseBody
	public Response getUserDetail(@ModelAttribute UserForTransaferVo userForTransaferVo,HttpServletRequest request, HttpServletResponse response){
    	Response respon = new Response(200,"");
    	if (checkAuthorizationNoWriter(request, response)){
    			response.setStatus(200);
    			respon = userService.getUserDetail(userForTransaferVo);
        }
    	return respon;
    }
    
    
    /**
     * 根据电话查询学员，用户今日签单的客户
     * @author: duanmenrun
     * @Title: getStudentDetailByContact 
     * @Description: TODO 
     * @throws 
     * @param studentContact 模糊查询
     * @param userForTransaferVo 
     * @param request
     * @param response
     */
     @RequestMapping(value="/getStudentDetailByContact")
     @ResponseBody
 	public Response getStudentDetailByContact(@ModelAttribute UserForTransaferVo userForTransaferVo,@RequestParam String  studentContact,HttpServletRequest request, HttpServletResponse response){
    	 Response respon = new Response(200,"");
         if (checkAuthorizationNoWriter(request, response)){
        	 	response.setStatus(200);
        	 	if(StringUtils.isBlank(studentContact)||studentContact.length()<6) {
        	 		respon.setResultCode(ErrorCode.PHONE_LENGTH_ERROR.getErrorCode());
        	 		respon.setResultMessage(ErrorCode.PHONE_LENGTH_ERROR.getErrorString());
        	 		return respon;
        	 	}
        	 	Response responUser = userService.findUserByUserForTransaferVo(userForTransaferVo);
        	 	if(200 != responUser.getResultCode()) {
        	 		return responUser;
        	 	}
        	 	User user =  (User) responUser.getData();
        	 	List<StudentTransaferVo> vos = new ArrayList<>();
        	 	if(StringUtils.isNotBlank(userForTransaferVo.getEmployeeNo())) {
        	 		//boss
        	 		vos = studentService.getStudentDetailByContact(user,studentContact);
    	    	}else {
    	    		//培优
    	    		vos = studentService.getStudentDetailByContactAdvance(user,studentContact);
    	    	}
        	 	
        	 	if(vos!=null&&vos.size()>0) {
        	 		Map<String, Object> map = new HashMap<String, Object>();
            	 	map.put("list", vos);
            	 	respon.setData(map);
        	 	}else {
        	 		respon.setResultCode(ErrorCode.LIVE_RESULT_EMPTY.getErrorCode());
        	 		respon.setResultMessage(ErrorCode.LIVE_RESULT_EMPTY.getErrorString());
        	 		return respon;
        	 	}
         }
         return respon;
     }
     /**
      * 获取订单二维码信息
      * @author: duanmenrun
      * @Title: sendOrderQRCodeUrl 
      * @Description: TODO 
      * @throws 
      * @param userForTransaferVo
      * @param studentContact
      * @param request
      * @param response
      * @return
      */
     @RequestMapping(value="/sendOrderQRCodeUrl")
     @ResponseBody
 	public Response sendOrderQRCodeUrl(@RequestBody LiveTransferPayVo liveTransferPayVo,HttpServletRequest request, HttpServletResponse response){
    	 Response respon = new Response(200,"");
         if (checkAuthorizationNoWriter(request, response)){
        	 	response.setStatus(200);
        	 	if(liveTransferPayVo.getAmount().compareTo(BigDecimal.ZERO)==0) {
        	 		respon.setResultCode(ErrorCode.LIVE_PARAM_EMPTY.getErrorCode());
        	 		respon.setResultMessage("支付金额amount-" + ErrorCode.LIVE_PARAM_EMPTY.getErrorString());
        	 		return respon;
        	 	}
        	 	if(StringUtils.isBlank(liveTransferPayVo.getCallbackUrl())) {
        	 		respon.setResultCode(ErrorCode.LIVE_PARAM_EMPTY.getErrorCode());
        	 		respon.setResultMessage("回调地址callbackUrl-" + ErrorCode.LIVE_PARAM_EMPTY.getErrorString());
        	 		return respon;
        	 	}
        	 	if(StringUtils.isBlank(liveTransferPayVo.getTransactionNum())) {
        	 		respon.setResultCode(ErrorCode.LIVE_PARAM_EMPTY.getErrorCode());
        	 		respon.setResultMessage("直播交易号transactionNum-" +ErrorCode.LIVE_PARAM_EMPTY.getErrorString());
        	 		return respon;
        	 	}
        	 	if(StringUtils.isBlank(liveTransferPayVo.getCampusId())) {
        	 		respon.setResultCode(ErrorCode.LIVE_PARAM_EMPTY.getErrorCode());
        	 		respon.setResultMessage("校区campusId-" +ErrorCode.LIVE_PARAM_EMPTY.getErrorString());
        	 		return respon;
        	 	}
        	 	if(StringUtils.isBlank(liveTransferPayVo.getTitle())) {
        	 		respon.setResultCode(ErrorCode.LIVE_PARAM_EMPTY.getErrorCode());
        	 		respon.setResultMessage("订单标题title-" +ErrorCode.LIVE_PARAM_EMPTY.getErrorString());
        	 		return respon;
        	 	}
        	 	if(StringUtils.isBlank(liveTransferPayVo.getPayType())) {
        	 		respon.setResultCode(ErrorCode.LIVE_PARAM_EMPTY.getErrorCode());
        	 		respon.setResultMessage("支付类型payType-" +ErrorCode.LIVE_PARAM_EMPTY.getErrorString());
        	 		return respon;
        	 	}else if(!liveTransferPayVo.getPayType().equals("W01")
        	 			&&!liveTransferPayVo.getPayType().equals("A01")
        	 			&&liveTransferPayVo.getPayType().equals("Q01")){
        	 		respon.setResultCode(ErrorCode.LIVE_PAYTYPE_PAY.getErrorCode());
        	 		respon.setResultMessage(ErrorCode.LIVE_PAYTYPE_PAY.getErrorString());
        	 		return respon;
        	 	}
        	 	try {
        	 		//StringBuffer url =request.getRequestURL();
        	 		
        	 		logger.info("######################################"+PropertiesUtils.getStringValue("requestURL_address"));
        	 		
        	 		//String projectUrl=url.substring(0,url.indexOf("eduboss")-1)+ request.getContextPath();
        	 		String projectUrl = PropertiesUtils.getStringValue("requestURL_address") + request.getContextPath();
        	 		logger.info("######################################"+projectUrl);
        	 		
        			//花生壳测试
        			//projectUrl = "http://177i6o0681.iok.la:37454/eduboss";
        			liveTransferPayVo.setUrl(projectUrl);
        			logger.info("直播申请二维码参数##："+"amount="+liveTransferPayVo.getAmount()+"##callbackUrl="+liveTransferPayVo.getCallbackUrl()
        			+"##transactionNum="+liveTransferPayVo.getTransactionNum()+"##campusId="+liveTransferPayVo.getCampusId()
        			+"##payType="+liveTransferPayVo.getPayType()+"##title="+liveTransferPayVo.getTitle());
        	 		Map<String,String> map = liveTransferPayService.sendOrderQRCodeUrl(liveTransferPayVo);
        	 		respon.setData(map);
                } catch (ApplicationException e) {
                    e.printStackTrace();
                    respon.setResultCode(e.getErrorCode().getErrorCode());
                    respon.setResultMessage(e.getErrorCode().getErrorString());
                }
         }
         return respon;
     }
     
    /**
      * 支付成功回调接口
      * @author: duanmenrun
      * @Title: saveLiveFinanceDetail 
      * @Description: TODO 
      * @param liveTransferPayVo
      * @param request
      * @param response
      * @return
      */
     @RequestMapping(value="/saveLiveFinanceDetail")
     @ResponseBody
 	public Response saveLiveFinanceDetail(@ModelAttribute IntenetPayResponseParam param ){
    	 Response response = new Response();
    	 response = liveTransferPayService.saveLiveFinanceDetail(param);
    	 return response;
     }
    
     
    @RequestMapping(value="/getCampusIdListDetail")
    @ResponseBody
 	public Response getCampusIdListDetail(@RequestBody LiveCapmusVo vos , HttpServletRequest request, HttpServletResponse response){
    	Response respon = new Response(200,"");
        if (checkAuthorizationNoWriter(request, response)){
       	 	response.setStatus(200);
       	 	respon = organizationService.getCampusIdListDetail(vos);
        }
    	return respon;
     }
    
   /**
    * 实时同步直播  星火
    * @author: duanmenrun
    * @Title: syncActualLiveChange 
    * @Description: TODO 
    * @param vo
    * @param request 
    * @param response
    * @return
    */
    @RequestMapping(value="/syncActualLiveChange")
    @ResponseBody
	public Response syncActualLiveChange(@RequestBody LivePaymentRecordVo vo , HttpServletRequest request, HttpServletResponse response){
   	 	Response respon = new Response(200,"");
   	 	if (checkAuthorizationNoWriter(request, response)){
    	 	response.setStatus(200);
    	 	logger.info("直播实时同步参数："+vo.toString());
    	 	if(LiveFinanceType.INCOME == vo.getFinanceType() && StringUtils.isNotBlank(vo.getTransactionNum())) {
    	 		LiveTransferPay payInfo = liveTransferPayService.findByTransactionNum(vo.getTransactionNum());
    	 		if(payInfo!=null) {
    	 			vo.setReqsn(payInfo.getReqsn());
    	 			vo.setPayType(payInfo.getPayType());
    	 		}
    	 	}
    	 	
    	 	livePaymentRecordService.saveLivePaymentRecordVo(vo);
   	 	}
   	 	return respon;
    }
    /**
     * 培优直播营收
     * @author: duanmenrun
     * @Title: syncMonthLiveChange 
     * @Description: TODO 
     * @param vo
     * @param request 
     * @param response
     * @return
     */
    @RequestMapping(value="/syncMonthLiveChange")
    @ResponseBody
	public Response syncMonthLiveChange(@RequestBody List<LivePaymentRecordVo> vos , HttpServletRequest request, HttpServletResponse response){
   	 	Response respon = new Response(200,"");
   	 	if (checkAuthorizationNoWriter(request, response)){
    	 	response.setStatus(200);
    	 	livePaymentRecordService.saveMonthLiveChangeList(vos);
   	 	}
   	 	return respon;
    }
    
    /**
     * 分公司、校区列表
     * @author: duanmenrun
     * @Title: getAllBranchCampus
     * @Description: TODO
     * @throws
     * @param request
     * @param response 
     */
    @RequestMapping(value="/getAllBranchCampus")
    @ResponseBody
	public Response getAllBranchCampus(HttpServletRequest request, HttpServletResponse response){
    	Response respon = new Response(200,"");
        if (checkAuthorizationNoWriter(request, response)){
        	response.setStatus(200);
            List<OranizationRelationVo> list = organizationService.getAllBranchCampus();
            Map<String, Object> map = new HashMap<String, Object>();
    	 	map.put("list", list);
            respon.setData( map );
        }
        return respon;
    }
    
    /**
     * 查询通联支付状态，处理订单
     * @author: duanmenrun
     * @Title: handlePayStatus 
     * @Description: TODO 
     * @param vo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/handlePayStatus")
    @ResponseBody
	public Response handlePayStatus(@RequestBody MessageQueueDataVo vo,HttpServletRequest request, HttpServletResponse response){
    	Response respon = new Response(200,"");
    	logger.info("handlePayStatus Receive token: {" + request.getHeader("Authorization") +  "}");
        if (checkAuthorizationNoWriter(request, response)){
        	logger.info("handlePayStatus Receive message: {" + vo.toString() + "}");
        	respon = liveTransferPayService.handlePayStatus(vo);
        	response.setStatus(respon.getResultCode());
        }
        return respon;
    }
    
     /**
      * 鉴权方法
      * @param request
      * @param response
      * @return
      */
     public Boolean checkAuthorizationNoWriter(HttpServletRequest request,HttpServletResponse response){
         try{
             response.setCharacterEncoding("UTF-8");
             //PrintWriter out=response.getWriter();
             String authorization=request.getHeader("authorization");
             if(authorization==null||authorization.equals("")){
                 response.setStatus(401);
                 response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
                 //out.print("对不起你没有权限！！");
                 return false;
             }
             String userAndPass=new String(new BASE64Decoder().decodeBuffer(authorization.split(" ")[1]));
             if(userAndPass.split(":").length<2){
                 response.setStatus(401);
                 response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
                 //out.print("对不起你没有权限！！");
                 return false;
             }
             String user=userAndPass.split(":")[0];
             String pass=userAndPass.split(":")[1];
             if(user.equals(PropertiesUtils.getStringValue("LIVE_AUTH_USER"))&&pass.equals(PropertiesUtils.getStringValue("LIVE_AUTH_PWD"))){
                 return true;
             }else{
                 response.setStatus(401);
                 response.setHeader("WWW-authenticate","Basic realm=\"帐号密码不匹配\"");
                 //out.print("对不起你没有权限！！");
                 return false;
             }
         }catch(Exception ex){
             ex.printStackTrace();
             response.setStatus(401);
         }
         return false;
     }
}
