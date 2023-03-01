package HW0;

public class BreakContinue {
	public static void windowPosSum(int[] a, int n) {
		/** your code here */
		for (int i = 0; i < a.length; i++) {
			int result = 0;
			if (a[i] > 0) {
				for (int j = i; j <= (n + i); j++) {
					if (j < a.length) {
						result = result + a[j];
					} else {
						break;
					}
				}
			} else {
				continue;
			}
			a[i] = result;
		}
	}
	public static void main(String[] args) {
		int[] a = { 1, 2, -3, 4, 5, 4 };
		int n = 3;
		windowPosSum(a, n);

		// Should print 4, 8, -3, 13, 9, 4
		System.out.println(java.util.Arrays.toString(a));

	}

}
