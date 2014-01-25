package featureExtraction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class ChainCodeCreator {
	private static final int VISITED = -1;
	//private static final int NON_VISITED = 1;
	
	public static int[] generateChainCode(int[][] skeletonValues){		
		List<Integer> chainCodeValues = new ArrayList<Integer>();
		
		for (int row = 0; row < skeletonValues.length; row++){
			for (int col = 0; col < skeletonValues[0].length; col++){
				if (skeletonValues[row][col] > 0){
					skeletonValues[row][col] = VISITED;
					findNextPixel(skeletonValues, chainCodeValues, row, col);
				}
			}
		}
		
		int[] chainCode = new int[chainCodeValues.size()];
		
		System.out.print("CHAIN CODE: ");
		for (int i = 0; i < chainCodeValues.size(); i++){
			chainCode[i] = chainCodeValues.get(i);
			System.out.print(chainCode[i] + " ");
		}
		System.out.println("\r\n");
		
		return chainCode;
	}
	
	private static void findNextPixel(int[][] skeletonValues, List<Integer> chainCodeValues, int row, int col){
		VectorDirection[] directions = VectorDirection.values();
		boolean hasTraversed = false;
		
		for (int i = 0; i < directions.length && !hasTraversed; i++){
			Point nextSurroundingPoint = directions[i].getNextPixelPoint(row, col, skeletonValues);
			int nextPointValue = (nextSurroundingPoint.x != -1 && nextSurroundingPoint.y != -1) ? skeletonValues[nextSurroundingPoint.y][nextSurroundingPoint.x] : 0;
			
			if (nextPointValue > 0){
				chainCodeValues.add(directions[i].ordinal());
				skeletonValues[row][col] = VISITED;
				findNextPixel(skeletonValues, chainCodeValues, nextSurroundingPoint.y, nextSurroundingPoint.x);
				hasTraversed = true;
			}
		}
	}
	
}
