package com.eduboss.sms;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


//import common.util.web.Base;

/**
 * 
 * @项目名称:xiaotuan
 * @类名称:SecondSMS
 * @类描述:发送短信;
 * @创建人:苗有虎
 * @创建时间:Sep 18, 20103:04:52 PM
 * @修改人:Administrator
 * @修改时间:Sep 18, 20103:04:52 PM
 * @修改备注:
 * @version
 */
public class MessageUtil {
	
	/*
	 * webservice服务器定义
	 */
	 // 我们所有的Demo，都是在GBK环境下测试的。
	 //如果您的系统是utf-8,调用注册方法可能不成功。
	 //java.io.IOException: Server returned HTTP response code: 400 for URL: http://sdk2.zucp.net:8060/webservice.asmx。
	 //如果出现上述400错误，请参考第105行。
	 //如果您的系统是utf-8，收到的短信可能是乱码，请参考第298行
	 //可以根据您的需要自行解析下面的地址
	 //http://sdk2.zucp.net:8060/webservice.asmx?wsdl
	private static String serviceURL = "http://115.29.194.198/api/http";

	private static String user = "tyej";// 序列号
	private static String passwd = "888888";// 密码
	
	
	public MessageUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * 构造函数
	 */
//	public MessageUtil(String sn, String password)
//			throws UnsupportedEncodingException {
//		this.sn = sn;
//		this.password = password;
//		this.pwd = this.getMD5(sn + password);
//	}

	
	/*
	 * 方法名称：mt 
	 * 功    能：发送短信 ,传多个手机号就是群发，一个手机号就是单条提交
	 * 参    数：mobile,content,ext,stime,rrid(手机号，内容，扩展码，定时时间，唯一标识)
	 * 返 回 值：唯一标识，如果不填写rrid将返回系统生成的
	 */
	public static String mt(String mobile, String content, String ext, String stime,
			String rrid) {
//		String back="【广州市药品检验所】";
//		if(ext!=null && "1".equals(ext)){
//			back="【广州市药品不良反应监测中心】";
//		}
		String result = "";
		String soapAction = "http://115.29.194.198/api/http";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mt xmlns=\"http://tempuri.org/\">";
		xml += "<act>sendmsg</sn>";
		xml += "<user>" + user + "</sn>";
		xml += "<passwd>" + passwd + "</pwd>";
		xml += "<phone>" + mobile + "</mobile>";
		xml += "<msg>" + content +"</content>";
		xml += "<sendtime>" + stime + "</stime>";
		xml += "</mt>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			//如果您的系统是utf-8,这里请改成bout.write(xml.getBytes("GBK"));

			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String
					.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(httpconn
					.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				System.out.print(inputLine);
//				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	 public static final String GET_URL="http://115.29.194.198/api/http";
	 
	 public static final String POST_URL="http://115.29.194.198/api/http";

	public static String readContentFromGet(String mobile, String content) throws IOException {
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		String getURL = GET_URL
				+ "?act=sendmsg&user=tyej&passwd=888888&msg="+content+"&phone="+mobile;
		URL getUrl = new URL(getURL);
		// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
		// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
		HttpURLConnection connection = (HttpURLConnection) getUrl
				.openConnection();
		// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
		// 服务器
		connection.connect();
		// 取得输入流，并使用Reader读取
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), "utf-8"));// 设置编码,否则中文乱码
		System.out.println("=============================");
		System.out.println("Contents of get request");
		System.out.println("=============================");
		String lines;
		String result="-1";
		while ((lines = reader.readLine()) != null) {
			// lines = new String(lines.getBytes(), "utf-8");
			System.out.println(lines);
			result=lines;
		}
		reader.close();
		// 断开连接
		connection.disconnect();
		System.out.println("=============================");
		System.out.println("Contents of get request ends");
		System.out.println("=============================");
		return result;
	}

	public static void readContentFromPost() throws IOException {
		// Post请求的url，与get不同的是不需要带参数
		URL postUrl = new URL(POST_URL);
		// 打开连接
		HttpURLConnection connection = (HttpURLConnection) postUrl
				.openConnection();
		// Output to the connection. Default is
		// false, set to true because post
		// method must write something to the
		// connection
		// 设置是否向connection输出，因为这个是post请求，参数要放在
		// http正文内，因此需要设为true
		connection.setDoOutput(true);
		// Read from the connection. Default is true.
		connection.setDoInput(true);
		// Set the post method. Default is GET
		connection.setRequestMethod("POST");
		// Post cannot use caches
		// Post 请求不能使用缓存
		connection.setUseCaches(false);
		// This method takes effects to
		// every instances of this class.
		// URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
		// connection.setFollowRedirects(true);

		// This methods only
		// takes effacts to this
		// instance.
		// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
		connection.setInstanceFollowRedirects(true);
		// Set the content type to urlencoded,
		// because we will write
		// some URL-encoded content to the
		// connection. Settings above must be set before connect!
		// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
		// 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
		// 进行编码
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
		// 要注意的是connection.getOutputStream会隐含的进行connect。
		connection.connect();
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());
		// The URL-encoded contend
		// 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
		String content = "key=j0r53nmbbd78x7m1pqml06u2&type=1&toemail=jiucool@gmail.com"
				+ "&activatecode=" + URLEncoder.encode("久酷博客", "utf-8");
		// DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
		out.writeBytes(content);
		out.flush();
		out.close();// flush and close
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), "utf-8"));// 设置编码,否则中文乱码
		String line = "";
		System.out.println("=============================");
		System.out.println("Contents of post request");
		System.out.println("=============================");
		while ((line = reader.readLine()) != null) {
			// line = new String(line.getBytes(), "utf-8");
			System.out.println(line);
		}
		System.out.println("=============================");
		System.out.println("Contents of post request ends");
		System.out.println("=============================");
		reader.close();
		connection.disconnect();
	}

	public static void main(String[] args) throws IOException {
		MessageUtil.readContentFromGet("13450214963", "学生jun004的2016-01-22\b11:00\b-\b13:00的高三-英语的课程发送扣费回滚，回滚原因是误操作，请查实修改后重新提交课程结算流程。");
//		MessageUtil.readContentFromGet();
//		Client c=new Client("SDK-GKG-010-00242","789463");
		
//		System.out.println(c.getBalance());
//		
//		System.out.println(27000*0.074);
		
//		Client c=new Client();
//		String r=c.mt("15820296719", "贵科室报修的”粉碎机“设备已进行网上验收，请您及时审核。", "","", "");
//		System.out.println(r);
//		URLEncoder.encode("%bb%d8%b8%b4%a1%ad", "gbk");
		
//		System.out.println(URLEncoder.encode(",", "gbk")+"---"+URLDecoder.decode("%bb%d8%b8%b4%a1%ad"));
		
		
		
//		 String test = "a1,a2,a3\r";
//		    test += "b1,b2,b3\r";
//		    test += "c1,c2,c3\r";
//		    System.out.println(test);
//		    String [] t=test.split(new String("\r\n"));
	}
}
