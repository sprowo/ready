package com.prowo.ydnamic.templates;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;
import com.prowo.ymlchain.yml.model.IConfiguration;
import com.prowo.ymlchain.yml.model.ITemplate;
import com.prowo.ymlchain.yml.model.impl.NullTemplate;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

import java.io.File;
import java.io.IOException;

public class FreemarkProxyConfiguration implements IConfiguration {

    private Configuration cfg;

    @Override
    public ITemplate getTemplate(String fileName) {
        try {
            return new FreemarkProxyTemplate(cfg.getTemplate(fileName));
        } catch (IOException e) {
            LoggerUtil.log(Level.WRAN, e, "Can not find template:" + fileName);
            return new NullTemplate();
        }
    }

    @Override
    public void setTmplPath(String tmplPath) {
        cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(new File(tmplPath));
        } catch (IOException e) {
            LoggerUtil.log(Level.WRAN, e, "Can not find directory:" + tmplPath);
        }
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

}
