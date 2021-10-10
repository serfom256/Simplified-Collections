package tests.DynamicStringTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import tests.Trie.TrieTests;

public class DynamicStringTestsRunner {
    public static void main(String[] args) {
        runTrieTest();
    }

    private static void runTrieTest() {
        Result result = JUnitCore.runClasses(DynamicLinkedStringTests.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("DynamicString tests" + (result.wasSuccessful() ? " was successful" : "was failed"));
    }

}

