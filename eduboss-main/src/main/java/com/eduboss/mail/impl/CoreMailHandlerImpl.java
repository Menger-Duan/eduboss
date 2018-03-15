package com.eduboss.mail.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.ObjectPool;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import tebie.applib.api.APIContext;
import tebie.applib.api.IClient;

import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.mail.MailHandler;
import com.eduboss.mail.pojo.MailBoxMsg;
import com.eduboss.mail.pojo.MailExternalContract;
import com.eduboss.mail.pojo.MailInfo;
import com.eduboss.mail.pojo.MailInfoView;
import com.eduboss.mail.pojo.MailList;
import com.eduboss.mail.pojo.MailUnit;
import com.eduboss.mail.pojo.MailUser;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;

/**@author wmy
 *@date 2015年9月24日上午11:39:03
 *@version 1.0 
 *@description
 */
public class CoreMailHandlerImpl extends BaseMailHandler implements MailHandler{
	
	private static Log log = LogFactory.getLog(CoreMailHandlerImpl.class);
	
	private ObjectPool<IClient> clientPool = null;
	
	public CoreMailHandlerImpl(String coreMailIp, String coreMailPort){
		super(coreMailIp, coreMailPort);
		initClientPool();
	}

	private void initClientPool(){
		if(clientPool == null){
			clientPool = CoreMailClientPool.getInstrance(this.getCoreMailIp(), Integer.valueOf(getCoreMailPort())).getClientPool();
		}
	}
	
	private boolean print(APIContext ret, String method){
		 if (ret.getRetCode() == 0) {
			 log.info(method + " ok " + ret.getResult());
	            return true;
	        } else {
	        	log.info(method + " failed, code=" + ret.getRetCode()
	                    + ", msg=" + ret.getErrorInfo());
	        	if (ret.getErrorInfo().equals("USER_NOT_FOUND")) {
	        		return true;
	        	}
	            return false;
	        }
	 }
	
