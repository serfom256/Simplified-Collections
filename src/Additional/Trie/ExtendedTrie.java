package Additional.Trie;

public class ExtendedTrie extends Trie {

    public ExtendedTrie() {
        super();
    }


    public void put(String sequence) {
        char[] wrapper = new char[sequence.length()];
        for (int i = 0; i < sequence.length(); i++)
            wrapper[i] = sequence.charAt(i);
        super.put(wrapper);
    }

    @Override
    public void put(char[] sequence) {
        char[] wrapper = new char[sequence.length];
        for (int i = 0; i < sequence.length; i++)
            wrapper[i] = sequence[i];
        super.put(wrapper);
    }

    public void put(int[] sequence) {
        char[] wrapper = new char[sequence.length];
        for (int i = 0; i < sequence.length; i++)
            wrapper[i] = (char) sequence[i];
        super.put(wrapper);
    }

    public int getSize(){
        return super.getSize();
    }

    public int getEntriesCount(){
        return super.getEntriesCount();
    }
}
