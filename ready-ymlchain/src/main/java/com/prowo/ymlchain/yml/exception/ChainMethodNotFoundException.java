package com.prowo.ymlchain.yml.exception;

/**
 * yml 文件的chainMaps中的方法不存在
 * 
 * @author Hua Gong
 * @see<a href="mailto:kagowu@126.com?subject=中国自贸网技术支持"/>技术支持</a>
 * @see<a href="http://www.cn-zimao.com/">中国自贸网</a>
 * 
 */
public class ChainMethodNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ChainMethodNotFoundException(String mesage) {
        super(mesage);
    }

}
