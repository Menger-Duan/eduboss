package com.eduboss.service.dwr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.domain.MessageRecord;
import com.eduboss.dto.OnlineUser;

@RemoteProxy(creator = SpringCreator.class)
@Service("com.eduboss.service.dwr.DwrMessageManager")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class DwrMessageManagerImpl implements DwrMessageManager {

	/**
	 * 初始化DWR
	 * @param userId 用户Id
	 */
	@RemoteMethod()
	public void onPageLoad(String userId) {
		DwrMessageUtil util = new DwrMessageUtil();
		util.addUserIdToScriptSession(userId);
	}
	
	/**
	 * 发送信息给单个用户
	 * 调用方式
	 * 第一步：@Autowired
	 *  private IDwrMessageManager messageManager;
	 * 第二步：messageManager.serverToClient();
	 */
	@RemoteMethod()
	public void sendMessageToSingle(String userId, MessageRecord message) {
		DwrMessageUtil util = new DwrMessageUtil();
		util.sendMessageAuto(userId, message);
	}
	
	/**
	 * 发送信息给指定用户
	 * @param sendMap key:userId, value:message
	 */
	@RemoteMethod()
	public void sendMessageToAssign(Map<String, MessageRecord>sendMap) {
		if (sendMap != null && sendMap.size() > 0) {
			DwrMessageUtil util = new DwrMessageUtil();
			for (String userId : sendMap.keySet()) {
				util.sendMessageAuto(userId, sendMap.get(userId));
			}
		}
	}

	/**
	 * 发送信息给所有用户
	 * @param message 消息内容
	 */
	@RemoteMethod
	public void sendMessageToAll(MessageRecord message) {
		DwrMessageUtil util = new DwrMessageUtil();
		util.sendMessageAll(message);
	}

	/**
	 * 发送信息指定的用户
	 * @param message 消息内容
	 */
	@RemoteMethod
	public void sendMessageToUserList(MessageRecord message, List<String> userIdList) {
		if (userIdList != null) {
			for (String userId : userIdList) {
				sendMessageToSingle(userId, message);
			}
		}
	}

	@Override
	public List<OnlineUser> getAllOnlineUsers() {
		Collection<ScriptSession> sessions = Browser.getTargetSessions();
		List<OnlineUser> onlineUsers = new ArrayList<OnlineUser> ();
		for (ScriptSession session : sessions) {
			onlineUsers.add(new OnlineUser(String.valueOf(session.getAttribute("userId")), String.valueOf(session.getAttribute("userId")), "", ""));
		}
		return onlineUsers;
	}

	@Override
	public boolean isUserOnline(String userId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
