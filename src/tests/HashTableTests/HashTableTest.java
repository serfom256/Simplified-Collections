package tests.HashTableTests;


import HashTables.HashTable;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.Assert.*;


public class HashTableTest {
    HashTable<String, String> hashTable;

    public HashTableTest() {
        this.hashTable = new HashTable<>();
    }

    @Before
    public void setUp() {
        for (int i = 0; i < 10; i++) {
            hashTable.add(i + "_key", i + "_value");
        }
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
        assertEquals(20, hashTable.getSize());
    }

    @Test
    public void get() {
        for (int i = 10; i < 20; i++) {
            hashTable.add(i + "_key", i + "_value");
        }

        for (int i = 0; i < 20; i++) {
            assertEquals(i + "_value", hashTable.get(i + "_key"));
        }
    }

    @Test
    public void clear() {
        for (int i = 10; i < 20; i++) {
            hashTable.add(getRandomString(5), i + "_");
        }
        hashTable.clear();
        assertEquals(0, hashTable.getSize());
    }

    @Test
    public void getSize() {
        for (int i = 10; i < 20; i++) {
            hashTable.add(getRandomString(5), i + "_");
        }
        assertEquals(20, hashTable.getSize());
    }

    @Test
    public void removeValue() {
        String s = getRandomString(4);
        String[] testArr = {"12345", "54321", "00000", "99999", "111111"};
        for (String i : testArr) {
            hashTable.add(s, i);
        }
        for (String value : testArr) {
            assertEquals(value, hashTable.removeValue(s, value));
        }
        assertNull(testArr[0], hashTable.removeValue(s, testArr[0]));


        for (int i = 0; i < 10; i++) {
            assertEquals(i + "_value", hashTable.removeValue(i + "_key", i + "_value"));
        }
        assertEquals(0, hashTable.getSize());
    }

    @Test
    public void remove() {
        for (int i = 0; i < 100; i++) {
            hashTable.add(i + "_key", i + "_value");
        }
        int len = hashTable.getSize();
        for (int i = 0; i < len; i++) {
            assertEquals(i + "_value", hashTable.remove(i + "_key"));
        }
        assertEquals(0, hashTable.getSize());
    }

    @Test
    public void update() {
        int len = hashTable.getSize();
        for (int i = 0; i < len; i++) {
            assertEquals(i + "_value", hashTable.updateValue(i + "_key", i + "000"));
        }
    }

    @Test
    public void containsKey() {
        int len = hashTable.getSize();
        for (int i = 0; i < len; i++) {
            assertTrue(hashTable.containsKey(i + "_key"));
            assertFalse(hashTable.containsKey("0000"));
        }
    }

    @Test
    public void containsValue() {
        String[] values = new String[10];
        for (int i = 1000; i < 1010; i++) {
            String value = getRandomString(5);
            values[i - 1000] = value;
            hashTable.add(i + "_key", value);
        }
        for (String str : values) {
            assertTrue(hashTable.containsValue(str));
        }
    }

    @Test
    public void getKeyByValue() {
        String[] keys = new String[20], values = new String[20];
        for (int i = 0; i < 20; i++) {
            String value = getRandomString(5), key = "key_" + i;
            keys[i] = key;
            values[i] = value;
            hashTable.add(key, value);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals(keys[i], hashTable.getKeyByValue(values[i]));
        }
        assertEquals(30, hashTable.getSize());
    }

    @Test
    public void replace() {
        String[] newKeys = new String[20];
        for (int i = 0; i < 20; i++) {
            String newKey = getRandomString(4), oldKey = i + "_key";
            newKeys[i] = newKey;
            hashTable.add(oldKey, i + "_value");
        }
        for (int i = 0; i < newKeys.length; i++) {
            assertEquals(i + "_key", hashTable.replace(i + "_key", newKeys[i]));
        }

        assertNull(hashTable.replace("0_key", newKeys[0]));
        assertEquals(newKeys.length, hashTable.getSize());
    }

    @Test
    public void forEach() {
        String[] keySet = new String[10000];
        String[] values = new String[10000];
        for (int i = 0; i < keySet.length; i++) {
            keySet[i] = i + "_key";
            values[i] = i + "_value";
            hashTable.add(keySet[i], values[i]);
        }
        int count = 0;
        for (String s : hashTable) {
            for (int i = 0; i < keySet.length; i++) {
                if (keySet[i].equals(s)) {
                    count++;
                    assertEquals(values[i], hashTable.get(s));
                    break;
                }
            }
        }
        assertEquals(keySet.length, count);
    }

    @Test
    public void itemsForEach() {
        String[] keySet = new String[10000];
        String[] values = new String[10000];
        for (int i = 0; i < keySet.length; i++) {
            keySet[i] = i + "_key";
            values[i] = i + "_value";
            hashTable.add(keySet[i], values[i]);
        }
        int count = 0;
        for (HashTable.Entry<String, String> s : hashTable.items) {
            for (int i = 0; i < keySet.length; i++) {
                if (keySet[i].equals(s.key) && values[i].equals(s.value)) {
                    count++;
                    break;
                }
            }
        }
        assertEquals(keySet.length, count);
    }

    @Test
    public void getCapacity() {
        for (int i = 0; i < 100; i++) {
            hashTable.add(i + "_k", i + "_v");
        }
        assertEquals(162, hashTable.getCapacity());
    }
}