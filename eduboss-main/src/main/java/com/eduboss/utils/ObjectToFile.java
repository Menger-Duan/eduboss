package com.eduboss.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectToFile {  
    public static void writeObject(Object o,String filePath) {  
        try {  
            FileOutputStream outStream = new FileOutputStream(filePath);  
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);  
            objectOutputStream.writeObject(o);  
            outStream.close();  
            objectOutputStream.close();
            System.out.println("successful");  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    public static Object readObject(String filePath) throws IOException, ClassNotFoundException{  
        FileInputStream freader= new FileInputStream(filePath);  
        ObjectInputStream objectInputStream = new ObjectInputStream(freader);  
        Object o =  objectInputStream.readObject(); 
        freader.close();
        objectInputStream.close();
        return o;
    }  
      
    public static String getTheFilePath(String servicePath,String fileName){
    	servicePath=servicePath+"\\"+PropertiesUtils.getStringValue("user_dept_job");//系统路径
		File f1=new File(servicePath);
		if(!f1.exists())
		{
			f1.mkdirs();
		}
		servicePath+="\\"+fileName;
		return servicePath;
	}
      
    public static void main(String args[]) throws IOException{  
//    	  HashMap<String,String> map = new HashMap<String,String>();  
//          map.put("name", "foolfish");  
//            
//          List<String> list = new ArrayList<String>();  
//          list.add("hello");  
//          list.add("everyone");  
//          
//
//	    	
//          ObjectToFile of = new ObjectToFile();  
//          of.writeObject(list);  
//	        
//	        
//	      list=(ArrayList<String>) of.readObject();
//	      
//	      for (String string : list) {
//	     	 System.out.println("list: " + string);  
//			}
    }  
}  