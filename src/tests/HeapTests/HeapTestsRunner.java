package tests.HeapTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class HeapTestsRunner {
    public static void main(String[] args) {
        runHeapTests();
        runPriorityQueueTests();

    }

    private static void runHeapTests() {
        Result result = JUnitCore.runClasses(HeapTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful() ? "Heap tests was successful" : "Heap tests was failed");
    }

    private static void runPriorityQueueTests() {
        Result result = JUnitCore.runClasses(PriorityQueueTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("PriorityQueue tests" + (result.wasSuccessful() ? " was successful" : "  was failed"));
    }
}
