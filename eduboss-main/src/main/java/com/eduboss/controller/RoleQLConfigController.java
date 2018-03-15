package com.eduboss.controller;

import com.eduboss.domain.RoleQLConfig;
import com.eduboss.dto.*;
import com.eduboss.service.RoleQLConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Administrator on 2014/12/11.
 */
@RequestMapping(value = "/RoleQLConfigController")
@Controller
public class RoleQLConfigController {

    @Autowired
    private RoleQLConfigService roleQLConfigService;

    @RequestMapping("/save")
    @ResponseBody
    public Response save(RoleQLConfig roleQLConfig){
        roleQLConfigService.save(roleQLConfig);
        return new Response();
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Response delete(@RequestParam String id){
        RoleQLConfig r = new RoleQLConfig();
        r.setId(id);
        roleQLConfigService.delete(r);
        return new Response();
    }

    @RequestMapping("findPage")
    @ResponseBody
    public DataPackageForJqGrid findPage(GridRequest gridRequest, RoleQLConfigVo roleQLConfigVo) {
        DataPackage dp = new DataPackage(gridRequest);
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
        roleQLConfigVo.setExpressions(new ArrayList<String>());
        for(Object paramName : request.getParameterMap().keySet()){
            if(paramName.toString().startsWith("filter_") && StringUtils.isNotBlank(request.getParameter(paramName.toString()))){
                roleQLConfigVo.getExpressions().add(paramName.toString().substring("filter_".length()) + "=" + request.getParameter(paramName.toString()));
            }
        }
        return new DataPackageForJqGrid(roleQLConfigService.findPage(dp,roleQLConfigVo));
    }

    @RequestMapping("findById")
    @ResponseBody
    public RoleQLConfig findById(@RequestParam String id) {
        return roleQLConfigService.findById(id);
    }

}
