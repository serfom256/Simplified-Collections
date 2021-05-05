package tests.SetTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class SetTestsRunner {
    public static void main(String[] args) {
        runSetTest();
        runSortedSetTest();
    }
    private static void runSetTest() {
        Result result = JUnitCore.runClasses(SetTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Set tests"+(result.wasSuccessful() ? " was successful" : "was failed"));
    }

    private static void runSortedSetTest() {
        Result result = JUnitCore.runClasses(SortedSetTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("SortedSet tests" + (result.wasSuccessful() ? " was successful" : "  was failed"));
    }
}
