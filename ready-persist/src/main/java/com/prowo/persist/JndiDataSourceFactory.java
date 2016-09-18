package com.prowo.persist;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

public class JndiDataSourceFactory extends Objectx implements FactoryBean<DataSource>, ApplicationContextAware, ServletContextAware, InitializingBean {

    private String jndi;

    private String sinfo;

    private DataSource source;

    private ApplicationContext context;

    @Override
    public void setServletContext(ServletContext context) {
        sinfo = context.getServerInfo();
    }

    protected boolean isTomcat() {
        return sinfo != null && sinfo.contains("Tomcat");
    }

    protected boolean isJboss() {
        return sinfo != null && sinfo.contains("JBoss");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InitialContext cxt = new InitialContext();
            if (isTomcat()) {
                source = (DataSource) cxt.lookup("java:/comp/env/jdbc/" + jndi);
            } else if (isJboss()) {
                source = (DataSource) cxt.lookup("java:jboss/datasources/" + jndi);
            } else {
                source = (DataSource) cxt.lookup(jndi);
            }
        } catch (Throwable e) {
            logger.error(e);
        }
        if (source == null) {
            source = context.getBean("debug_" + jndi, DataSource.class);
        }
    }

    @Override
    public DataSource getObject() throws Exception {
        return source;
    }

    @Override
    public Class<DataSource> getObjectType() {
        return DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getJndi() {
        return jndi;
    }

    public void setJndi(String jndi) {
        this.jndi = jndi;
    }

    public DataSource getSource() {
        return source;
    }

    public void setSource(DataSource source) {
        this.source = source;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
