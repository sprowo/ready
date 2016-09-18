package com.prowo.classloader;import org.apache.log4j.Logger;import java.io.*;import java.util.*;import java.util.concurrent.ConcurrentHashMap;public class ClassManager {    private static Logger logger = Logger.getLogger(ClassManager.class);    private static Map<String, ClassLoader> loaders = new ConcurrentHashMap<String, ClassLoader>();    private static Map<String, byte[]> classMap = new ConcurrentHashMap<String, byte[]>();    private static Map<String, byte[]> resourceMap = new ConcurrentHashMap<String, byte[]>();    private static Map<String, Set<String>> ymlFiles = new HashMap<String, Set<String>>();    ;    public static Map<String, Set<String>> getYmlFiles() {        return ymlFiles;    }    private static String path = "/";    public static String getPath() {        return path;    }    public static void setPath(String path) {        com.prowo.classloader.ClassManager.path = path;    }    /**     * 加载全部jar     */    public static void load() {        File file = new File(path);        try {            String[] list = JarVersionManager.getLastVersion(file.list());            if (list != null) {                for (String fileName : list) {                    load0(fileName);                }                if (logger.isInfoEnabled()) {                    logger.info(path + " 解析完成");                }            } else {                logger.warn(path + " 路径不存在或者没有可加载的jar");            }        } catch (Exception e) {            logger.error(path + " 路径访问失败");        }    }    public static void load0(String fileName) {        ClassLoader loader = loaders.get(fileName);        if (loader != null) {            // 如果是重复加载            loader.dump();            loader = null;            if (logger.isDebugEnabled()) {                logger.debug("JarFile:[" + fileName + "] 的ClassLoader:["                        + loader + "] 卸载完成");            }        }        loadJar(fileName);        loader = new ClassLoader();        loader.setClassFileMap(classMap);        loader.setResourceMap(resourceMap);        // 刷新class缓存        try {            loader.instantiateClass();            loaders.put(fileName, loader);            if (logger.isDebugEnabled()) {                logger.debug("ClassLoader:[" + loader + "] 把JarFile:["                        + fileName + "] 加载完成");            }        } catch (ClassNotFoundException e) {            logger.error(fileName + " 加载失败");        }    }    /**     * 加载一个jar包     *     * @param fileName jar包的文件名     */    public static void load(String fileName) {        File file = new File(path);        String newFileName = JarVersionManager.getLastVersion(file.list(),                fileName);        if (newFileName == null || !newFileName.equals(fileName)) {            logger.warn("JarFile:[" + fileName + "] 不是最新版本，忽略更新");            return;        }        load0(newFileName);    }    /**     * @param name 类名     * @return 类     * @throws ClassNotFoundException     */    public static Class<?> findClass(String name) throws ClassNotFoundException {        if (loaders.isEmpty()) {            logger.warn("加载Class:[" + name + "] 的ClassLoader是:["                    + ClassLoader.getSystemClassLoader() + "]");            // 为了兼容yd-ymlchain-1.0.jar            return Class.forName(name);        }        Class<?> clazz = null;        Collection<ClassLoader> loaderCollections = loaders.values();        for (ClassLoader loader : loaderCollections) {            clazz = loader.loadClass(name);            if (clazz != null) {                if (logger.isDebugEnabled()) {                    logger.debug("加载Class:[" + name + "] 的ClassLoader是:["                            + loader + "]");                }                return clazz;            }        }        throw new ClassNotFoundException(name);    }    public static void loadJar(String jarFileName) {        String fileName = path + jarFileName;        File jarFile = new File(fileName);        InputStream in = null;        try {            in = new BufferedInputStream(new FileInputStream(jarFile));            JarFileReader reader = new JarFileReader(in);            reader.readEntries();            classMap.clear();            classMap.putAll(reader.getClassStreamMap());            resourceMap.clear();            resourceMap.putAll(reader.getResourcesStreamMap());            Set<String> ymlResourceNames = new HashSet<String>();            for (String resourceName : resourceMap.keySet()) {                if (resourceName.endsWith(".yml")) {                    ymlResourceNames.add(resourceName);                }            }            ymlFiles.put(jarFileName, ymlResourceNames);            if (logger.isDebugEnabled()) {                logger.debug("JarFile:[" + fileName + "] 加载完成");            }        } catch (IOException e) {            logger.error("JarFile:[" + fileName + "] 加载失败");        } finally {            if (in != null) {                try {                    in.close();                } catch (IOException e) {                    logger.warn("JarFile:[" + fileName + "] 关闭失败");                }            }        }    }    public static InputStream getResourceAsStream(String path) {        if (loaders.isEmpty()) {            // return            // ClassManager.class.getClassLoader().getResourceAsStream(path)            logger.warn("加载Resource:[" + path + "] 的ClassLoader是:["                    + ClassLoader.getSystemClassLoader() + "]");            // 为了兼容yd-ymlchain-1.0.jar            return ClassManager.class.getResourceAsStream(path);        }        InputStream resourceStream = null;        Collection<ClassLoader> loaderCollections = loaders.values();        for (ClassLoader loader : loaderCollections) {            resourceStream = loader.getResourceAsStream(path);            if (resourceStream != null) {                if (logger.isDebugEnabled()) {                    logger.debug("加载Resource:[" + path + "] 的ClassLoader是:["                            + loader + "]");                }                return resourceStream;            }        }        return null;    }}