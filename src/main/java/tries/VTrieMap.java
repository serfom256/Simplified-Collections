package tries;

import additional.nodes.Pair;
import sets.AbstractSet;

import java.util.Iterator;

public class VTrieMap<V> implements AbstractTrieMap<String, V> {

    @Override
    public void add(String key, V value) {
        throw new IllegalStateException("Method not implemented yet!");
    }

    @Override
    public boolean containsKey(String key) {
        throw new IllegalStateException("Method not implemented yet!");
    }

    @Override
    public boolean containsValue(V value) {
        throw new IllegalStateException("Method not implemented yet!");
    }

    @Override
    public Pair<String, AbstractSet<V>> deleteKey(String key) {
        throw new IllegalStateException("Method not implemented yet!");
    }

    @Override
    public Iterator<Pair<String, AbstractSet<V>>> iterator() {
        throw new IllegalStateException("Method not implemented yet!");
    }
}
