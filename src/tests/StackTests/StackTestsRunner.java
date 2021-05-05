package tests.StackTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class StackTestsRunner {
    public static void main(String[] args) {
        runStackTest();
        runLinkedStackTest();
    }
    private static void runStackTest() {
        Result result = JUnitCore.runClasses(StackTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Set tests"+(result.wasSuccessful() ? " was successful" : "was failed"));
    }

    private static void runLinkedStackTest() {
        Result result = JUnitCore.runClasses(LinkedStackTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("SortedSet tests" + (result.wasSuccessful() ? " was successful" : "  was failed"));
    }
}
