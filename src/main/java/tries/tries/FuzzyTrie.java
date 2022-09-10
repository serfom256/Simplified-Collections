package tries.tries;

import additional.dynamicstring.DynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import sets.Set;
import sets.RBTSet;

import java.util.Arrays;

public class FuzzyTrie extends SimpleTrie {

    public FuzzyTrie() {
        super();
    }

    /**
     * Returns founded sequences as a String array if sequences founded
     *
     * @param word     to search sequences with specified word
     * @param count    desired count of founded words
     * @param distance maximum indistinct distance
     * @return sequences as a String array if sequences with specified word founded
     * otherwise empty String array
     * @throws IllegalArgumentException  if the specified word length less than specified distance
     * @throws NullableArgumentException if the specified word is null
     */
    public String[] lookupPrefix(String word, int count, int distance) {
        if (word == null) throw new NullableArgumentException();
        distance = distance < word.length() ? distance : word.length() - 1;
        Set<String> founded = new RBTSet<>();
        DynamicString result = new DynamicLinkedString();
        int len = word.length() - 1;
        TNode curr = root;
        for (int i = 0; i <= len; i++) {
            char c = word.charAt(i);
            TNode next = curr.nodes.get(c);
            if (next == null) {
                return search(i, curr, word, distance, count, founded);
            }
            result.add(c);
            if (i == len && next.isEnd) {
                founded.add(result.toString());
            }
            curr = next;
        }
        return search(len, curr, word, distance, count, founded);
    }

    private String[] search(int pos, TNode curr, String toSearch, int distance, int count, Set<String> founded) {
        Object[] instance;
        if (distance != 0) {
            for (int j = pos; j >= 0; j--) {
                collectWordsFuzzy(curr, toSearch, j, distance, count, founded);
                if (founded.getSize() >= count) break;
                curr = curr.prev;
            }
        } else {
            collect(founded, new DynamicLinkedString(toSearch), curr.nodes, count);
        }
        instance = founded.toObjectArray();
        return Arrays.copyOf(instance, instance.length, String[].class);
    }

    /**
     * Provides to collect all data from the specified node with using fuzzy search
     *
     * @param word    word of all collected words
     * @param pos     position in word from which search starts
     * @param typos   maximum count of typos in searched word
     * @param count   maximum count of founded words
     * @param founded set which contains all founded words
     */
    private void collectWordsFuzzy(TNode start, String word, int pos, int typos, int count, Set<String> founded) {
        if (typos < 0 || count <= founded.getSize()) return;
        if (pos + typos >= word.length() && start.prev != null) {
            collectForNode(founded, start, getReversed(start), count);
            if (count >= founded.getSize()) return;
        }
        for (Character k : start.nodes) {
            TNode v = start.nodes.get(k);
            if (pos < word.length() && k.equals(word.charAt(pos))) {
                collectWordsFuzzy(v, word, pos + 1, typos, count, founded);
            } else if (founded.getSize() < count) {
                collectWordsFuzzy(v, word, pos + 1, typos - 1, count, founded);
                collectWordsFuzzy(v, word, pos, typos - 1, count, founded);
                collectWordsFuzzy(start, word, pos + 1, typos - 1, count, founded);
            } else {
                return;
            }
        }
    }

    private void collectForNode(Set<String> founded, TNode node, DynamicString prefix, int count) {
        if (node == null || count <= founded.getSize()) return;
        if (node.isEnd) founded.add(prefix.toString());
        for (Character c : node.nodes) {
            if (count <= founded.getSize()) return;
            TNode curr = node.nodes.get(c);
            prefix.add(curr.element);
            if (curr.isEnd) founded.add(prefix.toString());
            collectForNode(founded, curr, prefix, count);
            prefix.deleteLast();
        }
    }

    /**
     * Returns String which contains characters from the specified node to the TrieMap root
     */
    private DynamicString getReversed(TNode node) {
        DynamicString prefix = new DynamicLinkedString();
        while (node != root) {
            prefix.addFirst(node.element);
            node = node.prev;
        }
        return prefix;
    }

    /**
     * @param distance maximum indistinct distance
     * @return true if specified sequence present in trie as a prefix
     * @throws IllegalArgumentException  if the specified distance more than length of the specified prefix
     * @throws NullableArgumentException if the specified prefix is null
     */
    public boolean presents(String prefix, int distance) {
        if (prefix == null) throw new NullableArgumentException();
        if (prefix.length() <= distance) {
            throw new IllegalArgumentException("Sequence length must be more than distance");
        }
        TNode curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!curr.nodes.containsKey(c)) {
                for (int j = i; j >= 0; j--) {
                    if (presentsWordFuzzy(curr, prefix, j, distance)) return true;
                    curr = curr.prev;
                }
                return false;
            }
            if (i == prefix.length() - 1 && curr.nodes.get(c).isEnd) return true;
            curr = curr.nodes.get(c);
        }
        return true;
    }

    private boolean presentsWordFuzzy(TNode start, String word, int pos, int typos) {
        if (pos == word.length() && typos >= 0) return true;
        for (Character k : start.nodes) {
            TNode v = start.nodes.get(k);
            if (pos < word.length() && k.equals(word.charAt(pos))) {
                if (presentsWordFuzzy(v, word, pos + 1, typos)) return true;
            } else if (typos >= 0) {
                if (presentsWordFuzzy(v, word, pos + 1, typos - 1) ||
                        presentsWordFuzzy(v, word, pos, typos - 1) ||
                        presentsWordFuzzy(start, word, pos + 1, typos - 1)) return true;
            } else break;
        }
        return false;
    }

    /**
     * @param distance maximum indistinct distance
     * @return Returns true considering the specified distance if specified sequence was added to trie otherwise false
     * @throws IllegalArgumentException  if the specified distance more than length of the specified sequence
     * @throws NullableArgumentException if the specified sequence is null
     */
    public boolean contains(String sequence, int distance) {
        if (sequence == null) throw new NullableArgumentException();
        if (sequence.length() <= distance) {
            throw new IllegalArgumentException("Sequence length must be more than distance");
        }
        TNode curr = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if (!curr.nodes.containsKey(c)) {
                for (int j = i; j >= 0; j--) {
                    if (containsWordFuzzy(curr, sequence, j, distance)) return true;
                    curr = curr.prev;
                }
                return false;
            }
            if (i == sequence.length() - 1 && curr.nodes.get(c).isEnd) return true;
            curr = curr.nodes.get(c);
        }
        return containsWordFuzzy(curr, sequence, sequence.length() - 1, distance);
    }

    private boolean containsWordFuzzy(TNode start, String word, int pos, int typos) {
        if (pos == word.length() && typos >= 0) return start.isEnd;
        for (Character k : start.nodes) {
            TNode v = start.nodes.get(k);
            if (pos < word.length() && k.equals(word.charAt(pos))) {
                if (containsWordFuzzy(v, word, pos + 1, typos)) return true;
            } else if (typos >= 0) {
                if (containsWordFuzzy(v, word, pos + 1, typos - 1) ||
                        containsWordFuzzy(start, word, pos + 1, typos - 1) ||
                        containsWordFuzzy(v, word, pos, typos - 1)) return true;
            } else break;
        }
        return false;
    }

}
