package com.suslovila.utils.collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {
    public static List<Integer> getNumbersInRange(int start, int end) {
        List<Integer> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(i);
        }
        return result;
    }
}
