package com.jegensomme.codewars;

import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * https://www.codewars.com/kata/5235c913397cbf2508000048
 */
class Calculator {
    public static Double evaluate(String expression) {
        return Double.parseDouble(calculate(Arrays.stream(expression.split(" \\+ "))
                .map(e -> Arrays.stream(e.split(" - "))
                        .map(e1 -> calculate(e1, Operation.MULTIPLYING, Operation.DIVISION))
                        .collect(Collectors.joining(" - ")))
                .collect(Collectors.joining(" + ")), Operation.PLUS, Operation.MINUS));
    }

    private enum Operation {
        PLUS(" \\+ ", Double::sum),
        MINUS(" - ", (l, r) -> l - r),
        DIVISION(" / ", (l, r) -> l / r),
        MULTIPLYING(" \\* ", (l, r) -> l * r);

        private final String regExp;
        private final DoubleBinaryOperator op;

        Operation(String regExp, DoubleBinaryOperator op) {
            this.regExp = regExp;
            this.op = op;
        }

        public double apply(double left, double right) {
            return op.applyAsDouble(left, right);
        }

        public String regExp() {
            return regExp;
        }

        public static Operation of(String operation) {
            return "+".equals(operation) ? PLUS
                    : "-".equals(operation) ? MINUS
                    : "*".equals(operation) ? MULTIPLYING
                    : DIVISION;
        }
    }

    public static String calculate(String expression, Operation op1, Operation op2) {
        String[] operands = expression.split(String.join("|", op1.regExp(), op2.regExp()));
        if (operands.length == 1) {
            return expression;
        }
        Pattern pattern = Pattern.compile(String.join("|", op1.regExp().strip(), op2.regExp().strip()));
        String[] operators = pattern.matcher(expression)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining(";"))
                .split(";");
        double result = Double.parseDouble(operands[0]);
        for (int i = 1; i < operands.length; i++) {
            result = Operation.of(operators[i - 1]).apply(result, Double.parseDouble(operands[i]));
        }
        return Double.toString(result);
    }
}
