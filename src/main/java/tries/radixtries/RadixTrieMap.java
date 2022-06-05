package tries.radixtries;

import additional.exceptions.NullableArgumentException;
import additional.nodes.HashNode;
import additional.nodes.Pair;
import hashtables.HashTable;
import lists.AbstractList;
import lists.impl.ArrayList;
import sets.Set;
import tries.AbstractTrieMap;

public class RadixTrieMap implements AbstractTrieMap<String, String> {

    private int size;
    public final HashTable<Character, TNode> rootNodes;

    private static class SearchEntity {
        private final int count;
        private final String toSearch;
        private final Set<Pair<String, AbstractList<String>>> founded;

        public SearchEntity(int count, String toSearch, Set<Pair<String, AbstractList<String>>> founded) {
            this.count = count;
            this.toSearch = toSearch;
            this.founded = founded;
        }

        public int getSearchedLength() {
            return toSearch.length();
        }

        public boolean isFounded() {
            return count <= founded.getSize();
        }

        public Set<Pair<String, AbstractList<String>>> getResult() {
            return founded;
        }
    }

    public RadixTrieMap() {
        this.size = 0;
        this.rootNodes = new HashTable<>(128);
    }

    private static class TNode {

        private String prefix;
        private AbstractList<TNode> successors;
        private AbstractList<TNode> pairs;
        private TNode prev;
        private boolean isEnd = false;

        public TNode(String prefix, TNode prev) {
            this.prefix = prefix;
            this.prev = prev;
        }

        public TNode(String prefix, TNode prev, boolean isEnd) {
            this.prefix = prefix;
            this.prev = prev;
            this.isEnd = isEnd;
        }

        public void addNewPair(TNode newPair) {
            if (pairs == null) pairs = new ArrayList<>(4);
            if (pairs.indexOf(newPair) == -1) pairs.add(newPair);
        }

        public void addSuccessor(TNode node) {
            if (successors == null) successors = new ArrayList<>(4);
            successors.add(node);
        }


        public int getSize() {
            if (successors == null) return 0;
            return successors.getSize();
        }

    }

    private TNode getNext(TNode nodes, char p2) {
        if (nodes.successors == null) return null;
        for (TNode n : nodes.successors) {
            if (n.prefix.charAt(0) == p2) return n;
        }
        return null;
    }

    private int findIntersection(String p1, String p2) {
        int start = 0, pos = Math.min(p1.length(), p2.length()) - 1;
        while (start < pos) {
            if (p1.charAt(start) != p2.charAt(start)) return start;
            start++;
        }
        return start + 1;
    }

    private TNode insert(TNode node, TNode toInsert) {
        node.addSuccessor(toInsert);
        size++;
        return toInsert;
    }

    public void add(String key, String value) {
        TNode keyNode = insert(key);
        if (value.length() != 0) {
            TNode valueNode = insert(value);
            keyNode.addNewPair(valueNode);
            valueNode.addNewPair(keyNode);
        }
    }

    public String get(String key) {
        TNode node = rootNodes.get(key.charAt(0));
        if (node == null) return null;
        int pos = 0;
        int cp = 0;
        while (pos < key.length()) {
            if (cp >= node.prefix.length()) {
                cp = 0;
                node = getNext(node, key.charAt(pos));
                if (node == null) return null;
            }
            cp++;
            pos++;
        }
        return getReversed(node);
    }

    public TNode insert(String key) {
        TNode node = rootNodes.get(key.charAt(0));
        if (node == null) {
            node = new TNode(key, null, true);
            rootNodes.add(key.charAt(0), node);
            return node;
        }
        int pos = 0;
        while (pos < key.length()) {
            if (pos >= node.prefix.length()) {
                TNode next = getNext(node, key.charAt(pos));
                if (next == null) {
                    return insert(node, new TNode(key.substring(pos), node, true));
                }
                key = key.substring(findIntersection(node.prefix, key));
                pos = 0;
                node = next;
            }
            if (key.charAt(pos) == node.prefix.charAt(pos)) {
                pos++;
            } else {
                node = splitNode(node, pos);
                return insert(node, new TNode(key.substring(pos), node));
            }
        }
        if (pos != node.prefix.length()) {
            size++;
            node = splitEnd(node, pos);
        } else node.isEnd = true;
        return node;
    }

    private TNode splitNode(TNode node, int pos) {
        String slice = node.prefix.substring(0, pos);
        node.prefix = node.prefix.substring(pos);
        TNode newPrev = new TNode(slice, node.prev);
        node.prev = newPrev;
        newPrev.addSuccessor(node);
        if (newPrev.prev == null) {
            rootNodes.delete(newPrev.prefix.charAt(0));
            rootNodes.add(newPrev.prefix.charAt(0), newPrev);
        } else {
            newPrev.prev.successors.delete(node);
            newPrev.prev.successors.add(newPrev);
        }
        if (node.getSize() == 1) {
            TNode s = node.successors.get(0);
            node.prefix += s.prefix;
            node.successors = s.successors;
        }
        node = node.prev;
        return node;
    }

