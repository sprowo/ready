package com.prowo.ymlchain.yml.exception;

/**
 * yml 文件的classMap中的的类找不到
 * 
 * @author Hua Gong
 * @see<a href="mailto:kagowu@126.com?subject=中国自贸网技术支持"/>技术支持</a>
 * @see<a href="http://www.cn-zimao.com/">中国自贸网</a>
 * 
 */
public class YmlClassNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public YmlClassNotFoundException(String mesage) {
        super(mesage);
    }
}
