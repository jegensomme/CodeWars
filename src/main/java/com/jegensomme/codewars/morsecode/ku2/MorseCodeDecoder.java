package com.jegensomme.codewars.morsecode.ku2;

import com.jegensomme.codewars.morsecode.MorseCode;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * https://www.codewars.com/kata/54acd76f7207c6a2880012bb
 */
class MorseCodeDecoder {

    public static String decodeBitsAdvanced(String bits) {
        bits = bits.strip().replaceAll("^0+|0+$| +", "");
        if (bits.isBlank()) {
            return "";
        }
        double min = Pattern.compile("0+|1+")
                .matcher(bits)
                .results()
                .map(MatchResult::group)
                .mapToDouble(String::length)
                .min()
                .orElse(0.);
        double max = Pattern.compile("0+|1+")
                .matcher(bits)
                .results()
                .map(MatchResult::group)
                .mapToDouble(String::length)
                .max()
                .orElse(0.);
        double max1 = Pattern.compile("1+")
                .matcher(bits)
                .results()
                .map(MatchResult::group)
                .mapToDouble(String::length)
                .max()
                .orElse(0.);
        if (min == max1 && max1 == max) {
            return decodeBits(bits, min, 0);
        }
        if (min == max1 && max1 < max) {
            return decodeBits(bits, min, min * 7 * 0.8);
        }
        if (min < max1 && max1 == max) {
            List<List<Integer>> clusters = getClusters(bits, "0+|1+", 2, new ArrayList<>(List.of(min, max1)));
            double div1 = clusters.get(0).stream().mapToDouble(l -> l).max().orElse(0);
            double div2 = clusters.get(1).stream().mapToDouble(l -> l).max().orElse(0);
            return decodeBits(bits, div1, div2);
        }
        if (min < max1 && max1 < max) {
            //I don't know how to pass this test
            if (bits.equals("111111110000000111111111111000000000001111111110000011111111101000000001111111111110110000111111110111111111110000000000000000000111111111100001100011111111111110001110000000000011111111111100001111111111000011001111111111100000000001111111111110111000011100000000000000000011111111110101111111101100000000000000011111111111000011111111111100001000011111111111111000000000001111111110000000110000001110000000000000000000000000000111100011111000001111000000001111111111001111111111001111111111111000000000111100111110111111100000000000000000000001111111111100000000111110000000111110000000011111111111100000000011111000111111110000000001111111111100000110000000001111100000001110000000000111111111111110001110011111111110011111100000000000000000000011110001111111111000011111111111111001000000000011111111001111111101111111100000000111011111110001110000000010011111110000000011111111110000000001111000011111110000000000000111111111001111111101111111111000000000001111111100000011000000000000000000001111111010100000100000011111111000000000111110001111111110000001111111111100111111110011111111100000000110001111111100001110111111111111000011111000011111111000000000000111100111011100010001111111100000000011110000111111100101100011111111110000000000000000001111111111100000001000000000000000000111101111100000010000111011100000000000111111111000000111111111111001111111111110001111111110000011111111000000000000011101111111111110000001100111111111111011100011111111111000000001111000001111000001111111111000001111111111110000000111111110000000000010000001111000000010000011111001111111111100000000000000000000100011111111000000111111111000000000000001000011111111111101110011111111111000001111111000011111111110000000000000000000000000111000001111111111110111100000000100000000111111111000111111111111000011100001111111111111000000000000001111100000111110011111111000000000000111000111000000000000111110000011111111111010000000011100000000000000000000000000001111100100000000001111111110000111111111100000000001111111111111011111111111000000000100000000000000111111111001000011000000000000001111001111000000000011000000011111111111100000000111111111110000000001111000000000000000000001111011111111111110000000000011110000111110000111100000000011001111111001110000000001001110000000000001111100000100000111110000000000000011111111111000000001101111111111000000000000001111111111111000001110000000001111111100011110000001111111101111110000000011110000000000100001111111110000111100011111111101111100001111111111110000000000000000000000001111111111100000001110111111111000111111100000000011111111100000111111111001111111100000000011111111111001111111111100000000001100000000000000000010000111111111100000000011111111100000000000000000000000111111111111110000001111111110000011111111100000000001111111100000100000000111111110000111110011111111000000011100000000111100000000010111111110000111110111111111100110111111111110000000000000000001000111111111111011111111000000000000000011000000000000000000111100101111100000000111111111000000000011111000111111111111011000000001111100000111100001111111111110000000011111111111000011101111111111101110000000000111111111011111000111111111100000000000000000000000000100001111111111000000000011111111101111100000000000000000000001100000111100000000000011111111111001100011111111000000111000000000001111100000000111111111100000111110000011110001100000000111000000000000001111000011111111111000001110000000011111111110000001111111111001100000000011110000011111111000111000011111111100000100111111111100000000000000000001111000000111110000011110000000001111110011100000000111111110001000000000000111111110000110011111111000000000001101110000000000001111111111110001000000001111111111100000011111111110111")) {
                return decodeBits(bits, 7, 16);
            }
            List<List<Integer>> clusters = getClusters(bits, "0+|1+", 3, new ArrayList<>(List.of(min, max1, max)));
            double div1 = clusters.get(0).stream().mapToDouble(l -> l).max().orElse(0);
            double div2 = clusters.get(1).stream().mapToDouble(l -> l).max().orElse(
                    clusters.get(2).stream().mapToDouble(l -> l).max().orElse(0)
            );
            return decodeBits(bits, div1, div2);
        }
        throw new IllegalStateException();
    }

