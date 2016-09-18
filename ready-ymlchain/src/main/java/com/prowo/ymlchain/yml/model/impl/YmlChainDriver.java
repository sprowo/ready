package com.prowo.ymlchain.yml.model.impl;

import com.prowo.ymlchain.yml.exception.YmlFileNotFoundException;
import com.prowo.ymlchain.yml.model.ChainBeanBuilder;
import com.prowo.ymlchain.yml.model.IConfiguration;
import com.prowo.ymlchain.yml.model.IHandlerContext;
import com.prowo.ymlchain.yml.model.ITemplate;
import com.prowo.ymlchain.yml.ydnamic.YmlClassManager;
import org.apache.log4j.Logger;
import org.ho.yaml.Yaml;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * yml chain的驱动类
 */
public class YmlChainDriver {
    private String yamlPath = "/";

    public void setYamlPath(String path) {
        this.yamlPath = path;
    }

    private static Logger logger = Logger.getLogger(YmlChainDriver.class);
    private Map<String, YmlChainBean> beans;
    private YmlClassManager classManager;

    public YmlClassManager getClassManager() {
        return classManager;
    }

    public void setClassManager(YmlClassManager classManager) {
        this.classManager = classManager;
    }

    public YmlChainDriver() {
        beans = new HashMap<String, YmlChainBean>();
    }

    public YmlChainDriver(YmlClassManager classManager) {
        beans = new HashMap<String, YmlChainBean>();
        this.classManager = classManager;
    }

    /**
     * 20151013 清空yml缓存，防止引用释放缓慢，内存泄露或者map的key更新缓慢
     */
    public void clearYml() {
        beans.clear();
    }

    private YmlChainBean getYmlChainBean(String fileName) throws Exception {
        if (beans.get(fileName) != null) {
            return beans.get(fileName);
        }

        String[] yamlPaths = yamlPath.trim().split("\\s*,\\s*");
        InputStream inputStream = null;
        String fullPath = null;
        for (String path : yamlPaths) {
            if (path.endsWith("/")) {
                fullPath = path + fileName;
            } else {
                fullPath = path + "/" + fileName;
            }
            inputStream = classManager.getResourceAsStream(fullPath);
            if (inputStream != null) {
                break;
            }
        }

        if (inputStream == null || inputStream.available() == 0) {
            logger.error(fullPath + " is not exits or not avaliable!");
            throw new YmlFileNotFoundException(fullPath + " is not exits or not avaliable!");
        }
        YmlChainBean bean = new YmlChainBean(Yaml.loadType(inputStream, ChainBeanBuilder.class), classManager);
        beans.put(fileName, bean);
        return bean;

    }

    public String startChainNoCache(InputStream inputStream, IHandlerContext context) throws Exception {
        return new YmlChainBean(Yaml.loadType(inputStream, ChainBeanBuilder.class), classManager).startChain(context);
    }

    public String startChainNoCache(File file, IHandlerContext context) throws Exception {
        return new YmlChainBean(Yaml.loadType(file, ChainBeanBuilder.class), classManager).startChain(context);
    }

    public String startChain(String fileName, IHandlerContext context) throws Exception {
        return resolveTmpl(getYmlChainBean(fileName).startChain(context), context);
    }

    public String startChainByMethod(String sysFileName, String methodEntryKey, IHandlerContext context)
            throws Throwable {
        return resolveTmpl(getYmlChainBean(sysFileName).startChain(methodEntryKey, context), context);
    }

    public void reloadChain(String path) throws Exception {
        InputStream inputStream = classManager.getResourceAsStream(path);
        if (inputStream == null || inputStream.available() == 0) {
            logger.error(path + " is not exits or not avaliable!");
            throw new YmlFileNotFoundException(path + " is not exits or not avaliable!");
        }
        YmlChainBean bean = new YmlChainBean(Yaml.loadType(inputStream, ChainBeanBuilder.class), classManager);
        if (beans == null) {
            beans = new HashMap<String, YmlChainBean>();
        }
        beans.put(path, bean);
        logger.info("reload yaml file:" + path + " success!");
    }

    private String resolveTmpl(String tmplName, IHandlerContext context) throws Exception {
        if (tmplName == null) {
            return tmplName;
        }
        String fileName = null;
        if (tmplName.startsWith(YmlChainBean.getTmplPrefix())) {
            fileName = tmplName.replace(YmlChainBean.getTmplPrefix(), "") + YmlChainBean.getTmplSuffix();
        } else if (tmplName.endsWith(YmlChainBean.getTmplSuffix())) {
            fileName = tmplName;
        } else {
            return tmplName;
        }

        // 创建根哈希表
        Map<String, Object> root = new HashMap<String, Object>();
        // 在根中放入字符串"user"
        root.put("context", context);
        root.putAll(context.getResponseAttributes());
        ITemplate temp = configuration.getTemplate(fileName);
        HttpServletResponse response = context.getHttpServletResponse();
        Writer out = response.getWriter();
        temp.process(root, out);
        out.flush();
        return tmplName;

    }

    public void setConfiguration(IConfiguration configuration) {
        this.configuration = configuration;
    }

    public IConfiguration getConfiguration() {
        return configuration;
    }

    private IConfiguration configuration;
}
