package com.jegensomme.codewars;

import java.util.stream.Collectors;
import java.util.Arrays;

import static java.util.Comparator.comparingInt;

/**
 * 5kyu
 * https://www.codewars.com/kata/55c6126177c9441a570000cc
 */
class WeightSort {
    public static String orderWeight(String str) {
        return Arrays.stream(str.strip().split(" +"))
                .sorted(comparingInt(s -> Arrays.stream(((String) s).split(""))
                        .mapToInt(Integer::parseInt).sum())
                        .thenComparing(val -> (String) val))
                .collect(Collectors.joining(" "));
    }
}
