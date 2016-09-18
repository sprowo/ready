package com.prowo.ymlchain.yml.model.impl;

import com.prowo.ymlchain.yml.model.ITemplate;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class NullTemplate implements ITemplate {

    @Override
    public void process(Map<String, Object> root, Writer out) {
        try {
            out.write(root == null ? "" : root.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
