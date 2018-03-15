package com.eduboss.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 
 * @author  author :Yao 
 * @date  2016年7月5日 下午12:00:23 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public class ObjectUtil {
    /**对象转byte[]
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] objectToBytes(Object obj) throws IOException{
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        byte[] bytes = bo.toByteArray();
        bo.close();
        oo.close();
        return bytes;
    }
    /**byte[]转对象
     * @param bytes
     * @return
     * @throws Exception
     */
    public static Object bytesToObject(byte[] bytes)  throws ClassNotFoundException,IOException{
    	if(bytes!=null){
	        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
	        ObjectInputStream sIn = new ObjectInputStream(in);
	        return sIn.readObject();
    	}else {
    		return null;
    	}
    }
}
