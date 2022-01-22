package tries;

public class ExtendedTrie extends FuzzyTrie {

    public ExtendedTrie() {
        super();
    }

    @Override
    public void add(String sequence) {
        super.add(sequence);
    }

    public void add(char[] sequence) {
        super.add(String.valueOf(sequence));
    }

    public void add(int[] sequence) {
        char[] wrapper = new char[sequence.length];
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] < -10 || sequence[i] > 10) throw new IllegalArgumentException();
            wrapper[i] = (char) sequence[i];
        }
        super.add(String.valueOf(wrapper));
    }

    public void add(int integer) {
        super.add(String.valueOf(integer));
    }

    public void add(long lng) {
        super.add(String.valueOf(lng));
    }

    public void add(float flt) {
        super.add(String.valueOf(flt));
    }

    public void add(double dbl) {
        super.add(String.valueOf(dbl));
    }

    public void add(Object o) {
        super.add(String.valueOf(o));
    }


    @Override
    public boolean delete(String sequence) {
        return super.delete(sequence);
    }

    public boolean delete(char[] sequence) {
        return super.delete(String.valueOf(sequence));
    }

    public boolean delete(int[] sequence) {
        char[] wrapper = new char[sequence.length];
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] < -10 || sequence[i] > 10) throw new IllegalArgumentException();
            wrapper[i] = (char) sequence[i];
        }
        return super.delete(String.valueOf(wrapper));
    }

    public boolean delete(int integer) {
        return super.delete(String.valueOf(integer));
    }

    public boolean delete(long lng) {
        return super.delete(String.valueOf(lng));
    }

    public boolean delete(float flt) {
        return super.delete(String.valueOf(flt));
    }

    public boolean delete(double dbl) {
        return super.delete(String.valueOf(dbl));
    }

    public boolean delete(Object o) {
        return super.delete(String.valueOf(o));
    }


}