    private TNode splitEnd(TNode node, int pos) {
        String slice = node.prefix.substring(0, pos);
        TNode newPrev = new TNode(slice, node.prev);
        node.prev = newPrev;
        if (pos != node.prefix.length()) {
            node.prefix = node.prefix.substring(pos);
            newPrev.addSuccessor(node);
        }
        if (newPrev.prev == null) {
            rootNodes.delete(newPrev.prefix.charAt(0));
            rootNodes.add(newPrev.prefix.charAt(0), newPrev);
        } else {
            newPrev.prev.successors.delete(node);
            newPrev.prev.successors.add(newPrev);
        }
        node.isEnd = true;
        node = node.prev;
        return node;
    }

    @Override
    public boolean contains(String key) {
        if (key == null) throw new NullableArgumentException();
        TNode valueNode = getNode(key);
        return valueNode != null && valueNode.isEnd;
    }

    @Override
    public int getSize() {
        return rootNodes.getSize() + size;
    }

    public int getPairsCount() {
        throw new IllegalStateException("Method not implemented yet!");
    }

//    public AbstractList<Pair<String, AbstractList<String>>> search(String toSearch, int distance, int count) {
//        Set<Pair<String, AbstractList<String>>> result = lookup(toSearch, distance, count);
//        AbstractList<Pair<String, AbstractList<String>>> lst = new ArrayList<>();
//        lst.addFrom(result);
//        lst.sort(Comparator.comparingInt(value -> optimizedLevenstainDistance(value.getKey(), toSearch)));
//        return lst;
//    }
    //fixme fuzzy search works incorrectly
    public Set<Pair<String, AbstractList<String>>> lookup(String toSearch, int distance, int count) {
        TNode curr = rootNodes.get(toSearch.charAt(0));
        if (curr == null) {
            return search(0, 0, null, toSearch, distance, count).getResult();
        }
        TNode prev = curr;
        int pos = 0, prefixPos = 0;
        while (pos < toSearch.length()) {
            if (prefixPos >= curr.prefix.length()) {
                curr = getNext(curr, toSearch.charAt(pos));
                if (curr == null) {
                    return search(pos - 1, prefixPos - 1, prev, toSearch, distance, count).getResult();
                }
                prefixPos = 0;
                prev = curr;
            }
            if (curr.prefix.charAt(prefixPos) != toSearch.charAt(pos)) {
                curr = prefixPos == 0 ? curr.prev : curr;
                return search(pos - 1, prefixPos - 1, curr, toSearch, distance, count).getResult();
            }
            prefixPos++;
            pos++;
        }
        return search(pos, prefixPos, prev, toSearch, distance, count).getResult();
    }

    private SearchEntity search(int p1, int p2, TNode curr, String toSearch, int distance, int count) {
        SearchEntity result = new SearchEntity(count, toSearch, new Set<>());
        int temp = p2;
        if (temp == 0) ++temp;
        while (curr != null && !result.isFounded()) {
            fuzzyLookup(curr, p1, p2, distance, result);
            if (result.isFounded()) break;
            curr = curr.prev;
            p1 -= temp;
            p2 = 0;
            if (curr != null) temp = curr.prefix.length();

        }
        if (!result.isFounded()) {
            rootLevDistance(distance, result);
        }
        return result;
    }


    private void rootLevDistance(int typos, SearchEntity result) {
        if (typos < 0) return;
        for (HashNode<Character, TNode> n : rootNodes.items) {
            fuzzyLookup(n.getValue(), 1, 1, typos - 1, result);
            fuzzyLookup(n.getValue(), 0, 1, typos - 1, result);
            fuzzyLookup(n.getValue(), 1, 0, typos - 1, result);
            if (result.isFounded()) return;
        }
    }


    private TNode tryGetNext(TNode start, int p1, int p2, int typos, SearchEntity result) {
        if (p2 >= start.prefix.length() && p1 < result.toSearch.length()) {
            if (start.successors == null) {
                fuzzySearch(start, p1, p2, typos, result);
                return null;
            }
            for (TNode next : start.successors) {
                fuzzyLookup(next, p1, 0, typos, result);
            }
            return null;
        }
        return start;
    }

