package com.prowo.persist;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;

public abstract class BaseBO extends Objectx implements InitializingBean {

	protected JdbcTemplate template;

	/**
	 * 大数据操作
	 */
	protected LobHandler lobHandler;

	/**
	 * 工厂
	 */
	private LobHandlerFactory factory;
	
    /**
     * 条件从句拼接对象
     */
    private ProtasisSplicer protasisSplicer;

	public BaseBO() {
		super();
	}

	public DataSource getSource() {
		return template.getDataSource();
	}

	public void setSource(DataSource source) {
		this.template = new JdbcTemplate(source);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (lobHandler == null && factory != null) {
			lobHandler = factory.getObject(template.getDataSource());
		}
	}

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public LobHandlerFactory getFactory() {
		return factory;
	}

	public void setFactory(LobHandlerFactory factory) {
		this.factory = factory;
	}

	public JdbcTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
    public ProtasisSplicer getProtasisSplicer() {
        return this.protasisSplicer;
    }
    
    public void setProtasisSplicer(ProtasisSplicer protasisSplicer){
        this.protasisSplicer=protasisSplicer;
    }
}
