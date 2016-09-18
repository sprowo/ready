package com.prowo.ymlchain.yml.model;

public interface IConfiguration {
    ITemplate getTemplate(String fileName);

    void setTmplPath(String tmplPath);
}
