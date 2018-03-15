package com.eduboss.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String getMD5(String str) {
		String  result = "";
		  try { 
		   MessageDigest md = MessageDigest.getInstance("MD5"); 
		   md.update(str.getBytes()); 
		   byte b[] = md.digest(); 
		   int i; StringBuffer buf = new StringBuffer(""); 
		   for (int offset = 0; offset < b.length; offset++) { 
		    i = b[offset]; 
		    if(i<0) 
		     i+= 256; 
		    if(i<16) 
		     buf.append("0"); 
		     buf.append(Integer.toHexString(i)); 
		   } 
		   result = buf.toString();
		   System.out.println("result: " + result);//32位的加密 
		//   System.out.println("result: " + buf.toString().substring(8,24));//16位的加密 
		  } catch (NoSuchAlgorithmException e) { 
		   // TODO Auto-generated catch block e.printStackTrace(); 
		  } 
		  return result;
	}
	
	
}
