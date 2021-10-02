package Additional.Trie;

import Lists.AbstractList;
import Lists.impl.ArrayList;

import java.util.Random;

public class TrieMap {

    public static void main(String[] args) {

        Trie trie = new Trie();
        AbstractList<String> values = new ArrayList<>();
        int count = 1000000;
        for (int i = 0; i < count; i++) {
            values.add(gen(10));
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            trie.put(values.get(i));
        }
        for (int i = 0; i < count; i++) {
            trie.contains(values.get(i));
        }
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(trie.getEntriesCount());
    }
    public static String gen(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }



//    public static void main(String[] args) {
//        AbstractList<String> source = new Lists.impl.ArrayList<>();
//        source.addAll("one", "two", "abc", "abg", "ad", "bca", "aba");
//        FuzzySearchTrie trie = new FuzzySearchTrie();
//        for (String c : source) trie.put(c);
//
//        AbstractList<String> red = new ArrayList<>();
//        red.addAll("obe", "tgo", "abca", "habg", "ad", "ada");
//        for (String c : red) {
//            System.out.println(Arrays.toString(trie.getByPrefix(c, 2, 1)));
//        }
//        for (String c : source) {
//            boolean x = trie.remove(c);
//            System.out.println(c + " " + trie + " " + x);
//        }
//        System.out.println(trie);
//    }
}
