package com.prowo.ydnamic.templates;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;
import com.prowo.ymlchain.yml.model.ITemplate;
import freemarker.template.Template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class FreemarkProxyTemplate implements ITemplate {

    private Template template;

    public FreemarkProxyTemplate(Template template) {
        this.template = template;
    }

    @Override
    public void process(Map<String, Object> root, Writer out) {
        try {
            template.process(root, out);
            out.flush();

        } catch (Exception e) {
            try {
                out.write("template error " + e.getMessage());
            } catch (IOException e1) {
                LoggerUtil.log(Level.WRAN, e1, "template error " + e1.getMessage());
            }
        }
    }

}
