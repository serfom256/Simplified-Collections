package Math;

import Additional.DynamicString.AbstractDynamicString;
import Additional.DynamicString.DynamicLinkedString;
import HashSet.AbstractSet;
import HashSet.Set;
import Lists.AbstractLinkedList;
import Lists.AbstractList;
import Lists.impl.ArrayList;
import Lists.impl.DoubleLinkedList;
import Math.MathOperations.*;
import HashTables.HashTable;
import Stack.ArrayStack;


public class ExpressionInterpreter {

    private final HashTable<Character, AbstractOperation> charSet = new HashTable<>(8);
    private final AbstractSet<Character> primaryOperations = new Set<>();
    private final AbstractSet<Character> planeOperations = new Set<>();

    public ExpressionInterpreter() {
        charSet.add('+', new MathSum());
        charSet.add('-', new MathSubtraction());
        charSet.add('*', new MathMultiply());
        charSet.add('/', new MathDivision());

        planeOperations.add('+');
        planeOperations.add('-');
        primaryOperations.add('*');
        primaryOperations.add('/');
    }


    public String calculate(String s) {
        return parse(new DynamicLinkedString(s.replaceAll(" ", ""))).toString();
    }

    private AbstractDynamicString parse(DynamicLinkedString exp) {
        ArrayStack<Integer> lastPos = new ArrayStack<>(), lastLstPos = new ArrayStack<>();
        ArrayList<String> lst = new ArrayList<>();
        AbstractLinkedList<Integer> primaryOperationsPos = new DoubleLinkedList<>();
        DynamicLinkedString currentNum = new DynamicLinkedString();
        for (int i = 0; i < exp.getSize(); i++) {
            char ch = exp.get(i);
            if (ch == '(') {
                lastLstPos.push(lst.getSize());
                lastPos.push(i);
            } else if (ch == ')') {
                int prevPos = lastPos.poll(), pPos = lastLstPos.poll();
                AbstractDynamicString subSeq = parse(exp.subSequence(prevPos + 1, i));
                exp.replace(prevPos, i + 1, subSeq.toString());
                lst.delete(pPos == 0 ? pPos : pPos - 1, lst.getSize());
                i = prevPos - 2;
                currentNum.clear();
                if (i < -1) i = -1;
                while (primaryOperationsPos.getSize() != 0 && primaryOperationsPos.getLast() > i)
                    primaryOperationsPos.removeLast();
            } else if (charSet.containsKey(ch) && i != 0) {
                if (currentNum.getSize() != 0) lst.add(currentNum.toString());
                if (primaryOperations.contains(ch)) {
                    if (primaryOperationsPos.getSize() == 0 || primaryOperationsPos.get(primaryOperationsPos.getSize() - 1) != i) {
                        primaryOperationsPos.add(lst.getSize());
                    }
                }
                lst.add(String.valueOf(ch));
                currentNum.clear();
            } else {
                currentNum.add(ch);
            }
        }
        if (currentNum.getSize() != 0) lst.add(currentNum.toString());
        return evaluateExp(lst, primaryOperationsPos);
    }

    private AbstractDynamicString evaluateExp(AbstractList<String> exp, AbstractList<Integer> pPos) {
        if (exp.getSize() <= 2) {
            DynamicLinkedString s = new DynamicLinkedString();
            for (String i : exp) s.add(i);
            return s;
        }
        int temp = 0;
        for (Integer i : pPos) {
            int gap = exp.getSize();
            i -= temp;
            exp.replace(i - 1, Math.min(i + 2, exp.getSize()), calculateExp(exp.get(i - 1), exp.get(i).charAt(0), exp.get(i + 1)));
            temp += gap - exp.getSize();
        }
        char prevChar = ' ';
        for (int i = 0; i < exp.getSize(); i++) {
            char ch = exp.get(i).charAt(0);
            if (planeOperations.contains(prevChar) && planeOperations.contains(ch) && exp.get(i - 1).length() == 1) {
                ch = collapseCharacters(prevChar, ch);
                exp.replace(i - 1, i + 1, String.valueOf(ch));
                i--;
            } else if (i > 1 && !planeOperations.contains(ch)) {
                String e = calculateExp(exp.get(i - 2), prevChar, exp.get(i));
                exp.replace(i - 2, i + 1, e);
                i -= e.length() + 1;
                ch = ' ';
                if (i < -1) i = -1;
            }
            prevChar = ch;
        }
        return new DynamicLinkedString(exp.get(0));
    }


    private char collapseCharacters(char c1, char c2) {
        if (c1 == '-' && c2 == '-') return '+';
        if (c1 == '-' || c2 == '-') return '-';
        if (c1 == '+' || c2 == '+') return '+';
        throw new RuntimeException();
    }

    private String calculateExp(String v1, char sym, String v2) {
        String f = "";
        if (v1.charAt(0) == '-') {
            v1 = v1.substring(1);
            if (sym == '+') {
                String temp = v1;
                v1 = v2;
                v2 = temp;
                sym = '-';
            } else {
                sym = '+';
                f = "-";
            }
        }
        return f + charSet.get(sym).calculate(v1, v2);
    }
}


