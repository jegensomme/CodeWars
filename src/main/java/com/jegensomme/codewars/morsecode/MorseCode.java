package com.jegensomme.codewars.morsecode;

import java.util.HashMap;
import java.util.Map;

public class MorseCode {
    public static String get(String ch) {
        return DICTIONARY.get(ch);
    }

    private static final Map<String, String> DICTIONARY = new HashMap<>() {{
        put(".-", "A");
        put("-...", "B");
        put(".--", "W");
        put("--.", "G");
        put("-..", "D");
        put(".", "E");
        put("...-", "V");
        put("--..", "Z");
        put("..", "I");
        put(".---", "J");
        put("-.-", "K");
        put(".-..", "L");
        put("--", "M");
        put("-.", "N");
        put("---", "O");
        put(".--.", "P");
        put(".-.", "R");
        put("...", "S");
        put("-", "T");
        put("..-", "U");
        put("..-.", "F");
        put("....", "H");
        put("-.-.", "C");
        put("--.-", "Q");
        put("-.--", "Y");
        put("-..-", "X");
        put("...---...", "SOS");
        put(".----", "1");
        put("..---", "2");
        put("...--", "3");
        put("....-", "4");
        put(".....", "5");
        put("-....", "6");
        put("--...", "7");
        put("---..", "8");
        put("----.", "9");
        put("----", "0");
        put(".-.-.-", ".");
        put("--..--", ",");
        put("---...", ":");
        put("-.-.-.", ";");
        put(".----.", "'");
        put(".-..-.", "\"");
        put("-....-", "-");
        put("-..-.", "/");
        put("..--.-", "_");
        put("..--..", "?");
        put("-.-.--", "!");
        put(".-.-.", "+");
        put(".--.-.", "@");
    }};
}
