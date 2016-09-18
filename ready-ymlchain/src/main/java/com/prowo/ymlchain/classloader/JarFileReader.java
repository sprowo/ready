package com.prowo.ymlchain.classloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * 读取jar文件工具类
 */
public class JarFileReader {
    private JarInputStream jarInput;

    /**
     * jar中的class
     */
    private Map<String, byte[]> classStreamMap;

    /**
     * jar中的资源文件
     */
    private Map<String, byte[]> resourcesStreamMap;

    public Map<String, byte[]> getClassStreamMap() {
        return classStreamMap;
    }

    public void setClassStreamMap(Map<String, byte[]> classStreamMap) {
        this.classStreamMap = classStreamMap;
    }

    public Map<String, byte[]> getResourcesStreamMap() {
        return resourcesStreamMap;
    }

    public void setResourcesStreamMap(Map<String, byte[]> resourcesStreamMap) {
        this.resourcesStreamMap = resourcesStreamMap;
    }

    public JarFileReader(InputStream in) throws IOException {
        jarInput = new JarInputStream(in);
        classStreamMap = new ConcurrentHashMap<String, byte[]>();
        resourcesStreamMap = new ConcurrentHashMap<String, byte[]>();
    }

    public void readEntries() throws IOException {
        JarEntry entry = jarInput.getNextJarEntry();
        String manifestEntry = null;
        while (entry != null) {
            manifestEntry = entry.getName();
            if (manifestEntry.endsWith(".class")) {
                copyClassInputStream(jarInput, manifestEntry);
            } else {
                copyResourceInputStream(jarInput, manifestEntry);
            }
            entry = jarInput.getNextJarEntry();
        }
    }

    public void copyClassInputStream(InputStream in, String entryName) throws IOException {
        ByteArrayOutputStream _copy = new ByteArrayOutputStream();
        int chunk = 0;
        byte[] data = new byte[256];
        while (-1 != (chunk = in.read(data))) {
            _copy.write(data, 0, chunk);
        }
        classStreamMap.put(entryName, _copy.toByteArray());
    }

    public void copyResourceInputStream(InputStream in, String entryName) throws IOException {
        ByteArrayOutputStream _copy = new ByteArrayOutputStream();
        int chunk = 0;
        byte[] data = new byte[256];
        while (-1 != (chunk = in.read(data))) {
            _copy.write(data, 0, chunk);
        }
        resourcesStreamMap.put(entryName, _copy.toByteArray());
    }

    public InputStream getCopy(String entryName) {
        byte[] _copy = classStreamMap.get(entryName);
        if (_copy == null) {
            _copy = resourcesStreamMap.get(entryName);
        }
        return new ByteArrayInputStream(_copy);
    }

}