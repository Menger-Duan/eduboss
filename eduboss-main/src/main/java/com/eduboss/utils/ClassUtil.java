package com.eduboss.utils;

import com.eduboss.controller.SystemController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;


/**
 * 通过包名获取class
 *
 * @author sunchenbin
 * @version 2016年6月23日 下午5:55:18 
 */
public class ClassUtil{

	/**
	 * 从包package中获取所有的Class
	 *
	 * @param pack
	 * @return
	 */
	public static Set<Class<?>> getClasses(String pack){

		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try{
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()){
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					System.err.println("file类型的扫描");
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				}else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					System.err.println("jar类型的扫描");
					JarFile jar;
					try{
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()){
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx).replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class") && !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										try{
											// 添加到classes
											classes.add(Class.forName(packageName + '.' + className));
										}catch (ClassNotFoundException e){
											// log
											// .error("添加用户自定义视图类错误 找不到此类的.class文件");
											e.printStackTrace();
										}
									}
								}
							}
						}
					}catch (IOException e){
						// log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 *
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(
			String packageName,
			String packagePath,
			final boolean recursive,
			Set<Class<?>> classes){
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter(){

			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file){
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles){
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			}else{
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try{
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' +
					// className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
				}catch (ClassNotFoundException e){
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 从包package中获取所有的Class
	 *
	 * @param pack
	 * @return
	 */
	public static Set<String> getClassesName(String pack,String type){

		// 第一个class类的集合
		Set<String> classes = new LinkedHashSet<String>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try{
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()){
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					System.err.println("file类型的扫描");
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesNameInPackageByFile(packageName, filePath, recursive, classes,type);
				}else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					System.err.println("jar类型的扫描");
					JarFile jar;
					try{
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()){
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx).replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class") && !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										classes.add(className);
									}
								}
							}
						}
					}catch (IOException e){
						// log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 *
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesNameInPackageByFile(
			String packageName,
			String packagePath,
			final boolean recursive,
			Set<String> classes,String type){
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter(){

			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file){
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles){
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesNameInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes,type);
			}else{
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				if(type!=null) {
					try {
						Class c = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
//						Annotation[] anno = c.getAnnotations();
//						for (Annotation a : anno) {
//							if (a.annotationType().getSimpleName().equals("GrayClassAnnotation")) {
								classes.add(c.getName());
//							}
//						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}else {
					classes.add(className);
				}
			}
		}
	}


	public static Set<Map<String,String>> getNeedGrayPublishControllerMethod(Class c){

		Set<Map<String,String>> set = new HashSet<Map<String,String>>();
			Method[] method=c.getMethods();
			for(Method m:method) {
				Map<String, String> map = new HashMap<String, String>();
//				Annotation[] anno = m.getAnnotations();
//				for (Annotation a : anno) {
//					if (a.annotationType().getSimpleName().equals("GrayMethodAnnotation")) {
//						GrayMethodAnnotation an = (GrayMethodAnnotation)a;
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("value", c.getSimpleName()+"_"+m.getName());
//						map.put("name", an.name());
//						set.add(map);
//					}
//				}

				Annotation a=m.getAnnotation(RequestMapping.class);
				if(a!=null) {
					map.put("value", c.getSimpleName() + "_" + m.getName());
					map.put("name", m.getName());
					set.add(map);
				}
			}
		return set;
	}

	public static void main(String[] args){
//		Set<String> set=getClassesName("com.eduboss.controller","controller");
//		Iterator<String> it = set.iterator();
//		while (it.hasNext()) {
//			String cla = it.next();
//			System.out.println(cla);
//		}
		JedisUtil.set("1","2");
//			Set<Map<String,String>> set =getNeedGrayPublishControllerMethod(SystemController.class);
//			for(Map<String,String> map :set){
//				System.out.print(map.get("value"));
//				System.out.print(map.get("name"));
//			}

	}
}