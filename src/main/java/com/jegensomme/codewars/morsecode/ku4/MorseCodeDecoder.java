package com.jegensomme.codewars.morsecode.ku4;

import com.jegensomme.codewars.morsecode.MorseCode;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * https://www.codewars.com/kata/54b72c16cd7f5154e9000457
 */
class MorseCodeDecoder {
    public static String decodeBits(String bits) {
        bits = bits.replaceAll("^0+|0+$", "");
        int min1length = Arrays.stream(bits.split("0+")).mapToInt(String::length).min().orElse(0);
        int min0length = Arrays.stream(bits.replaceAll("^1+|1+$", "").split("1+")).mapToInt(String::length).min().orElse(0);
        int timeUnit = min1length == 0 ? min0length
                : min0length == 0 ? min1length :
                Math.min(min0length, min1length);
        return bits.replaceAll(String.format("1{%d}", timeUnit * 3), "-")
                .replaceAll(String.format("1{%d}", timeUnit), ".")
                .replaceAll(String.format("0{%d}", timeUnit * 7), "   ")
                .replaceAll(String.format("0{%d}", timeUnit * 3), " ")
                .replaceAll(String.format("0{%d}", timeUnit), "");
    }

    public static String decodeMorse(String morseCode) {
        return Arrays.stream(morseCode.strip().split("   "))
                .map(wd -> Arrays.stream(wd.split(" "))
                        .map(MorseCode::get)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining(" "));
    }
}
