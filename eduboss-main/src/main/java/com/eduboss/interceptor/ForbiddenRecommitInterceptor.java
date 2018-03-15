package com.eduboss.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.eduboss.exception.ApplicationException;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.MD5;




public class ForbiddenRecommitInterceptor extends HandlerInterceptorAdapter {
    
    private Logger logger = LoggerFactory.getLogger(ForbiddenRecommitInterceptor.class);
    //默认POST方法重复提交的间隔控制1秒
    private static final Integer DEFAULT_REPEAT_COMMIT_INTEVAL = 2;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        logger.debug("RequestCheck Start");
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ForbiddenRecommit annotation = method.getAnnotation(ForbiddenRecommit.class);
            //判断注解
            if (annotation != null) {
                return checkRepeatCommit(request, annotation.value());
            }
            //判断(POST)方法
            RequestMapping anoRequest = method.getAnnotation(RequestMapping.class);
            if(null != anoRequest && ArrayUtils.contains(anoRequest.method(), RequestMethod.POST)) {
                return checkRepeatCommit(request, DEFAULT_REPEAT_COMMIT_INTEVAL);
            }
            
        }
        return super.preHandle(request, response, handler); 
    }

    private static final String IGNORE_PARAM_1 = "monitoringTime";
    public boolean checkRepeatCommit(HttpServletRequest request, int expiration) throws IOException {
        //先获取get post的提交数据
        Map<String, String[]> params = request.getParameterMap();
        //boss有monitoringTime，剔除掉
        if(params.containsKey(IGNORE_PARAM_1)) {
            params = Maps.newHashMap(params);
            params.remove(IGNORE_PARAM_1);
        }
        String body = null;
        //上次文件类型重复判断
        StringBuilder sb = new StringBuilder();
        if(request instanceof MultipartHttpServletRequest) {
            Map<String,MultipartFile> files = ((MultipartHttpServletRequest) request).getFileMap();
            for(Entry<String,MultipartFile> mf: files.entrySet()) {
                sb.append(mf.getValue().getOriginalFilename());
            }
        }else{
          //再处理json post的数据，需要注意处理inputStream只能读取一次
            body = new String(IOUtils.toByteArray(request.getInputStream()), request.getCharacterEncoding());
        }
        
        //转换为string数据
        String requestStr = JSONObject.toJSONString(params);
        if(StringUtils.isNotBlank(body) || StringUtils.isNotBlank(requestStr)) {
            String uniqueKey = request.getSession().getId() + request.getRequestURI() + body + requestStr + sb.toString();
            
            //当前session用户的同一个接口的提交数据不能重复
            String redisKey = MD5.getMD5(uniqueKey);
            // redis 的set get TODO
            String keys = JedisUtil.get(redisKey);
            if(keys != null && keys.length()>0) {
                logger.info("isRepeatCommit: uniqueKey:{}", uniqueKey);
                throw new ApplicationException(String.format("重复请求，请等待:%s秒再提交", expiration));
            }
            JedisUtil.set(redisKey, "1", expiration);
        }

        return true;
    }

   
    


}



