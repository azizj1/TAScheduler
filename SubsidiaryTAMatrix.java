
public class SubsidiaryTAMatrix {
	
	private int[][] original, rudimentary;
	//remember that the first column is just to tell us which TA the row belongs to.
	
	public SubsidiaryTAMatrix(int[][] matrix) {
		if (matrix.length + 1 != matrix[0].length)
			throw new IllegalArgumentException("Invalid matrix.");
		original = matrix;
		rudimentary = new int[matrix.length][matrix[0].length];
		clearSolution();
	}
	private void clearSolution() {
		rudimentary = new int[original.length][original[0].length];
		for (int i = 0; i < original.length; i++)
			rudimentary[i][0] = original[i][0];
	}
	private int onesInRow(int row) {
		if (row >= original.length)
			throw new ArrayIndexOutOfBoundsException("row " + row + " does not exist. " +
					"Vaild row: 0 <= row <= " + (original.length-1));
		int count = 0;
		for (int i = 1; i < original[row].length; i++)
			if (original[row][i] == 1)
				count++;
		return count;
	}
	private void swapRows(int r1, int r2) {
		if (r1 >= original.length || r2 >= original.length)
			throw new ArrayIndexOutOfBoundsException("row " + r1 + " or " + r2 + 
					" does not exist. Vaild row: 0 <= row <= " + (original.length-1));
		int[] temp = original[r1];
		original[r1] = original[r2];
		original[r2] = temp;
		
		int[] tempR = rudimentary[r1];
		rudimentary[r1] = rudimentary[r2];
		rudimentary[r2] = tempR;
	}
	//bubble sort
	private void sort() {
		int size = original.length;
		boolean swapped;
		do {
			swapped = false;
			for (int i = 1; i < size; i++)
				if (onesInRow(i-1) > onesInRow(i)) {
					swapRows(i-1, i);
					swapped = true;
				}
			size--;
		} while (swapped);
	}
	private void sortByTA() {
		int size = original.length;
		boolean swapped;
		do {
			swapped = false;
			for (int i = 1; i < size; i++)
				if (original[i-1][0] > original[i][0]) {
					swapRows(i-1, i);
					swapped = true;
				}
			size--;
		} while (swapped);
	}
	private int nextAvailableLab(int row, int col) {
		if (row >= original.length || col >= original[0].length || col < 1)
			return -1;
		for (int i = col; i < original[row].length; i++)
			if (original[row][i] == 1 && !isLabEmployed(i))
				return i;
		return -1;
	}
	private boolean isLabEmployed(int col) {
		if (col >= original[0].length || col < 1)
			return false;
		for (int i = 0; i < rudimentary.length; i++)
			if (rudimentary[i][col] == 1)
				return true;
		return false;
			
	}
	private boolean place(int row, int col) {
		int index = nextAvailableLab(row, col);
		if (index == -1)
			return false;
		rudimentary[row][index] = 1;
		if (row == rudimentary.length-1)
			return true;
		if (place(row+1, 1))
			return true;
		rudimentary[row][index] = 0;
		if (place(row, index+1))
			return true;
		return false;
	}
	private boolean isSolvable() {
		for (int j = 1; j < original[0].length; j++) {
			int i = 0;
			for (; i < original.length; i++)
				if (original[i][j] == 1)
					break;
			if (i == original.length) 
				return false;
		}
		return true;
	}
	/**
	 * 
	 * @return if solution found, returns -1. Else it returns the TA's # that's causing
	 * problem. 
	 */
	public int solve() {
		sort();
		if (!isSolvable())
			return original[0][0];
		boolean foundSolution = place(0,1);
		if (!foundSolution)
			return original[0][0];
		sortByTA();
		return -1;
	}
	public int[][] getSolution() {
		return rudimentary;
	}
	/**
	 * 
	 * @param row that needs replacement
	 * @param taIndex replaces all rows with int[] row if its ta index corresponds to taIndex
	 * Clears the solution
	 */
	public void setTARow(int[] row, int taIndex) {
		if (row.length != original[0].length)
			throw new ArrayIndexOutOfBoundsException("Array is not valid dimension");
		int count = 0;
		for (int i = 0; i < original.length; i++)
			if (original[i][0] == taIndex) {
				original[i] = row;
				count++;
			}
		if (count == 0)
			throw new ArrayIndexOutOfBoundsException(taIndex + " does not exist.");
		clearSolution();
	}
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < original.length; i++) {
			result.append('|');
			for (int j = 0; j < original[i].length; j++)
				result.append(original[i][j] + " ");
			result.deleteCharAt(result.length()-1).append("|\n");
		}
		result.append("\n");
		for (int i = 0; i < rudimentary.length; i++) {
			result.append('|');
			for (int j = 0; j < rudimentary[i].length; j++)
				result.append(rudimentary[i][j] + " ");
			result.deleteCharAt(result.length()-1).append("|\n");
		}
		return result.deleteCharAt(result.length()-1).toString();	
	}

}
