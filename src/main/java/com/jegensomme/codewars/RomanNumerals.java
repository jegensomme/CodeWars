package com.jegensomme.codewars;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 4kyu
 * https://www.codewars.com/kata/51b66044bce5799a7f000003
 */
public class RomanNumerals {

    public static String toRoman(int n) {
        n = n < 10000 ? n : 9999;
        return Stream.builder()
                .add(Stream.generate(Roman.M::toString).limit(n / 1000).collect(Collectors.joining()))
                .add(Roman.arrange(Roman.C, Roman.D, Roman.M, (n % 1000) / 100))
                .add(Roman.arrange(Roman.X, Roman.L, Roman.C, (n % 100) / 10))
                .add(Roman.arrange(Roman.I, Roman.V, Roman.X, n % 10))
                .build()
                .map(s -> (String) s)
                .collect(Collectors.joining());
    }

    public static int fromRoman(String romanNumeral) {
        String[] romanNums =romanNumeral.split("");
        return IntStream.rangeClosed(0, romanNums.length - 1)
                .map(i -> {
                    int intVal = Roman.valueOf(romanNums[i]).intVal;
                    int intValNext = (i == romanNums.length - 1 ? 0 :Roman.valueOf(romanNums[i + 1]).intVal);
                    return intVal < intValNext ? -intVal : intVal;
                }).sum();
    }

    private enum Roman {
        I(1),
        V(5),
        X(10),
        L(50),
        C(100),
        D(500),
        M(1000);

        int intVal;

        Roman(int intVal) {
            this.intVal = intVal;
        }

        static String arrange(Roman val, Roman middle, Roman next, int count) {
            return count == 0 ? ""
                    : count < 4 ? Stream.generate(val::toString).limit(count).collect(Collectors.joining())
                    : count == 4 ? Stream.builder()
                    .add(val.toString())
                    .add(middle.toString())
                    .build().map(s -> (String) s).collect(Collectors.joining())
                    : count == 5 ? middle.toString()
                    : count < 9 ? Stream.builder()
                    .add(middle.toString())
                    .add(Stream.generate(val::toString).limit(count - 5).collect(Collectors.joining()))
                    .build().map(s -> (String) s).collect(Collectors.joining())
                    : Stream.builder()
                    .add(val.toString())
                    .add(next.toString())
                    .build().map(s -> (String) s).collect(Collectors.joining());
        }
    }
}
