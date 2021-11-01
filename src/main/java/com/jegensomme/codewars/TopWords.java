package com.jegensomme.codewars;

import java.util.*;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * https://www.codewars.com/kata/51e056fe544cf36c410000fb
 */
class TopWords {
    public static List<String> top3(String s) {
        return Pattern.compile("[a-z][a-z']*").matcher(s.toLowerCase()).results()
                .map(MatchResult::group)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());
    }
}
