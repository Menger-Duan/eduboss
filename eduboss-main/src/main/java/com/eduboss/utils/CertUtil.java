package com.eduboss.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.gnete.merchant.api.Crypt;


public class CertUtil {
	 private static String GZYLCertPath = CertUtil.class.getResource("/cert/").getPath()+"GZYL_1.cer";  // 银联公钥
	 private static String XHJYCertPath = CertUtil.class.getResource("/cert/").getPath()+"XHJY_2.cer";  // 星火教育公钥
	 private static String XHJYPfxPath = CertUtil.class.getResource("/cert/").getPath()+"XHJY_2.pfx";   // 星火教育私钥
	 private static String SendCertPWD = PropertiesUtils.getStringValue("SendCertPWD");  // 星火教育证书密码
	 private static Logger log=Logger.getLogger(CertUtil.class);
	public static Map decreptMsg(String EncryptedMsg,String SignedMsg){
			
			Map map =new HashMap();
		try{
			   Crypt Obj = new Crypt();  //创建商户加密对象			
			    String SourceText = "";
			    boolean EncrypFlag = Boolean.FALSE, SignFlag = Boolean.FALSE;
			    // 1. 星火教育私钥解密
			    EncrypFlag = Obj.DecryptMsg(EncryptedMsg, XHJYPfxPath, SendCertPWD,"UTF-8");	    
			    // EncrypFlag ：返回解密成功与否
				if(EncrypFlag == Boolean.TRUE) { 
					SourceText = Obj.getLastResult(); 	//解密出来传递过来的原文
					System.out.println("【解密后的明文】："+SourceText);
					// 2. 校验签名
					SignFlag = Obj.VerifyMsg(SignedMsg, SourceText, GZYLCertPath,"UTF-8");
					if(SignFlag) {
						System.out.println("校验成功！");
						SignFlag=true;
						map.putAll(getUrlParameters(Obj.getLastResult()));
					}else{
						log.error("解密失败");
						return null;
					}
				} else {
					log.error("解密失败");
					return null;
				}
			}catch(Exception e){
				
				e.printStackTrace();
			}
		
		return map;
	}
	
	
	
