package com.digdes.school;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class EvaluateExpression {
    private static final List<Pattern> patterns = new ArrayList<>(
            List.of(Pattern.compile("!="),
                    Pattern.compile(">="),
                    Pattern.compile("<="),
                    Pattern.compile("="),
                    Pattern.compile(">"),
                    Pattern.compile("<"),
                    Pattern.compile("ilike"),
                    Pattern.compile("like")
            ));

//    public static void main(String[] args) {
////        String expression = "'abc' = 'cde' or (10.25 >= 10.25 and 0 = 0)";
//        String expression = "5 = null or 5!=null";
//        System.out.println(evaluateExpression(expression));
//    }

    public static boolean evaluateExpression(String expression) {
        expression = formatExpression(expression);
//        System.out.println(expression);

        Stack<String> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            if (token.length() == 0) {
                continue;
            } else if (token.equals("or")) {
                while (!operatorStack.isEmpty() && (operatorStack.peek().equals("or") || operatorStack.peek().equals("and"))) {
                    processAnOperator(operandStack, operatorStack);
                }
                operatorStack.push(token);
            } else if (token.equals("and")) {
                while (!operatorStack.isEmpty() && (operatorStack.peek().equals("and"))) {
                    processAnOperator(operandStack, operatorStack);
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.peek().equals("(")) {
                    processAnOperator(operandStack, operatorStack);
                }

                operatorStack.pop();
            }
            else {
                operandStack.push(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            processAnOperator(operandStack, operatorStack);
        }

        String result = operandStack.pop();
        if (!(result.equals("true") || result.equals("false"))) {
            return calculateBoolean(result);
        }
        return Boolean.parseBoolean(result);
    }

    private static void processAnOperator(Stack<String> operandStack, Stack<String> operatorStack) {
        String operator = operatorStack.pop();
        String operand1 = operandStack.pop();
        String operand2 = operandStack.pop();

        if (operator.equals("or")) {
            boolean boolean1 = calculateBoolean(operand1);
            boolean boolean2 = calculateBoolean(operand2);
            operandStack.push((boolean1 || boolean2) + "");
        } else if (operator.equals("and")) {
            boolean boolean1 = calculateBoolean(operand1);
            boolean boolean2 = calculateBoolean(operand2);
            operandStack.push((boolean1 && boolean2) + "");
        }
    }

    private static boolean calculateBoolean(String expression) {
        if (expression.equals("true")) return true;
        if (expression.equals("false")) return false;


        Pattern foundPattern = patterns.stream()
                .filter(pattern -> expression.matches("(.*)" + pattern.toString() + "(.*)"))
                .findFirst().orElseThrow();

        String[] ops = expression.split(foundPattern.toString());

        switch (foundPattern.toString()) {
            case "!=" -> {
                return !ops[0].equals(ops[1]);
            }
            case "=" -> {
                return ops[0].equals(ops[1]);
            }
            case "like" -> {
                ops[0] = ops[0].substring(ops[0].indexOf('\'') + 1, ops[0].lastIndexOf('\''));
                ops[1] = ops[1].substring(ops[1].indexOf('\'') + 1, ops[1].lastIndexOf('\''));
                return searchByTemplate(ops);
            }
            case "ilike" -> {
                ops[0] = ops[0].substring(ops[0].indexOf('\'') + 1, ops[0].lastIndexOf('\'')).toLowerCase();
                ops[1] = ops[1].substring(ops[1].indexOf('\'') + 1, ops[1].lastIndexOf('\'')).toLowerCase();
                return searchByTemplate(ops);
            }
            case ">=" -> {
                return compareNumericOperands(ops, ">=");
            }
            case "<=" -> {
                return compareNumericOperands(ops, "<=");
            }
            case "<" -> {
                return compareNumericOperands(ops, "<");
            }
            case ">" -> {
                return compareNumericOperands(ops, ">");
            }

        }
        return false;
    }

    private static boolean searchByTemplate(String[] ops) {
        if (ops[1].endsWith("%") && ops[1].startsWith("%")) {
            return ops[0].contains(ops[1].replace("%", ""));
        } else if (ops[1].endsWith("%")) {
            return ops[0].startsWith(ops[1].replace("%", ""));

        } else if (ops[1].startsWith("%")) {
            return ops[0].endsWith(ops[1].replace("%", ""));
        }
        return ops[0].equals(ops[1]);
    }

    private static boolean compareNumericOperands(String[] operands, String operator) {
        if (operands[0].equals("null") || operands[1].equals("null")) {
            return false;
        }
        try {
            BigDecimal op0, op1;
            if (operands[0].contains(".") || operands[1].contains(".")) {
                 op0 = BigDecimal.valueOf(Double.parseDouble(operands[0]));
                 op1 = BigDecimal.valueOf(Double.parseDouble(operands[1]));
            } else {
                op0 = BigDecimal.valueOf(Long.parseLong(operands[0]));
                op1 = BigDecimal.valueOf(Long.parseLong(operands[1]));
            }
            switch (operator) {
                case (">=") -> {
                    return op0.compareTo(op1) >= 0;
                }
                case ("<=") -> {
                    return op0.compareTo(op1) <= 0;
                }
                case (">") -> {
                    return op0.compareTo(op1) > 0;

                }
                case ("<") -> {
                    return op0.compareTo(op1) < 0;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(operator + "can be applied only to Double or Long types");
        }
        return false;
    }

    private static String formatExpression(String expression) {
        String formattedExpression = expression.replace(" ", "");
        formattedExpression = formattedExpression.replace("and", " and ");
        formattedExpression = formattedExpression.replace("or", " or ");
        formattedExpression = formattedExpression.replace("(", " ( ");
        formattedExpression = formattedExpression.replace(")", " ) ");
        return formattedExpression;
    }
}
