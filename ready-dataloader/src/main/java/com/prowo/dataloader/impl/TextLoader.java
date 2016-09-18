package com.prowo.dataloader.impl;

import com.prowo.dataloader.ITestBuilder;
import com.prowo.dataloader.ITextLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextLoader implements ITextLoader {
    private Map<String, String> map = new HashMap<String, String>();
    private Pattern pattern;
    private String path;

    public TextLoader(ITestBuilder builder) {
        this.pattern = builder.getPattern();
        this.path = builder.getDefaultPath();
        this.map = new HashMap<String, String>();
    }

    public String getTestDataById(String id) {
        return map.get(id);
    }

    public void initData() throws IOException {
        initData(path);
    }

    public void initData(String path) throws IOException {
        InputStream fs = TextLoader.class.getResourceAsStream(path);
        int b;
        // 顺序读取文件text里的内容并赋值给整型变量b,直到文件结束为止。
        StringBuffer sb = new StringBuffer();
        while ((b = fs.read()) != -1) {
            sb.append((char) b);
        }
        fs.close();
        parse(new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8"));

    }

    private void parse(String s) {
        Matcher mat = pattern.matcher(s);
        while (mat.find()) {
            map.put(mat.group(1), mat.group(2));
        }
    }

}
