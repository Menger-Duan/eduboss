package com.eduboss.service.dwr;

import org.directwebremoting.impl.DefaultScriptSessionManager;



public class AdvanDWRScriptSessionManager extends DefaultScriptSessionManager { 
	 public AdvanDWRScriptSessionManager(){   
   
	 
		 
		 //绑定一个ScriptSession增加销毁事件的监听器    8.  
		 this.addScriptSessionListener( new DWRScriptSessionListener());    
		 System. out.println( "bind DWRScriptSessionListener");   
		 
	 }
	 
}	 
