public class ArrayDeque<T> {
	private T[] items;
	private int size;
	private int nextFirst;
	private int nextLast;

	/* constructor for ArrayDeque */
	public ArrayDeque() {
		items = (T[]) new Object[8];
		size = 0;
		nextFirst = 0;
		nextLast = 1;
	}

	private boolean isFull() {
		return size == items.length;
	}

	private boolean isSparse() {
		return items.length >= 16 && size < (items.length / 4);
	}

	private int plusOne(int index) {
		return (index + 1) % items.length;
	}

	private int minusOne(int index) {
		return (index - 1 + items.length) % items.length;
	}

	private void resize(int capacity) {
		T[] newDeque = (T[]) new Object[capacity];
		int oldIndex = plusOne(nextFirst);
		for (int i = 0; i < size; i++) {
			newDeque[i] = items[oldIndex];
			oldIndex = plusOne(oldIndex);
		}
		items = newDeque;
		nextFirst = capacity - 1;
		nextLast = size;
	}

	private void increase() {
		resize(size * 2);
	}

	private void decrease() {
		resize(size / 2);
	}


	public void addFirst(T item) {
		if (isFull()) {
			increase();
		}
		items[nextFirst] = item;
		nextFirst = minusOne(nextFirst);
		size += 1;
	}

	public void addLast(T item) {
		if (isSparse()) {
			decrease();
		}
		items[nextLast] = item;
		nextLast = plusOne(nextLast);
		size += 1;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public void printDeque() {
		for (int i = plusOne(nextFirst); i != nextLast; i = plusOne(nextFirst)) {
			System.out.print(items[i] + " ");
		}
	}

	public T removeFirst() {
		if (isSparse()) {
			decrease();
		}
		nextFirst = plusOne(nextFirst);
		T removedItem = items[nextFirst];
		items[nextFirst] = null;

		if (!isEmpty()) {
			size -= 1;
		}
		return removedItem;
	}

	public T removeLast() {
		if (isSparse()) {
			decrease();
		}
		nextLast = minusOne(nextLast);
		T removedItem = items[nextLast];
		items[nextLast] = null;

		if (!isEmpty()) {
			size -= 1;
		}
		return removedItem;
	}

	public T get(int index) {
		if (index >= size) {
			return null;
		}
		int start = plusOne(nextFirst);
		return items[(start + index) % items.length];
	}
}
