package math.operations;

public class MathDivision implements MathOperation {

    private final NumberComparator comparator;
    private final MathSubtraction subtraction;
    private final MathAddition addition;

    public MathDivision() {
        this.comparator = new NumberComparator();
        this.subtraction = new MathSubtraction();
        this.addition = new MathAddition();
    }

    @Override
    public String calculate(String s1, String s2) {
        String result = "0";
        while (comparator.compare(s1, s2) >= 0) {
            s1 = subtraction.calculate(s1, s2);
            result = addition.calculate(result, "1");
        }
        return result;
    }
}
