package com.prowo.ydnamic.context;

import java.util.List;

/**
 * 加载下拉选单的option选项
 */
public interface OptionLoader {

    /**
     * 加载可选项
     *
     * @param factor 查询因子
     * @return
     */
    public List<Option> load(String factor);

}
