package com.eduboss.controller;

import java.lang.reflect.Method;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;


@Entity
public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	/** serialVersionUID */
	@GeneratedValue
	private static final long serialVersionUID = -4863333241280926659L;

	/** HttpServletRequest */
	protected HttpServletRequest request;
	
	/** HttpServletResponse */
	protected HttpServletResponse response;
	
	//分页控制
	protected String pageSize;
	protected String pageNum;
	private String rows;
    private String page;
    
    //pagination for jqgrid
	private String sord;  
	private String sidx;
	private String oper;

	protected DataPackage dataPackage;
	
	protected Response simpleRespose = new Response();
	
    //初始化分页参数
	public DataPackage getDataPackage() {
		if (dataPackage == null) {
			dataPackage = new DataPackage();
		}
		if (StringUtil.isNotBlank(pageSize) && StringUtil.isNotBlank(pageNum)) {
			dataPackage.setPageNo(StringUtil.convertToInt(this.pageNum, 0));
			dataPackage.setPageSize(StringUtil.convertToInt(this.pageSize, 0));
		} else if (StringUtil.isNotBlank(rows) && StringUtil.isNotBlank(page)) {
			int pageNum = StringUtil.convertToInt(this.page, 0) - 1;
			if (pageNum < 0) {
				pageNum = 0;
			}
			dataPackage.setPageNo(pageNum);
			dataPackage.setPageSize(StringUtil.convertToInt(this.rows, 0));
		} else {
			dataPackage.setPageNo(0);
			dataPackage.setPageSize(15);
		}
		
		dataPackage.setSidx(sidx);
		dataPackage.setSord(sord);
		
		return dataPackage;
	}
	
	//把参数绑定到对象中
		public Object bindObjectProperties(Object object) {
			
			Map<String, String[]> paraMap = request.getParameterMap();
			if (paraMap != null) {
				for (String key : paraMap.keySet()) {
					if (StringUtils.isBlank(paraMap.get(key)[0])) {
						continue;
					}
					StringBuilder sb = new StringBuilder();
					String firstWord = key.substring(0, 1).toUpperCase();
				    sb.append(firstWord);
				    sb.append(key.substring(1, key.length()));
				    final String methodName = "set" + sb.toString();
				    
					try {
						for (Method method : object.getClass().getDeclaredMethods()) {
							Class<?>[] parameterTypes = method.getParameterTypes();
							if (methodName.equals(method.getName())) {
								if (parameterTypes[0].getSimpleName().equalsIgnoreCase("String")) {
									method.invoke(object, new String(paraMap.get(key)[0]));
								} else if (parameterTypes[0].getSimpleName().equalsIgnoreCase("Integer")) {
									method.invoke(object, new Integer(paraMap.get(key)[0]));
								}
						    }
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			
			return object;
		}

	public void setdataPackage(DataPackage page) {
		this.dataPackage = page;
	}
	
	
	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	
	/**
	 * @param request the request to set
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	
	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	
	/**
	 * @param response the response to set
	 */
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * ����ÿҳ����
	 * @return
	 */
	public int getPageSize() {
		return StringUtil.convertToInt(this.pageSize, 15);
	}

	
	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	
	/**
	 * ����ÿҳ����
	 * @return
	 */
	public int getPageNum() {
		return StringUtil.convertToInt(this.pageNum, 0);
	}

	
	/**
	 * @param rows the rows to set
	 */
	public void setRows(String rows) {
		this.rows = rows;
	}

	
	/**
	 * @param page the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}

	
	/**
	 * @return the simpleRespose
	 */
	public Response getSimpleRespose() {
		return simpleRespose;
	}

	
	/**
	 * @param simpleRespose the simpleRespose to set
	 */
	public void setSimpleRespose(Response simpleRespose) {
		this.simpleRespose = simpleRespose;
	}

	/**
	 * @return the sord
	 */
	public String getSord() {
		return sord;
	}

	/**
	 * @param sord the sord to set
	 */
	public void setSord(String sord) {
		this.sord = sord;
	}

	/**
	 * @return the sidx
	 */
	public String getSidx() {
		return sidx;
	}

	/**
	 * @param sidx the sidx to set
	 */
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	/**
	 * @return the oper
	 */
	public String getOper() {
		return oper;
	}

	/**
	 * @param oper the oper to set
	 */
	public void setOper(String oper) {
		this.oper = oper;
	}

}
