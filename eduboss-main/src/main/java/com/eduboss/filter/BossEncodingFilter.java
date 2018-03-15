package com.eduboss.filter;

import org.apache.log4j.Logger;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/5/26.
 */
public class BossEncodingFilter extends CharacterEncodingFilter{

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri=request.getRequestURI();
        if(uri.contains("BillPayController/saveResultNotify")){
            request.setCharacterEncoding("gbk");
            response.setCharacterEncoding("gbk");
            filterChain.doFilter(request, response);
        }else {
            super.doFilterInternal(request,response,filterChain);
        }
    }
}
