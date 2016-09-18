package com.prowo.ydnamic.persist;

import com.prowo.persist.KoC;

/**
 * 条件拼接（OR）用对象
 */
public class OrKoC extends KoC {

    public OrKoC(String key, Object value) {
        super(key, value);
    }

    public OrKoC(String key, String optor, Object value) {
        super(key, optor, value);
    }

}
