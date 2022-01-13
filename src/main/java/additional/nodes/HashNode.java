package additional.nodes;

public interface HashNode<K, V> {

    K getKey();

    V getValue();

    int getHash();

}