	public static Map EncryptMsg(String SourceText){
		Map map =new HashMap();
		 Crypt Obj = new Crypt();  //创建商户加密对象		
		String EncryptedMsg = "";
		String SignedMsg = "";
		try{
		boolean EncrypFlag = Boolean.FALSE, SignFlag = Boolean.FALSE;
			// 把组好的变量，都放到这里
	//		SourceText = "payNo=123456&amount=000000000012&...";
			// 1. 用银联公钥证书加密,生成EncryptedMsg
		    EncrypFlag = Obj.EncryptMsg(SourceText, GZYLCertPath, "UTF-8");
		    if(EncrypFlag == Boolean.TRUE) {
		    	EncryptedMsg = Obj.getLastResult(); 	//解密出来传递过来的原文
				System.out.println("【加密后的密文】："+EncryptedMsg);
				map.put("encryptedMsg", EncryptedMsg);
				// 2. 用星火教育私钥证书签名，生成SignMsg签名信息
				SignFlag = Obj.SignMsg(SourceText.toString(), XHJYPfxPath, SendCertPWD, "UTF-8");
				if(SignFlag == Boolean.TRUE) {
					SignedMsg = Obj.getLastResult();
			  		System.out.println("【签名数据】:"+SignedMsg);
			  		map.put("signedMsg", SignedMsg);
				}else {
					log.error("验签失败！");
					return null;
				}
		    }else{
				log.error("验签失败！");
				return null;
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	

	
	
	
	/*********************************分割线*********************************************/
	/**
	 * @param EncryptedMsg 加密密文
	 * @param Obj  商户加密对象		
	 * @return
	 */
	public static Crypt decreptMsg(String EncryptedMsg,Crypt Obj){
	    boolean EncrypFlag = Boolean.FALSE;
	    // 1. 星火教育私钥解密
	    EncrypFlag = Obj.DecryptMsg(EncryptedMsg, XHJYPfxPath, SendCertPWD);	    
	    // EncrypFlag ：返回解密成功与否
		if(EncrypFlag == Boolean.TRUE) { 
			return Obj;
		} else {
			return null;//解密失败，返回空值
		}
	}
	
	
	/**
	 * @param SignedMsg 传递过来的签名信息
	 * @param SourceText 解密出来传递过来的原文
	 * @param Obj 商户加密对象		
	 * @return
	 */
	public static Crypt verifyMsg(String SignedMsg,String SourceText,Crypt Obj){
	    boolean SignFlag = Boolean.FALSE;
	    SignFlag = Obj.VerifyMsg(SignedMsg, SourceText, GZYLCertPath);
		if(SignFlag == Boolean.TRUE) { 
			return Obj;
		} else {
			System.out.println("解密失败，返回来的时候resultCode=2代表验签失败！");
			return null;///解密失败，返回null
		}
	}
	
	
	/**
	 * @param SourceText  要加密的参数 ，("payNo=123456&amount=000000000012&...")
	 * @param Obj 商户加密对象		 
	 * @return
	 */
	public static Crypt encryptMsg(String SourceText,Crypt Obj){
		String EncryptedMsg = "";
		boolean EncrypFlag = Boolean.FALSE;
	    EncrypFlag = Obj.EncryptMsg(SourceText, GZYLCertPath, "UTF-8");
	    if(EncrypFlag == Boolean.TRUE) {
	    	EncryptedMsg = Obj.getLastResult(); 	//加密
			System.out.println("【加密后的密文】："+EncryptedMsg);
			return Obj;
	    }else{
	    	return null;
	    }
	}
	
	/**
	 * @param SourceText  要加密的参数 ，("payNo=123456&amount=000000000012&...")
	 * @param Obj 商户加密对象		 
	 * @return
	 */
	public static Crypt signMsg(String SourceText,Crypt Obj){
		String SignedMsg = "";
		boolean SignFlag = Boolean.FALSE;
		// 2. 用星火教育私钥证书签名，生成SignMsg签名信息
		SignFlag = Obj.SignMsg(SourceText, XHJYPfxPath, SendCertPWD, "UTF-8");
		if(SignFlag == Boolean.TRUE) {
			SignedMsg = Obj.getLastResult();
	  		System.out.println("【签名数据】:"+SignedMsg);
	  		return Obj;
		}else {
			System.out.println("验签失败！");
			return null;
		}
	}
	
	public static String mapToString(Map map){
		String returnstr="";
		Set set = map.keySet();
		for (Object object : set) {
			returnstr+=object+"="+map.get(object)+"&";
		}
		if(StringUtil.isNotBlank(returnstr)){
			return returnstr.substring(0,returnstr.length()-1);
		}else{
			return returnstr;
		}
	}
	
	
	public static Map getUrlParameters(String parameters) {
		Map map =new HashMap();
		String [] array = parameters.split("&");
		for(int i=0; i<array.length; i++) {
			String [] v = array[i].split("=");
			if(v.length>1) {
				map.put(v[0], v[1]);
			}
		}
		return map;
	}
	
   public static void main(String[] args) {
		
//	   CertUtil.EncryptMsg("payNo=MON0000000013&payAmount=22.00&resultCode=0&resultMessage=成功");
	   
	   
	   System.out.println("6bb9effb02fb6b6e581fc1f76238312e8af7cca66daf2d73f01aea2edf822f394adebb897bbb0509d6cfe03ae255ef8d70f523e02a881e207249d994d05039738285757575f53f8e8c3b5839146f0ee7489ebc8c6940b3f83a6cb0bc8cf367cfbea5deaac8654c4e9318e6c2fced0364e42e781b652e897805a67832cad76fea958e4caff2b84116ad30c16cbc2fe5ac9e8bc630201dcfaa2074d28bed813ab540090e6bddab00c3f0880622e3f1d9bc12bd04e147caf287b3ce78b63b71a14a9b8c918538fb421d3c0e4ea95c5720a52f5b9f0e0a47167a51c1a12b19daf99e0bbc0172e987cbda17bca54874c56f2b54bb4206f324031d8e87da0328d80139a44af0e654e42288809e7f3cf65b9563011c66c4b8dec4f8f422207b998fcbeef6df06bd8b5dae494f4a7367d71d95c1b1949adee6584b6c033337cbf6cf83ef50172a2ef3e0fd513590c302722649091acf73b929181f6366f60a".equals("6bb9effb02fb6b6e581fc1f76238312e8af7cca66daf2d73f01aea2edf822f394adebb897bbb0509d6cfe03ae255ef8d70f523e02a881e207249d994d05039738285757575f53f8e8c3b5839146f0ee7489ebc8c6940b3f83a6cb0bc8cf367cfbea5deaac8654c4e9318e6c2fced0364e42e781b652e897805a67832cad76fea958e4caff2b84116ad30c16cbc2fe5ac9e8bc630201dcfaa2074d28bed813ab540090e6bddab00c3f0880622e3f1d9bc12bd04e147caf287b3ce78b63b71a14a9b8c918538fb421d3c0e4ea95c5720a52f5b9f0e0a47167a51c1a12b19daf99e0bbc0172e987cbda17bca54874c56f2b54bb4206f324031d8e87da0328d80139a44af0e654e42288809e7f3cf65b9563011c66c4b8dec4f8f422207b998fcbeef6df06bd8b5dae494f4a7367d71d95c1b1949adee6584b6c033337cbf6cf83ef50172a2ef3e0fd513590c302722649091acf73b929181f6366f60a"));
	   System.out.println("197d5960bde4d55974e0452162347776440caa6296f29f3dbcd18156b12dd071ca8e79002488e4d34109bc70a24abb062e4f7117e7cb39c823c750628fbf619130639a85791bd95b60f32f555d33b7c488a6882e37761dca36205cc40bc8722d200464fd9870f216bd198bda393944f195b8beb04577fab75755b6292d867559".equals("197d5960bde4d55974e0452162347776440caa6296f29f3dbcd18156b12dd071ca8e79002488e4d34109bc70a24abb062e4f7117e7cb39c823c750628fbf619130639a85791bd95b60f32f555d33b7c488a6882e37761dca36205cc40bc8722d200464fd9870f216bd198bda393944f195b8beb04577fab75755b6292d867559"));
		
	}
}
