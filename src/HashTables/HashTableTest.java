package HashTables;


import org.junit.Test;
import tests.HashTableTests.HashTable;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.Assert.assertEquals;


public class HashTableTest {
    HashTable<String, String> hashTable;

    public HashTableTest() {
        this.hashTable = new HashTable<>();
    }

    private String getRandomString(int size) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] string = new byte[size];
        secureRandom.nextBytes(string);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(string);
    }

    @Test
    public void add() {
        for (int i = 0; i < 10; i++) {
            hashTable.add(getRandomString(5), i + "_");
        }
        assertEquals(10, hashTable.getSize());
    }

    @Test
    public void clear() {
        for (int i = 0; i < 10; i++) {
            hashTable.add(getRandomString(5), i + "_");
        }
        hashTable.clear();
        assertEquals(0, hashTable.getSize());
    }

    @Test
    public void getSize() {
        for (int i = 0; i < 10; i++) {
            hashTable.add(getRandomString(5), i + "_");
        }
        assertEquals(10, hashTable.getSize());
    }

    // TODO implement iterable interface and Iterator and SelfIterator classes to iterate over the hashTable keys and values
    @Test
    public void getKeysCount() {
        for (int i = 0; i < 10; i++) {
            hashTable.add(getRandomString(4), i + "_");
        }
    }

}