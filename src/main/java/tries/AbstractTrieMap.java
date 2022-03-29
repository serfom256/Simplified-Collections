package tries;


import additional.nodes.Pair;
import sets.AbstractSet;

public interface AbstractTrieMap<K, V> extends Iterable<Pair<K, AbstractSet<V>>> {

    enum Verbose {
        MAX,
        MIN
    }

    void add(K key, V value);

    boolean containsKey(K key);

    boolean containsValue(V value);

    Pair<K, AbstractSet<V>> deleteKey(K key);

}
