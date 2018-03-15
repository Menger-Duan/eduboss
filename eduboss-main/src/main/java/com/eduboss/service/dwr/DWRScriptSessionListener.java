package com.eduboss.service.dwr;
import java.util.Collection;
import java.util.HashMap;   
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;

public class DWRScriptSessionListener implements ScriptSessionListener{

	
	//维护一个Map key为session的Id， value为ScriptSession对象    
	public static final Map<String, ScriptSession> scriptSessionMap = new HashMap<String, ScriptSession>();  
	
	@Override
	public void sessionCreated(ScriptSessionEvent event) {
		// TODO Auto-generated method stub
		WebContext webContext = WebContextFactory. get();   
		HttpSession session = webContext.getSession(); 
		ScriptSession scriptSession = event.getSession();   
		scriptSessionMap.put(session.getId(), scriptSession);     
		//添加 scriptSession   
		System. out.println( "session: " + session.getId() + " scriptSess ion: " + scriptSession.getId() + "is created!"); 
		
	}

	@Override
	public void sessionDestroyed(ScriptSessionEvent arg0) {
		// TODO Auto-generated method stub
		
		WebContext webContext = WebContextFactory. get();   
		HttpSession session = webContext.getSession();   
	
		   ScriptSession scriptSession = scriptSessionMap.remove(session.getId()); 
		           //移除scriptSession    
		     System. out.println( "session: " + session.getId() + " scriptSess ion: " + scriptSession.getId() + "is destroyed!"); 
		
	}
	
	
	/**   42.       * 获取所有ScriptSession  43.       */  
	public static Collection<ScriptSession> getScriptSessions(){  
		return scriptSessionMap.values();   
		}

}
