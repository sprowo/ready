package com.prowo.ydnamic.persist;

import com.prowo.persist.KoC;
import com.prowo.persist.Objectx;
import com.prowo.persist.ProtasisSplicer;

public class ProtasisSplicerMySQLImpl extends Objectx implements ProtasisSplicer {

    @Override
    public String splice(KoC koc) {
        if (koc instanceof OrKoC) {
            if (koc.getOptor().toLowerCase().contains("like")) {
                return " OR " + koc.getKey() + " " + koc.getOptor() + " CONCAT('%',?,'%')";
            } else {
                return " OR " + koc.getKey() + " " + koc.getOptor() + " ?";
            }
        } else {
            if (koc.getOptor().toLowerCase().contains("like")) {
                return " AND " + koc.getKey() + " " + koc.getOptor() + " CONCAT('%',?,'%')";
            } else {
                return " AND " + koc.getKey() + " " + koc.getOptor() + " ?";
            }
        }

    }

}
