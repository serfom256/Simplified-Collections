package TrieTests;

import lists.List;
import lists.impl.ArrayList;
import tries.tries.SearchTrieMap;

public class SearchTrieTests {

    private final SearchTrieMap trieMap;
    private final List<String> lst;

    public SearchTrieTests() {
        trieMap = new SearchTrieMap();
        lst = new ArrayList<>();
    }

//    @Test
//    public void lookUpExtendedTest() {
//        ArrayList<String> lst = new ArrayList<>();
//
//        for (int i = 0; i < 10000; i++) {
//            int minStringLen = 5;
//            int maxStringLen = 15;
//            String randString = generateString(minStringLen, maxStringLen);
//            trieMap.add(randString, "");
//            lst.add(randString);
//        }
//        assertEquals(10000, trieMap.getSize());
//        for (int i = 0; i < 1000; i++) {
//            String s = lst.get((int) ((Math.random() * (10000))));
//            int randAction = (int) ((Math.random() * (3 - 1)) + 1);
//            DynamicString mutableString = new DynamicLinkedString(s);
//            int randomPosition = (int) (Math.random() * (s.length() - 1));
//            char randomChar = (char) (new Random().nextInt(26) + 'a');
//            switch (randAction) {
//                case 1:
//                    if (s.length() > 2) {
//                        mutableString.deleteAtPosition(randomPosition);
//                        break;
//                    }
//                case 2:
//                    mutableString.insert(randomPosition, randomChar);
//                    break;
//                case 3:
//                    mutableString.replace(randomPosition, randomPosition, String.valueOf(randomChar));
//                    break;
//            }
//            List<Pair<String, List<String>>> founded = trieMap.lookup(mutableString.toString(), 1, 10000);
//
//            ArrayList<String> list = new ArrayList<>();
//            for (Pair<String, List<String>> f : founded) {
//                list.add(f.getKey());
//            }
//            assertNotEquals(list.indexOf(s), -1);
//        }
//    }
//    private String generateString(int minLen, int maxLen) {
//        int leftLimit = 48;
//        int rightLimit = 122;
//        int len = (int) ((Math.random() * (maxLen - minLen)) + minLen);
//        DynamicString s = new DynamicLinkedString();
//        new Random().ints(leftLimit, rightLimit + 1)
//                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
//                .limit(len)
//                .forEach(s::addUnicodeChar);
//        return s.toString();
//    }


}
