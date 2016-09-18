package com.prowo.ymlchain.classloader;

import java.io.InputStream;
import java.util.Map;

public interface DynamicLoader {
    void setClassFileMap(Map<String, byte[]> classFileMap);

    Map<String, byte[]> getClassFileMap();

    void setResourceMap(Map<String, byte[]> resourceMap);

    Map<String, byte[]> getResourceMap();

    void instantiateClass() throws ClassNotFoundException;

    void refreshClass() throws ClassNotFoundException;

    void unload();

    Class<?> loadClass(String name);

    InputStream getResourceAsStream(String path);

}
