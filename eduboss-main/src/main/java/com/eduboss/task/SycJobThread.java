package com.eduboss.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.eduboss.dao.UserJobDao;
import com.eduboss.domain.UserJob;
import com.eduboss.service.UserJobService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.HttpClientUtils;
import com.eduboss.utils.Pinyin4jUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.WebClientDevWrapper;

@Component
@Scope("prototype")
public class SycJobThread implements Runnable {
	
	static private UserJobService userJobService = (UserJobService) ApplicationContextUtil.getContext().getBean(UserJobService.class);
	
    static private UserJobDao userJobDao = (UserJobDao) ApplicationContextUtil.getContext().getBean(UserJobDao.class);

    private UserJob job;

    private static final Log log = LogFactory.getLog(SycJobThread.class);

    public SycJobThread() {
    }

    
    public SycJobThread(JSONObject info) {
    	if(StringUtils.isNotEmpty(info.getString("id"))){
    		job=userJobDao.findById(info.getString("id"));
    		if(job==null){
    			job= new UserJob();
        		job.setId(info.getString("id"));
    		}
    	}else{
    		job= new UserJob();
    		job.setId(info.getString("id"));
    	}
    	job.setJobName(info.getString("name"));
    	job.setJobSign(userJobService.getUniqueJobSign(Pinyin4jUtil.getFirstSpell(info.getString("name"))));
    	job.setFlag(info.getInt("flag"));
    	job.setRemark(getRealString(info.get("remark").toString()));
    	job.setUserCount(info.getInt("userCount"));
    }

    @Override
    public void run() {
		log.info(job.getName());
    	userJobDao.save(job);
    	userJobDao.commit();
        try {
			clearOrgSycData(job.getId());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    public static Boolean clearOrgSycData(String id) throws ClientProtocolException, IOException{
		String url = PropertiesUtils.getStringValue("OA_HOST")+"/clearStationSycData";
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
 	
	private static HttpClient wrapHttpClient()
	{
		HttpClient client = HttpClientUtils.getHttpClient();
		client = WebClientDevWrapper.wrapClient(client);
		return client;
	}
	
	
	public String getRealString(String info){
		if(StringUtils.isEmpty(info) || info.equals("null")){
			return null;
		}
		return info;
	}
	
	private static void setRequestHeader(HttpPost postRequest){
		String auth = PropertiesUtils.getStringValue("API_USER")+":" +  PropertiesUtils.getStringValue("API_PWD");
		byte[] encodedAuth = Base64.encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		postRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
	}
}
