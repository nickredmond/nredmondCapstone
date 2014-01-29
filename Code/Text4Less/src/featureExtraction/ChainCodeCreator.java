package featureExtraction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import math.GeneralMath;
import debug.FeatureExtractionDebug;

public class ChainCodeCreator {
	private static final int VISITED = -1;
	private int numberPixelsFollowed = 0;
	//private static final int NON_VISITED = 1;
	
	private int NORMALIZED_CHAIN_CODE_LENGTH = 15;
	private int MIN_CHAIN_VALUE_LENGTH = 2;
	
	private List<Point> convertChainCodeToMatrix(int[] chainCode){
		List<Point> chainCodeOccurrences = new ArrayList<Point>();
		
		final int BEGINNING_VALUE = -1;
		int currentChainCodeValue = BEGINNING_VALUE;
		int currentValueLength = 0;
		
		for (int i = 0; i < chainCode.length; i++){
			if (chainCode[i] != currentChainCodeValue){
				if (currentChainCodeValue != BEGINNING_VALUE){
					chainCodeOccurrences.add(new Point(currentChainCodeValue, currentValueLength));
				}
				
				currentChainCodeValue = chainCode[i];
				currentValueLength = 1;
			}
			else currentValueLength++;
		}
		
		chainCodeOccurrences.add(new Point(currentChainCodeValue, currentValueLength));
		
		List<Point> acceptedChainCodeValues = new ArrayList<Point>();
		
		
		for (Point nextPoint : chainCodeOccurrences){
			if (nextPoint.y >= MIN_CHAIN_VALUE_LENGTH){
				acceptedChainCodeValues.add(nextPoint);
			}
		}
		
//		for (Point nextPt : chainCodeOccurrences){
//			System.out.print(nextPt.x + " ");
//		}
//		System.out.println();
//		for (Point nextPt : chainCodeOccurrences){
//			System.out.print(nextPt.y + " ");
//		}
//		System.out.println("\r\n");
		
//		for (Point nextPt : acceptedChainCodeValues){
//			System.out.print(nextPt.x + " ");
//		}
//		System.out.println();
//		for (Point nextPt : acceptedChainCodeValues){
//			System.out.print(nextPt.y + " ");
//		}
//		System.out.println("\r\n");
		
		return acceptedChainCodeValues;
	}
	
	private int[] normalizeChainCode(List<Point> occurrences){
		//List<Point> normalizedOccurrences = new ArrayList<Point>();
		int[] normalizedChainCode = new int[NORMALIZED_CHAIN_CODE_LENGTH];
		
		int occurrenceSum = 0;
		
		for (Point nextOccurrence : occurrences){
			occurrenceSum += nextOccurrence.y;
		}
		
		if (occurrenceSum != 0){
			float[][] normalizedValues = new float[occurrences.size()][2];
			
			for (int i = 0; i < normalizedValues.length; i++){
				Point nextOccurrence = occurrences.get(i);
				normalizedValues[i][0] = nextOccurrence.x;
				normalizedValues[i][1] = ((float)nextOccurrence.y / occurrenceSum) * NORMALIZED_CHAIN_CODE_LENGTH;
			}
			
			int normalizedIndex = 0;
			
			for (int i = 0; i < normalizedValues.length && normalizedIndex < normalizedChainCode.length; i++){
				int nextNormalizedOccurrence = Math.round(normalizedValues[i][1]);
				
				for (int j = 0; j < nextNormalizedOccurrence && normalizedIndex < normalizedChainCode.length; j++){
					normalizedChainCode[normalizedIndex] = (int)normalizedValues[i][0];
					normalizedIndex++;
				}
			}
		}
		else{
			final int EMPTY = -1;
			for (int i = 0; i < normalizedChainCode.length; i++){
				normalizedChainCode[i] = EMPTY;
			}
		}
		
//		System.out.println();
//		System.out.print("yes'm: ");
//		for (int i = 0; i < normalizedChainCode.length; i++){
//			System.out.print(normalizedChainCode[i] + " ");
//		}
//		System.out.println("\r\n");
		
		return normalizedChainCode;
	}
	
	public int[] generateChainCode(int[][] skeletonValues){		
		List<Integer> chainCodeValues = new ArrayList<Integer>();
		List<FeaturePoint> featurePoints = VectorCalculator.calculateFeaturePointLocations(skeletonValues);
		
		boolean foundStartingPt = false;
		for (int i = 0; i < featurePoints.size() && !foundStartingPt; i++){
			FeaturePoint nextPoint = featurePoints.get(i);
			if (nextPoint.getType() == FeatureType.END_POINT){
				foundStartingPt = true;
				skeletonValues[nextPoint.y()][nextPoint.x()] = VISITED;
				
				numberPixelsFollowed = 0;
				findNextPixel(skeletonValues, chainCodeValues, nextPoint.y(), nextPoint.x());
			}
		}
		
		for (int row = 0; row < skeletonValues.length; row++){
			for (int col = 0; col < skeletonValues[0].length; col++){
				if (skeletonValues[row][col] > 0){
					skeletonValues[row][col] = VISITED;
					
					numberPixelsFollowed = 0;
					findNextPixel(skeletonValues, chainCodeValues, row, col);
				}
			}
		}
		
		int[] chainCode = new int[chainCodeValues.size()];
		
		for (int i = 0; i < chainCodeValues.size(); i++){
			chainCode[i] = chainCodeValues.get(i);
		}
		
		//FeatureExtractionDebug.printChainCode(chainCode);
		List<Point> occurencePoints = convertChainCodeToMatrix(chainCode);
		int[] normalizedChainCode = normalizeChainCode(occurencePoints);
		
		return normalizedChainCode;
	}
	
	private void findNextPixel(int[][] skeletonValues, List<Integer> chainCodeValues, int row, int col){
		VectorDirection[] directions = VectorDirection.values();
		boolean hasTraversed = false;
		int currentPosition = numberPixelsFollowed;
		skeletonValues[row][col] = VISITED;
		
		for (int i = 0; i < directions.length && !hasTraversed; i++){
			Point nextSurroundingPoint = directions[i].getNextPixelPoint(row, col, skeletonValues);
			int nextPointValue = (nextSurroundingPoint.x != -1 && nextSurroundingPoint.y != -1) ? skeletonValues[nextSurroundingPoint.y][nextSurroundingPoint.x] : 0;
			
			if (nextPointValue > 0){				
				if (numberPixelsFollowed - currentPosition <= 1){
					if (numberPixelsFollowed - currentPosition == 1){
						chainCodeValues.remove(chainCodeValues.size() - 1);
						numberPixelsFollowed--;
					}
					
					chainCodeValues.add(directions[i].ordinal());
					
					numberPixelsFollowed++;
					findNextPixel(skeletonValues, chainCodeValues, nextSurroundingPoint.y, nextSurroundingPoint.x);
				}
				//else hasTraversed = true;
			}
		}
	}	
}