    private void fuzzySearch(TNode node, int p1, int p2, int typos, SearchEntity result) {
        if (node == null) return;
        if (node.successors == null) {
            if (levenstainDistance(result.toSearch, node.prefix, p1, p2) <= typos) {
                collectAll(result.founded, node);
            }
            return;
        }
        if (p2 < node.prefix.length()) {
            int gap = node.prefix.length() - p2;
            int end = p1 + (node.prefix.length() - p2) > result.toSearch.length() ? result.getSearchedLength() : p1 + (node.prefix.length() - p2);
            int tps = levenstainDistance(result.toSearch.substring(0, end), node.prefix, p1, p2);
            typos -= tps;
            if (typos < 0) return;
            p1 += gap;
            if (node.isEnd && p1 + typos >= result.toSearch.length()) {
                collectAll(result.founded, node);
            }
            for (TNode n : node.successors) {
                for (int i = p1; i <= p1 + gap; i++) {
                    fuzzyLookup(n, i, 1, typos - (p1 + gap - i), result);
                    fuzzyLookup(n, i, 0, typos - (p1 + gap - i), result);
                }
            }
            return;
        }
        for (TNode n : node.successors) {
            fuzzyLookup(n, p1 + 1, 1, typos - 1, result);
            fuzzyLookup(n, p1, 1, typos - 1, result);
            fuzzyLookup(n, p1 + 1, 0, typos - 1, result);
            if (result.isFounded()) return;
        }
    }

    private TNode getNode(String s) {
        TNode curr = rootNodes.get(s.charAt(0));
        if (curr == null) return null;
        int j = 0;
        for (int i = 1; i < s.length(); i++) {
            if (j >= curr.prefix.length()) {
                curr = getNext(curr, s.charAt(i));
                if (curr == null) return null;
            }
        }
        return curr;
    }

    private void fuzzyLookup(TNode start, int p1, int p2, int typos, SearchEntity result) {
        if (typos < 0 || start == null || result.isFounded()) return;
        if (p1 + typos >= result.getSearchedLength() && p2 != 0 && start.isEnd) {
            collectAll(result.getResult(), start);
            if (result.isFounded()) return;
        }
        start = tryGetNext(start, p1, p2, typos, result);
        if (start == null || p1 >= result.getSearchedLength()) return;
        while (p1 < result.getSearchedLength() && p2 < start.prefix.length() && !result.isFounded()) {
            if (result.toSearch.charAt(p1) == start.prefix.charAt(p2)) {
                ++p1;
                if (++p2 >= start.prefix.length()) {
                    tryGetNext(start, p1, p2, typos, result);
                    break;
                }
            } else {
                fuzzySearch(start, p1, p2, typos, result);
                break;
            }
        }
        if (p1 + typos >= result.toSearch.length() && !result.isFounded()) {
            collectAll(result.founded, start);
        }
    }

    private void collectAll(Set<Pair<String, AbstractList<String>>> founded, TNode node) {
        if (node == null) return;
        collectForNode(founded, node);
    }

    private void collectForNode(Set<Pair<String, AbstractList<String>>> founded, TNode node) {
        if (node.pairs == null) {
            founded.add(new Pair<>(getReversed(node), null));
            return;
        }
        AbstractList<String> values = new ArrayList<>(2);
        Pair<String, AbstractList<String>> pair = new Pair<>();
        String v = getReversed(node);
        pair.setKey(v);
        for (TNode val : node.pairs) {
            values.add(getReversed(val));
        }
        pair.setValue(values);
        founded.add(pair);
    }

    private String getReversed(TNode node) {
        StringBuilder prefix = new StringBuilder();
        while (node != null) {
            prefix.insert(0, node.prefix);
            node = node.prev;
        }
        return prefix.toString();
    }

    private int levenstainDistance(String s1, String s2, int start1, int start2) {
        if (s1.length() == 1 && s2.length() == 1) return s1.charAt(0) == s2.charAt(0) ? 0 : 1;
        if (start2 >= s2.length()) return s1.length() - start1;
        if (start1 >= s1.length()) return s2.length() - start2;
        int p1 = start1, p2 = start2;
        int d1 = s1.length() - 1, d2 = s2.length() - 1;
        while (p1 < s1.length() && p2 < s2.length() && s1.charAt(p1) == s2.charAt(p2)) {
            p1++;
            p2++;
        }
        if (p1 == d1) return d2 + 1 - p2;
        if (p2 == d2) return d1 + 1 - p1;
        p1--;
        p2--;
        while (d1 > p1 && d2 > p2 && s1.charAt(d1) == s2.charAt(d2)) {
            d1--;
            d2--;
        }
        if (p1 == d1) return d2 - p2;
        if (p2 == d2) return d1 - p1;
        int l1 = d1 - p1, l2 = d2 - p2;
        int[][] dp = new int[l1 + 1][l2 + 1];
        for (int i = 0; i <= l1; i++) {
            for (int j = 0; j <= l2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int m = s1.charAt(p1 + i) == s2.charAt(p2 + j) ? 0 : 1;
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + m, Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return Math.abs(dp[l1][l2]);
    }

}
