package hashtables;

public interface AbstractMap<K, V> {

    void add(K key, V value);

    void clear();

    V delete(K key);

    V get(K key);

    boolean replace(K oldKey, K newKey);

    boolean containsKey(K key);

    boolean deleteValue(K key, V value);

    int getSize();

}
