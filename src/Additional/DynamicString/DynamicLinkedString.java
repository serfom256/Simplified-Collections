package Additional.DynamicString;

public class DynamicLinkedString implements AbstractDynamicString {

    //FIXME
    public DynamicLinkedString(char[] lst) {
        this();
        for (int i = 0; i < lst.length; i++) {
            add(lst[i]);
        }
//        if (lst.length == 0) return;
//        size = 1;
//        Node curr = new Node(lst[0]);
//        head = last = curr;
//        for (int i = 1; i < lst.length; i++, size++) {
//            Node p = curr;
//            curr.next = new Node(lst[i]);
//            last = curr;
//            last.prev = p;
//            curr = curr.next;
//        }
    }

    //FIXME
    public DynamicLinkedString(String str) {
        this();
        for (int i = 0; i < str.length(); i++) {
            add(str.charAt(i));
        }
//        if(str == null || str.length() == 0) return;
//        size = 1;
//        Node curr = new Node(str.charAt(0));
//        head = last = curr;
//        for (int i = 1; i < str.length(); i++, size++) {
//            curr.next = new Node(str.charAt(i));
//            last = curr.next;
//            last.prev = curr;
//            curr = curr.next;
//        }
    }

    public DynamicLinkedString(AbstractDynamicString str) {

    }

    private static class Node {
        char val;
        Node next;
        Node prev;

        Node(char val) {
            this.val = val;
        }
    }

    private Node head;
    private Node last;
    private int size;

    public DynamicLinkedString() {
        this.head = null;
        this.last = null;
        this.size = 0;
    }


    @Override
    public DynamicLinkedString add(AbstractDynamicString s) {
        return this;
    }

    @Override
    public DynamicLinkedString add(String s) {
        return this;
    }

    @Override
    public DynamicLinkedString add(char element) {
        Node newNode = new Node(element);
        if (last == null) {
            head = newNode;
            last = head;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        size++;
        return this;
    }

    @Override
    public DynamicLinkedString addFirst(char element) {
        Node newNode = new Node(element);
        newNode.next = head;
        if (head != null) {
            head.prev = newNode;
        } else {
            last = newNode;
        }
        head = newNode;
        size++;
        return this;
    }

    @Override
    public DynamicLinkedString insert(int pos, char c) {
        if (pos <= 0 || head == null) {
            addFirst(c);
            return this;
        }
        if (pos >= size) {
            add(c);
            return this;
        }
        Node newNode = new Node(c);
        Node current = head;
        size++;
        int currentPos = 0;
        while (currentPos < pos && current.next != null) {
            current = current.next;
            currentPos++;
        }
        newNode.prev = current.prev;
        current.prev = newNode;
        newNode.next = current;
        newNode.prev.next = newNode;
        return this;
    }

    @Override
    public DynamicLinkedString insert(int pos, String s) {
        return this;
    }

    @Override
    public DynamicLinkedString insert(int pos, AbstractDynamicString s) {
        return this;
    }

    @Override
    public char getLast() {
        return last.val;
    }

    @Override
    public char getFirst() {
        return head.val;
    }

    @Override
    public DynamicLinkedString popFirst() {
        if (head == null) throw new ArrayIndexOutOfBoundsException("String is empty");
        size--;
        head = head.next;
        if (head != null) head.prev = null;
        else last = null;
        return this;
    }

    @Override
    public DynamicLinkedString popLast() {
        if (head == null) throw new ArrayIndexOutOfBoundsException("String is empty");
        size--;
        last = last.prev;
        if (last != null) last.next = null;
        else head = null;
        return this;
    }

    @Override
    public int count(char element) {
        return 0;
    }

    @Override
    //FIXME
    public DynamicLinkedString subString(int start, int end) {
        Node first = head;
        for (int pos = 0; pos < start; pos++, first = first.next) ;
        size = end - start;
        char[] lst = new char[size];
        for (int pos = start; pos < end; pos++, first = first.next) lst[pos] = first.val;
        return new DynamicLinkedString(lst);
    }

    @Override
    public AbstractDynamicString subString(int start) {
        Node first = head;
        for (int pos = 0; pos < start; pos++, first = first.next) ;
        char[] lst = new char[size - start];
        for (int pos = start; pos < size; pos++, first = first.next) lst[pos] = first.val;
        size -= start;
        return new DynamicLinkedString(lst);
    }

    @Override
    public String toString() {
        if (head == null) return "";
        Node first = head;
        char[] lst = new char[size];
        for (int pos = 0; pos < size; pos++, first = first.next)
            lst[pos] = first.val;
        return new String(lst);
    }

    @Override
    public int getSize() {
        return size;
    }
}
