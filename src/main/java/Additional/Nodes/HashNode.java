package Additional.Nodes;

public interface HashNode<K, V> {
    K getKey();

    V getValue();

    int getHash();

}
