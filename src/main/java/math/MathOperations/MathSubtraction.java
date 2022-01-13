package math.MathOperations;

import additional.DynamicString.AbstractDynamicString;
import additional.DynamicString.DynamicLinkedString;

public class MathSubtraction implements AbstractOperation {

    @Override
    public String calculate(String s1, String s2) {
        if (s1.equals(s2)) return "0";
        int carry = 0;
        String max = new MathMax().calculate(s1, s2), min = new MathMin().calculate(s1, s2);
        AbstractDynamicString result = new DynamicLinkedString();
        for (int i = max.length() - 1, j = min.length() - 1; j >= 0; i--, j--) {
            carry += (max.charAt(i) - '0') - (min.charAt(j) - '0');
            if (carry < 0) {
                result.add(10 + carry);
                carry -= 10;
            } else {
                result.add(carry);
                carry = 0;
            }
            carry /= 10;
        }

        for (int i = max.length(); i > min.length(); i--) {
            int end = (max.charAt(max.length() - i) - '0') + carry;
            carry = end < 0 ? 1 : 0;
            result.add(end % 10);
        }
        while (result.endsWith('0')) {
            result.deleteLast();
        }
        result.reverse();
        if (s1.equals(min)) result.insert(0, '-');
        return result.toString();
    }

}
