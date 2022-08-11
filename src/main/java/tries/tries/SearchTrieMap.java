package tries.tries;

import additional.dynamicstring.DynamicLinkedString;
import additional.dynamicstring.DynamicString;
import additional.exceptions.NullableArgumentException;
import additional.nodes.Pair;
import hashtables.HashTable;
import lists.List;
import lists.impl.ArrayList;
import sets.HashedSet;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SearchTrieMap {

    private final TNode root;
    private final AtomicInteger pairs;
    private final HashTable<Character, RootNode> rootNodes;
    private final Lock rootLock = new ReentrantLock();

    private static class RootNode {

        private final Lock lock;
        private TNode node;

        private RootNode(TNode node) {
            this.node = node;
            this.lock = new ReentrantLock();
        }
    }

    private static class SearchEntity {
        private final int count;
        private final String toSearch;
        private final List<Pair<String, List<String>>> founded;
        private final HashedSet<TNode> set;


        public SearchEntity(int count, String toSearch, List<Pair<String, List<String>>> founded) {
            this.count = count;
            this.toSearch = toSearch;
            this.founded = founded;
            set = new HashedSet<>();
        }

        public int getSearchedLength() {
            return toSearch.length();
        }

        public boolean isFounded() {
            return count <= founded.getSize();
        }

        public List<Pair<String, List<String>>> getResult() {
            return founded;
        }
    }

    private static class TNode {
        private char element;
        private String seq;
        private boolean isKey;
        private boolean isVal;
        private final TNode prev;
        private ArrayList<TNode> successors;
        private List<TNode> pairs;

        public TNode(Character element, TNode prev) {
            this.element = element;
            this.prev = prev;
            this.isKey = this.isVal = false;
            this.successors = new ArrayList<>(4);
        }

        public TNode(Character element, TNode prev, String seq) {
            this.element = element;
            this.prev = prev;
            this.seq = seq;
            this.isKey = this.isVal = false;
        }


        public TNode() {
            this.isKey = this.isVal = false;
            this.pairs = new ArrayList<>(4);
            this.prev = null;
        }

        TNode getNode(char c) {
            if (successors == null) return null;
            for (TNode node : successors) {
                if (node.element == c) return node;
            }
            return null;
        }

        public void addPair(TNode newPair) {
            if (pairs == null) pairs = new ArrayList<>(2);
            pairs.addIfAbsent(newPair);
        }

        public void addSuccessor(TNode node) {
            if (successors == null) successors = new ArrayList<>(4);
            successors.add(node);
        }

        public int getSuccessorsSize() {
            if (successors == null) return 0;
            return successors.getSize();
        }

        public boolean isEmpty() {
            return seq == null || seq.length() == 0;
        }

    }

    public SearchTrieMap() {
        this.root = new TNode();
        this.rootNodes = new HashTable<>(128);
        this.pairs = new AtomicInteger();
    }

    public void addConcurrent(String sequence) {
        char f = sequence.charAt(0);
        RootNode rn = rootNodes.get(f);
        if (rn == null) {
            rootLock.lock();
            if (rootNodes.get(f) == null) {
                insertToRoot(sequence).isKey = true;
                pairs.incrementAndGet();
                rootLock.unlock();
                return;
            }
            rn = rootNodes.get(f);
            rootLock.unlock();
        }
        rn.lock.lock();
        TNode curr = rn.node;
        for (int i = 1; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            TNode next = curr.getNode(c);
            if (next == null) {
                String sq = sequence.substring(i);
                if (Objects.equals(curr.seq, sq)) {
                    rn.lock.unlock();
                    return;
                }
                TNode res = buildTree(curr, sq);
                if (!res.isKey) pairs.incrementAndGet();
                res.isKey = true;
                rn.lock.unlock();
                return;
            }
            curr = next;
        }
        TNode res = splitTreeSync(curr);
        if (!res.isKey) pairs.incrementAndGet();
        res.isKey = true;
        rn.lock.unlock();
    }

    private TNode splitTreeSync(TNode node) {
        if (!node.isEmpty()) {
            TNode prev = node.prev;
            if (prev == null) {
                prev = root;
            }
            char c = node.element;
            prev.successors.delete(node);
            TNode curr = new TNode(c, prev);
            prev.addSuccessor(curr);
            TNode toNext = new TNode(node.seq.charAt(0), curr, node.seq.substring(1));
            toNext.isKey = node.isKey;
            toNext.isVal = node.isVal;
            curr.addSuccessor(toNext);
            if (prev == root) {
                rootNodes.get(c).node = curr;
            }
            return curr;
        }
        return node;
    }

    /**
     * Appends the new key-values pair to the TrieMap of the specified key and the specified value
     *
     * @throws NullableArgumentException if the specified key or value is null
     * @throws IllegalArgumentException  if the length of the specified key is equals 0
     */
    public void add(String key, String value) {
        if (key == null || value == null) throw new NullableArgumentException();
        if (key.length() == 0) throw new IllegalArgumentException();
        TNode keyNode = putSequence(key);
        if (value.length() != 0) {
            TNode valueNode = putSequence(value);
            keyNode.addPair(valueNode);
            valueNode.addPair(keyNode);
            valueNode.isVal = true;
        }
        if (!keyNode.isKey) pairs.incrementAndGet();
        keyNode.isKey = true;
    }

    private TNode putSequence(String sequence) {
        RootNode rn = rootNodes.get(sequence.charAt(0));
        if (rn == null) {
            return insertToRoot(sequence);
        }
        TNode curr = rn.node;
        for (int i = 1; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            TNode next = curr.getNode(c);
            if (next == null) {
                String sq = sequence.substring(i);
                if (Objects.equals(curr.seq, sq)) return curr;
                return buildTree(curr, sq);
            }
            curr = next;
        }
        return splitTree(curr);
    }


    private TNode splitTree(TNode node) {
        if (!node.isEmpty()) {
            TNode prev = node.prev;
            if (prev == null) {
                prev = root;
            }
            prev.successors.delete(node);
            TNode curr = new TNode(node.element, prev);
            prev.addSuccessor(curr);
            TNode toNext = new TNode(node.seq.charAt(0), curr, node.seq.substring(1));
            toNext.isKey = node.isKey;
            toNext.isVal = node.isVal;
            curr.addSuccessor(toNext);
            if (prev == root) {
                rootNodes.get(node.element).node = curr;
            }
            return curr;
        }
        return node;
    }

    private TNode insertToRoot(String seq) {
        char c = seq.charAt(0);
        TNode curr = new TNode(c, null, seq.substring(1));
        this.rootNodes.add(c, new RootNode(curr));
        root.addSuccessor(curr);
        return curr;
    }


    private TNode buildTree(TNode node, String seq) {
        if (node.seq == null) {
            TNode newNode = new TNode(seq.charAt(0), node, seq.substring(1));
            node.addSuccessor(newNode);
            return newNode;
        }
        String nodeSeq = node.seq;
        node.seq = null;
        boolean isKey = node.isKey, isVal = node.isVal;
        node.isKey = node.isVal = false;
        int pos = 0, len = Math.min(seq.length(), nodeSeq.length());
        while (pos < len && seq.charAt(pos) == nodeSeq.charAt(pos)) {
            TNode newNode = new TNode(seq.charAt(pos), node);
            node.addSuccessor(newNode);
            node = newNode;
            pos++;
        }
        if (pos < len) {
            TNode newNode = new TNode(nodeSeq.charAt(pos), node, nodeSeq.substring(pos + 1));
            TNode inserted = new TNode(seq.charAt(pos), node, seq.substring(pos + 1));
            newNode.isKey = isKey;
            newNode.isVal = isVal;
            node.addSuccessor(newNode);
            node.addSuccessor(inserted);
            return inserted;
        }
        if (pos < nodeSeq.length()) {
            TNode newNode = new TNode(nodeSeq.charAt(pos), node, nodeSeq.substring(pos + 1));
            newNode.isKey = isKey;
            newNode.isVal = isVal;
            node.addSuccessor(newNode);
            return node;
        } else if (pos < seq.length()) {
            TNode newNode = new TNode(seq.charAt(pos), node, seq.substring(pos + 1));
            node.isKey = isKey;
            node.isVal = isVal;
            node.addSuccessor(newNode);
            return newNode;
        }
        return node;
    }

    public List<Pair<String, List<String>>> lookup(String input, int distance, int count) {
        if (input == null) throw new NullableArgumentException();
        if (input.length() <= 1 || input.length() <= distance) {
            throw new IllegalArgumentException("Input length must be more than specified distance");
        }
        SearchEntity result = new SearchEntity(count, input, new ArrayList<>());
        TNode curr = root;
        int len = input.length();
        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);
            TNode next = curr.getNode(c);
            if (next == null) {
                search(i, curr, distance, result);
                return result.getResult();
            }
            curr = next;
        }
        search(len, curr, distance, result);
        return result.getResult();
    }

    private void search(int pos, TNode curr, int distance, SearchEntity toSearch) {
        for (int j = pos; j >= 0 && curr != null; j--) {
            fuzzyCompound(curr, j, distance, toSearch);
            if (toSearch.founded.getSize() != 0) break;
            curr = curr.prev;
        }
    }

    private void fuzzyCompound(TNode start, int pos, int typos, SearchEntity entity) {
        if (typos < 0 || start == null) return;
        if ((pos + typos >= entity.getSearchedLength() && start.prev != null && start.seq == null)) {
            collectForNode(entity, start);
        }
        if (start.seq != null && distance(entity.toSearch, start.seq, pos, 0) <= typos) {
            collectForNode(entity, start);
            return;
        }
        if (start.successors == null) return;
        for (TNode v : start.successors) {
            if (pos < entity.getSearchedLength() && v.element == entity.toSearch.charAt(pos)) {
                fuzzyCompound(v, pos + 1, typos, entity);
            } else {
                fuzzyCompound(v, pos + 1, typos - 1, entity);
                fuzzyCompound(v, pos, typos - 1, entity);
                fuzzyCompound(start, pos + 1, typos - 1, entity);
            }
        }
    }

    private void collectForNode(SearchEntity entity, TNode node) {
        if (!node.isVal && !node.isKey || entity.set.contains(node)) return;
        List<String> values = new ArrayList<>(2);
        Pair<String, List<String>> pair = new Pair<>();
        String v = getReversed(node);
        if (node.isKey) pair.setKey(v);
        else values.add(v);
        if (node.pairs != null) {
            for (TNode val : node.pairs) {
                if (val.isKey) pair.setKey(getReversed(val));
                if (val.isVal) values.add(getReversed(val));
            }
        }
        pair.setValue(values);
        entity.getResult().add(pair);
        entity.set.add(node);
    }


    private String getReversed(TNode node) {
        DynamicString prefix = new DynamicLinkedString();
        if (node.seq != null) prefix.add(node.seq);
        while (node != null) {
            prefix.addFirst(node.element);
            node = node.prev;
        }
        return prefix.toString();
    }

    private static int distance(String s1, String s2, int start1, int start2) {
        if (start2 >= s2.length()) return s1.length() - start1;
        if (start1 >= s1.length()) return s2.length() - start2;
        int p1 = start1, p2 = start2;
        int d1 = s1.length() - 1, d2 = s2.length() - 1;
        while (p1 < s1.length() && p2 < s2.length() && s1.charAt(p1) == s2.charAt(p2)) {
            p1++;
            p2++;
        }
        if (p1 == d1) return d2 - (p1 - 1);
        if (p2 == d2) return d1 - (p2 - 1);
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

    public int getSize() {
        return pairs.get();
    }

    public void clear() {
        synchronized (this) {
            rootNodes.clear();
            if (root.successors != null) root.successors.clear();
            root.isKey = root.isVal = false;
            pairs.set(0);
        }
    }

}
