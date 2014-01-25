package featureExtraction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class FeatureExtractionHelper {
	public static final int TOP = 0;
	public static final int TOP_RIGHT = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM_RIGHT = 3;
	public static final int BOTTOM = 4;
	public static final int BOTTOM_LEFT = 5;
	public static final int LEFT = 6;
	public static final int TOP_LEFT = 7;
	
	public static List<Point> getSurroundingPixels(int[][] skeletonValues, int row, int col){
		List<Point> surroundingPixels = new ArrayList<Point>();
		
		surroundingPixels.add(VectorDirection.TOP.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.TOP_RIGHT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.RIGHT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.BOTTOM_RIGHT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.BOTTOM.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.BOTTOM_LEFT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.LEFT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.TOP_LEFT.getNextPixelPoint(row, col, skeletonValues));
		
		return surroundingPixels;
	}
	
	public static List<Integer> getSurroundingValuesFromPoints(int[][] correctedVectorValues, int row, int col){
		List<Point> surroundingPixels = getSurroundingPixels(correctedVectorValues, row, col);
		List<Integer> surroundingPoints = new ArrayList<Integer>();
		
		for (Point nextPt : surroundingPixels){
			int nextValue = (nextPt.x != -1 && nextPt.y != -1) ? correctedVectorValues[nextPt.y][nextPt.x] : 0;
			surroundingPoints.add(nextValue);
		}
		
		return surroundingPoints;
	}
}
