package featureExtraction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class FeatureExtractionHelper {	
	public static List<Point> getSurroundingPixels(int[][] skeletonValues, int row, int col){
		List<Point> surroundingPixels = new ArrayList<Point>();
		
		surroundingPixels.add(VectorDirection.TOP_RIGHT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.RIGHT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.BOTTOM_RIGHT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.BOTTOM.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.BOTTOM_LEFT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.LEFT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.TOP_LEFT.getNextPixelPoint(row, col, skeletonValues));
		surroundingPixels.add(VectorDirection.TOP.getNextPixelPoint(row, col, skeletonValues));
		
		return surroundingPixels;
	}
	
	public static List<Point> getBlackNeighbors(int[][] imageValues, int row, int col){
		List<Point> surroundingPixels = getSurroundingPixels(imageValues, row, col);
		List<Point> blackNeighbors = new ArrayList<Point>();
		
		for (Point nextPoint : surroundingPixels){
			if (nextPoint.x >= 0 && nextPoint.y >= 0 && imageValues[nextPoint.y][nextPoint.x] > 0){
				blackNeighbors.add(nextPoint);
			}
		}
		
		return blackNeighbors;
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
