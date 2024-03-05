package com.suslovila.utils.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {
    public static List<Integer> getNumbersInRange(int start, int end) {
        List<Integer> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(i);
        }
        return result;
    }
    public static<T> List<T> shuffle(List<T> list){
        Collections.shuffle(list);
        return list;
    }
}
