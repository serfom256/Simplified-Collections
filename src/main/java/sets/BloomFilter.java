package sets;

import java.util.function.ToIntFunction;

public class BloomFilter {

    private final long[] array;
    private int size;
    private final ToIntFunction<String> hashFunction;

    public BloomFilter(int logicalSize) {
        this.array = new long[logicalSize];
        this.size = 0;
        this.hashFunction = String::hashCode;
    }

    public boolean add(String value) {
        int hash = getHash(hashFunction.applyAsInt(value));
        long isContains = (array[hash >>> 6] |= 1L << hash);
        if (isContains != 0) size++;
        return isContains != 0;
    }

    public boolean contains(String value) {
        int hash = getHash(hashFunction.applyAsInt(value));
        return (array[hash >>> 6] & (1L << hash)) != 0;
    }

    private int getHash(int hash) {
        return hash & (array.length - 1);
    }
}
