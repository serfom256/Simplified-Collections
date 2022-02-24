package math.operations;

public class NumberComparator {

    public int compare(String s1, String s2) {
        if (s1.length() > s2.length()) return 1;
        if (s1.length() < s2.length()) return -1;
        int len = s1.length();
        int pos = 0;
        while (pos < len && s1.charAt(pos) == s2.charAt(pos)) {
            pos++;
        }
        if (pos == len) return 0;
        return s1.charAt(pos) > s2.charAt(pos) ? 1 : -1;
    }
}
