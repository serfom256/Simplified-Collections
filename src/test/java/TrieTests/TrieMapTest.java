package TrieTests;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.nodes.Pair;
import lists.List;
import lists.impl.ArrayList;
import org.junit.Before;
import org.junit.Test;
import sets.Set;
import sets.HashedSet;
import tries.tries.SimpleTrieMap;

import java.util.Random;

import static org.junit.Assert.*;

public class TrieMapTest {

    private final SimpleTrieMap trieMap;

    public TrieMapTest() {
        this.trieMap = new SimpleTrieMap();
    }

    @Before
    public void setUp() {
        assertEquals(0, trieMap.getPairsCount());
        assertEquals(0, trieMap.getSize());
        trieMap.add("1234", "value");
        trieMap.add("4567", "value");
        trieMap.add("qwe", "00000");
        trieMap.add("qwe", "11111111111");
        trieMap.add("test", "00000");
        trieMap.add("test", "00000");
        trieMap.add("abc909", "qwe");
        trieMap.add("12345", "val");
        trieMap.add("12345", "temp");
        trieMap.add("java", "qwerty");
        trieMap.add("java", "random value");
        trieMap.add("c++", "some value");
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());
    }

    @Test
    public void putTest() {
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());
        trieMap.add("string234", "00990099");
        trieMap.add("string234", "00990099");
        trieMap.add("string234", "");
        assertEquals(9, trieMap.getPairsCount());
        assertEquals(90, trieMap.getSize());
        trieMap.add("1value1", "1value1");
        trieMap.add("88888", "11");
        trieMap.add("6", "99999999");
        assertEquals(12, trieMap.getPairsCount());
        assertEquals(110, trieMap.getSize());
        trieMap.add("0987", "0987");
        trieMap.add("wasd", "");
        trieMap.add("qwerty", "");
        assertEquals(15, trieMap.getPairsCount());
    }

    @Test
    public void deleteKeyTest() {
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());

        HashedSet<String> value = new HashedSet<>();
        value.addAll("value");
        assertEquals(new Pair<>("1234", value).toString(), trieMap.deleteKey("1234").toString());
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());
        value.clear();

        value.addAll("11111111111", "00000");
        assertEquals(new Pair<>("qwe", value).toString(), trieMap.deleteKey("qwe").toString());
        assertEquals(6, trieMap.getPairsCount());
        assertEquals(66, trieMap.getSize());
        value.clear();
        value.addAll("random value", "qwerty");
        assertEquals(new Pair<>("java", value).toString(), trieMap.deleteKey("java").toString());
        assertEquals(5, trieMap.getPairsCount());
        assertEquals(47, trieMap.getSize());
        value.clear();

        assertEquals(new Pair<>().toString(), trieMap.deleteKey("java").toString());
        assertEquals(5, trieMap.getPairsCount());
        assertEquals(47, trieMap.getSize());
        value.clear();

        value.addAll("value");
        assertEquals(new Pair<>("4567", value).toString(), trieMap.deleteKey("4567").toString());
        assertEquals(4, trieMap.getPairsCount());
        assertEquals(41, trieMap.getSize());
        value.clear();

        value.addAll("some value");
        assertEquals(new Pair<>("c++", value).toString(), trieMap.deleteKey("c++").toString());
        assertEquals(3, trieMap.getPairsCount());
        assertEquals(28, trieMap.getSize());
        value.clear();

        value.addAll("val", "temp");
        assertEquals(new Pair<>("12345", value).toString(), trieMap.deleteKey("12345").toString());
        assertEquals(2, trieMap.getPairsCount());
        assertEquals(18, trieMap.getSize());
        value.clear();

        value.addAll("00000");
        assertEquals(new Pair<>("test", value).toString(), trieMap.deleteKey("test").toString());
        assertEquals(1, trieMap.getPairsCount());
        assertEquals(9, trieMap.getSize());
        value.clear();

        value.addAll("qwe");
        assertEquals(new Pair<>("abc909", value).toString(), trieMap.deleteKey("abc909").toString());
        assertEquals(0, trieMap.getPairsCount());
        assertEquals(0, trieMap.getSize());
    }

    @Test
    public void deleteValueAndKeyTest() {
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());

        assertTrue(trieMap.deleteValue("value"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertEquals(new Pair<>("1234", new HashedSet<>()).toString(), trieMap.deleteKey("1234").toString());
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertFalse(trieMap.deleteValue("value"));
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertTrue(trieMap.deleteValue("qwe"));
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertFalse(trieMap.deleteValue("1111"));
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        trieMap.add("qwerty", "");
        assertEquals(8, trieMap.getPairsCount());
        trieMap.deleteKey("qwerty");
        assertTrue(trieMap.deleteValue("qwerty"));
        assertEquals(7, trieMap.getPairsCount());

    }

    @Test
    public void deleteValueOrKeyTest() {
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());

        assertTrue(trieMap.deleteValue("value"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(74, trieMap.getSize());

        assertTrue(trieMap.deleteValue("val"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(71, trieMap.getSize());

        assertTrue(trieMap.deleteValue("temp"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(69, trieMap.getSize());

        assertTrue(trieMap.deleteValue("some value"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(59, trieMap.getSize());

        assertTrue(trieMap.deleteValue("00000"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(54, trieMap.getSize());

        assertTrue(trieMap.deleteValue("11111111111"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(44, trieMap.getSize());

        assertTrue(trieMap.deleteValue("qwerty"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(41, trieMap.getSize());

        assertTrue(trieMap.deleteValue("random value"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(29, trieMap.getSize());

        assertTrue(trieMap.deleteValue("qwe"));
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(29, trieMap.getSize());

        assertEquals(new Pair<>("12345", new HashedSet<>()).toString(), trieMap.deleteKey("12345").toString());
        assertEquals(7, trieMap.getPairsCount());
        assertEquals(28, trieMap.getSize());

        assertEquals(new Pair<>("1234", new HashedSet<>()).toString(), trieMap.deleteKey("1234").toString());
        assertEquals(6, trieMap.getPairsCount());
        assertEquals(24, trieMap.getSize());

        assertEquals(new Pair<>("qwe", new HashedSet<>()).toString(), trieMap.deleteKey("qwe").toString());
        assertEquals(5, trieMap.getPairsCount());
        assertEquals(21, trieMap.getSize());

        assertEquals(new Pair<>("c++", new HashedSet<>()).toString(), trieMap.deleteKey("c++").toString());
        assertEquals(4, trieMap.getPairsCount());
        assertEquals(18, trieMap.getSize());

        assertEquals(new Pair<>("abc909", new HashedSet<>()).toString(), trieMap.deleteKey("abc909").toString());
        assertEquals(3, trieMap.getPairsCount());
        assertEquals(12, trieMap.getSize());


        assertEquals(new Pair<>("java", new HashedSet<>()).toString(), trieMap.deleteKey("java").toString());
        assertEquals(2, trieMap.getPairsCount());
        assertEquals(8, trieMap.getSize());


        assertEquals(new Pair<>("test", new HashedSet<>()).toString(), trieMap.deleteKey("test").toString());
        assertEquals(1, trieMap.getPairsCount());
        assertEquals(4, trieMap.getSize());

        assertEquals(new Pair<>().toString(), trieMap.deleteKey("java").toString());
        assertEquals(1, trieMap.getPairsCount());
        assertEquals(4, trieMap.getSize());

        assertEquals(new Pair<>("4567", new HashedSet<>()).toString(), trieMap.deleteKey("4567").toString());
        assertEquals(0, trieMap.getPairsCount());
        assertEquals(0, trieMap.getSize());
    }

    @Test
    public void containsKeyTest() {
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll("1234", "4567", "qwe", "test", "abc909", "12345", "java", "c++");
        for (String s : keys) {
            assertTrue(trieMap.containsKey(s));
        }
        assertFalse(trieMap.containsKey("some value"));
        assertFalse(trieMap.containsKey("value"));
        assertFalse(trieMap.containsKey("00000"));
        assertFalse(trieMap.containsKey("temp"));
        assertFalse(trieMap.containsKey("qwerty"));
        assertFalse(trieMap.containsKey(""));
    }

    @Test
    public void containsKeyValue() {
        ArrayList<String> values = new ArrayList<>();
        values.addAll("value", "00000", "11111111111", "qwe", "val", "temp", "qwerty", "random value", "some value");
        for (String s : values) {
            assertTrue(trieMap.containsValue(s));
        }
        assertFalse(trieMap.containsValue("java"));
        assertFalse(trieMap.containsValue("4567"));
        assertFalse(trieMap.containsValue("0000"));
        assertFalse(trieMap.containsValue("test"));
        assertFalse(trieMap.containsValue("abc909"));
        assertFalse(trieMap.containsValue("54321"));
        assertFalse(trieMap.containsValue(""));
    }

    @Test
    public void containsKeyOrValueTest() {
        ArrayList<String> items = new ArrayList<>();
        items.addAll("1234", "4567", "qwe", "test",
                "abc909", "12345", "java", "c++", "value",
                "00000", "11111111111", "qwe", "val", "temp",
                "qwerty", "random value", "some value");
        for (String s : items) {
            assertTrue(trieMap.contains(s));
        }
        assertFalse(trieMap.contains("some val"));
        assertFalse(trieMap.contains("123"));
        assertFalse(trieMap.contains("8"));
        assertFalse(trieMap.contains("6"));
        assertFalse(trieMap.contains(""));
    }

    @Test
    public void getTest() {

        HashedSet<String> values = new HashedSet<>();
        values.addAll("value");
        assertEquals(new Pair<>("1234", values).toString(), trieMap.get("1234").toString());
        values.clear();

        values.addAll("11111111111", "00000");
        assertEquals(new Pair<>("qwe", values).toString(), trieMap.get("qwe").toString());
        values.clear();

        values.addAll("random value", "qwerty");
        assertEquals(new Pair<>("java", values).toString(), trieMap.get("java").toString());
        values.clear();

        values.addAll("value");
        assertEquals(new Pair<>("4567", values).toString(), trieMap.get("4567").toString());
        values.clear();

        values.addAll("some value");
        assertEquals(new Pair<>("c++", values).toString(), trieMap.get("c++").toString());

        values.clear();

        values.addAll("val", "temp");
        assertEquals(new Pair<>("12345", values).toString(), trieMap.get("12345").toString());

        values.clear();

        values.addAll("00000");
        assertEquals(new Pair<>("test", values).toString(), trieMap.get("test").toString());

        values.clear();

        values.addAll("qwe");
        assertEquals(new Pair<>("abc909", values).toString(), trieMap.get("abc909").toString());

        assertEquals(new Pair<>().toString(), trieMap.get("qwerty").toString());

    }

    @Test
    public void lookupTest() {
        HashedSet<String> values = new HashedSet<>();
        values.addAll("value");
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("124", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("134", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("234", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("123", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("1234", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("0123", 2, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("1023", 2, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("1203", 2, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("1230", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("01234", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("10234", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("12034", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("12304", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("12340", 1, SimpleTrieMap.Verbose.MIN).get(0).toString());
        assertEquals(new Pair<>("1234", values).toString(), trieMap.lookup("001234", 2, SimpleTrieMap.Verbose.MIN).get(0).toString());
        values.clear();
        values.addAll("random value");
        assertEquals(new Pair<>("java", values).toString(), trieMap.lookup("rnd value", 3, SimpleTrieMap.Verbose.MIN).get(0).toString());
        values.clear();
        values.addAll("random value");
        assertEquals(new Pair<>("java", values).toString(), trieMap.lookup("rnd val", 5, SimpleTrieMap.Verbose.MIN).get(0).toString());
    }

    @Test
    public void lookUpExtendedTest() {
        ArrayList<String> lst = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            int minStringLen = 5;
            int maxStringLen = 15;
            String randString = generateString(minStringLen, maxStringLen);
            trieMap.add(randString, "");
            lst.add(randString);
        }
        assertEquals(10008, trieMap.getPairsCount());
        for (int i = 0; i < 1000; i++) {
            String s = lst.get((int) ((Math.random() * (10000))));
            int randAction = (int) ((Math.random() * (3 - 1)) + 1);
            DynamicString mutableString = new DynamicLinkedString(s);
            int randomPosition = (int) (Math.random() * (s.length() - 1));
            char randomChar = (char) (new Random().nextInt(26) + 'a');
            switch (randAction) {
                case 1:
                    if (s.length() > 2) {
                        mutableString.deleteAtPosition(randomPosition);
                        break;
                    }
                case 2:
                    mutableString.insert(randomPosition, randomChar);
                    break;
                case 3:
                    mutableString.replace(randomPosition, randomPosition, String.valueOf(randomChar));
                    break;
            }
            List<Pair<String, Set<String>>> founded = trieMap.lookup(mutableString.toString(), 1, SimpleTrieMap.Verbose.MIN);

            ArrayList<String> list = new ArrayList<>();
            for (Pair<String, Set<String>> f : founded) {
                list.add(f.getKey());
            }
            assertNotEquals(list.indexOf(s), -1);
        }
    }

    private String generateString(int minLen, int maxLen) {
        int leftLimit = 48;
        int rightLimit = 122;
        int len = (int) ((Math.random() * (maxLen - minLen)) + minLen);
        DynamicString s = new DynamicLinkedString();
        new Random().ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(len)
                .forEach(s::addUnicodeChar);
        return s.toString();
    }

    @Test
    public void forEach() {
        Set<String> prev = new HashedSet<>();
        for (Pair<String, Set<String>> s : trieMap) {
            String stringPair = s.toString();
            assertFalse(prev.contains(stringPair));
            prev.add(stringPair);
        }
        assertEquals(trieMap.getPairsCount(), prev.getSize());
    }

}