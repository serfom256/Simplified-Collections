package Math;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
import Math.MathOperations.*;
import HashTables.HashTable;

import java.util.Stack;

public class ExpressionEvaluator {

    private final HashTable<Character, AbstractOperation> charSet = new HashTable<>();

    public ExpressionEvaluator() {
        charSet.add('+', new MathSum());
        charSet.add('-', new MathSubtraction());
        charSet.add('*', new MathMultiply());
        charSet.add('/', new MathDivision());
    }


    public String calculate(String s) {
        return parse(new DynamicLinkedString(s.replaceAll(" ", ""))).toString();
    }

    private AbstractDynamicString parse(DynamicLinkedString exp) {
        Stack<Integer> lastPos = new Stack<>();
        lastPos.push(0);
        for (int i = 0; i < exp.getSize(); i++) {
            char ch = exp.get(i);
            if (ch == '(') lastPos.push(i);
            else if (ch == ')') {
                int prevPos = lastPos.pop() + 1, currPos = i;
                AbstractDynamicString subSeq = parse(new DynamicLinkedString(exp.subSequence(prevPos, currPos)));
                exp.replace(prevPos - 1, currPos + 1, subSeq.toString());
                i = -1;
            }
        }
        return evaluateExp(exp);
    }

    private AbstractDynamicString evaluateExp(AbstractDynamicString exp) {
        System.out.println(exp);
        Stack<Character> stack = new Stack<>();
        int sLen = 0;
        for (int i = 0; i < exp.getSize(); i++) {
            char c = exp.get(i);
            if (charSet.containsKey(c) && !stack.isEmpty()) {
                if (charSet.containsKey(stack.peek())) {
                    while (charSet.containsKey(stack.peek()) && i < exp.getSize() && charSet.containsKey(c)) {
                        stack.add(collapseCharacters(stack.pop(), c));
                        c = exp.get(++i);
                    }
                }
                sLen++;
            }
            if (charSet.containsKey(c) && sLen >= 2) {
                String[] r = createSimpleExp(stack);
                String evaluated = evaluate(r[0], r[1].charAt(0), r[2]);
                exp.replace(0, i, evaluated);
                for (int j = 0; j < evaluated.length(); j++) {
                    stack.add(evaluated.charAt(j));
                }
                i = evaluated.length();
                sLen = 1;
            }
            stack.add(c);
        }
        if (sLen >= 1) {
            String[] r = createSimpleExp(stack);
            if (r[0].length() == 0) return exp;
            exp.replace(0, exp.getSize(), evaluate(r[0], r[1].charAt(0), r[2]));
        }
        return exp;
    }

    private String[] createSimpleExp(Stack<Character> stack) {
        String[] result = new String[3];
        StringBuilder str = new StringBuilder();
        while (!stack.isEmpty()) {
            char c = stack.pop();
            if (!charSet.containsKey(c) || result[2] != null) {
                str.append(c);
            } else {
                result[2] = str.reverse().toString();
                str = new StringBuilder();
                result[1] = String.valueOf(c);
            }
        }
        result[0] = str.reverse().toString();
        return result;
    }


    private char collapseCharacters(char c1, char c2) {
        if (c1 == '-' && c2 == '-') return '+';
        if (c1 == '-' || c2 == '-') return '-';
        if (c1 == '+' || c2 == '+') return '+';
        throw new RuntimeException();
    }

    private String evaluate(String v1, char sym, String v2) {
        return charSet.get(sym).calculate(v1, v2);
        // throw new IllegalArgumentException("No such operation symbol");
    }

}


