package featureExtraction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import debug.FeatureExtractionDebug;

public class VectorCalculator {
	private static final int EMPTY = 0;
	private static final int NON_VISITED = 1;
	private static final int HORIZONTAL = 3;
	private static final int VERTICAL = 2;
	private static final int DIAG_LEFT = 5;
	private static final int DIAG_RIGHT = 4;
	
	public static final int MAX_DIRECTION_VALUE = DIAG_LEFT;
	
	public static List<FeaturePoint> calculateFeaturePoints(int[][] skeleton, int zoneDimensionX, int zoneDimensionY){
		float numPixelsPerZoneX = (float)skeleton[0].length / zoneDimensionX;
		float numPixelsPerZoneY = (float)skeleton.length / zoneDimensionY;
		
		List<FeaturePoint> points = new ArrayList<FeaturePoint>();
		
		for (int row = 0; row < skeleton.length; row++){
			for (int col = 0; col < skeleton[0].length; col++){
				int x = (int)((float)col / numPixelsPerZoneX) + 1;
				int y = (int)((float)row / numPixelsPerZoneY) + 1;
				FeaturePoint nextPoint = scanForFeaturePoint(skeleton, x, y, row, col);
				
				if (nextPoint != null){
					points.add(nextPoint);
				}
			}
		}
		
		return points;
	}
	
	public static List<FeaturePoint> calculateFeaturePointLocations(int[][] skeleton){
		List<FeaturePoint> points = new ArrayList<FeaturePoint>();
		
		for (int row = 0; row < skeleton.length; row++){
			for (int col = 0; col < skeleton[0].length; col++){
				FeaturePoint nextPoint = scanForFeaturePoint(skeleton, col, row, row, col);
				
				if (nextPoint != null){
					points.add(nextPoint);
				}
			}
		}
		
		return points;
	}
	
	private static FeaturePoint scanForFeaturePoint(int[][] skeleton, int x, int y, int row, int col){
		int currentPixel = skeleton[row][col];
		FeaturePoint point = null;
		
		if (currentPixel != 0){
			List<Integer> surroundingPoints = FeatureExtractionHelper.getSurroundingValuesFromPoints(skeleton, row, col);
			
			List<Integer> cardinalPoints = new ArrayList<Integer>();
			cardinalPoints.add(surroundingPoints.get(VectorDirection.RIGHT.ordinal()));
			cardinalPoints.add(surroundingPoints.get(VectorDirection.BOTTOM.ordinal()));
			cardinalPoints.add(surroundingPoints.get(VectorDirection.LEFT.ordinal()));
			cardinalPoints.add(surroundingPoints.get(VectorDirection.TOP.ordinal()));
			
			int numDirections = 0;
			
			for (Integer nextValue : cardinalPoints){
				if (nextValue != 0){
					numDirections++;
				}
			}
			
			if (numDirections == 1){
				point = new FeaturePoint(x, y, FeatureType.END_POINT);
			}
			if (numDirections == 3){
				point = new FeaturePoint(x, y, FeatureType.T_JUNCTION);
			}
		}
		
		return point;
	}
	
	public static void removeDiagonalLineThickness(int[][] correctedVectorValues){
		for (int row = 0; row < correctedVectorValues.length; row++){
			for (int col = 0; col < correctedVectorValues[0].length; col++){
				if (correctedVectorValues[row][col] == DIAG_LEFT){
					correctAroundDiagonal(correctedVectorValues, row, col, DIAG_LEFT, 
							VectorDirection.TOP_LEFT.ordinal(), VectorDirection.BOTTOM_RIGHT.ordinal());
				}
				else if (correctedVectorValues[row][col] == DIAG_RIGHT){
					correctAroundDiagonal(correctedVectorValues, row, col, DIAG_RIGHT,
							VectorDirection.TOP_RIGHT.ordinal(), VectorDirection.BOTTOM_LEFT.ordinal());
				}
			}
		}
	}
	
	private static void correctAroundDiagonal(int[][] correctedVectorValues, int row, int col, 
			int diagValue, int aboveValue, int belowValue){
		List<Integer> surroundingValues = FeatureExtractionHelper.getSurroundingValuesFromPoints(correctedVectorValues, row, col);
		
		if (surroundingValues.get(aboveValue) == diagValue && 
				surroundingValues.get(belowValue) == diagValue){
			if (row > 0){
				correctedVectorValues[row-1][col] = 0;
			}
			if (col > 0){
				correctedVectorValues[row][col-1] = 0;
			}
			if (row < correctedVectorValues.length - 1){
				correctedVectorValues[row+1][col] = 0;
			}
			if (col < correctedVectorValues[0].length - 1){
				correctedVectorValues[row][col+1] = 0;
			}
		}
	}
	
	
	
