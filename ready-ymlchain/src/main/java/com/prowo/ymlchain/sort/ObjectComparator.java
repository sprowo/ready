package com.prowo.ymlchain.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ObjectComparator implements Comparator<Object> {
    private boolean asc;

    public ObjectComparator(String order) {
        this.asc = "asc".equalsIgnoreCase(order);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return 0;
        }
        if (o1 instanceof Comparable && o2 instanceof Comparable) {
            return asc ? ((Comparable<Object>) o1).compareTo((Comparable<Object>) o2) : ((Comparable<Object>) o2)
                    .compareTo((Comparable<Object>) o1);

        }
        return asc ? o1.toString().compareTo(o2.toString()) : o2.toString().compareTo(o1.toString());
    }

    public static void sort(List<Map<String, Object>> infoList, final String sort, final String order) {
        Collections.sort(infoList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return new ObjectComparator(order).compare(o1.get(sort), o2.get(sort));
            }
        });
    }

}