package com.eduboss.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
	
	/**
	 * 文件拷贝
	 * @param oldFilePath
	 * @param newFilePath
	 * @return
	 */
	public static boolean fileCopy(String oldFilePath, String newFilePath) {
		FileInputStream in = null;
		FileOutputStream out = null;

		byte[] buff = new byte[4096];
		try {
			in = new FileInputStream(oldFilePath);
			out = new FileOutputStream(newFilePath);

			int len = 0;
			while ((len = in.read(buff)) > 0) {
				out.write(buff, 0, len);
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
		return true;
	}



	
	public static boolean readInputStreamToFile(InputStream in, String newFilePath) {
		FileOutputStream out = null;
		byte[] buff = new byte[4096];
		try {
			out = new FileOutputStream(newFilePath);
			int len = 0;
			while ((len = in.read(buff)) > 0) {
				out.write(buff, 0, len);
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
		return true;
	}
	
	/**  
     *<p>  
     *@param  file 待压缩文件的名称 例如,src/zip/文件1.txt  
     *@param  zipFile 压缩后文件的名称 例如,src/zip/单个文件压缩.zip  
     *@return boolean  
     *@throws :IOException  
     *@Function: zipSingleFile  
     *@Description:单个文件的压缩  
     *@version : v1.0.0  
     *@author: niudongdong  
     *@Date:2014012-10  
     *</p>  
     *Modification History:  
     * Date                     Author          Version         Description  
     * ---------------------------------------------------------------------  
     * May 24, 2012        pantp           v1.0.0           Create  
     */  
    public static boolean zipSingleFile(String file, String zipFile)  
            throws IOException {  
        boolean bf = true;  
        File f = new File(file);  
        if (!f.exists()) {  
            System.out.println("文件不存在");  
            bf = false;  
        } else {  
            File ff = new File(zipFile);  
            if (!f.exists()) {  
                ff.createNewFile();  
            }  
            // 创建文件输入流对象  
            FileInputStream in = new FileInputStream(file);  
            // 创建文件输出流对象  
            FileOutputStream out = new FileOutputStream(zipFile);  
            // 创建ZIP数据输出流对象  
            ZipOutputStream zipOut = new ZipOutputStream(out);  
            // 得到文件名称  
            String fileName = file.substring(file.lastIndexOf('/') + 1, file.length());  
            // 创建指向压缩原始文件的入口  
            ZipEntry entry = new ZipEntry(fileName);  
            zipOut.putNextEntry(entry);  
            // 向压缩文件中输出数据  
            int number = 0;  
            byte[] buffer = new byte[512];  
            while ((number = in.read(buffer)) != -1) {  
                zipOut.write(buffer, 0, number);  
            }  
            zipOut.close();  
            out.close();  
            in.close();  
        }  
        return bf;  
    }  
    
    
    public static void isNewFolder(String folder){
		File f1=new File(folder);
		if(!f1.exists())
		{
			f1.mkdirs();
		}
	}

	public static void main(String[] args) throws IOException {
//		fileCopy("G:\\project\\svn\\eduboss\\trunk\\xinghuo\\javaio-appendfile.txt","G:\\project\\svn\\eduboss\\trunk\\xinghuo\\target.txt");


		///load data infile "/root/temp/target.txt" into table xinghuo_sat.studentschooltemp fields terminated by'|' LINES TERMINATED BY '\n' ;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。

		FileOutputStream fos=new FileOutputStream(new File("G:\\project\\svn\\eduboss\\trunk\\xinghuo\\target.txt"));
		OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter  bw=new BufferedWriter(osw);
		try {
			String str = "";
			String str1 = "";
			fis = new FileInputStream("G:\\project\\svn\\eduboss\\trunk\\xinghuo\\javaio-appendfile.txt");// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
			Integer id =0;
			while ((str = br.readLine()) != null) {
				bw.write(id.toString()+"|"+str+"\n");
				id++;
			}
			// 当读取的一行不为空时,把读到的str的值赋给str1
			System.out.println(str1);// 打印出str1
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
