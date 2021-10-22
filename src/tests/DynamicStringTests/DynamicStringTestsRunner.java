package tests.DynamicStringTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class DynamicStringTestsRunner {
    public static void main(String[] args) {
        runLinkedDSTest();
        runArrayDSTest();
    }

    private static void runLinkedDSTest() {
        Result result = JUnitCore.runClasses(DynamicLinkedStringTests.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Linked DynamicString tests" + (result.wasSuccessful() ? " was successful" : "was failed"));
    }

    private static void runArrayDSTest() {
        Result result = JUnitCore.runClasses(DynamicLinkedStringTests.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Array DynamicString tests" + (result.wasSuccessful() ? " was successful" : "was failed"));
    }

}

