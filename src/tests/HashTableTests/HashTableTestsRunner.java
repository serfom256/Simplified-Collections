package tests.HashTableTests;

import HashTables.HashTableTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class HashTableTestsRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(HashTableTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());

    }
}
