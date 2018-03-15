package com.eduboss.service.dwr;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.servlet.DwrServlet;

/**
 * 处理页面的ScriptSession类
 * @author liangmingjiong
 */
public class DwrScriptSessionManager extends DwrServlet {

	private static final long serialVersionUID = -6530107253211635142L;	
	
	public void init(final String userId){
		Container container = ServerContextFactory.get().getContainer();
		ScriptSessionManager mamager = container.getBean(ScriptSessionManager.class);
		ScriptSessionListener listener = new ScriptSessionListener() {
			// System.out.println("------ScriptSession被销毁--------");
			@Override
			public void sessionDestroyed(ScriptSessionEvent evt) {
				// do nothing
			}
			
			// System.out.println("------ScriptSession被创建--------");
			@Override
			public void sessionCreated(ScriptSessionEvent evt) {
				ScriptSession session = evt.getSession();//WebContextFactory.get().getScriptSession();
				session.setAttribute("userId", userId);
			}
		};
		mamager.addScriptSessionListener(listener);
	}

}
