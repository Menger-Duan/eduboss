package com.eduboss.utils;




import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 将httpClient转换成https可访问的客户端
 * WebClientDevWrapper.java
 * @author linlihua
 * 2016年11月5日
 */
public class WebClientDevWrapper {

	@SuppressWarnings("deprecation")
	public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    		ClientConnectionManager ccm = base.getConnectionManager();
    		SchemeRegistry sr = ccm.getSchemeRegistry();
    		sr.register(new Scheme("https", ssf, 443));
    		return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
