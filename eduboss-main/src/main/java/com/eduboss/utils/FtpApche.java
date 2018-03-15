package com.eduboss.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FtpApche {
	
	private final static Logger log = Logger.getLogger(FtpApche.class);
//	private static final String url = "14.23.46.115";
	private static final int port = 21;
//	private static final String username = "xhftp";
//	private static final String password = "xhftp0120";
	private static final String remotePath = "/home/xhftp/";
	
	private static FTPClient ftpClient = new FTPClient();
	private static String encoding = System.getProperty("file.encoding");
	

	/**
	 * Description: 向FTP服务器上传文件
	 * 
	 * @Version1.0
	 * @param url
	 *   FTP服务器hostname
	 * @param port
	 *   FTP服务器端口
	 * @param username
	 *   FTP登录账号
	 * @param password
	 *   FTP登录密码
	 * @param path
	 *   FTP服务器保存目录,如果是根目录则为“/”
	 * @param filename
	 *   上传到FTP服务器上的文件名
	 * @param input
	 *   本地文件输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String url, int port, String username,
			String password, String path, String filename, InputStream input) {
		boolean result = false;
		
		try {
			int reply;
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.connect(url);
			// ftp.connect(url, port);// 连接FTP服务器
			// 登录
			ftpClient.login(username, password);
			ftpClient.setControlEncoding(encoding);
			// 检验是否连接成功
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.println("连接失败");
				ftpClient.disconnect();
				return result;
			}
			
			// 转移工作目录至指定目录下
			boolean change = ftpClient.changeWorkingDirectory(path);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			if (change) {
				result = ftpClient.storeFile(new String(filename.getBytes(encoding),"iso-8859-1"), input);
				if (result) {
					System.out.println("上传成功!");
				}
			}
			input.close();
			ftpClient.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	

	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @Version1.0
	 * @param url
	 *   FTP服务器hostname
	 * @param port
	 *   FTP服务器端口
	 * @param username
	 *   FTP登录账号
	 * @param password
	 *   FTP登录密码
	 * @param remotePath
	 *   FTP服务器上的相对路径
	 * @param fileName
	 *   要下载的文件名
	 * @param localPath
	 *   下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(String url, String username, String password, String fileName, String localPath) {
		boolean result = false;
		log.info("FTP下载到本地地址：" + localPath + fileName);
		try {
			int reply;
			ftpClient.setControlEncoding(encoding);
			
			/*
			 * 为了上传和下载中文文件，有些地方建议使用以下两句代替
			 * new String(remotePath.getBytes(encoding),"iso-8859-1")转码。
			 * 经过测试，通不过。
			 */
			//   FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
			//   conf.setServerLanguageCode("zh");
			
			ftpClient.setDefaultTimeout(PropertiesUtils.getIntValue("ftp.download.defaultTimeout"));
			ftpClient.setDataTimeout(PropertiesUtils.getIntValue("ftp.download.dataTimeout"));
			ftpClient.connect(url, port);
			ftpClient.setSoTimeout(PropertiesUtils.getIntValue("ftp.download.socketTimeout"));
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.login(username, password);// 登录
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登陆成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				log.error("FTP登陆失败");
				return result;
			}
			// 转移到FTP服务器目录至指定的目录下
			ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding),"iso-8859-1"));
			ftpClient.enterLocalPassiveMode(); 
			// 获取文件列表
			FTPFile[] fs = ftpClient.listFiles();
			log.info("FTP获取文件列表大小" + fs.length);
			for (FTPFile ff : fs) {
				log.info("ftp fileNames:" + ff.getName());
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftpClient.retrieveFile(ff.getName(), is);
					is.close();
					log.info("下载成功：" + localPath + fileName);
					result = true;
				}
			}
			
			ftpClient.logout();
		} catch (SocketException e) {
		    log.error("FTP Socket ERROR:" + e.getMessage());
            e.printStackTrace();
        } catch(SocketTimeoutException e) {
            log.error("FTP SocketTimeOut:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	
	public void readFile(String url) {
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(url),getCharset(url));
            BufferedReader br = new BufferedReader(isr);
            String str = null;  
            String[] tmps= null;
	        while((str = br.readLine()) != null) {
	        	if (str.contains("999999999999999|")) { // 最后一行
	        		break;
	        	}
	        	tmps = str.split("\\|");
	        	for (int i = 0; i < tmps.length ; i++) {
	        		if (i == 1 || i == 3 || i == 7 || i == 12 || i == 15 || i ==18 || i == 20) {
	        			System.out.println(tmps[i]);
	        		}
	        	}
	        	System.out.println("\n");
	        }
	        br.close();
	        // write string to file
//	        FileWriter writer = new FileWriter("c://test2.txt");
//	        BufferedWriter bw = new BufferedWriter(writer);
//	        bw.write(sb.toString());
//	        bw.close();
//	        writer.close();
	    } catch(FileNotFoundException e) {
	    	e.printStackTrace();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	public void readFile2(String url) {
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(url),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String str = null;  
            String[] tmps= null;
	        while((str = br.readLine()) != null) {
	        	if (StringUtil.isBlank(str) || !str.substring(0,2).equals(" |")) {
					continue;
				}
	        	tmps = str.split("\\|");
	        	
	        	for (int i = 0; i < tmps.length ; i++) {
	        		if (i == 9 || i == 19 || i == 31 || i == 91 || i == 16 || i ==102 || i == 48 || i ==57) {
	        			System.out.println(i + ":" + tmps[i]);
	        		}
	        	}
	        	System.out.println("\n");
	        }
	        br.close();
	        // write string to file
//	        FileWriter writer = new FileWriter("c://test2.txt");
//	        BufferedWriter bw = new BufferedWriter(writer);
//	        bw.write(sb.toString());
//	        bw.close();
//	        writer.close();
	    } catch(FileNotFoundException e) {
	    	e.printStackTrace();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	  public static String getCharset(String fileName) throws IOException{
	        
          BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));  
          int p = (bin.read() << 8) + bin.read();  
          bin.close();
          
          String code = null;  
          
          switch (p) {  
              case 0xefbb:  
                  code = "UTF-8";  
                  break;  
              case 0xfffe:  
                  code = "Unicode";  
                  break;  
              case 0xfeff:  
                  code = "UTF-16BE";  
                  break;  
              default:  
                  code = "GBK";  
          }  
          return code;
  }
	
	
	/**
	 * 将FTP服务器上文件下载到本地
	 * 
	 */
	public void testDownFile() {
		try {
			boolean flag = downFile("14.23.46.115", "xhftp", "xhftp0120", "SFS_034_20170216.txt", "D:/");
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		FtpApche fa = new FtpApche();
//		fa.testDownFile();
		fa.readFile2("d://spark_2017-06-15.txt");
	}
}