package tests.Trie;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TrieTestsRunner {
    public static void main(String[] args) {
        runTrieTest();
    }

    private static void runTrieTest() {
        Result result = JUnitCore.runClasses(TrieTests.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Trie tests" + (result.wasSuccessful() ? " was successful" : "was failed"));
    }

}
