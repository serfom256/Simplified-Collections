package tries;


public interface AbstractTrieMap<K, V> {

    enum Verbose {
        MAX,
        MIN
    }

    boolean contains(K key);

    int getSize();

    int getPairsCount();

}
