public class LinkedListDeque<T> {
    private Node sentinel;
    private int size;

    public class Node {
        private Node prev;
        private T item;
        private Node next;

        /* constructor for Node */
        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

        /* constructor for sentinel Node */
        public Node(Node prev, Node next) {
            this.prev = prev;
            this.next = next;
        }
    }

    /* constructor for Deque */
    public LinkedListDeque() {
        this.sentinel = new Node(null, null);
        this.sentinel.prev = sentinel;
        this.sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node first = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = first;
        sentinel.next = first;
        size++;
    }

    public void addLast(T item) {
        Node last = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = last;
        sentinel.prev = last;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node print = sentinel.next;
        while (print != sentinel) {
            System.out.print(print.item + " ");
            print = print.next;
        }
    }

    public T removeFirst() {
        if (size == 0) return null;

        T t = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return t;
    }

    public T removeLast() {
        if (size == 0) return null;

        T t = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return t;
    }

    public T get(int index) {
        if (index >= size) return null;
        Node ptr = sentinel;
        for (int i = 0; i <= index; i++) {
            ptr = ptr.next;
        }
        return ptr.item;
    }

    private T getRecursiveHelp(Node current, int index) {
        if (index == 0) return current.item;
        return getRecursiveHelp(current.next, index - 1);
    }


    public T getRecursive(int index) {
        if (index >= size) return null;
        return getRecursiveHelp(sentinel.next, index);
    }

}
