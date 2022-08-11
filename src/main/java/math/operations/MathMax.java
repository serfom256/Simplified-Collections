package math.operations;

public class MathMax implements MathOperation {

    @Override
    public String calculate(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return s1.length() < s2.length() ? s2 : s1;
        }
        for (int i = 0; i < s1.length(); i++)
            if (s1.charAt(i) != s2.charAt(i)) {
                return s1.charAt(i) < s2.charAt(i) ? s2 : s1;
            }
        return s1;
    }
}
