package com.pad.controller;


import com.eduboss.common.OrganizationType;
import com.eduboss.domain.Organization;
import com.eduboss.dto.Response;
import com.eduboss.service.CommonService;
import com.eduboss.utils.TokenUtil;
import com.pad.common.CmsContentStatus;
import com.pad.dto.*;
import com.pad.entity.CmsMenu;
import com.pad.service.CmsContentService;
import com.pad.service.CmsMenuIcoService;
import com.pad.service.CmsMenuService;
import com.pad.service.UserIpadPwdService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Controller
@RequestMapping("/ipad")
public class IpadController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserIpadPwdService userIpadPwdService;

    @Autowired
    private CmsMenuService cmsMenuService;

    @Autowired
    private CmsContentService cmsContentService;

    @RequestMapping(value ="/ipadLogin")
    @ResponseBody
    public IpadLoginDto ipadLogin(@RequestParam String account, @RequestParam String passwordMd5) {
        return commonService.ipadLogin(account, passwordMd5);
    }

    @RequestMapping(value ="/setPadPassword",method = RequestMethod.POST)
    @ResponseBody
    public Response setPadPassword(@RequestParam String userId, @RequestParam String password,@RequestParam String token) {
        if(!TokenUtil.checkToken(token)){
            return new Response(1204,"token失效");
        }
        return userIpadPwdService.setNewPadPassword(userId, password);
    }

    @RequestMapping(value ="/checkPadPwd",method = RequestMethod.POST)
    @ResponseBody
    public Response checkPadPwd(@RequestParam String userId, @RequestParam String password,@RequestParam String token) {
        if(!TokenUtil.checkToken(token)){
            return new Response(1204,"token失效");
        }
        return userIpadPwdService.checkPadPwd(userId, password);
    }

    @RequestMapping(value = "/getCmsMenuList", method =  RequestMethod.GET)
    @ResponseBody
    public Response getCmsMenuList (@ModelAttribute  CmsMenuVo cmsMenu){
        Response res= new Response();
        res.setData(cmsMenuService.getCmsMenuListForIpad(cmsMenu));
        return  res;
    }

    @RequestMapping(value = "/getIpadContentUnLogin", method =  RequestMethod.GET)
    @ResponseBody
    public Response getIpadContentUnLogin (CmsContentVo vo, CommonDto dto){
        if(dto.getSize()==null || dto.getSize()<=0){
            dto.setSize(4);
        }
        if(dto.getPage()==null || dto.getPage()<=0){
            dto.setPage(1);
        }
        Response res = new Response();
        vo.setStatus(CmsContentStatus.PUBLISH);
        if(vo.getOrgType()==null) {
            vo.setOrgType(OrganizationType.GROUNP);//默认是集团的
        }
        if(StringUtils.isBlank(dto.getOrderBy()) && vo.getIsFront()!=null && vo.getIsFront()==0){//首页推荐 默认发布时间排序
            dto.setOrderBy("publish_time");
            dto.setOrderType("desc");
        }
        res.setData(cmsContentService.getIpadCmsContentList(vo,dto));
        return res;
    }

    @RequestMapping(value = "/getIpadContentByLogin", method =  RequestMethod.GET)
    @ResponseBody
    public Response getIpadContentByLogin (CmsContentVo vo, CommonDto dto,@RequestParam String token){
        Response res = new Response();
        if(!TokenUtil.checkToken(token)){
            res.setResultCode(1204);
            res.setResultMessage("token失效");
            return res;
        }
        if(dto.getSize()==null || dto.getSize()<=0){
            dto.setSize(4);
        }
        if(dto.getPage()==null || dto.getPage()<=0){
            dto.setPage(1);
        }
        if(StringUtils.isBlank(dto.getOrderBy()) && vo.getIsFront()!=null && vo.getIsFront()==0){//首页推荐 默认发布时间排序
            dto.setOrderBy("publish_time");
            dto.setOrderType("desc");
        }
        vo.setStatus(CmsContentStatus.PUBLISH);
        res.setData(cmsContentService.getIpadCmsContentList(vo,dto));
        return res;
    }


    @RequestMapping(value = "/getCmsContentById", method =  RequestMethod.GET)
    @ResponseBody
    public Response getCmsContentById (@RequestParam Integer id,@RequestParam String token,HttpServletRequest request){
        Response res = new Response();
        if(!TokenUtil.checkToken(token)){
            res.setResultCode(1204);
            res.setResultMessage("token失效");
            return res;
        }
        CmsContentVo vo = cmsContentService.getCmsContentById(id);
        if(vo!=null) {
            res.setData(vo);
        }else{
            res.setResultMessage("找不到对应的信息");
            res.setResultCode(-1);
        }
        return res;
    }

    @RequestMapping(value = "/getCmsSignList", method =  RequestMethod.GET)
    @ResponseBody
    public Response getCmsSignList (CmsSignVo vo){
        Response res = new Response();
        res.setData(cmsContentService.getCmsSignList(vo));
        return res;
    }


    @RequestMapping(value = "/getBrenchAndGroup", method =  RequestMethod.GET)
    @ResponseBody
    public Response getBrenchAndGroup (@RequestParam String token){
        Response res = new Response();
        if(!TokenUtil.checkToken(token)){
            res.setResultCode(1204);
            res.setResultMessage("token失效");
            return res;
        }
        return  cmsMenuService.getBrenchAndGroup();
    }

    @RequestMapping(value = "/getCmsContentByIdUnLogin", method =  RequestMethod.GET)
    @ResponseBody
    public Response getCmsContentByIdUnLogin (@RequestParam Integer id,String token,HttpServletRequest request){
        Response res = new Response();
        if(StringUtils.isNotBlank(token) && !TokenUtil.checkToken(token)){
            res.setResultCode(1204);
            res.setResultMessage("token失效");
            return res;
        }
        CmsContentVo vo = cmsContentService.getCmsContentById(id);
        if(vo!=null) {
            res.setData(vo);
        }else{
            res.setResultMessage("找不到对应的信息");
            res.setResultCode(-1);
        }
        return res;
    }

    @RequestMapping(value = "/addContentVisitNum", method =  RequestMethod.POST)
    @ResponseBody
    public Response addContentVisitNum (@RequestParam Integer id,String token){
        Response res = new Response();
        if(StringUtils.isNotBlank(token) && !TokenUtil.checkToken(token)){
            res.setResultCode(1204);
            res.setResultMessage("token失效");
            return res;
        }

        return cmsContentService.addContentVisitNum(id);
    }




}
