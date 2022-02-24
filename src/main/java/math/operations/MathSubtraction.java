package math.operations;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;

public class MathSubtraction implements AbstractOperation {

    private final MathMin mathMin;
    private final MathMax mathMax;

    public MathSubtraction() {
        this.mathMax = new MathMax();
        this.mathMin = new MathMin();
    }

    @Override
    public String calculate(String s1, String s2) {
        if (s1.equals(s2)) return "0";
        int carry = 0;
        String max = new DynamicLinkedString(mathMax.calculate(s1, s2)).reverse().toString();
        String min = mathMin.calculate(s1, s2);
        boolean isMin = min.equals(s1);
        min = new DynamicLinkedString(min).reverse().toString();
        int maxLen = max.length(), minLen = min.length();
        AbstractDynamicString result = new DynamicLinkedString();
        for (int i = 0; i < minLen; i++) {
            carry += (max.charAt(i) - '0') - (min.charAt(i) - '0');
            if (carry < 0) {
                result.add(10 + carry);
                carry -= 10;
            } else {
                result.add(carry);
                carry = 0;
            }
            carry /= 10;
        }
        carry = Math.abs(carry);
        for (int i = minLen; i < maxLen; i++) {
            int sub = (max.charAt(i) - '0') - carry;
            carry = sub < 0 ? 1 : 0;
            if (sub < 0) sub += 10;
            result.add(sub);
        }
        while (result.endsWith('0')) {
            result.deleteLast();
        }
        result.reverse();
        if (isMin) result.addFirst('-');
        return result.toString();
    }
}
