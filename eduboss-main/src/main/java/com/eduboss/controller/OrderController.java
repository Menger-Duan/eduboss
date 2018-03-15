package com.eduboss.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.boss.rpc.base.dto.OrderOrgRpcVo;
import org.boss.rpc.mobile.service.BossOrderRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eduboss.common.Constants;
import com.eduboss.dto.MessageQueueDataVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.OrderService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.Base64Util;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.PropertiesUtils;


@Controller
@RequestMapping(value = "/OrderController")
public class OrderController {
    
    private final static Logger logger = Logger.getLogger(OrderController.class);
    
    @Autowired
    private OrderService orderService;
    
    @Resource(name = "bossOrderRpc")
    private BossOrderRpc bossOrderRpc;
    
    /**
     * 付款成功订单入库boss
     * @param MessageQueueDataVo
     * @return
     */
    @RequestMapping("/storageOrder")
    public void storageOrder(@RequestBody MessageQueueDataVo vo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("storageOrder Receive message: {" + vo.toString() + "}");
        logger.info("storageOrder Receive token: {" + request.getHeader("Authorization") +  "}");
        if(Base64Util.checkAuthorization(request, response)){
            String redisKey = Constants.STORAGE_ORDER + vo.getData();
            OrderOrgRpcVo orgRpcVo = bossOrderRpc.findOrderOrgRpcByOrderNo(vo.getData());
            if (orgRpcVo == null) {
                throw new ApplicationException("找不到订单:" + vo.getData());
            }
            if (!orgRpcVo.getOrgSysType().equalsIgnoreCase(PropertiesUtils.getStringValue("institution"))) {
                return;
            }
            if (!JedisUtil.exists(redisKey.getBytes())) {
                JedisUtil.set(redisKey.getBytes(), redisKey.getBytes(), 5);
                this.setLoginUser(request);
                orderService.storageOrder(vo.getData());
                JedisUtil.del(redisKey.getBytes());
            }
        }
    }
    
    private void setLoginUser(HttpServletRequest request) {
        //从spring容器中获取UserDetailsService(这个从数据库根据用户名查询用户信息,及加载权限的service)  
        UserDetailsService userDetailsService =   
              (UserDetailsService)ApplicationContextUtil.getContext().getBean("userDetailsService");  
        String userName = "xhadmin";
        if (PropertiesUtils.getStringValue("institution").equals("advance")) {
            userName = "aplusadmin";
        }
        //根据用户名username加载userDetails  
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);  
          
        //根据userDetails构建新的Authentication,这里使用了  
        //PreAuthenticatedAuthenticationToken当然可以用其他token,如UsernamePasswordAuthenticationToken                 
        PreAuthenticatedAuthenticationToken authentication =   
              new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());  
          
        //设置authentication中details  
        authentication.setDetails(new WebAuthenticationDetails(request));  
          
        //存放authentication到SecurityContextHolder  
        SecurityContextHolder.getContext().setAuthentication(authentication);  
        HttpSession session = request.getSession(true);
        //在session中存放security context,方便同一个session中控制用户的其他操作  
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }
    
    /**
     * 订单退款调用boss退费
     * @param vo
     * @param request
     * @param response
     */
    @RequestMapping("/surrenderOrder")
    public void surrenderOrder(@RequestBody MessageQueueDataVo vo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("surrenderOrder Receive message: {" + vo.toString() + "}");
        logger.info("surrenderOrder Receive token: {" + request.getHeader("Authorization") +  "}");
        if(Base64Util.checkAuthorization(request, response)){
            String redisKey = Constants.SURRENDER_ORDER + vo.getData();
            OrderOrgRpcVo orgRpcVo = bossOrderRpc.findOrderOrgRpcByOrderNo(vo.getData());
            if (orgRpcVo == null) {
                throw new ApplicationException("找不到订单:" + vo.getData());
            }
            if (!orgRpcVo.getOrgSysType().equalsIgnoreCase(PropertiesUtils.getStringValue("institution"))) {
                return;
            }
            if (!JedisUtil.exists(redisKey.getBytes())) {
                JedisUtil.set(redisKey.getBytes(), redisKey.getBytes(), 5);
                this.setLoginUser(request);
                orderService.surrenderOrder(vo.getData());
                JedisUtil.del(redisKey.getBytes());
            }
        }
    }
    
}
