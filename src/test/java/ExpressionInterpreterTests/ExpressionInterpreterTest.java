package ExpressionInterpreterTests;

import math.ExpressionInterpreter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ExpressionInterpreterTest {

    private final ExpressionInterpreter interpreter;

    public ExpressionInterpreterTest() {
        this.interpreter = new ExpressionInterpreter();
    }

    @Test
    public void calculateExpressionTest() {
        assertEquals("-5", interpreter.calculate("1-(3+5-2+(3+19-(3-1-4+(9-4-(4-(1+(3)-2)-5)+8-(3-5)-1)-4)-5)-4+3-9)-4-(3+2-5)"));
        assertEquals("-1", interpreter.calculate("(5-(6))"));
        assertEquals("-1", interpreter.calculate("5-(6)"));
        assertEquals("-1", interpreter.calculate("-2-1+2"));
        assertEquals("10", interpreter.calculate("10"));
        assertEquals("23", interpreter.calculate("(1+(4+5+2)-3)+(6+8)"));
        assertEquals("15", interpreter.calculate("9-4-(4-5)+8-(3-5)-1"));
        assertEquals("-5", interpreter.calculate("1-(3+5-2+(3+19-(3-1-4+(9-4-(4-(1+(3)-2)-5)+8-(3-5)-1)-4)-5)-4+3-9)-4-(3+2-5)"));
        assertEquals("-35", interpreter.calculate("5+3-4-(1+2-7+(10-1+3+5+(3-0+(8-(3+(8-(10-(6-10-8-7+(0+0+7)-10+5-3-2+(9+0+(7+(2-(2-(9)-2+5+4+2+(2+9+1+5+5-8-9-2-9+1+0)-(5-(9)-(0-(7+9)+(10+(6-4+6))+0-2+(10+7+(8+(7-(8-(3)+(2)+(10-6+10-(2)-7-(2)+(3+(8))+(1-3-8)+6-(4+1)+(6))+6-(1)-(10+(4)+(8)+(5+(0))+(3-(6))-(9)-(4)+(2))))))-1)))+(9+6)+(0))))+3-(1))+(7))))))))"));
        assertEquals("-12", interpreter.calculate("- (3 + (4 + 5))"));
        assertEquals("-1", interpreter.calculate("-2-1+2"));
    }

}

