package Trie;

public class ExtendedTrie extends FuzzySearchTrie {

    public ExtendedTrie() {
        super();
    }

    @Override
    public void put(String sequence) {
        super.put(sequence);
    }

    public void put(char[] sequence) {
        super.put(String.valueOf(sequence));
    }

    public void put(int[] sequence) {
        char[] wrapper = new char[sequence.length];
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] < -10 || sequence[i] > 10) throw new IllegalArgumentException();
            wrapper[i] = (char) sequence[i];
        }
        super.put(String.valueOf(wrapper));
    }

    public void put(int integer) {
        super.put(String.valueOf(integer));
    }

    public void put(long lng) {
        super.put(String.valueOf(lng));
    }

    public void put(float flt) {
        super.put(String.valueOf(flt));
    }

    public void put(double dbl) {
        super.put(String.valueOf(dbl));
    }

    public void put(Object o) {
        super.put(String.valueOf(o));
    }


    @Override
    public boolean remove(String sequence) {
        return super.remove(sequence);
    }

    public boolean remove(char[] sequence) {
        return super.remove(String.valueOf(sequence));
    }

    public boolean remove(int[] sequence) {
        char[] wrapper = new char[sequence.length];
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] < -10 || sequence[i] > 10) throw new IllegalArgumentException();
            wrapper[i] = (char) sequence[i];
        }
        return super.remove(String.valueOf(wrapper));
    }

    public boolean remove(int integer) {
        return super.remove(String.valueOf(integer));
    }

    public boolean remove(long lng) {
        return super.remove(String.valueOf(lng));
    }

    public boolean remove(float flt) {
        return super.remove(String.valueOf(flt));
    }

    public boolean remove(double dbl) {
        return super.remove(String.valueOf(dbl));
    }

    public boolean remove(Object o) {
        return super.remove(String.valueOf(o));
    }


    @Override
    public int getSize() {
        return super.getSize();
    }

    @Override
    public int getEntriesCount() {
        return super.getEntriesCount();
    }
}
