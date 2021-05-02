package HashSet;

import tests.HashTableTests.HashTable;

import java.util.HashMap;

public class Set<E> {
    private final HashTable<E, Object> hashTable;
    private final Object VALUE = 0;

    public Set() {
        hashTable = new HashTable<>();
    }

    public void add(E element) {
        hashTable.add(element, VALUE);
    }

    public void remove(E element) {
        hashTable.remove(element);
    }

    public boolean contains(E element) {
        return hashTable.containsKey(element);
    }
    public int getSize(){
        return hashTable.getSize();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        for (E e: hashTable) {
            res.append(e).append(", ");
        }
        return res.substring(0,res.length()-2)+"}";
    }
}

class Runner {
    public static void main(String[] args) {
        Set<String> set= new Set<>();
        for (int i = 0; i < 20; i++) {
            set.add(i+"_key");
        }
        System.out.println(set);
    }
}
//{0_key, 5_key, 14_key, 7_key, 19_key, 16_key, 3_key, 9_key, 12_key, 1_key, 15_key, 10_key, 6_key, 17_key, 18_key, 8_key, 13_key, 11_key, 4_key, 2_key}
//{0_key, 5_key, 7_key, 14_key, 19_key, 16_key, 3_key, 9_key, 12_key, 1_key, 10_key, 15_key, 6_key, 17_key, 18_key, 8_key, 13_key, 11_key, 4_key, 2_key}
//{0_key, 5_key, 14_key, 19_key, 16_key, 3_key, 9_key, 12_key, 1_key, 15_key, 6_key, 17_key, 18_key, 8_key, 13_key, 11_key, 4_key, 2_key}