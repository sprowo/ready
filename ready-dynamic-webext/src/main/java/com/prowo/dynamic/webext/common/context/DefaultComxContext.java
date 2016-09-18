package com.prowo.dynamic.webext.common.context;

import com.prowo.ydnamic.cache.CacheManager;
import com.prowo.ydnamic.context.ComxContext;
import com.prowo.ydnamic.context.IRefresher;
import com.prowo.ydnamic.handler.HandlerConstans;
import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;
import com.prowo.ydnamic.persist.ComxPagingDAO;
import com.prowo.ydnamic.record.Recorder;
import com.prowo.ydnamic.thread.WatchManGroup;
import com.prowo.ydnamic.validation.Validate;
import com.prowo.ymlchain.thread.ThreadManager;
import com.prowo.ymlchain.yml.model.impl.YmlChainDriver;
import com.prowo.ymlchain.yml.ydnamic.YmlChainDriverReloader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.io.IOException;

/**
 * 默认全局上下文实现类
 * 1.spring容器启动时会调用afterPropertiesSet和setApplicationContext(ApplicationContext spcxt)
 * 2.spring容器销毁时会调用destroy()
 */
public class DefaultComxContext extends ComxContext implements InitializingBean, ApplicationContextAware,
        DisposableBean {

    private ApplicationContext spcxt;

    private CacheManager cacheManager;

    private YmlChainDriverReloader ymlChainDriverReloader;

    private IRefresher refresher;

    public YmlChainDriverReloader getYmlChainDriverReloader() {
        return ymlChainDriverReloader;
    }

    public void setYmlChainDriverReloader(YmlChainDriverReloader ymlChainDriverReloader) {
        this.ymlChainDriverReloader = ymlChainDriverReloader;
    }

    private ComxPagingDAO cadao;

    public DefaultComxContext() {
        setContext(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext spcxt) throws BeansException {
        this.setSpcxt(spcxt);

        try {
            if (System.getProperty("os.name") == null || !System.getProperty("os.name").startsWith("Windows")) {
                ymlChainDriverReloader.loadClass();
                ymlChainDriverReloader.reload();
            }

        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("load yml with YmlChainDriverReloader failed");
        }

        // 加载附加的bean
        try {
            String tempFilePath = getYmlChainDriver().getClassManager().getPath().replace("shadow", "");
            loadBeans(tempFilePath);
        } catch (Exception e) {
            LoggerUtil.log(Level.ERROR, e, e.getMessage());
        }

        try {
            getRefresher().refreshCache();
        } catch (Exception e) {
            LoggerUtil.log(Level.ERROR, e, e.getMessage());
        }

    }

    public ComxPagingDAO getDao(String daoName) {
        return (ComxPagingDAO) this.spcxt.getBean(daoName);
    }

    public ApplicationContext getSpcxt() {
        return spcxt;
    }

    public void setSpcxt(ApplicationContext spcxt) {
        this.spcxt = spcxt;
    }

    public ComxPagingDAO getCadao() {
        return cadao;
    }

    public void setCadao(ComxPagingDAO cadao) {
        this.cadao = cadao;
    }

    @Override
    public String get(String name) {
        String value = cacheManager.getString(name + "@" + Recorder.getPartnerid());
        if (value != null) {
            return value;
        }
        value = cacheManager.getString(name);
        if (value != null) {
            return value;
        }
        value = cacheManager.getMap(HandlerConstans.CACHE_KEY_USER).get(name + "@" + Recorder.getPartnerid());
        if (value != null) {
            return value;
        }
        value = cacheManager.getMap(HandlerConstans.CACHE_KEY_USER).get(name);
        if (value != null) {
            return value;
        }

        value = cacheManager.getMap(HandlerConstans.CACHE_KEY_CONFIG).get(name + "@" + Recorder.getPartnerid());
        if (value != null) {
            return value;
        }
        value = cacheManager.getMap(HandlerConstans.CACHE_KEY_CONFIG).get(name);
        if (value != null) {
            return value;
        }

        if (value == null) {
            logger.info("key:" + name + " is not available");
        }
        return value;
    }


    public void loadBean(String configLocation) throws BeansException {
        XmlBeanDefinitionReader beanDefinitionReader = getXmlBeanDefinitionReader();
        beanDefinitionReader.setResourceLoader(getSpcxt());
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(getSpcxt()));
        try {
            beanDefinitionReader.loadBeanDefinitions(getSpcxt().getResources(configLocation));
            logger.debug("load bean:" + configLocation + " ok");
        } catch (BeansException e) {
            logger.warn("loadBean failed", e);
            throw e;
        } catch (IOException e) {
            logger.warn("loadBean failed", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void loadBeans(String tempFilePath) throws BeansException {
        String beanLocations = tempFilePath + "/classes/spring";
        if (!Validate.isNull(beanLocations)) {
            File locationsFolder = new File(beanLocations);
            if (locationsFolder.isDirectory()) {
                for (File file : locationsFolder.listFiles()) {
                    loadBean("classpath:" + "spring/" + file.getName());
                }
            }
        }
    }

    private XmlBeanDefinitionReader xmlBeanDefinitionReader;

    private XmlBeanDefinitionReader getXmlBeanDefinitionReader() {
        if (xmlBeanDefinitionReader != null) {
            return xmlBeanDefinitionReader;
        }
        xmlBeanDefinitionReader = new XmlBeanDefinitionReader(
                (BeanDefinitionRegistry) ((AbstractApplicationContext) getSpcxt()).getBeanFactory());
        return xmlBeanDefinitionReader;
    }

    @Override
    public void destroy() throws Exception {
        ThreadManager.interruptAll();
        WatchManGroup.massSuicide();
    }

    @Override
    public YmlChainDriver getYmlChainDriver() {
        return ymlChainDriverReloader.getYmlChainDriver();
    }

    @Override
    public IRefresher getRefresher() {
        return this.refresher;
    }

    public void setRefresher(IRefresher refresher) {
        this.refresher = refresher;
    }

    @Override
    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


}
