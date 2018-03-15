package com.eduboss.logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * hmtl缓存过滤器
 * @author lixuejun
 * @version v1.0
 * 2016-05-31
 *
 */
public class HtmlCachedFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		//loggerThreadPool.shutdown();

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 首页强制不缓存
        if(request.getRequestURI().contains("index.html")) {
            response.addHeader("Expires","0");
            response.addHeader("pragma","no-cache");
            response.addHeader("Cache-Control","no-cache");
        }else{ 
            response.addHeader("Cache-Control","max-age=0");
        }
        filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
