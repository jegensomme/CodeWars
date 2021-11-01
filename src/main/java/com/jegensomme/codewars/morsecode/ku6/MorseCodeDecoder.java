package com.jegensomme.codewars.morsecode.ku6;

import com.jegensomme.codewars.morsecode.MorseCode;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * https://www.codewars.com/kata/54b724efac3d5402db00065e
 */
public class MorseCodeDecoder {
    public static String decode(String morseCode) {
        return Arrays.stream(morseCode.strip().split("   "))
                .map(wd -> Arrays.stream(wd.split(" "))
                        .map(MorseCode::get)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining(" "));
    }
}
