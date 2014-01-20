package app;

public class VectorCalculator {
	private static final int EMPTY = 0;
	private static final int NON_VISITED = 1;
	private static final int HORIZONTAL = 3;
	private static final int VERTICAL = 2;
	private static final int DIAG_LEFT = 5;
	private static final int DIAG_RIGHT = 4;
	
	public static int[][] calculateVectorsForSkeleton(int[][] skeleton){
		int[][] skeletonCopy = copySkeleton(skeleton);
		
		for (int row = skeletonCopy.length - 1; row >= 0; row--){
			for (int col = 0; col < skeletonCopy[row].length; col++){
				if (skeletonCopy[row][col] == NON_VISITED){
					followSkeleton(row, col, skeletonCopy);
					
					if (skeletonCopy[row][col] == NON_VISITED){
						skeletonCopy[row][col] = VERTICAL;
					}
				}
			}
		}
		
		return skeletonCopy;
	}
	
	private static int[][] copySkeleton(int[][] skeleton){
		int[][] copy = new int[skeleton.length][skeleton[0].length];
		
		for (int row = 0; row < skeleton.length; row++){
			for (int col = 0; col < skeleton[0].length; col++){
				copy[row][col] = skeleton[row][col];
			}
		}
		
		return copy;
	}
	
	private static void followSkeleton(int row, int col, int[][] skeleton){ // recursive
		int left = (col - 1 >= 0) ? skeleton[row][col-1] : 0;
		int topLeft = (row - 1 >= 0 && col - 1 >= 0) ? skeleton[row-1][col-1] : 0;
		int top = (row - 1 >= 0) ? skeleton[row-1][col] : 0;
		int topRight = (row - 1 >= 0 && col + 1 < skeleton[row].length) ? skeleton[row-1][col+1] : 0;
		int right = (col + 1 < skeleton[row].length) ? skeleton[row][col+1] : 0;
		
		followIfNonVisited(row, col, topLeft, DIAG_LEFT, row-1, col-1, skeleton);
		followIfNonVisited(row, col, topRight, DIAG_RIGHT, row-1, col+1, skeleton);
		followIfNonVisited(row, col, left, HORIZONTAL, row, col-1, skeleton);
		followIfNonVisited(row, col, top, VERTICAL, row-1, col, skeleton);
		followIfNonVisited(row, col, right, HORIZONTAL, row, col+1, skeleton);
	}
	
	private static void followIfNonVisited(int currentRow, int currentCol, int searchValue, int markValue,
			int rowToAdvance, int colToAdvance, int[][] skeleton){
		if (searchValue == NON_VISITED){
			if (skeleton[currentRow][currentCol] == NON_VISITED){
				skeleton[currentRow][currentCol] = markValue;
			}
			followSkeleton(rowToAdvance, colToAdvance, skeleton);
		}
		else if (searchValue != EMPTY && skeleton[currentRow][currentCol] == NON_VISITED){
			skeleton[currentRow][currentCol] = markValue;
		}
	}
}
