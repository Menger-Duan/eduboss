package com.eduboss.service.dwr;

import java.util.Collection;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.WebContextFactory;
import org.nutz.json.Json;

import com.eduboss.domain.MessageRecord;

public class DwrMessageUtil {

	/**
	 * @param userId
	 */
	public void addUserIdToScriptSession(final String userId) {
		try {
			ScriptSession session = WebContextFactory.get().getScriptSession();
			session.setAttribute("userId", userId);
			DwrScriptSessionManager manager = new DwrScriptSessionManager();
			manager.init(userId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 推送消息给指定用户
	 * @param userId 用户ID
	 * @param message  推送消息内容
	 */
	public void sendMessageAuto(String userId, MessageRecord message) {
		sendMessageBase(userId, message);
	}
	

	/**
	 * 推送消息给所有人
	 * @param message 内容
	 */
	public void sendMessageAll(MessageRecord message) {
		sendMessageBase(null, message);
	}
	
	/**
	 * 推送方法
	 * @param userId 用户ID
	 * @param message  推送消息内容
	 */
	private void sendMessageBase(final String userId, final MessageRecord message) {
		try {
			Browser.withAllSessionsFiltered(
				new ScriptSessionFilter() {
					public boolean match(ScriptSession session) {
						if (session == null  || session.getAttribute("userId") == null) {
							return false;
						} else {
							if (userId == null) {
								return true;
							}
							String tempUserId = (String) session.getAttribute("userId");
							return (tempUserId.equals(userId));
						}
					}
				}, 
				new Runnable() {
					private ScriptBuffer script = new ScriptBuffer();
		
					public void run() {
						script.appendCall("showMessage", Json.toJson(message));
						Collection<ScriptSession> sessions = Browser.getTargetSessions();
						if (sessions != null && sessions.size() > 0) {
							for (ScriptSession scriptSession : sessions) {
								scriptSession.addScript(script);
							}
						}
					}
				}
			);
		} catch (Exception ex) {
			// 当前没有用户在使用本系统!!
			// do nothing
		}
	}
}