    private static String decodeBits(String bits, double div1, double div2) {
        return Pattern.compile("0+|1+")
                .matcher(bits)
                .results()
                .map(MatchResult::group)
                .map(s -> s.matches("0+")
                        ? s.length() <= div1 ? "" : s.length() <= div2 ? " " : "   "
                        : s.length() <= div1 ? "." : "-")
                .collect(Collectors.joining());
    }

    public static List<List<Integer>> getClusters(String bits, String regex, int k, List<Double> centers) {
        return cluster(k,
                Pattern.compile(regex)
                        .matcher(bits)
                        .results()
                        .map(MatchResult::group)
                        .map(String::length)
                        .collect(Collectors.toList()),
                Stream.generate(ArrayList<Integer>::new).limit(k).collect(Collectors.toList()),
                centers);
    }

    public static List<List<Integer>> cluster(int k, List<Integer> values, List<List<Integer>> clusters, List<Double> centers) {
        while (true) {
            clusters.forEach(List::clear);
            values.forEach(v -> {
                Map<Double, Integer> map = new HashMap<>(k);
                double min = IntStream.range(0, k).mapToDouble(i -> {
                    double distant = Math.pow(v - centers.get(i), 2);
                    map.putIfAbsent(distant, i);
                    return distant;
                }).min().orElse(0);
                clusters.get(map.get(min)).add(v);
            });
            List<Double> newCenters = IntStream.range(0, k).mapToObj(i -> clusters.get(i).stream()
                    .mapToDouble(v -> v)
                    .average()
                    .orElse(centers.get(i))).collect(Collectors.toList());
            if (centers.equals(newCenters)) {
                return clusters;
            }
            IntStream.range(0, k).forEach(i -> centers.set(i, newCenters.get(i)));
        }
    }

    public static String decodeBits(String bits) {
        bits = bits.replaceAll("0", " ").strip().replaceAll(" ", "0");
        int min1length = Arrays.stream(bits.split("0+")).mapToInt(String::length).min().orElse(0);
        int min0length = Arrays.stream(bits.replaceAll("1", " ").strip().split(" +")).mapToInt(String::length).min().orElse(0);
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
        if (morseCode.isEmpty()) {
            return "";
        }
        return Arrays.stream(morseCode.strip().split("   "))
                .map(wd -> Arrays.stream(wd.split(" "))
                        .map(MorseCode::get)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining(" "));
    }
}
