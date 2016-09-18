package com.prowo.dataloader.impl;

import com.prowo.dataloader.ITestBuilder;
import com.prowo.dataloader.ITextLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TextLoaderFactory {
    public static String XML_DATA = "XML_DATA";
    public static String JSON_DATA = "JSON_DATA";

    public static String XML_PATTERN = "<test-data id=\"([\\s\\S]*?)\">([\\s\\S]*?)</test-data>";
    public static String JSON_PATTERN = "var\\s+([0-9_a-zA-Z]*?)\\s+=\\s+(\\{[\\s\\S]*?\\})";

    public static String XML_TXT_PATH = "/testdata.xml";

    public static String JSON_TXT_PATH = "/testdata.js";

    public static ITextLoader DEFAULT_XML_LOADER = new TextLoader(new ITestBuilder() {
        @Override
        public Pattern getPattern() {
            return Pattern.compile(XML_PATTERN);
        }

        @Override
        public String getDefaultPath() {
            return XML_TXT_PATH;
        }
    });

    public static ITextLoader DEFAULT_JSON_LOADER = new TextLoader(new ITestBuilder() {

        @Override
        public Pattern getPattern() {
            return Pattern.compile(JSON_PATTERN);
        }

        @Override
        public String getDefaultPath() {
            return JSON_TXT_PATH;
        }
    });

    public static ITextLoader getTextLoader(final String pattern, final String defaultPath) {
        if (textLoaders.get(pattern.toString()) == null) {
            return new TextLoader(new ITestBuilder() {
                @Override
                public Pattern getPattern() {
                    return Pattern.compile(pattern);
                }

                @Override
                public String getDefaultPath() {
                    return defaultPath;
                }
            });
        }
        return textLoaders.get(pattern.toString());
    }

    private static Map<String, ITextLoader> textLoaders = new HashMap<String, ITextLoader>();
}
