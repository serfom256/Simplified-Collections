package tests.Trie;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TrieTestsRunner {
    public static void main(String[] args) {
        runTrieTest();
        runFuzzyTrieTest();
        runTrieMapTest();
        runExtendedTrieTest();
    }

    private static void runTrieTest() {
        Result result = JUnitCore.runClasses(TrieTests.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Trie tests" + (result.wasSuccessful() ? " was successful" : " was failed"));
    }

    private static void runFuzzyTrieTest() {
        Result result = JUnitCore.runClasses(FuzzySearchTrieTests.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Fuzzy Trie tests" + (result.wasSuccessful() ? " was successful" : " was failed"));
    }

    private static void runTrieMapTest() {
        Result result = JUnitCore.runClasses(TrieMapTests.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Trie Map tests" + (result.wasSuccessful() ? " was successful" : " was failed"));
    }

    private static void runExtendedTrieTest() {
        Result result = JUnitCore.runClasses(ExtendedTrieTests.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Extended Trie tests" + (result.wasSuccessful() ? " was successful" : " was failed"));
    }

}
