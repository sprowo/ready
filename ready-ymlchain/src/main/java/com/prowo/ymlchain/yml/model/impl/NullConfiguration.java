package com.prowo.ymlchain.yml.model.impl;

import com.prowo.ymlchain.yml.model.IConfiguration;
import com.prowo.ymlchain.yml.model.ITemplate;

public class NullConfiguration implements IConfiguration {

    @Override
    public ITemplate getTemplate(String fileName) {
        return new NullTemplate();
    }

    @Override
    public void setTmplPath(String tmplPath) {

    }

}
