import java.util.Arrays;



public class LydiaTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(compareColumns1(new int[][] {{1,2,3}, {1,2,3}}, 0, 1));
		System.out.println(compareColumns1(new int[][] {{1,2,3}, {1,2,3}}, 1, 0));

		System.out.println(compareColumns2(new int[][] {{1,2,3}, {1,2,3}}, 0, 1));
		System.out.println(compareColumns2(new int[][] {{1,2,3}, {1,2,3}}, 1, 0));

		System.out.println(countEven(new int[][] {{1,2,3}, {1,1,1}, {2, 3, 4}}));
		System.out.println(Arrays.toString(countEven(new int[][] {{1,2,3}, {1,1,1}, {2, 3, 4}})));
	}

	
	private static boolean compareColumns1(int[][] array, int column1, int column2) {
		int sum1 = 0;
		for (int i = 0;  i < array.length;  i++) {
			sum1 += array[i][column1];
		}
		
		int sum2 = 0;
		for (int i = 0;  i < array.length;  i++) {
			sum2 += array[i][column2];
		}
		
		return sum1 > sum2;
	}
	
	
	//////////////////////////////////////////////////
	
	private static boolean compareColumns2(int[][] array, int column1, int column2) {
		return sumColumn(array, column1) > sumColumn(array, column2);
	}

	private static int sumColumn(int[][] array, int column) {
		int sum = 0;
		for (int i = 0;  i < array.length;  i++) {
			sum += array[i][column];
		}
		return sum;
	}
	
	//////////////////////////////////////////////////
	
	
	private static int[] countEven(int[][] a) {
		int[] evens = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			evens[i] = countEven(a[i]);
		}
		return evens;
	}
	
	private static int countEven(int[] a) {
		int count = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] % 2 == 0) {
				count++;
			}
		}
		return count;
	}
}
