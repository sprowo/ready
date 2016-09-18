package com.prowo.ymlchain.yml.model;

import java.io.Writer;
import java.util.Map;

public interface ITemplate {
    void process(Map<String, Object> root, Writer out);
}
