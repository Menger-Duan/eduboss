package com.eduboss.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eduboss.common.StateOfEmergency;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.xmlbeans.impl.util.Base64;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.aliyun.oss.common.utils.HttpHeaders;
import com.eduboss.common.OrganizationType;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.Organization;
import com.eduboss.service.MailService;
import com.eduboss.service.OperationCountService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.UserService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HttpClientUtils;
import com.eduboss.utils.Pinyin4jUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.eduboss.utils.WebClientDevWrapper;


@Component
@Scope("prototype")
public class SycInfoThread implements Runnable {
    static private OrganizationDao organizationDao = (OrganizationDao) ApplicationContextUtil.getContext().getBean(OrganizationDao.class);
    
    static private OperationCountService operationCountService = (OperationCountService) ApplicationContextUtil.getContext().getBean(OperationCountService.class);

    static private UserService userService = (UserService) ApplicationContextUtil.getContext().getBean(UserService.class);
    
    static private OrganizationService organizationService = (OrganizationService) ApplicationContextUtil.getContext().getBean(OrganizationService.class);
    
    static private MailService mailService = (MailService) ApplicationContextUtil.getContext().getBean(MailService.class);
    
    private Organization org;

    private static final Log log = LogFactory.getLog(SycInfoThread.class);
    
    private String orgId;
    private boolean isCreated = false;

    public SycInfoThread() {
    }

    
    public SycInfoThread(JSONObject info) {
    	this.orgId = info.getString("id");
    	if(StringUtils.isNotEmpty(info.getString("id"))){
    		org=organizationDao.findById(info.getString("id"));
    		if(org==null){
    			org= new Organization();
    			org.setId(info.getString("id"));
				org.setStateOfEmergency(StateOfEmergency.NORMAL);
    			this.setCreated(true);
    		}
    	}else{
    		org= new Organization();
    		org.setId(info.getString("id"));
			org.setStateOfEmergency(StateOfEmergency.NORMAL);
    	}
    	org.setName(info.getString("name"));
		org.setParentId((getRealString(info.get("parentId")).equals("null")|| StringUtil.isBlank(getRealString(info.get("parentId"))))?null:getRealString(info.get("parentId")));
		org.setRemark((getRealString(info.get("remark")).equals("null")|| StringUtil.isBlank(getRealString(info.get("remark"))))?null:getRealString(info.get("remark")));
		org.setOrgLevel((getRealString(info.get("orgLevel")).equals("null")|| StringUtil.isBlank(getRealString(info.get("orgLevel"))))?null:getRealString(info.get("orgLevel")));
    	org.setOrgType(OrganizationType.valueOf(info.getString("orgType")));
//		org.setOrgSign((getRealString(info.get("orgSign")).equals("null")|| StringUtil.isBlank(getRealString(info.get("orgSign"))))?null:getRealString(info.get("orgSign")));
		org.setOrgSign(userService.getUniqueOrgSign(Pinyin4jUtil.getFirstSpell(org.getName())));
		
		org.setAddress((getRealString(info.get("address")).equals("null")|| StringUtil.isBlank(getRealString(info.get("address"))))?null:getRealString(info.get("address")));
		org.setLon((getRealString(info.get("lon")).equals("null")|| StringUtil.isBlank(getRealString(info.get("lon"))))?null:getRealString(info.get("lon")));
		org.setLat((getRealString(info.get("lat")).equals("null")|| StringUtil.isBlank(getRealString(info.get("lat"))))?null:getRealString(info.get("lat")));
    	org.setRegionId(info.getString("regionId"));
		org.setContact((getRealString(info.get("contact")).equals("null")|| StringUtil.isBlank(getRealString(info.get("contact"))))?null:getRealString(info.get("contact")));
		org.setProvinceId((getRealString(info.get("provinceId")).equals("null")|| StringUtil.isBlank(getRealString(info.get("provinceId"))))?null:getRealString(info.get("provinceId")));
		org.setCityId((getRealString(info.get("cityId")).equals("null")|| StringUtil.isBlank(getRealString(info.get("cityId"))))?null:getRealString(info.get("cityId")));
		org.setAreaId((getRealString(info.get("areaId")).equals("null")|| StringUtil.isBlank(getRealString(info.get("areaId"))))?null:getRealString(info.get("areaId")));
    }
    
    public static void main(String[] args) {
		
	}

    @Override
    public void run() {
        try {
			organizationDao.save(org);
			if (mailService.isMailSysInUse()) {
			    if(isCreated) { //开启邮件系统
			        //同步组织到邮件系统
			        mailService.createMailUnit(org);
			    } else {
		            //更新邮件系统的部门名称
	                mailService.updateUnitName(org.getId(), org.getName());
		            //更新排序
		            if(org.getOrgOrder() != null){
		                mailService.updateMailUnitOrder(org.getId(), org.getOrgOrder());
		            }
			    }
            }
			organizationDao.flush();
			clearOrgSycData(org.getId());
			if(this.isCreated() && org.getOrgType() == OrganizationType.CAMPUS){
				String currentMonth = DateTools.getCurrentDate().substring(0, 4) + DateTools.getCurrentDate().substring(5,7);
				String nextMonth = DateTools.getNextMonth(DateTools.getCurrentDate()).substring(0, 4) 
						+  DateTools.getNextMonth(DateTools.getCurrentDate()).substring(5,7);
				operationCountService.createCampusSubejctGroup(org.getParentId(), org.getId(), Integer.parseInt(currentMonth), Integer.parseInt(nextMonth));
				organizationService.saveOrUpdateSchoolArea(org);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    public static Boolean clearOrgSycData(String id) throws ClientProtocolException, IOException{
		String url = PropertiesUtils.getStringValue("OA_HOST")+"/clearOrgSycData";
		HttpClient client = wrapHttpClient();
		HttpPost postrequet = new HttpPost(url);
		setRequestHeader(postrequet);
	    List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
        nvps.add(new BasicNameValuePair("ids", id));  
        postrequet.setEntity(new UrlEncodedFormEntity(nvps)); 
		
		HttpResponse getResponse = client.execute(postrequet);
		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
          return true;
        }
		
		return false;
	}
    
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	private static HttpClient wrapHttpClient()
	{
		HttpClient client = HttpClientUtils.getHttpClient();
		client = WebClientDevWrapper.wrapClient(client);
		return client;
	}
	
	
	public String getRealString(Object info){
		if(info==null){
			return null;
		}
		return info.toString();
	}
	
	private static void setRequestHeader(HttpPost postRequest){
		String auth = PropertiesUtils.getStringValue("API_USER")+":" +  PropertiesUtils.getStringValue("API_PWD");
		byte[] encodedAuth = Base64.encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		postRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
	}
	
	
}
