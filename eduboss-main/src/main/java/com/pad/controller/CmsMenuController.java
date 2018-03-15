package com.pad.controller;


import com.eduboss.domain.Organization;
import com.eduboss.dto.Response;
import com.pad.dto.CmsMenuAuthVo;
import com.pad.dto.CmsMenuIcoVo;
import com.pad.dto.CmsMenuVo;
import com.pad.entity.CmsMenu;
import com.pad.entity.CmsMenuAuth;
import com.pad.entity.CmsMenuIco;
import com.pad.service.CmsMenuIcoService;
import com.pad.service.CmsMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Controller
@RequestMapping("/cmsMenu")
public class CmsMenuController {

    @Autowired
    private CmsMenuService cmsMenuService;
    @Autowired
    private CmsMenuIcoService cmsMenuIcoService;


    @RequestMapping(value = "/saveCmsMenu", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsMenu (@RequestBody CmsMenuVo cmsMenu){
        return  cmsMenuService.saveCmsMenu(cmsMenu);
    }

    @RequestMapping(value = "/getCmsMenuList", method =  RequestMethod.GET)
    @ResponseBody
    public List<CmsMenuVo> getCmsMenuList (@ModelAttribute  CmsMenuVo cmsMenu){
        return  cmsMenuService.getCmsMenuList(cmsMenu);
    }

    @RequestMapping(value = "/setShowMenuContent", method =  RequestMethod.POST)
    @ResponseBody
    public Response setShowMenuContent (@RequestBody CmsMenuVo cmsMenu){
        return  cmsMenuService.setShowMenuContent(cmsMenu);
    }


    @RequestMapping(value = "/saveCmsMenuAuth", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsMenuAuth (@RequestBody CmsMenuVo cmsMenu){
        return  cmsMenuService.saveCmsMenuAuth(cmsMenu);
    }

    @RequestMapping(value = "/deleteCmsMenu", method =  RequestMethod.POST)
    @ResponseBody
    public Response deleteCmsMenu (@RequestParam Integer id){
        return  cmsMenuService.deleteCmsMenu(id);
    }


    @RequestMapping(value = "/saveCmsMenuOrder", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsMenuOrder (@RequestBody List<CmsMenuVo> cmsMenu){
        return  cmsMenuService.saveCmsMenuOrder(cmsMenu);
    }

    @RequestMapping(value = "/saveCmsMenuIco", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsMenuIco (@RequestBody CmsMenuIcoVo vo){
        return  cmsMenuIcoService.saveCmsMenuIco(vo);
    }

    @RequestMapping(value = "/publicMenuIco", method =  RequestMethod.POST)
    @ResponseBody
    public Response publicMenuIco (@RequestBody CmsMenuIcoVo vo){
        return  cmsMenuIcoService.publicMenuIco(vo);
    }


    @RequestMapping(value = "/getMenuIcoList", method =  RequestMethod.GET)
    @ResponseBody
    public List<CmsMenuIcoVo> getMenuIcoList (@ModelAttribute CmsMenuIcoVo vo){
        return  cmsMenuIcoService.getMenuIcoList(vo);
    }


    @RequestMapping(value = "/getAllBrenchAndGroup", method =  RequestMethod.GET)
    @ResponseBody
    public List<Organization> getAllBrenchAndGroup (){
        return  cmsMenuService.getAllBrenchAndGroup();
    }

    @RequestMapping(value = "/cacelMenuBySelf", method =  RequestMethod.POST)
    @ResponseBody
    public Response cacelMenuBySelf (@RequestParam Integer id,@RequestParam String branchId){
        return  cmsMenuService.cacelMenuBySelf(id,branchId);
    }

    /**
     * 设置图文排版个数
     * @param id
     * @param showContentNum
     * @return
     */
    @RequestMapping(value = "/saveCmsMenuContentNum", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsMenuContentNum (@RequestParam Integer id,@RequestParam Integer showContentNum){
        return  cmsMenuService.saveCmsMenuContentNum(id,showContentNum);
    }

}