	public static void correctVectorValues(int[][] vectorValues){		
		for (int row = 0; row < vectorValues.length; row++){
			for (int col = 0; col < vectorValues[0].length; col++){
				List<Integer> surroundingPixels = 
						FeatureExtractionHelper.getSurroundingValuesFromPoints(vectorValues, row, col);
				Map<Integer, Integer> vectorCounts = new HashMap<Integer, Integer>();
				
				if (vectorValues[row][col] != 0){
					for (Integer nextVectorValue : surroundingPixels){
						if (nextVectorValue != 0){
							int currentValueCount = (vectorCounts.get(nextVectorValue) != null) ? vectorCounts.get(nextVectorValue) : 0;
							vectorCounts.put(nextVectorValue, currentValueCount + 1);
						}
					}
					
					if (vectorCounts.keySet().size() > 0){
						correctPixelVectorValue(vectorValues, row, col, vectorCounts);
					}
				}
			}
		}
	}

	private static void correctPixelVectorValue(int[][] vectorValues, int row,
			int col, Map<Integer, Integer> vectorCounts) {
		final int UNDEFINED = -1;
		int maxValue = Collections.max(vectorCounts.values());
		boolean hasGreatestNeighbor = true;
		int greatestNeighbor = UNDEFINED;
		
		for (Integer nextDirection : vectorCounts.keySet()){
			if (vectorCounts.get(nextDirection) == maxValue){
				if (greatestNeighbor == UNDEFINED){
					greatestNeighbor = nextDirection;
				}
				else hasGreatestNeighbor = false;
			}
		}
		
		if (hasGreatestNeighbor){
			vectorValues[row][col] = greatestNeighbor;
		}
	}
	
	public static float[] calculateZonedVectorsForSkeleton(int[][] skeleton, int zoneDimensionX, int zoneDimensionY, boolean calculateVectors){
		int[][] vectorSkeleton = (calculateVectors ? calculateVectorsForSkeleton(skeleton) : skeleton);
		
		if (calculateVectors)
			correctVectorValues(vectorSkeleton);
		
		removeDiagonalLineThickness(vectorSkeleton);
		
		float[] zonedValues = new float[zoneDimensionX * zoneDimensionY];
		int zoneValueIndex = 0;
		
		float numPixelsPerZoneX = (float)skeleton[0].length / zoneDimensionX;
		float numPixelsPerZoneY = (float)skeleton.length / zoneDimensionY;
		
		for (int zoneY = 0; zoneY < zoneDimensionY; zoneY++){
			for (int zoneX = 0; zoneX < zoneDimensionX; zoneX++){
				int startingRow = (int) (zoneY * numPixelsPerZoneY);
				int endingRow = (int) (startingRow + numPixelsPerZoneY);
				int startingCol = (int) (zoneX * numPixelsPerZoneX);
				int endingCol = (int) (startingCol + numPixelsPerZoneX);
				
				zonedValues[zoneValueIndex] = 
						calculateVectorValueForZone(startingRow, endingRow, startingCol, endingCol, skeleton);
						
				zoneValueIndex++;
			}
		}
		
		return zonedValues;
	}
	
	private static float calculateSumVectorValueForZone(int startingRow, int endingRow, int startingCol,
			int endingCol, int[][] vectorValues){
		Map<Integer, Integer> vectorCounts = new HashMap<Integer, Integer>();
		
		for (int row = startingRow; row <= endingRow; row++){
			for (int col = startingCol; col <= endingCol; col++){
				if (row < vectorValues.length && col < vectorValues[row].length && vectorValues[row][col] != 0){
					int vectorValue = (vectorCounts.get(vectorValues[row][col]) != null) ? vectorCounts.get(vectorValues[row][col]) : 0;
					vectorCounts.put(vectorValues[row][col], (vectorValue + 1));
				}
			}
		}
		
		int chosenVector = 0;
		int highestVectorCount = 0;
		
		for (Integer nextVectorDirection : vectorCounts.keySet()){
			int nextVectorCount = vectorCounts.get(nextVectorDirection);
			
			if (nextVectorCount > highestVectorCount){
				highestVectorCount = nextVectorCount;
				chosenVector = nextVectorDirection;
			}
		}
		
		return chosenVector / DIAG_LEFT;
	}
	
	private static float calculateVectorValueForZone(int startingRow,
			int endingRow, int startingCol, int endingCol, int[][] vectorValues) {
		int vectorSum = 0;
		int numVectorsFound = 0;
		
		for (int row = startingRow; row <= endingRow; row++){
			for (int col = startingCol; col <= endingCol; col++){
				if (row < vectorValues.length && col < vectorValues[row].length && vectorValues[row][col] != 0){
					vectorSum += vectorValues[row][col];
					numVectorsFound++;
				}
			}
		}
		
		return ((numVectorsFound > 0) ? ((float)vectorSum / numVectorsFound) : 0);
	}

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