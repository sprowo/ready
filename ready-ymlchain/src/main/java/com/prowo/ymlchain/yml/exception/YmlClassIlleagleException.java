package com.prowo.ymlchain.yml.exception;

/**
 * yml 文件中classMap中的类访问异常
 * 
 * @author Hua Gong
 * @see<a href="mailto:kagowu@126.com?subject=中国自贸网技术支持"/>技术支持</a>
 * @see<a href="http://www.cn-zimao.com/">中国自贸网</a>
 * 
 */
public class YmlClassIlleagleException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public YmlClassIlleagleException(String mesage) {
        super(mesage);
    }
}
