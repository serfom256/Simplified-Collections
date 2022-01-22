package TrieTests;

import additional.nodes.HashNode;
import org.junit.Before;
import org.junit.Test;
import tries.TrieMap;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.Assert.*;

public class TrieMapTest {
    TrieMap<String, String> trieMap;

    public TrieMapTest() {
        this.trieMap = new TrieMap<>();
    }

    @Before
    public void setUp() {
        for (int i = 0; i < 10; i++) {
            trieMap.add(i + "_key", i + "_value");
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
            trieMap.add(getRandomString(5), i + "_");
        }
        assertEquals(20, trieMap.getSize());
    }

    @Test
    public void get() {
        for (int i = 10; i < 20; i++) {
            trieMap.add(i + "_key", i + "_value");
        }

        for (int i = 0; i < 20; i++) {
            assertEquals(i + "_value", trieMap.get(i + "_key"));
        }
    }

    @Test
    public void clear() {
        for (int i = 10; i < 20; i++) {
            trieMap.add(getRandomString(5), i + "_");
        }
        trieMap.clear();
        assertEquals(0, trieMap.getSize());
    }

    @Test
    public void getSize() {
        for (int i = 10; i < 20; i++) {
            trieMap.add(getRandomString(5), i + "_");
        }
        assertEquals(20, trieMap.getSize());
    }

    @Test
    public void removeValue() {
        String s = getRandomString(4);
        String[] testArr = {"12345", "54321", "00000", "99999", "111111"};
        for (String i : testArr) {
            trieMap.add(s, i);
        }
        for (String value : testArr) {
            assertTrue(trieMap.deleteValue(s, value));
        }
        assertFalse(trieMap.deleteValue(s, testArr[0]));

        for (int i = 0; i < 10; i++) {
            assertTrue(trieMap.deleteValue(i + "_key", i + "_value"));
        }
        assertEquals(0, trieMap.getSize());
    }

    @Test
    public void delete() {
        for (int i = 0; i < 100; i++) {
            trieMap.add(i + "_key", i + "_value");
        }
        int len = trieMap.getSize();
        for (int i = 0; i < len; i++) {
            assertEquals(i + "_value", trieMap.delete(i + "_key"));
        }
        assertEquals(0, trieMap.getSize());
    }

    @Test
    public void containsKey() {
        int len = trieMap.getSize();
        for (int i = 0; i < len; i++) {
            assertTrue(trieMap.containsKey(i + "_key"));
            assertFalse(trieMap.containsKey("0000"));
        }
    }

    @Test
    public void replace() {
        String[] newKeys = new String[20];
        for (int i = 0; i < 20; i++) {
            String newKey = getRandomString(4), oldKey = i + "_key";
            newKeys[i] = newKey;
            trieMap.add(oldKey, i + "_value");
        }
        for (int i = 0; i < newKeys.length; i++) {
            assertTrue(trieMap.replace(i + "_key", newKeys[i]));
        }

        assertFalse(trieMap.replace("0_key", newKeys[0]));
        assertEquals(newKeys.length, trieMap.getSize());
    }

    @Test
    public void itemsForEach() {
        String[] keySet = new String[10000];
        String[] values = new String[10000];
        for (int i = 0; i < keySet.length; i++) {
            keySet[i] = i + "_key";
            values[i] = i + "_value";
            trieMap.add(keySet[i], values[i]);
        }
        int count = 0;
        for (HashNode<String, String> s : trieMap.items) {
            for (int i = 0; i < keySet.length; i++) {
                if (keySet[i].equals(s.getKey()) && values[i].equals(s.getValue())) {
                    count++;
                    break;
                }
            }
        }
        assertEquals(keySet.length, count);
    }

    public void speedTest() {
        trieMap.clear();
        for (int i = 0; i < 1_000_000; i++) {
            trieMap.add(i + "_k", i + "_v");
        }
        assertEquals(1_000_000, trieMap.getSize());
    }
}