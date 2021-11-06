package tests.TrieTests;

import Tries.FuzzySearchTrie;
import Lists.AbstractList;
import Lists.impl.ArrayList;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class FuzzySearchTrieTests {
    private final FuzzySearchTrie trie;
    private final AbstractList<String> lst;

    public FuzzySearchTrieTests() {
        trie = new FuzzySearchTrie();
        lst = new ArrayList<>();
    }

    @Before
    public void setUp() {
        trie.clear();
        for (int i = 0; i < 10000; i++) {
            int minStringLen = 3;
            int maxStringLen = 30;
            String randString = generateString(minStringLen, maxStringLen);
            trie.put(randString);
            lst.add(randString);
        }
    }

    @Test
    public void getByPrefixShouldFoundValue() {
        for (int i = 0; i < 1000; i++) {
            String s = lst.get((int) ((Math.random() * (10000))));
            int randAction = (int) ((Math.random() * (3 - 1)) + 1);
            StringBuilder mutableString = new StringBuilder(s);
            int randomPosition = (int) (Math.random() * (s.length() - 1));
            char randomChar = (char) (new Random().nextInt(26) + 'a');
            switch (randAction) {
                case 1:
                    if (s.length() > 2) {
                        mutableString.deleteCharAt(randomPosition);
                        break;
                    }
                case 2:
                    mutableString.insert(randomPosition, randomChar);
                    break;
                case 3:
                    mutableString.replace(randomPosition, randomPosition, String.valueOf(randomChar));
                    break;
            }
            if (!trie.presents(mutableString.toString())) {
                String[] founded = trie.getByPrefix(mutableString.toString(), 1000, 1);
                AbstractList<String> list = new ArrayList<>();
                list.addAll(founded);
                assertNotEquals(list.indexOf(s), -1);
            }
        }
    }

    @Test
    public void containsShouldReturnTrue() {
        for (int i = 0; i < 1000; i++) {
            String s = lst.get((int) ((Math.random() * (10000))));
            int randAction = (int) ((Math.random() * (3 - 1)) + 1);
            StringBuilder mutableString = new StringBuilder(s);
            int randomPosition = (int) (Math.random() * (s.length() - 1));
            char randomChar = (char) (new Random().nextInt(26) + 'a');
            switch (randAction) {
                case 1:
                    if (s.length() > 2) {
                        mutableString.deleteCharAt(randomPosition);
                        break;
                    }
                case 2:
                    mutableString.insert(randomPosition, randomChar);
                    break;
                case 3:
                    mutableString.replace(randomPosition, randomPosition, String.valueOf(randomChar));
                    break;
            }
            if (!trie.presents(mutableString.toString())) {
                assertTrue(trie.contains(mutableString.toString(), 1));
            }
        }
    }

    @Test
    public void presentsShouldReturnTrue() {
        for (int i = 0; i < 1000; i++) {
            String s = lst.get((int) ((Math.random() * (10000))));
            int randAction = (int) ((Math.random() * (3 - 1)) + 1);
            StringBuilder mutableString = new StringBuilder(s);
            int randomPosition = (int) (Math.random() * 3);
            char randomChar = (char) (new Random().nextInt(26) + 'a');
            switch (randAction) {
                case 1:
                    if (s.length() > 2) {
                        mutableString.deleteCharAt(randomPosition);
                        break;
                    }
                case 2:
                    mutableString.insert(randomPosition, randomChar);
                    break;
                case 3:
                    mutableString.replace(randomPosition, randomPosition, String.valueOf(randomChar));
                    break;
            }
            if (mutableString.length() > 5) mutableString = new StringBuilder(mutableString.substring(0, 3));
            if (!trie.presents(mutableString.toString())) {
                assertTrue(trie.presents(mutableString.toString(), 1));
            }
        }
    }

    private String generateString(int minLen, int maxLen) {
        int leftLimit = 48;
        int rightLimit = 122;
        int len = (int) ((Math.random() * (maxLen - minLen)) + minLen);
        return new Random().ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


}
