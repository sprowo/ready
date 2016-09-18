package com.prowo.test.mock;

import org.springframework.mock.web.MockHttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class HttpServletRequestMock extends MockHttpServletRequest {
    public String getCharacterEncoding() {
        return "UTF-8";
    }

    /**
     * @param queryStr
     * @see org.springframework.mock.web.MockHttpServletRequest#setQueryString(String)
     */
    public void setQueryString(String queryStr) {
        String[] key_vals = queryStr.split("&");
        String key = null;
        String value = null;

        for (String key_val : key_vals) {
            String[] temp_key_val = key_val.split("=");
            switch (temp_key_val.length) {
                case 1:
                    key = temp_key_val[0];
                    value = "";
                    break;
                case 2:
                default:
                    key = temp_key_val[0];
                    value = temp_key_val[1];
            }
            try {
                setParameter(key, URLDecoder.decode(value, getCharacterEncoding()));
            } catch (UnsupportedEncodingException e) {
                setParameter(key, value);
            }
        }
    }
}