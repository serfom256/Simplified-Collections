package tries;


public interface TrieMap<K, V> {

    enum Verbose {
        MAX,
        MIN
    }

    boolean contains(K key);

    int getSize();

    int getPairsCount();

}
