package Additional.Trie;

import HashSet.AbstractSet;
import HashSet.Set;

import java.util.Arrays;

public class FuzzySearchTrie extends Trie {
    public FuzzySearchTrie() {
        super();
    }


    public String getByPrefixFuzzy(String prefix, int distance) {
        String[] res = getByPrefix(prefix, 1, 1);
        return res.length == 0 ? "" : res[0];
    }

    public String[] getByPrefix(String prefix, int count, int distance) {
        if (count <= 0) throw new IllegalArgumentException("Count must be more then 0");
        if (prefix.length() <= 1 || prefix.length() <= distance) {
            throw new IllegalArgumentException("Prefix length must be more then distance");
        }
        AbstractSet<String> founded = new Set<>(distance + 1);
        StringBuilder result = new StringBuilder();
        TNode curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!curr.nodes.containsKey(c)) {
                for (int j = i; j >= 0; j--) {
                    String res = prefix.substring(0, j);
                    collectWordsFuzzy(curr, prefix, j, distance, count, founded, res);
                    if (founded.getSize() >= count) break;
                    curr = curr.prev;
                }
                Object[] instance = founded.toObjectArray();
                return Arrays.copyOf(instance, instance.length, String[].class);
            }
            result.append(c);
            if (i == prefix.length() - 1 && curr.nodes.get(c).isEnd) {
                count--;
                founded.add(result.toString());
            }
            curr = curr.nodes.get(c);
        }
        collect(founded, new StringBuilder(prefix), curr.nodes, count);
        Object[] instance = founded.toObjectArray();
        return Arrays.copyOf(instance, instance.length, String[].class);
    }


    private void collectWordsFuzzy(TNode start, String word, int pos, int typos, int count, AbstractSet<String> founded, String prefix) {
        if (count == founded.getSize()) return;
        if (pos == word.length() && typos >= 0) {
            if (prefix.length() != 0) {
                char lastChar = prefix.charAt(prefix.length() - 1);
                if (start.prev != null && start.prev.nodes.containsKey(lastChar) && start.isEnd) founded.add(prefix);
            }
            collect(founded, new StringBuilder(prefix), start.nodes, count - founded.getSize());
            return;
        }
        for (Character k : start.nodes) {
            TNode v = start.nodes.get(k);
            if (pos < word.length() && k.equals(word.charAt(pos))) {
                collectWordsFuzzy(v, word, pos + 1, typos, count, founded, prefix + k);
            } else if (typos >= 0 && count > founded.getSize()) {
                collectWordsFuzzy(v, word, pos + 1, typos - 1, count, founded, prefix + k);
                collectWordsFuzzy(v, word, pos, typos - 1, count, founded, prefix + k);
                collectWordsFuzzy(start, word, pos + 1, typos - 1, count, founded, prefix);
            } else break;
        }
        if (count == founded.getSize()) return;
        if (word.length() - pos <= typos && start.nodes.getSize() == 0 && count > 0) {
            collect(founded, new StringBuilder(prefix.substring(0, prefix.length() - 1)), start.prev.nodes, count - founded.getSize());
        }
    }

    public boolean presents(String prefix, int distance) {
        if (prefix.length() <= 1 || prefix.length() <= distance) {
            throw new IllegalArgumentException("Sequence length must be more then distance");
        }
        TNode curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!curr.nodes.containsKey(c)) {
                for (int j = i; j >= 0; j--) {
                    if (presentsWordFuzzy(curr, prefix, 0, distance, false)) return true;
                    curr = curr.prev;
                }
                return false;
            }
            if (i == prefix.length() - 1 && curr.nodes.get(c).isEnd) return true;
            curr = curr.nodes.get(c);
        }
        return true;
    }

    private boolean presentsWordFuzzy(TNode start, String word, int pos, int typos, boolean isFound) {
        if (isFound) return true;
        if (pos == word.length() && typos >= 0) return true;
        for (Character k : start.nodes) {
            TNode v = start.nodes.get(k);
            if (pos < word.length() && k.equals(word.charAt(pos))) {
                if (presentsWordFuzzy(v, word, pos + 1, typos, isFound)) return true;
            } else if (typos >= 0) {
                isFound = presentsWordFuzzy(v, word, pos + 1, typos - 1, isFound);
                isFound = presentsWordFuzzy(v, word, pos, typos - 1, isFound);
                isFound = presentsWordFuzzy(start, word, pos + 1, typos - 1, isFound);
                if (isFound) return true;
            } else break;
        }
        return word.length() - pos <= typos && start.nodes.getSize() == 0;
    }

    public boolean contains(String sequence, int distance) {
        if (sequence.length() <= 1 || sequence.length() <= distance) {
            throw new IllegalArgumentException("Sequence length must be more then distance");
        }
        TNode curr = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if (!curr.nodes.containsKey(c)) {
                for (int j = i; j >= 0; j--) {
                    if (containsWordFuzzy(curr, sequence, 0, distance, false)) return true;
                    curr = curr.prev;
                }
                return false;
            }
            if (i == sequence.length() - 1 && curr.nodes.get(c).isEnd) return true;
            curr = curr.nodes.get(c);
        }
        return containsWordFuzzy(curr, sequence, sequence.length() - 1, distance, false);
    }

    private boolean containsWordFuzzy(TNode start, String word, int pos, int typos, boolean isFound) {
        if (isFound) return true;
        if (pos == word.length() && typos >= 0) return start.isEnd;
        for (Character k : start.nodes) {
            TNode v = start.nodes.get(k);
            if (pos < word.length() && k.equals(word.charAt(pos))) {
                if (containsWordFuzzy(v, word, pos + 1, typos, isFound)) return true;
            } else if (typos >= 0) {
                isFound = containsWordFuzzy(v, word, pos + 1, typos - 1, isFound);
                isFound = containsWordFuzzy(v, word, pos, typos - 1, isFound);
                isFound = containsWordFuzzy(start, word, pos + 1, typos - 1, isFound);
                if (isFound) return true;
            } else break;
        }
        // FIXME
        return word.length() - pos <= typos && start.prev.isEnd;
    }


    @Override
    public void put(String sequence) {
        super.put(sequence);
    }

    @Override
    public boolean presents(String sequence) {
        return super.presents(sequence);
    }

    @Override
    public boolean contains(String sequence) {
        return super.contains(sequence);
    }

    @Override
    public String getByPrefix(String prefix) {
        return super.getByPrefix(prefix);
    }

    @Override
    public String[] getByPrefix(String prefix, int count) {
        return super.getByPrefix(prefix, count);
    }

    @Override
    public boolean removeHard(String sequence) {
        return super.removeHard(sequence);
    }

    @Override
    public boolean removeWeak(String sequence) {
        return super.removeWeak(sequence);
    }

    @Override
    public boolean remove(String sequence) {
        return super.remove(sequence);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public int getSize() {
        return super.getSize();
    }

    @Override
    public int getEntriesCount() {
        return super.getEntriesCount();
    }

}
