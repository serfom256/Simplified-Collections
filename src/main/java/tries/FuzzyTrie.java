package tries;

import additional.dynamicstring.AbstractDynamicString;
import additional.dynamicstring.DynamicLinkedString;
import additional.exceptions.NullableArgumentException;
import sets.AbstractSet;
import sets.RBTSet;

import java.util.Arrays;

public class FuzzyTrie extends Trie {
    public FuzzyTrie() {
        super();
    }

    /**
     * Returns one founded sequence as a String if sequence founded
     *
     * @param prefix   to search sequences with specified prefix
     * @param distance maximum indistinct distance
     * @return sequences as a String if found otherwise empty String
     * @throws IllegalArgumentException  if the specified prefix length less then specified distance
     * @throws NullableArgumentException if the specified prefix is null
     */
    public String getByPrefixFuzzy(String prefix, int distance) {
        String[] res = getByPrefixFuzzy(prefix, 1, distance);
        return res.length == 0 ? "" : res[0];
    }

    /**
     * Returns founded sequences as a String array if sequences founded
     *
     * @param prefix   to search sequences with specified prefix
     * @param count    desired count of founded words
     * @param distance maximum indistinct distance
     * @return sequences as a String array if sequences with specified prefix founded
     * otherwise empty String array
     * @throws IllegalArgumentException  if the specified prefix length less then specified distance
     * @throws NullableArgumentException if the specified prefix is null
     */
    public String[] getByPrefixFuzzy(String prefix, int count, int distance) {
        if (prefix == null) throw new NullableArgumentException();
        if (count <= 0) throw new IllegalArgumentException("Count must be more then 0");
        if (prefix.length() <= 1 || prefix.length() <= distance) {
            throw new IllegalArgumentException("Prefix length must be more then specified distance");
        }
        AbstractSet<String> founded = new RBTSet<>();
        AbstractDynamicString result = new DynamicLinkedString();
        TNode curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!curr.nodes.containsKey(c)) {
                AbstractDynamicString res = new DynamicLinkedString(prefix);
                for (int j = i; j >= 0; j--) {
                    collectWordsFuzzy(curr, prefix, j, distance, count, founded, res.subSequence(0, j));
                    if (founded.getSize() >= count) break;
                    curr = curr.prev;
                }
                Object[] instance = founded.toObjectArray();
                return Arrays.copyOf(instance, instance.length, String[].class);
            }
            result.add(c);
            if (i == prefix.length() - 1 && curr.nodes.get(c).isEnd) {
                count--;
                founded.add(result.toString());
            }
            curr = curr.nodes.get(c);
        }
        collect(founded, new DynamicLinkedString(prefix), curr.nodes, count);
        Object[] instance = founded.toObjectArray();
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
     * @param prefix  prefix from which will starts all founded words
     */
    private void collectWordsFuzzy(TNode start, String word, int pos, int typos, int count, AbstractSet<String> founded, AbstractDynamicString prefix) {
        if (typos < 0) return;
        if (pos == word.length()) {
            if (prefix.getSize() != 0 && start.prev != null && start.prev.nodes.containsKey(prefix.getLast()) && start.isEnd) {
                founded.add(prefix.toString());
            }
            collect(founded, prefix.copy(), start.nodes, count - founded.getSize());
            return;
        }
        for (Character k : start.nodes) {
            TNode v = start.nodes.get(k);
            if (pos < word.length() && k.equals(word.charAt(pos))) {
                collectWordsFuzzy(v, word, pos + 1, typos, count, founded, prefix.add(k));
                prefix.deleteLast();
            } else if (count > founded.getSize()) {
                collectWordsFuzzy(v, word, pos + 1, typos - 1, count, founded, prefix.add(k));
                collectWordsFuzzy(v, word, pos, typos - 1, count, founded, prefix.deleteLast().add(k));
                collectWordsFuzzy(start, word, pos + 1, typos - 1, count, founded, prefix.deleteLast());
            } else break;
        }
        if (count == founded.getSize()) return;
        if (word.length() - pos <= typos && count > 0 && start.nodes.getSize() == 0) {
            collect(founded, prefix.subSequence(0, prefix.getSize() - 1), start.prev.nodes, count - founded.getSize());
        }
    }

    /**
     * @param distance maximum indistinct distance
     * @return true if specified sequence present in trie as a prefix
     * @throws IllegalArgumentException  if the specified distance more then length of the specified prefix
     * @throws NullableArgumentException if the specified prefix is null
     */
    public boolean presents(String prefix, int distance) {
        if (prefix == null) throw new NullableArgumentException();
        if (prefix.length() <= distance) {
            throw new IllegalArgumentException("Sequence length must be more then distance");
        }
        TNode curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!curr.nodes.containsKey(c)) {
                for (int j = i; j >= 0; j--) {
                    if (presentsWordFuzzy(curr, prefix, 0, distance)) return true;
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
        return word.length() - pos <= typos && start.nodes.getSize() == 0;
    }

    /**
     * @param distance maximum indistinct distance
     * @return Returns true considering the specified distance if specified sequence was added to trie otherwise false
     * @throws IllegalArgumentException  if the specified distance more then length of the specified sequence
     * @throws NullableArgumentException if the specified sequence is null
     */
    public boolean contains(String sequence, int distance) {
        if (sequence == null) throw new NullableArgumentException();
        if (sequence.length() <= distance) {
            throw new IllegalArgumentException("Sequence length must be more then distance");
        }
        TNode curr = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if (!curr.nodes.containsKey(c)) {
                for (int j = i; j >= 0; j--) {
                    if (containsWordFuzzy(curr, sequence, 0, distance)) return true;
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
        return word.length() - pos <= typos && start.prev.isEnd;
    }

}
