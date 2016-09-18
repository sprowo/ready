package com.prowo.classloader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YdClassLoader extends ClassLoader {

	/**
	 * 字节码文件缓存
	 */
	private Map<String, byte[]> classFileMap;

	/**
	 * 资源文件缓存
	 */
	private Map<String, byte[]> resourceMap;

	public YdClassLoader() {
		super();
	}

	public YdClassLoader(ClassLoader parent) {
		super(parent);
	}

	public Map<String, byte[]> getClassFileMap() {
		return classFileMap;
	}

	public void setClassFileMap(Map<String, byte[]> classFileMap) {
		if (this.classFileMap == null) {
			this.classFileMap = new ConcurrentHashMap<String, byte[]>();
		}
		this.classFileMap.clear();
		this.classFileMap.putAll(classFileMap);
	}

	public Map<String, byte[]> getResourceMap() {
		return resourceMap;
	}

	public void setResourceMap(Map<String, byte[]> resourceMap) {
		if (this.resourceMap == null) {
			this.resourceMap = new ConcurrentHashMap<String, byte[]>();
		}
		this.resourceMap.clear();
		this.resourceMap.putAll(resourceMap);
	}

	/**
	 * 初始化加载class
	 */
	public void instantiateClass() throws ClassNotFoundException {
		for (String name : classFileMap.keySet()) {
			loadClass(getClassNameByFileName(name));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.ClassLoader#loadClass(java.lang.String)
	 */
	public Class<?> loadClass(String name) {
		Class<?> clazz = null;
		try {
			clazz = super.loadClass(name);
		} catch (Exception e) {
		}
		if (clazz == null) {
			try {
				clazz = Class.forName(name);
			} catch (Exception e) {
			}
		}
		if (clazz == null) {
			try {
				clazz = readClass(name);
			} catch (Exception e) {
			}
		}
		return clazz;
	}

	private Class<?> readClass(String name) {
		String fileName = getFileNameByClassName(name);
		byte[] bytes = classFileMap.get(fileName);
		if (bytes != null) {
			return defineClass(name, bytes, 0, bytes.length);
		}
		return null;
	}

	/**
	 * 用于重新加载class
	 *
	 * @param classFileMap
	 */
	public void refreshClass() throws ClassNotFoundException {
		instantiateClass();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
	 * 获取资源文件字节流
	 */
	public InputStream getResourceAsStream(String path) {
		// 优先以由YamlClassLoader加载的jar包中的资源文件优先
		byte[] resouceBytes = resourceMap.get(path);

		// 找不到就加载classpath下的资源文件
		return resouceBytes != null ? new ByteArrayInputStream(resouceBytes)
				: super.getResourceAsStream(path);
	}

	private String getFileNameByClassName(String className) {
		String separator = File.separator;
		if (separator.equals("\\")) {
			separator = "/";
		}
		String fileName = new StringBuilder()
				.append(className.replaceAll("\\.", separator))
				.append(".class").toString();
		return fileName;
	}

	private String getClassNameByFileName(String fileName) {
		String separator = File.separator;
		if (separator.equals("\\")) {
			separator = "/";
		}
		String className = fileName.replaceAll("\\/", ".").replaceFirst(
				"\\.class", "");
		return className;
	}

	public void dump() {
		if (classFileMap != null) {
			classFileMap.clear();
			classFileMap = null;
		}
		if (resourceMap != null) {
			resourceMap.clear();
			resourceMap = null;
		}
	}
}
