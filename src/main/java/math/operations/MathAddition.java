package math.operations;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;

public class MathAddition implements AbstractOperation {

    @Override
    public String calculate(String s1, String s2) {
        AbstractDynamicString result = new DynamicLinkedString();
        int carry = 0;
        for (int i = s1.length() - 1, j = s2.length() - 1; i >= 0 || j >= 0; i--, j--) {
            if (i >= 0) carry += s1.charAt(i) - '0';
            if (j >= 0) carry += s2.charAt(j) - '0';
            result.add(carry % 10);
            carry /= 10;
        }
        if (carry != 0) result.add(carry);
        return result.reverse().toString();
    }
}
