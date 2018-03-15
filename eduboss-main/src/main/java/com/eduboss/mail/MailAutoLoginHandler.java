package com.eduboss.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import tebie.applib.api.APIContext;
import tebie.applib.api.IClient;

import com.eduboss.domain.SystemConfig;
import com.eduboss.service.SystemConfigService;

/**@author wmy
 *@date 2015年9月29日下午5:29:35
 *@version 1.0 
 *@description
 */
public class MailAutoLoginHandler {
    private static Log log = LogFactory.getLog(MailAutoLoginHandler.class);
	
    //private String cookieDomain = ".xinghuo100.net"; //.xinghuo100.net
    private String apiHost = null;
    private Integer apiPort = null;
/*    private String apiUid = null;  
    private String apiPassword = null;*/
    private static MailAutoLoginHandler instrance;
    
	private ObjectPool<IClient> clientPool = new GenericObjectPool<IClient>(new BasePoolableObjectFactory<IClient>() {
        @Override
        public IClient makeObject() throws Exception {
            Socket socket = new Socket(apiHost, apiPort);
            IClient client = APIContext.getClient(socket);
          /*  if (apiUid != null) {
                // 首先必须使用分配的账号进行客户端认证
                APIContext ret = client.clientAuthenticate(apiUid, apiPassword);
                if (ret.getRetCode() == APIContext.RC_NORMAL) {
                    // 客户端认证成功，可以进行后续的测试
                    System.out.println("Socket client authenticate: " + apiUid + "@" + socket);
                } else {
                    // 客户端认证失败，输出相关异常信息
                    throw new ApplicationException("Socket client authenticate failed: " + ret);
                }
            } // 没有配置 apiUid 表示无需客户端认证
*/            return client;
        }

        @Override
        public void activateObject(IClient client) throws IOException {
            // 在每次从连接池中获取到实例时，校验一下链接是否可用
            client.apiSubmit("cmd=-2");
        }

        @Override
        public void destroyObject(IClient client) throws IOException {
            client.close();
        }
    });

    // 配置连接池参数
    {
        // 配置最多 30 个 Socket 链接
        ((GenericObjectPool<IClient>) clientPool).setMaxActive(30);
        ((GenericObjectPool<IClient>) clientPool).setMaxIdle(20);
        // 当 20 个链接全满时，最多等待 20 秒，之后会抛出 NoSuchElementException
        ((GenericObjectPool<IClient>) clientPool).setMaxWait(20000);
    }
    
    private MailAutoLoginHandler(String mailIp, Integer mailPort){
    	this.apiHost = mailIp;
    	this.apiPort = mailPort;
    }
    
    public static synchronized MailAutoLoginHandler getInstrance(SystemConfigService systemConfigService){
    	if(instrance == null){
    		SystemConfig findSc = new SystemConfig();
			findSc.setTag("coreMailIp");
			SystemConfig ipSc = systemConfigService.getSystemPath(findSc);
			findSc.setTag("coreMailPort");
			SystemConfig portSc = systemConfigService.getSystemPath(findSc);
    		String mailIp = ipSc.getValue();
    		String mailPort = portSc.getValue();
    		instrance = new MailAutoLoginHandler(mailIp, Integer.valueOf(mailPort));
    	}
    	return instrance;
    }
    
    public void doSsoLogin(HttpServletRequest request,
			HttpServletResponse response, String userEmail) throws Exception {
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
			APIContext ret = client.userLoginEx(userEmail, loginAttrs);
			if (ret.getRetCode() == APIContext.RC_NORMAL) {
				// 统一登录成功
				log.info("login succeed: " + ret.getResult());
				// 从结果中提取 sid 并进入邮箱页面
				succeed(response, ret.getResult(), cookieValue);
			} else {
				// 统一登录失败
				log.error("login failed: " + ret);
			}
		} finally {
			clientPool.returnObject(client);
		}
	}

	private void succeed(HttpServletResponse response, String encodedResult,
			String cookieValue) throws IOException {
		String sid = getParameter(encodedResult, "sid");
		String webname = getParameter(encodedResult, "webname");

		// Coremail XT 的 Webmail 主页
		String mainURL = webname + "/coremail/main.jsp?sid=" + sid;
		
		//移动端ipad登录页
		//String mainURL = webname + "/coremail/ipad/main.jsp?sid=" + sid;
		
		//移动端iphone等
		//String mainURL = webname + "/coremail/xphone/main.jsp?sid=" + sid;

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

}


