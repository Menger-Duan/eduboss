package com.eduboss.interceptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/**
 * 过滤器把请求流保存起来
 *
 */
//@WebFilter(value = {"/*"})
public class BodyReaderFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		
		//只处理json post的情况
        if("POST".equals(httpServletRequest.getMethod().toUpperCase()) && 
                StringUtils.contains(httpServletRequest.getHeader("Content-Type"), "application/json")) {
            // 防止流读取一次后就没有了, 所以需要将流继续写出去
            ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
            chain.doFilter(requestWrapper, response);
        }else {
            chain.doFilter(request, response);
        }
	}

	@Override
	public void destroy() {
		
	}

	/**
	 * 保存流
	 * 
	 * @author lyj 2015年12月16日
	 */
	public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {
		private Logger log = LoggerFactory.getLogger(this.getClass());

		private final byte[] body;

		public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
			super(request);
			body = IOUtils.toByteArray(request.getInputStream());
			log.debug("保存的流size:{}", body.length);
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream()));
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {

			final ByteArrayInputStream bais = new ByteArrayInputStream(body);

			return new ServletInputStream() {

				@Override
				public int read() throws IOException {
					return bais.read();
				}

			};
		}
	}
}