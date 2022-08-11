package math.operations;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;

public class MathMultiply implements MathOperation {

    @Override
    public String calculate(String s1, String s2) {
        int[] d = new int[s1.length() + s2.length()];
        DynamicString result = new DynamicLinkedString();
        for (int i = s1.length() - 1; i >= 0; i--) {
            for (int j = s2.length() - 1; j >= 0; j--) {
                int curr = d.length - 1 - (i + j + 1);
                int cVal = d[curr] + (s1.charAt(i) - '0') * (s2.charAt(j) - '0');
                d[curr + 1] += cVal / 10;
                d[curr] = cVal % 10;
            }
        }
        int i = d.length - 1;
        while (d[i] == 0) i--;
        for (int j = i; j >= 0; j--) {
            result.add(d[j]);
        }
        return result.toString();
    }
}
