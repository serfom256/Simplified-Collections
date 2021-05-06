package tests.ListTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class ListsTestsRunner {
    public static void main(String[] args) {
        runLinkedListTest();
        runDoublyLinkedListTest();
    }

    private static void runLinkedListTest() {
        Result result = JUnitCore.runClasses(LinkedListTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("LinkedList tests" + (result.wasSuccessful() ? " was successful" : "was failed"));
    }

    private static void runDoublyLinkedListTest() {
        Result result = JUnitCore.runClasses(DoubleLinkedListTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("DoublyLinkedList tests" + (result.wasSuccessful() ? " was successful" : "  was failed"));
    }
}
