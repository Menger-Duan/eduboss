package com.eduboss.bill;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CerEncode {
	private static Logger logger = Logger.getLogger(CerEncode.class);
	public boolean enCodeByCer(String val, String msg) {
		logger.info("val:"+val);
		logger.info("msg:"+msg);
		boolean flag = false;
		try {
			String fStr = CerEncode.class.getResource("/cert/mgw.cer").toURI().getPath();
			InputStream inStream = new FileInputStream(fStr);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf
					.generateCertificate(inStream);
			PublicKey pk = cert.getPublicKey();

			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(pk);
			signature.update(val.getBytes());

			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			flag = signature.verify(decoder.decodeBuffer(msg));
			logger.info("flag =="+flag);
		} catch (IOException e) {
			logger.error("IOException鉴权失败",e);
		} catch (CertificateException e) {
			logger.error("CertificateException鉴权失败",e);
		} catch (URISyntaxException e) {
			logger.error("URISyntaxException鉴权失败",e);
		} catch (InvalidKeyException e) {
			logger.error("InvalidKeyException鉴权失败",e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException鉴权失败",e);
		} catch (SignatureException e) {
			logger.error("SignatureException鉴权失败",e);
		}
		return flag;
	}

	public static String appendParam(String returnStr, String paramId, String paramValue) {
		if (!returnStr.equals("")) {
			if (!paramValue.equals("") && null != paramValue) {
				returnStr = returnStr + paramId + "=" + paramValue;
			}
		} else {
			if (!paramValue.equals("")) {
				returnStr = paramId + "=" + paramValue;
			}
		}
		return returnStr;
	}
	public static void main(String[] args) {
		String mscVal="";
		mscVal = appendParam(mscVal,"orderId","YEE000000000214");
		mscVal = appendParam(mscVal,"reqTime","20170525112744");
		mscVal = appendParam(mscVal,"ext1","");
		mscVal = appendParam(mscVal,"ext2","");
		System.out.println("mscVal  == "+ mscVal);

		CerEncode cer = new CerEncode();
		boolean flag = cer.enCodeByCer(mscVal,
				"eb0ONBHv9XA2yj0epliNi4thRgj9Ji+ny+EcfF4CK9evBULKQheA6DdV8OHjOxd/sON3CsNa0U+gCnD95GE7EHkxNYshwqej2oEYyjJ7dHLCbfccdFpraNcjRYZ0SCN/56Z1ohGauamnfVcRStP3BLYd25jBjBM8yd3mIBe0uig=");
		System.out.println("flag ==="+flag);
	}
}
