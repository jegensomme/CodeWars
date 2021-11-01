package com.jegensomme.codewars;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 4kyu
 * https://www.codewars.com/kata/525c7c5ab6aecef16e0001a5
 */
class Parser {

    public static int parseInt(String numStr) {
        List<Integer> nums = Arrays.stream(numStr.split(" and |[ -]"))
                .map(nStr -> StringNumber.valueOf(nStr.toUpperCase()).value)
                .collect(Collectors.toList());
        int i = 0;
        int sum = 0;
        while (i < nums.size()) {
            int numPart = (i == 0 ? nums.get(i++) : 0);
            while (i < nums.size() && nums.get(i) < nums.get(i - 1)) {
                numPart += nums.get(i++);
            }
            sum = (i == nums.size() ? sum + numPart
                    : sum < nums.get(i) ? (sum + numPart) * nums.get(i++)
                    : sum + numPart * nums.get(i++));
        }
        return sum;
    }

    private enum StringNumber {
        ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9),
        TEN(10), ELEVEN(11), TWELVE(12), THIRTEEN(13), FOURTEEN(14), FIFTEEN(15), SIXTEEN(16), SEVENTEEN(17), EIGHTEEN(18), NINETEEN(19),
        TWENTY(20), THIRTY(30), FORTY(40), FIFTY(50), SIXTY(60), SEVENTY(70), EIGHTY(80), NINETY(90), HUNDRED(100), THOUSAND(1000), MILLION(1000000);

        int value;

        StringNumber(int value) {
            this.value = value;
        }
    }
}