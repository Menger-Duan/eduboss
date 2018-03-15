package com.eduboss.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;

/**
 * Created by Administrator on 2017/5/26.
 */
public class UrlRewriteFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;  
        HttpServletResponse httpResponse = (HttpServletResponse) response;  
        String requestPath = httpRequest.getRequestURI();
        if(requestPath.indexOf("/login.jsp")>0) {
            String loginUrl = PropertiesUtils.getStringValue("filter.url.login");
            if (StringUtil.isNotBlank(loginUrl)) {
                httpResponse.sendRedirect(loginUrl);  
            } else {
                chain.doFilter(request, response);
            }
            return;  
        }
        if(requestPath.indexOf("/phoneCustomer.html")>0) {
            Enumeration em = httpRequest.getParameterNames();
            String params = "";
            while (em.hasMoreElements()) {
                String name = (String) em.nextElement();
                params += name + "=";
                params += httpRequest.getParameter(name);
                params += "&";
            }
            if (params.length() > 0) {
                params = params.substring(0, params.length() - 1);
                params = "?" + params;
            }
            httpResponse.sendRedirect(PropertiesUtils.getStringValue("filter.url.ip") + "/eduboss/phoneCustomer.html" + params);   
            return;  
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
}
