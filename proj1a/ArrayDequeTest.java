public class ArrayDequeTest {
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.print("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.print("checkSize() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	public static void addIsEmptySizeTest() {
		System.out.print("Running add/isEmpty/Size test.");
		ArrayDeque<String> ad1 = new ArrayDeque<String>();

		boolean passed = checkEmpty(true, ad1.isEmpty());

		ad1.addFirst("front");

		passed = checkSize(1, ad1.size()) && passed;
		passed = checkEmpty(false, ad1.isEmpty()) && passed;

		ad1.addLast("middle");
		passed = checkSize(2, ad1.size()) && passed;

		ad1.addLast("back");
		passed = checkSize(3, ad1.size()) && passed;

		System.out.println("Printing out deque: ");
		ad1.printDeque();

		printTestStatus(passed);
	}

	public static void addRemoveTest() {
		System.out.print("Running add/remove test.");

		ArrayDeque<Integer> ad2 = new ArrayDeque<Integer>();

		boolean passed = checkEmpty(true, ad2.isEmpty());

		ad2.addFirst(10);
		passed = checkEmpty(false, ad2.isEmpty()) && passed;

		ad2.removeFirst();
		passed = checkEmpty(true, ad2.isEmpty()) && passed;

		printTestStatus(passed);
	}

	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();
	}
}