	@Override
	public boolean addUnit(Object unitObj) throws Exception {
		MailUnit unit = (MailUnit)unitObj;
		if(unit != null && StringUtils.isNotBlank(unit.getOrg_id()) && StringUtils.isNotBlank(unit.getOrg_unit_id())){
			log.info("start addUnit!");					
			IClient cli = clientPool.borrowObject();
	   	    //IClient cli = getIClient();
		   	 try{
		   		 String attr = unit.getAttrs();
		   		 APIContext ret = cli.addUnit(unit.getOrg_id(), unit.getOrg_unit_id(), attr);
		   		 if(ret != null){
		   			return print(ret, "addUnit");
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return false;
	}

	@Override
	public boolean updateUnit(Object unitObj) throws Exception {
		MailUnit unit = (MailUnit)unitObj;
		if(unit != null && StringUtils.isNotBlank(unit.getOrg_id()) && StringUtils.isNotBlank(unit.getOrg_unit_id())){
			log.info("start updateUnit!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String attr = unit.getAttrs();
		   		 if(StringUtils.isNotBlank(attr)){
			   		 APIContext ret = cli.setUnitAttrs(unit.getOrg_id(), unit.getOrg_unit_id(), attr);
			   		 if(ret != null){
			   			return print(ret, "updateUnit");
			   		 }
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return false;
	}

	@Override
	public boolean deleteUnit(Object unitObj) throws Exception {
		MailUnit unit = (MailUnit)unitObj;
		if(unit != null && StringUtils.isNotBlank(unit.getOrg_id()) && StringUtils.isNotBlank(unit.getOrg_unit_id())){
			log.info("start deleteUnit!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 APIContext ret = cli.delUnit(unit.getOrg_id(), unit.getOrg_unit_id());
		   		 if(ret != null){
		   			return print(ret, "deleteUnit");
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return false;
	}

	@Override
	public String getTopUnitIds(String mailOrgId) throws Exception {
		String topUnitIds = null;
		if(StringUtil.isNotBlank(mailOrgId)){
			log.info("start getTopUnitIds!");
	   	    IClient cli = clientPool.borrowObject();
			try{
				APIContext ret = cli.getHierarchyJNDI(mailOrgId, null, null);
				Attributes resultEx = ((Attributes) ret.getResultEx());
				// 得到返回值并执行强制类型转换
				Attribute ous = resultEx.get("ou"); // 无任何部门时,返回null值
				// 获得首层部门
				if (ous != null) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0, size = ous.size(); i < size; i++) {
			             Attributes level1Ous = (Attributes) ous.get(i);
			             String orgUnitId = (String)level1Ous.get("id").get();	    
			             //topUnitIds =  topUnitIds +  "," + orgUnitId;
			             if(StringUtils.isNotBlank(orgUnitId)) {
			            	 sb.append("," + orgUnitId);
			             }
	                     if(sb != null && sb.length() > 0)						
	                    	 topUnitIds = sb.toString().substring(1);
			      }
				}
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   
		}
		return topUnitIds;
	}
	
	@Override
	public void removeAllUnit(String mailOrgId) throws Exception {
		if(StringUtil.isNotBlank(mailOrgId)){
			log.info("start removeAllUnit!");
	   	    IClient cli = clientPool.borrowObject();
			try{
				APIContext ret = cli.getHierarchyJNDI(mailOrgId, null, null);
				Attributes resultEx = ((Attributes) ret.getResultEx());
				// 得到返回值并执行强制类型转换
				Attribute ous = resultEx.get("ou"); // 无任何部门时,返回null值
				if (ous != null) {
					removeUnit(cli,mailOrgId, ous);
				}
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   
		}
	}
	@Override
	public boolean unitExist(String mailOrgId, String unitId) throws Exception {
		if(StringUtil.isNotBlank(mailOrgId) && StringUtil.isNotBlank(unitId)){
			log.info("start unitExist!");
	   	    IClient cli = clientPool.borrowObject();
			try{
				APIContext ret = cli.getUnitAttrs(mailOrgId, unitId, ""); 
			   	 if (ret.getRetCode() == 0) {
			   		return print(ret, "unitExist_" + unitId);			            
			        }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   
		}
		return false;
	}
	
	private void removeUnit(IClient cli, String mailOrgId, Attribute ous) throws NamingException {
		if (ous == null) {
			return;
		} else {
			for (int i = 0, size = ous.size(); i < size; i++) {
				Attributes thisLevelOus = (Attributes) ous.get(i);
				String orgUnitId = (String) thisLevelOus.get("id").get();
				String orgUnitName = (String) thisLevelOus.get("name").get();
				Attribute nextLevelOus = thisLevelOus.get("ou");
				removeUnit(cli, mailOrgId, nextLevelOus);
				APIContext ret = cli.delUnit(mailOrgId, orgUnitId);				
				if(ret != null){
		   			print(ret, "deleteUnit_" + orgUnitId + "_" + orgUnitName);
		   		 }
			}
		}
	}
	
	@Override
	public boolean addUser(Object userObj) throws Exception {
		MailUser user = (MailUser)userObj;
		if(user != null && StringUtils.isNotBlank(user.getOrg_id()) && StringUtils.isNotBlank(user.getOrg_unit_id())){
			log.info("start addUser!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String attr = user.getAttrs(0);
		   		 APIContext ret = cli.createUser(user.getProviderId(), user.getOrg_id(), user.getUser_id(), attr);
		   		 if(ret != null){
		   			 if (59 == ret.getRetCode()) {
		   				 throw new ApplicationException(ErrorCode.MAIL_OVER_LIMIT);
		   			 }
		   			return print(ret, "addUser");
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return false;
	}

	@Override
	public boolean updateUser(Object userObj) throws Exception {
		MailUser user = (MailUser)userObj;
		if(user != null && StringUtils.isNotBlank(user.getUser_id()) && StringUtils.isNotBlank(user.getDomain_name())){
			log.info("start updateUser!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String attr = user.getAttrs(1);
		   		 String mailAddr = user.getUser_id() + "@" + user.getDomain_name();
		   		 if(StringUtils.isNotBlank(attr)){
			   		 APIContext ret = cli.changeAttrs(mailAddr, attr);
			   		 if(ret != null){
			   			return print(ret, "updateUser");
			   		 } 
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return false;
	}

	@Override
	public boolean deleteUser(Object userObj, Integer preserveDays) throws Exception {
		MailUser user = (MailUser)userObj;
		if(user != null && StringUtils.isNotBlank(user.getUser_id()) && StringUtils.isNotBlank(user.getDomain_name())){
			log.info("start deleteUser!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String mailAddr = user.getUser_id() + '@' + user.getDomain_name(); //user@user.com
		   		 APIContext ret = null;
		   		 if(preserveDays != null) {
		   			ret = cli.apiSubmit("cmd=2&user_at_domain=" + mailAddr + "&preserve_days=" + preserveDays.toString());
		   		 }else {
		   			ret = cli.deleteUser(mailAddr);
		   		 }
		   		 if(ret != null){
		   			return print(ret, "deleteUser");
		   		 } 
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return false;
	}
	
	@Override
	public MailBoxMsg getMailBoxMsgByMailAddr(String mailAddr) throws Exception {
		if(StringUtils.isNotBlank(mailAddr)){
			log.info("start getMailListInfoByAttr!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String attrKeys = "mbox_msgcnt&mbox_msgsize&mbox_newmsgcnt&mbox_newmsgsize";
		   		 APIContext ret = cli.getAttrs(mailAddr, attrKeys);
		   		 if(ret != null){
		   			 if (ret.getRetCode() == 0) {
				            System.out.println(" ok " + ret.getResult());
				            //返回结果如: mbox_msgcnt=675&mbox_newmsgcnt=227
				            String result = ret.getResult();
				            if(StringUtils.isNotBlank(result)){
				            	MailBoxMsg mailBoxMsg = new MailBoxMsg();
				            	String[] msg_1 = result.split("&");
				            	for(int i = 0; i < msg_1.length; i++){
				            		String[] msg_2 = msg_1[i].split("=");
				            		if(msg_2[0].equals("mbox_msgcnt")) 
				            			mailBoxMsg.setMboxMsgCnt(Integer.valueOf(msg_2[1]));
				            		if(msg_2[0].equals("mbox_msgsize")) 
				            			mailBoxMsg.setMboxMsgSize(new BigDecimal(msg_2[1]));
				            		if(msg_2[0].equals("mbox_newmsgcnt")) 
				            			mailBoxMsg.setMboxNewMsgCnt(Integer.valueOf(msg_2[1]));
				            		if(msg_2[0].equals("mbox_newmsgsize")) 
				            			mailBoxMsg.setMboxNewMsgSize(Integer.valueOf(msg_2[1]));
				            	}
				            	if(mailBoxMsg != null) mailBoxMsg.setMailAddr(mailAddr);
				            	return mailBoxMsg;
				            }
				        } else {
				            System.out.println(" failed, code=" + ret.getRetCode()
				                    + ", msg=" + ret.getErrorInfo());
				            return null;
				        }
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return null;
	}


	@Override
	public boolean addMailList(Object mailListObj) throws Exception {
		MailList mailList = (MailList)mailListObj;
		if(mailList != null && StringUtils.isNotBlank(mailList.getOrgId()) && StringUtils.isNotBlank(mailList.getMailListId())){
			log.info("start addMailList!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String attr = mailList.getAttrs(0);
		   		 APIContext ret = cli.createUser(mailList.getProviderId(), mailList.getOrgId(), mailList.getMailListId(), attr);
		   		 if(ret != null){
		   			return print(ret, "addMailList");
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return false;
	}
	
	@Override
	public void addMailListList(List mailListList) throws Exception {
		log.info("start addMailListList!");
   	    IClient cli = clientPool.borrowObject();
	   	 try{
	   		 for(Object obj : mailListList) {
	   			MailList mailList = (MailList)obj;
	   			if(mailList != null && StringUtils.isNotBlank(mailList.getOrgId()) && StringUtils.isNotBlank(mailList.getMailListId())) {
	   				String attr = mailList.getAttrs(0);
		   		    APIContext ret = cli.createUser(mailList.getProviderId(), mailList.getOrgId(), mailList.getMailListId(), attr);
		   		    if(ret != null){
		   			   print(ret, "addMailListList_" + mailList.getTrueName());
		   		    }
	   			}	   		    
	   		 }
	   	 } finally {
	   		if(cli != null) clientPool.returnObject(cli);
	            
	     }  		
	}

	@Override
	public boolean updateMailList(Object mailListObj) throws Exception {
		MailList mailList = (MailList)mailListObj;
		if(mailList != null && StringUtils.isNotBlank(mailList.getMailListId()) && StringUtils.isNotBlank(mailList.getDomainName())){
			log.info("start updateMailList!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String attr = mailList.getAttrs(1);
		   		 String mailAddr = mailList.getMailListId().toLowerCase() + '@' + mailList.getDomainName(); //user@user.com
		   		 APIContext ret = cli.changeAttrs(mailAddr, attr);
		   		 if(ret != null){
		   			return print(ret, "updateMailList");
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return false;
	}
	
	public void updateMailListList(List mailListList) throws Exception {		
		log.info("start updateMailListList!");
   	    IClient cli = clientPool.borrowObject();
	   	 try{
	   		for(Object obj : mailListList) {
	   			MailList mailList = (MailList)obj;
	   			if(mailList != null && StringUtils.isNotBlank(mailList.getMailListId()) && StringUtils.isNotBlank(mailList.getDomainName())){
	   			     String attr = mailList.getAttrs(1);
		   		     String mailAddr = mailList.getMailListId().toLowerCase() + '@' + mailList.getDomainName(); //user@user.com
		   		     APIContext ret = cli.changeAttrs(mailAddr, attr);
		   		     if(ret != null){
		   			     print(ret, "updateMailListList_" + mailAddr);
		   		     }
	   			}	   		    
	   		 }	   		
	   	 } finally {
	   		if(cli != null) clientPool.returnObject(cli);
	            
	     }  
	}

	@Override
	public boolean deleteMailList(Object mailListObj, Integer preserveDays) throws Exception {
		MailList mailList = (MailList)mailListObj;
		if(mailList != null && StringUtils.isNotBlank(mailList.getMailListId()) && StringUtils.isNotBlank(mailList.getDomainName())){
			log.info("start deleteMailList!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String mailAddr = mailList.getMailListId() + '@' + mailList.getDomainName(); //user@user.com
		   		 APIContext ret = null;
		   		 if(preserveDays != null) {
		   			ret = cli.apiSubmit("cmd=2&user_at_domain=" + mailAddr + "&preserve_days=" + preserveDays.toString());
		   		 }else {
		   			ret = cli.deleteUser(mailAddr);
		   		 }
		   		 if(ret != null){
		   			return print(ret, "deleteMailList");
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return false;
	}
	
	@Override
	public void deleteMailListList(List mailListList, Integer preserveDays) throws Exception {
		log.info("start deleteMailListList!");
   	    IClient cli = clientPool.borrowObject();
	   	 try{
	   		for(Object obj : mailListList) {
	   			MailList mailList = (MailList)obj;
	   			if(mailList != null && StringUtils.isNotBlank(mailList.getMailListId()) && StringUtils.isNotBlank(mailList.getDomainName())){
		   		     String mailAddr = mailList.getMailListId().toLowerCase() + '@' + mailList.getDomainName(); //user@user.com
			   		 APIContext ret = null;
			   		 if(preserveDays != null) {
			   			ret = cli.apiSubmit("cmd=2&user_at_domain=" + mailAddr + "&preserve_days=" + preserveDays.toString());
			   		 }else {
			   			ret = cli.deleteUser(mailAddr);
			   		 }
		   		     if(ret != null){
		   			     print(ret, "deleteMailListList_" + mailAddr);
		   		     }
	   			}	   		    
	   		 }	   		
	   	 } finally {
	   		if(cli != null) clientPool.returnObject(cli);
	            
	     }  
		
	}
	
	@Override
	public String getMailListInfoByAttrs(String mailAddr, String attrKey) throws Exception {
		if(StringUtils.isNotBlank(mailAddr) && StringUtils.isNotBlank(attrKey)){
			log.info("start getMailListInfoByAttr!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 APIContext ret = cli.getAttrs(mailAddr, attrKey);
		   		 if(ret != null){
		   			 if (ret.getRetCode() == 0) {
		   				    log.info(" ok " + ret.getResult());
				            return ret.getResult();
				        } else {
				        	log.info(" failed, code=" + ret.getRetCode()
				                    + ", msg=" + ret.getErrorInfo());
				            return null;
				        }
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return null;
	}


	@Override
	public List<MailInfoView> listMailInfos(Object infoObj) throws Exception {
		MailInfo mailInfo = (MailInfo)infoObj;
		if(StringUtils.isNotBlank(mailInfo.getMailAddr())){
			log.info("start listMailInfos!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String attrs = mailInfo.getAttrs();
		   		 APIContext ret = cli.listMailInfos(mailInfo.getMailAddr(), attrs);
		   		 if(ret != null){
		   			 if (ret.getRetCode() == 0) {
		   				    log.info(" ok " + ret.getResult());
				            return parserXmlForMailInfos(ret.getResult());
				        } else {
				        	log.info(" failed, code=" + ret.getRetCode()
				                    + ", msg=" + ret.getErrorInfo());
				            return null;
				        }
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
	   	return null;
	}

	private List<MailInfoView> parserXmlForMailInfos(String XmlStr){
		if(StringUtils.isNotBlank(XmlStr)){
			try {
				List<MailInfoView> list = new ArrayList<MailInfoView>();
				Document document = DocumentHelper.parseText(XmlStr);
				Element rootElm = document.getRootElement();
				List nodes = rootElm.elements("mail");      
				for (Iterator it_1 = nodes.iterator(); it_1.hasNext();) {      
				    Element mailElm = (Element) it_1.next(); 
				    MailInfoView mailInfoView = new MailInfoView();
				    for(Iterator it_2=mailElm.elementIterator();it_2.hasNext();){      
		                 Element node = (Element) it_2.next();  
		                 System.out.println(node.getName()+":"+node.getText());
		                 if("mid".equalsIgnoreCase(node.getName())) 
		                	 mailInfoView.setMid(node.getText());
		                 if("msid".equalsIgnoreCase(node.getName())) 
		                	 mailInfoView.setMsid(Integer.valueOf(node.getText()));
		                 if("fid".equalsIgnoreCase(node.getName())) 
		                	 mailInfoView.setFid(Integer.valueOf(node.getText()));		                 
		                 if("flag".equalsIgnoreCase(node.getName())) 
		                	 mailInfoView.setFlag(Long.valueOf(node.getText()));
		                 if("from".equalsIgnoreCase(node.getName())) 
		                	 mailInfoView.setFrom(node.getText());
		                 if("to".equalsIgnoreCase(node.getName())) 
		                	 mailInfoView.setTo(node.getText());
		                 if("subject".equalsIgnoreCase(node.getName())) 
		                	 mailInfoView.setSubject(node.getText());
		                 if("size".equalsIgnoreCase(node.getName())) 
		                	 mailInfoView.setSize(Integer.valueOf(node.getText()));
		                 if("date".equalsIgnoreCase(node.getName())) 
		                	 mailInfoView.setDate(node.getText());   
		             }  
				    if(mailInfoView != null){
				    	list.add(mailInfoView);
				    }
				} 
				return list;
			} catch (DocumentException e) {
				e.printStackTrace();
			}			
		}
		return null;
	}

	@Override
	public List<MailInfoView> getNewMailInfos(Object infoObj) throws Exception {
		MailInfo mailInfo = (MailInfo)infoObj;
		if(StringUtils.isNotBlank(mailInfo.getMailAddr())){
			log.info("start getNewMailInfos!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 String attrs = mailInfo.getAttrs();
		   		 APIContext ret = cli.getNewMailInfos(mailInfo.getMailAddr(), attrs);
		   		 if(ret != null){
		   			 if (ret.getRetCode() == 0) {
		   				    log.info(" ok " + ret.getResult());
				            return parserXmlForMailInfos(ret.getResult());
				        } else {
				        	log.info(" failed, code=" + ret.getRetCode()
				                    + ", msg=" + ret.getErrorInfo());
				            return null;
				        }
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);		            
		     }   	
		}
	   	return null;
	}

	@Override
	public boolean mailAddrExist(String mailAddr) throws Exception {
		if(StringUtils.isNotBlank(mailAddr)){
			log.info("start mailAddrExist!");
	   	    IClient cli = clientPool.borrowObject();
		   	 try{
		   		 APIContext ret = cli.userExist(mailAddr);
		   		 if(ret != null){
		   			return print(ret, "mailAddrExist_" + mailAddr);
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     }   	
		}
		return false;
	}

	@Override
	public void mailAutoLogin(HttpServletRequest request,
			HttpServletResponse response, String mailAddr, Integer deviceType)  throws Exception {
		String loginAttrs = "remote_ip=" + request.getRemoteAddr();
		String cookieValue = null;
/*		if (cookieDomain != null) {  //暂时去掉cookie检查
			cookieValue = Long.toHexString(new Random().nextLong());
			loginAttrs += "&cookiecheck=" + cookieValue;  
		}*/
		IClient client = clientPool.borrowObject();
		try {
			// 尝试进行统一登录（不需要用户的密码）
			/** loginAttrs = //"" + "language=" + language + "&style=" + style
                    // + "face=" + face + "&locale=" + locale
                    + "&remote_ip=" + request.getRemoteAddr()
                    // + "&ipcheck=1" // 如果需要检查IP, 去掉这行的注释
                    + "&cookiecheck=" + cookieValue*/
			APIContext ret = client.userLoginEx(mailAddr, loginAttrs);
			if (ret.getRetCode() == APIContext.RC_NORMAL) {
				// 统一登录成功
				log.info("login succeed: " + mailAddr + " - " + ret.getResult());
				// 从结果中提取 sid 并进入邮箱页面
				succeed(response, ret.getResult(), cookieValue, deviceType);
			} else {
				// 统一登录失败
			    log.error("login failed: " + mailAddr + " - " + ret);
			    String url = request.getSession().getServletContext().getRealPath("/");
			    File html_file = new File(url + "/mail_error.html");  
			    FileInputStream inputStream = new FileInputStream(html_file);
			    OutputStream out = response.getOutputStream(); 
	            response.reset();   
	            response.setContentType("text/html;charset=UTF-8"); 
	            int b = 0;  
	            byte[] buffer = new byte[1024];  
	            while ((b = inputStream.read(buffer)) != -1) {  
	                // 4.写到输出流(out)中  
	                out.write(buffer, 0, b);  
	            }  
//	            out.write(redirectHtml.getBytes("UTF-8"));  
	            out.close();  
			}
		} finally {
			clientPool.returnObject(client);
		}
	}
	
	/**
	 * 获取邮箱自动登录信息
	 */
	public String getMailAutoLoginInfo(HttpServletRequest request, HttpServletResponse response, String mailAddr, Integer deviceType) throws Exception {
		String loginAttrs = "remote_ip=" + request.getRemoteAddr();
		IClient client = clientPool.borrowObject();
		try {
			APIContext ret = client.userLoginEx(mailAddr, loginAttrs);
			if (ret.getRetCode() == APIContext.RC_NORMAL) {
				// 统一登录成功
				log.info("login succeed: " + ret.getResult());
				// 从结果中提取 sid 并进入邮箱页面
				String sid = getParameter(ret.getResult(), "sid");
				String webname = getParameter(ret.getResult(), "webname");
				String httpsWebName = "";
				if (webname.contains("http")) {
				    httpsWebName = webname.replace("http", "https");
				} else {
				    httpsWebName = "https://" + webname;
				}
		        
				// Coremail XT 的 Webmail 主页
				String mainURL = httpsWebName + "/coremail/main.jsp?sid=" + sid;
				if(deviceType == 1) {    //移动端iphone/android系统登录页
					mainURL = webname + "/coremail/xphone/main.jsp?sid=" + sid;
				}
				if(deviceType == 2) {    //移动端ipad登录页
					mainURL = webname + "/coremail/ipad/main.jsp?sid=" + sid;
				}
				return mainURL;
				
			} else {
				// 统一登录失败
				log.error("login failed: " + ret);
				throw new ApplicationException("获取登录信息失败");
			}
		} finally {
			clientPool.returnObject(client);
		}
	}
	
	private void succeed(HttpServletResponse response, String encodedResult,
			String cookieValue, Integer deviceType) throws IOException {
		String sid = getParameter(encodedResult, "sid");
		String webname = getParameter(encodedResult, "webname");
        
		// Coremail XT 的 Webmail 主页
		String mainURL = webname + "/coremail/main.jsp?sid=" + sid;
		if(deviceType == 1) {    //移动端iphone/android系统登录页
			mainURL = webname + "/coremail/xphone/main.jsp?sid=" + sid;
		}
		if(deviceType == 2) {    //移动端ipad登录页
			mainURL = webname + "/coremail/ipad/main.jsp?sid=" + sid;
		}


		addCoremailCookie(response, cookieValue);
		response.sendRedirect(mainURL);
	}

	private void addCoremailCookie(HttpServletResponse response,
			String cookieValue) {
		if (cookieValue != null) {
			Cookie cookie = new Cookie("Coremail", cookieValue);
			cookie.setPath("/");
	/*		if (cookieDomain != null) {
				cookie.setDomain(cookieDomain);
			}*/
			response.addCookie(cookie);
		}
	}

	private static String getParameter(String encoded, String key)
			throws UnsupportedEncodingException {
		int start;
		if (encoded.startsWith(key + '=')) {
			start = key.length() + 1;
		} else {
			int i = encoded.indexOf('&' + key + '=');
			if (i == -1) {
				return null;
			}
			start = i + key.length() + 2;
		}

		int end = encoded.indexOf('&', start);
		String value = (end == -1) ? encoded.substring(start) : encoded
				.substring(start, end);

		return URLDecoder.decode(value, "GBK");
	}

	@Override
	public void deleteUnitUsersByCosId(String orgId, String orgUnitId,
			Integer cosId, Integer preserveDays) throws Exception {
		log.info("start deleteUnitUsersByCosId!");
		if(StringUtils.isNotBlank(orgId) && StringUtils.isNotBlank(orgUnitId)){
			String[] userAttrIds = {"user_id", "domain_name", "true_name", "cos_id"}; // 设定要查询返回的属性
			IClient cli = clientPool.borrowObject();
		   	 try{
		   		 APIContext ret = cli.listUsersJNDI(orgId, orgUnitId, true, true, userAttrIds, null, null, 0, -1);
		   		 if(ret != null){
		 	        // 得到查询得到的用户列表
		 	        javax.naming.directory.Attribute uList = ((javax.naming.directory.Attributes) ret.getResultEx()).get("u");
		 	        // 对返回的用户属性进行遍历
		 	        for (int i = 0; i < uList.size(); i++) {
		 	            javax.naming.directory.Attributes item = (javax.naming.directory.Attributes) uList.get(i);
		 	            Integer thisCosId = (Integer) item.get("cos_id").get();
		 	            if(thisCosId.intValue() == cosId.intValue()){
		 	                // 得到邮件地址
			 	            String mailAddr = "" + item.get("user_id").get() + '@' + item.get("domain_name").get();
			 	            String mailAddr2 = "" + item.get("user_id").get() + '@' + "xinghuo100.net";
			 	            APIContext ret_2;
			 	            if(preserveDays != null) {
			 	            	ret_2 = cli.apiSubmit("cmd=2&user_at_domain=" + mailAddr + "&preserve_days=" + preserveDays.toString());
					   		 }else {
					   			ret_2 = cli.deleteUser(mailAddr);
					   			if (!mailAddr2.equals(mailAddr)) {
					   				cli.deleteUser(mailAddr2);
					   			}
					   		 }
					   		 if(ret_2 != null){
					   			print(ret_2, "deleteUser——" + mailAddr);
					   		 }
		 	            }	 	            
		 	        }
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     } 
		} 	    
	}
	
	@Override
	public boolean deleteMailExternalContractById(String objUid) throws Exception {
		log.info("start deleteMailExternalContractById!");
		IClient cli = clientPool.borrowObject();
		try {
			APIContext ret = cli.deleteObj(objUid);
			if(ret != null){
				log.info("delete MailExternalContract: " + objUid + ", success!");
				 return true;
			} else {
				log.info("delete MailExternalContract: " + objUid + ", failure!");
				return false;
			}
		} finally {
	   		if(cli != null) clientPool.returnObject(cli);
	    } 
	}
	
	@Override
	public String createMailExternalContract(MailExternalContract contract) throws Exception {
		log.info("start createMailExternalContract!");
		IClient cli = clientPool.borrowObject();
		String attrParams = contract.getAttrs();
		try {
			APIContext ret = cli.createObj(attrParams);
			if (ret.getRetCode() == 0) {
				log.info("create MailExternalContract: " + contract.getObj_email() + ", success!");
				return ret.getResult().substring(ret.getResult().indexOf("=") + 1);
		    } else {
		    	log.info("create MailExternalContract: " + contract.getObj_email() + ", failure!");
		    }
		} finally {
	   		if(cli != null) clientPool.returnObject(cli);
	    }
		return null;
	}
	
	@Override
	public void moveAllUserToOrg(String orgId, String orgUnitId) throws Exception {
		log.info("start deleteUnitUsersByCosId!");
		if(StringUtils.isNotBlank(orgId) && StringUtils.isNotBlank(orgUnitId)){
			String[] userAttrIds = {"user_id", "domain_name", "true_name", "cos_id"}; // 设定要查询返回的属性
			IClient cli = clientPool.borrowObject();
		   	 try{
		   		 APIContext ret = cli.listUsersJNDI(orgId, orgUnitId, true, true, userAttrIds, null, null, 0, -1);
		   		 if(ret != null){
		 	        // 得到查询得到的用户列表
		 	        javax.naming.directory.Attribute uList = ((javax.naming.directory.Attributes) ret.getResultEx()).get("u");
		 	        // 对返回的用户属性进行遍历
		 	        for (int i = 0; i < uList.size(); i++) {
		 	            javax.naming.directory.Attributes item = (javax.naming.directory.Attributes) uList.get(i);
		 	            // 得到邮件地址
		 	            String mailAddr = "" + item.get("user_id").get() + '@' + item.get("domain_name").get();
		 	            APIContext ret_2 = cli.changeAttrs(mailAddr, "org_unit_id=");  //更新到根组织下
				   		 if(ret_2 != null){
				   			print(ret_2, "updateUserToOrg——" + mailAddr);
				   		 } 	            
		 	        }
		   		 }
		   	 } finally {
		   		if(cli != null) clientPool.returnObject(cli);
		            
		     } 
		}
	}
	
    /**
     * coreMail中排序以数字大的优先，即数字越大，排位越靠前
     */
	@Override
	public Integer getNewRank(Integer originalRank) {
		int maxRank = 10000;
		Integer newRank = null;
		if(originalRank != null){
			newRank =  Integer.valueOf((maxRank - originalRank.intValue()));
		}else{  //null时设为最大
			newRank = Integer.valueOf(maxRank);
		}
		return newRank;
	}
	
	/**
	 * 获取外部联系人
	 */
	@Override
	public List<MailExternalContract> listMailExternalContract(String objUid, String mailAddr) throws Exception {
		log.info("start listMailExternalContract!");
		 // 设定要查询返回的属性
        String attrs = "obj_uid" + "&" + "obj_email" + "&" + "true_name";
        // 设定要查询的过滤条件
        javax.naming.directory.Attributes filter1 = new javax.naming.directory.BasicAttributes();
        filter1.put("name", "obj_email");           // 查询字段: 邮件地址
        filter1.put("op", 0);                       // 查询操作: 等于比较
        filter1.put("val", mailAddr);  // 查询内容
        
        javax.naming.directory.Attribute filters = new javax.naming.directory.BasicAttribute("filter", true);
        filters.add(filter1);

        javax.naming.directory.Attributes asCmd = new javax.naming.directory.BasicAttributes();
        asCmd.put("cmd", 327);          // 命令号 - LIST_OBJ
//        asCmd.put("org_id", "");        // 可以设定搜索起始位置 (组织)
//        asCmd.put("org_unit_id", "");   // 可以设定搜索起始位置 (部门)
        asCmd.put("scope", 2);          // 包含子部门 (recursive): 2, 不包含子部门: 1
        asCmd.put("attrs", attrs);
        asCmd.put(filters);
        IClient cli = clientPool.borrowObject();
        APIContext ret = null;
        try{
        	ret = cli.apiSubmitJNDI(asCmd);
        } finally {
	   		if(cli != null) clientPool.returnObject(cli);
	     }

        log.info("查询成功，服务器返回信息：" + ret.getErrorInfo() + "，结果：" + ret.getResultEx());
        javax.naming.directory.Attribute uList = ((javax.naming.directory.Attributes) ret.getResultEx()).get("u");
        List<MailExternalContract> retList = new ArrayList<MailExternalContract>();
        if (uList != null && uList.size() > 0) {
        	log.info("===========================================================================================");
        	log.info("查询到的联系人数量：" + uList.size());
        	for (int i = 0; i < uList.size(); i++) {
        		javax.naming.directory.Attributes item = (javax.naming.directory.Attributes) uList.get(i);
        		log.info("第 " + (i + 1) + " 个联系人：" + item);
        		
        		String objUid2 = (String) item.get("obj_uid").get();
        		String objEmail = (String) item.get("obj_email").get();
        		String name = (String) item.get("true_name").get();
        		MailExternalContract contract = new MailExternalContract();
        		contract.setObjUid(objUid2);
        		contract.setObj_email(objEmail);
        		retList.add(contract);
        		log.info("第 " + (i + 1) + " 个联系人的 id    ：" + objUid2);
        		log.info("第 " + (i + 1) + " 个联系人的 email ：" + objEmail);
        		log.info("第 " + (i + 1) + " 个联系人的 姓名  ：" + name);
        	}
        }
        log.info("end listMailExternalContract!");
        return retList;
	}
	
}


