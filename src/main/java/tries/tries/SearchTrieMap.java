package tries.tries;

import additional.dynamicstring.DynamicLinkedString;
import additional.dynamicstring.DynamicString;
import additional.exceptions.NullableArgumentException;
import lists.List;
import lists.impl.ArrayList;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SearchTrieMap<V> {

    private final TNode root;
    private final AtomicInteger items;
    private final Map<Character, RootNode> rootNodes;

    private class RootNode {

        private final Lock lock;
        private TNode node;

        private RootNode(TNode node) {
            this.node = node;
            this.lock = new ReentrantLock();
        }
    }

    public class LookupResult {
        private final String key;
        private final List<V> values;

        public LookupResult(String key, List<V> values) {
            this.key = key;
            this.values = values;
        }

        public String getKey() {
            return key;
        }

        public List<V> getValues() {
            return values;
        }

        @Override
        public String toString() {
            return "LookupResult{" +
                    "key='" + key + '\'' +
                    ", values=" + values +
                    '}';
        }
    }

    private class SearchEntity {
        private final int count;
        private int typos;
        private final String[] toSearch;
        private String current;
        private final List<LookupResult> founded;
        private final java.util.Set<TNode> set;
        private int wordPos;


        public SearchEntity(int count, int typos, String[] toSearch, List<LookupResult> founded) {
            this.count = count;
            this.typos = typos;
            this.toSearch = toSearch;
            this.founded = founded;
            this.current = toSearch[0];
            set = new HashSet<>();
            wordPos = 0;
        }

        public String getNext() {
            return toSearch[++wordPos];
        }

        public boolean hasNextSequence() {
            return wordPos + 1 < toSearch.length;
        }

        public int getSearchedLength() {
            return current.length();
        }

        public boolean isFounded() {
            return count <= founded.getSize();
        }

        public List<LookupResult> getResult() {
            return founded;
        }

        public void addEntry(LookupResult result) {
            founded.add(result);
        }

        public void memorize(TNode result) {
            set.add(result);
        }

        public boolean hasNode(TNode node) {
            return set.contains(node);
        }

        public int getTypos() {
            return typos;
        }

        public String getCurrent() {
            return current;
        }

        public int getWordPos() {
            return wordPos;
        }

        public void setTypos(int typos) {
            this.typos = typos;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public void setWordPos(int wordPos) {
            this.wordPos = wordPos;
        }
    }

    private class TNode {
        char element;
        String seq;
        boolean isEnd;
        TNode prev;
        java.util.List<TNode> successors;
        java.util.List<V> values;

        public TNode(Character element, TNode prev) {
            this.element = element;
            this.prev = prev;
            this.isEnd = false;
        }

        public TNode(Character element, TNode prev, String seq) {
            this.element = element;
            this.prev = prev;
            this.seq = seq;
            this.isEnd = false;
        }

        public TNode() {
            this.isEnd = false;
            this.prev = null;
        }

        public TNode getNode(char c) {
            if (successors == null) return null;
            for (TNode node : successors) {
                if (node.element == c) return node;
            }
            return null;
        }

        public void addSuccessor(TNode node) {
            if (successors == null) successors = new CopyOnWriteArrayList<>();
            successors.add(node);
        }

        public void addValue(V value) {
            if (values == null) values = new CopyOnWriteArrayList<>();
            if (!values.contains(value)) {
                values.add(value);
            }
        }

        public int getEndSize() {
            return seq == null ? 0 : seq.length();
        }

        public boolean isEmpty() {
            return seq == null || seq.length() == 0;
        }


    }

    public SearchTrieMap() {
        this.root = new TNode();
        this.rootNodes = new ConcurrentHashMap<>(128);
        this.items = new AtomicInteger();
    }

    public synchronized List<V> delete(String key) {
        TNode node = getNode(key);
        if (node == null) return null;
        node.isEnd = false;
        removeBranch(node);
        ArrayList<V> result = new ArrayList<>();
        result.addFrom(node.values);
        return result;
    }

    public synchronized boolean delete(String key, V value) {
        TNode node = getNode(key);
        if (node == null) return false;
        node.isEnd = false;
        if (!node.values.remove(value)) return false;
        removeBranch(node);
        return true;
    }

    private void removeBranch(TNode node) {
        if (node.successors == null || node.successors.isEmpty()) {
            while (node.prev != null && node.prev.successors.size() == 1 && !node.prev.isEnd) {
                node = node.prev;
            }
            if (node.prev != null) {
                node.prev.successors.remove(node);
                node.prev = null;
            } else {
                rootNodes.remove(node.element);
                root.successors.remove(node);
            }
        }
    }

    /**
     * Appends the new key-values pair to the TrieMap of the specified key and the specified value
     *
     * @throws NullableArgumentException if the specified key or value is null
     * @throws IllegalArgumentException  if the length of the specified key is equals 0
     */
    public void add(String key, V value) {
        char f1 = key.charAt(0);
        RootNode rn1 = rootNodes.get(f1);
        if (rn1 == null) {
            synchronized (this) {
                rn1 = rootNodes.get(f1);
                TNode keyNode;
                if (rn1 == null) {
                    keyNode = insertToRoot(key);
                } else {
                    keyNode = putSequence(key);
                    if (!keyNode.isEnd) items.incrementAndGet();
                }
                keyNode.addValue(value);
                keyNode.isEnd = true;
            }
            return;
        }
        rn1.lock.lock();
        TNode keyNode = putSequence(key);
        if (!keyNode.isEnd) items.incrementAndGet();
        keyNode.addValue(value);
        keyNode.isEnd = true;
        rn1.lock.unlock();
    }


    private TNode getNode(String key) {
        TNode curr = root;
        for (int i = 0; i < key.length(); i++) {
            curr = curr.getNode(key.charAt(i));
            if (curr == null) return null;
        }
        return curr;
    }

    private TNode putSequence(String sequence) {
        RootNode rn = rootNodes.get(sequence.charAt(0));
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
            prev.successors.remove(node);
            TNode curr = new TNode(node.element, prev);
            prev.addSuccessor(curr);
            TNode toNext = new TNode(node.seq.charAt(0), curr, node.seq.substring(1));
            toNext.isEnd = node.isEnd;
            toNext.values = node.values;
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
        this.rootNodes.put(c, new RootNode(curr));
        this.root.addSuccessor(curr);
        this.items.incrementAndGet();
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
        boolean isEnd = node.isEnd;
        node.isEnd = false;
        java.util.List<V> ids = node.values;
        node.values = null;
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
            newNode.isEnd |= isEnd;
            newNode.values = ids;
            node.addSuccessor(newNode);
            node.addSuccessor(inserted);
            return inserted;
        } else if (pos < nodeSeq.length()) {
            TNode newNode = new TNode(nodeSeq.charAt(pos), node, nodeSeq.substring(pos + 1));
            newNode.isEnd |= isEnd;
            newNode.values = ids;
            node.addSuccessor(newNode);
            return node;
        } else if (pos < seq.length()) {
            TNode newNode = new TNode(seq.charAt(pos), node, seq.substring(pos + 1));
            node.isEnd |= isEnd;
            node.values = ids;
            node.addSuccessor(newNode);
            return newNode;
        }
        return node;
    }

    private int getFuzziness(String s) {
        int fuzziness = 0;
        for (int i = 1; i < s.length(); i *= 2 + 2) {
            fuzziness++;
        }
        return fuzziness;
    }

    public List<LookupResult> search(String input, int distance, int count, boolean fuzziness) {
        String[] indexes = input.split(" ");
        SearchEntity result = new SearchEntity(count, distance, indexes, new ArrayList<>());
        int attempts = indexes.length;
        while (result.getWordPos() < indexes.length && attempts >= 0) {
            int pPos = result.getWordPos();
            int estimatedDistance = distance;
            if (fuzziness) {
                estimatedDistance = Math.min(getFuzziness(result.getCurrent()), distance);
            }
            result.setTypos(estimatedDistance);
            fuzzySearch(root, 0, estimatedDistance, result);
            if (pPos == result.getWordPos() && result.hasNextSequence()) {
                result.setCurrent(result.getNext());
            }
            attempts--;
        }
        return result.getResult();
    }

    public List<LookupResult> matchPrefix(String input, int distance, int count) {
        int fuzziness = getFuzziness(input);
        int estimatedDistance = Math.min(distance, fuzziness);
        return searchForPrefix(input, estimatedDistance, count, root);
    }

    private List<LookupResult> searchForPrefix(String input, int distance, int count, TNode curr) {
        int len = input.length() - 1;
        SearchEntity searchEntity = new SearchEntity(count, distance, new String[]{input}, new ArrayList<>());
        for (int i = 0; i <= len; i++) {
            char c = input.charAt(i);
            TNode next = curr.getNode(c);
            if (next == null) {
                len = i;
                break;
            }
            if (i == len && next.isEnd) collectForNode(searchEntity, curr);
            curr = next;
        }
        return searchForPrefix(len, curr, searchEntity);
    }

    private List<LookupResult> searchForPrefix(int pos, TNode curr, SearchEntity entity) {
        for (int j = pos; j >= 0 && !entity.isFounded() && curr != null; j--, curr = curr.prev) {
            fuzzyPrefixSearch(curr, j, entity.getTypos(), entity);
        }
        return entity.getResult();
    }

    private void fuzzyPrefixSearch(TNode start, int pos, int typos, SearchEntity entity) {
        if (typos < 0 || entity.isFounded()) return;
        int endSize = start.getEndSize();
        if (pos + typos + endSize >= entity.getCurrent().length()) {
            collectForNode(entity, start, new StringBuilder(getReversed(start)));
            if (entity.isFounded()) return;
        }
        if (start.successors == null) return;
        for (TNode v : start.successors) {
            char k = v.element;
            if (pos < entity.getSearchedLength() && k == entity.getCurrent().charAt(pos)) {
                fuzzyPrefixSearch(v, pos + 1, typos, entity);
            } else {
                fuzzyPrefixSearch(v, pos + 1, typos - 1, entity);
            }
            fuzzyPrefixSearch(v, pos, typos - 1, entity);
            fuzzyPrefixSearch(start, pos + 1, typos - 1, entity);
            if (entity.isFounded()) return;
        }
    }

    private void fuzzySearch(TNode start, int pos, int typos, SearchEntity entity) {
        if (typos < 0 || start == null || entity.isFounded()) return;
        if (start.isEnd && distance(getReversed(start), entity.getCurrent()) <= entity.getTypos()) {
            collectForNode(entity, start);
        }
        if ((start.isEnd || (pos + typos) >= entity.getSearchedLength()) && entity.hasNextSequence()) {
            findNext(start, pos, typos, entity);
        }
        if (start.successors == null) return;
        for (TNode v : start.successors) {
            char k = v.element;
            if (pos < entity.getSearchedLength() && k == entity.getCurrent().charAt(pos)) {
                fuzzySearch(v, pos + 1, typos, entity);
            } else {
                fuzzySearch(v, pos + 1, typos - 1, entity);
            }
            fuzzySearch(v, pos, typos - 1, entity);
            fuzzySearch(start, pos + 1, typos - 1, entity);
            if (entity.isFounded()) return;
        }
    }

    private void findNext(TNode start, int pos, int typos, SearchEntity entity) {
        String next = entity.getNext();
        String curr = entity.getCurrent();
        int founded = entity.getResult().getSize();
        entity.setCurrent(entity.getCurrent() + " " + next);
        fuzzySearch(start, pos, typos, entity);
        entity.setCurrent(curr);
        if (founded == entity.getResult().getSize()) {
            entity.setWordPos(entity.getWordPos() - 1);
        }
    }

    private void collectForNode(SearchEntity entity, TNode node) {
        if (!node.isEnd || entity.hasNode(node)) return;
        ArrayList<V> values = new ArrayList<>(node.values.size() + 1);
        values.addFrom(node.values);
        LookupResult lookupResult = new LookupResult(getReversed(node), values);
        entity.addEntry(lookupResult);
        entity.memorize(node);
    }

    private void collectForNode(SearchEntity entity, TNode node, StringBuilder prefix) {
        if (node == null || entity.isFounded() || entity.hasNode(node)) return;
        entity.memorize(node);
        if (node.isEnd) {
            ArrayList<V> values = new ArrayList<>(node.values.size() + 1);
            values.addFrom(node.values);
            entity.addEntry(new LookupResult(prefix.toString(), values));
        }
        if (node.successors == null) return;
        for (TNode curr : node.successors) {
            if (entity.isFounded()) return;
            prefix.append(curr.element);
            collectForNode(entity, node, prefix);
            prefix.delete(prefix.length() - 1, prefix.length());
        }
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

    private static int distance(String s1, String s2) {
        int len1 = s1.length(), len2 = s2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int a = 0;
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) ++a;
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + a, Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[len1][len2];
    }

    public int getSize() {
        return items.get();
    }

    public void clear() {
        synchronized (this) {
            rootNodes.clear();
            if (root.successors != null) root.successors.clear();
            root.isEnd = false;
            items.set(0);
        }
    }

    /**
     * Helps with printing all entries from the TrieMap
     */
    private void toStringHelper(DynamicString res, TNode node) {
        if (node.successors == null) return;
        for (TNode c : node.successors) {
            if (c.isEnd) {
                res.add(c.values).add(", ");
            }
            toStringHelper(res, c);
        }
    }

    @Override
    public String toString() {
        if (items.get() == 0) return "[]";
        DynamicString s = new DynamicLinkedString('[');
        toStringHelper(s, root);
        return s.replace(s.getSize() - 2, ']').toString();
    }

}
