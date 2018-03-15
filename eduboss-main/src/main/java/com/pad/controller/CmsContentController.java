package com.pad.controller;


import com.eduboss.domain.DataDict;
import com.eduboss.domainVo.DataDictVo;
import com.eduboss.dto.Response;
import com.pad.dto.*;
import com.pad.entity.CmsContent;
import com.pad.service.CmsContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/cmsContent")
public class CmsContentController {

    @Autowired
    private CmsContentService cmsContentService;


    @RequestMapping(value = "/saveCmsContent", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsContent (@RequestBody  CmsContentVo vo){
        return cmsContentService.saveInfo(vo);
    }


    @RequestMapping(value = "/getCmsContentList", method =  RequestMethod.GET)
    @ResponseBody
    public List<CmsContentVo> getCmsContentList (CmsContentVo vo,CommonDto dto){
        return cmsContentService.getCmsContentList(vo,dto);
    }

    @RequestMapping(value = "/deleteCmsContentFile", method =  RequestMethod.POST)
    @ResponseBody
    public Response deleteCmsContentFile (@RequestParam Integer id){
        return cmsContentService.deleteCmsContentFile(id);
    }

    @RequestMapping(value = "/saveCmsSign", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsSign (@RequestBody CmsSignVo vo){
        return cmsContentService.saveCmsSign(vo);
    }

    @RequestMapping(value = "/saveMultiCmsSign", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveMultiCmsSign (@RequestBody List<CmsSignMultiVo> cmsSignVos){
        return  cmsContentService.saveMultiCmsSign(cmsSignVos);
    }

    @RequestMapping(value = "/deleteSignById", method =  RequestMethod.POST)
    @ResponseBody
    public Response deleteSignById (@RequestBody DataDictVo dataDict){
        return  cmsContentService.deleteSignById(dataDict);
    }

    @RequestMapping(value = "/deleteCmsSign", method =  RequestMethod.POST)
    @ResponseBody
    public Response deleteCmsSign (@RequestParam Integer id){
        return cmsContentService.deleteCmsSign(id);
    }

    @RequestMapping(value = "/saveCmsContentSign", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsContentSign (@RequestBody CmsContentSignVo vo){
        return cmsContentService.saveCmsContentSign(vo);
    }

    @RequestMapping(value = "/getCmsSignList", method =  RequestMethod.GET)
    @ResponseBody
    public List<CmsSignVo> getCmsSignList (CmsSignVo vo){
        return cmsContentService.getCmsSignList(vo);
    }


    @RequestMapping(value = "/saveCmsContentOrder", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsContentOrder (@RequestBody List<CmsContentVo> cmsContentVos){
        return  cmsContentService.saveCmsContentOrder(cmsContentVos);
    }

    @RequestMapping(value = "/saveCmsCotentAuth", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveCmsCotentAuth (@RequestBody CmsContentVo cmsContentVo){
        return  cmsContentService.saveCmsCotentAuth(cmsContentVo);
    }

    @RequestMapping(value = "/publishContent", method =  RequestMethod.POST)
    @ResponseBody
    public Response publishContent (@RequestBody List<CmsContentVo> cmsContentVo){
        return  cmsContentService.publishContent(cmsContentVo);
    }


    @RequestMapping(value = "/deleteContent", method =  RequestMethod.POST)
    @ResponseBody
    public Response deleteContent (@RequestParam Integer id){
        return  cmsContentService.deleteContent(id);
    }

    @RequestMapping(value = "/pushContentToFront", method =  RequestMethod.POST)
    @ResponseBody
    public Response pushContentToFront (@RequestParam Integer id){
        return  cmsContentService.pushContentToFront(id);
    }

    @RequestMapping(value = "/cacelContentToFront", method =  RequestMethod.POST)
    @ResponseBody
    public Response cacelContentToFront (@RequestParam Integer id){
        return  cmsContentService.cacelContentToFront(id);
    }


    @RequestMapping(value = "/copyContent", method =  RequestMethod.POST)
    @ResponseBody
    public Response copyContent (@RequestBody CmsCopyContentDto dto){
        return  cmsContentService.copyContent(dto);
    }

    @RequestMapping(value = "/getCmsContentById", method =  RequestMethod.GET)
    @ResponseBody
    public CmsContentVo getCmsContentById (@RequestParam Integer id){
        return cmsContentService.getCmsContentById(id);
    }


}
