package tests.ExpressionEvaluatorTests;

import Math.ExpressionEvaluator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ExpressionEvaluatorTest {

    private final ExpressionEvaluator evaluator;

    public ExpressionEvaluatorTest() {
        this.evaluator = new ExpressionEvaluator();
    }

    @Test
    public void calculateExpressionTest() {
        //FIXME fix mathSum and mathSub
        assertEquals("10", evaluator.calculate("10"));
        assertEquals("23", evaluator.calculate("(1+(4+5+2)-3)+(6+8)"));
        assertEquals("3", evaluator.calculate("2-(5-6))"));
        assertEquals("-15", evaluator.calculate("1-(3+5-2+(3+19-(3-1-4+(9-4-(4-(1+(3)-2)-5)+8-(3-5)-1)-4)-5)-4+3-9)-4-(3+2-5)-10"));
        assertEquals("35", evaluator.calculate("5+3-4-(1+2-7+(10-1+3+5+(3-0+(8-(3+(8-(10-(6-10-8-7+(0+0+7)-10+5-3-2+(9+0+(7+(2-(2-(9)-2+5+4+2+(2+9+1+5+5-8-9-2-9+1+0)-(5-(9)-(0-(7+9)+(10+(6-4+6))+0-2+(10+7+(8+(7-(8-(3)+(2)+(10-6+10-(2)-7-(2)+(3+(8))+(1-3-8)+6-(4+1)+(6))+6-(1)-(10+(4)+(8)+(5+(0))+(3-(6))-(9)-(4)+(2))))))-1)))+(9+6)+(0))))+3-(1))+(7))))))))"));
        assertEquals("-1", evaluator.calculate("(5-(1+(5)))"));
        assertEquals("-12", evaluator.calculate("- (3 + (4 + 5))"));
        assertEquals("-1", evaluator.calculate("-2-1+2"));
    }

}
