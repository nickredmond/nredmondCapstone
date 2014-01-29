package featureExtraction;

import imageProcessing.ImageScaler;

import java.awt.Point;

import debug.FeatureExtractionDebug;

public class ZernikeImageNormalizer {
	private final static int BETA = 130;
	
	public static int[][] padToRequiredDimension(int[][] imageValues, int dimension){
		if (imageValues.length != imageValues[0].length){
			throw new IllegalArgumentException("Image must be square.");
		}
		
		int[][] paddedImgValues = imageValues;
		
		while (paddedImgValues.length <= dimension){
			paddedImgValues = padRight(paddedImgValues, 1);
			paddedImgValues = padDown(paddedImgValues, 1);
			
			if (paddedImgValues.length != dimension){
				paddedImgValues = padLeft(paddedImgValues, 1);
				paddedImgValues = padUp(paddedImgValues, 1);
			}
		}
		
		return paddedImgValues;
	}
	
	public static int[][] normalizeImageOnCentroidScaleInvariant(int[][] imageValues){
		return normalizeImageOnCentroidScaleInvariant(imageValues, BETA);
	}
	
	public static int[][] normalizeImageOnCentroidScaleInvariant(int[][] imageValues, int beta){
		int[][] squareValues = squareImage(imageValues);
		
		int m00 = MomentCalculator.calculateRegularMoment(squareValues, 0, 0);
		float scaleFactor = (m00 > 0) ? (float) Math.sqrt((double)BETA / m00) : 1.0f;
		
		int newWidth = (int) (scaleFactor * squareValues[0].length);
		int newHeight = (int) (scaleFactor * squareValues.length);
		
		int[][] scaledImg = (scaleFactor != 1.0f) ? ImageScaler.scaleWithBilinearInterpolation(squareValues, newWidth, newHeight) :
			squareValues;
		
		int[][] centeredImgValues = centerAroundCentroid(scaledImg);
		if (centeredImgValues.length != centeredImgValues[0].length){
			centeredImgValues = squareImage(centeredImgValues);
		}
		
		int newMoment = MomentCalculator.calculateRegularMoment(centeredImgValues, 0, 0);
		System.out.println("old: " + m00 + " new: " + newMoment);
		
		return centeredImgValues;
	}
	
	public static int[][] squareImage(int[][] imageValues){
		int ratio = getHeightToWidthRatio(imageValues);
		int[][] squaredImage = copyImage(imageValues);
		
		while (ratio != 0){
			if (ratio > 0){
				squaredImage = padRight(squaredImage, 1);
				ratio = getHeightToWidthRatio(squaredImage);
				if (ratio != 0){
					squaredImage = padLeft(squaredImage, 1);
					ratio = getHeightToWidthRatio(squaredImage);
				}
			}
			else{
				squaredImage = padUp(squaredImage, 1);
				ratio = getHeightToWidthRatio(squaredImage);
				if (ratio != 0){
					squaredImage = padDown(squaredImage, 1);
					ratio = getHeightToWidthRatio(squaredImage);
				}
			}
		}
		
		return squaredImage;
	}
	
	private static int getHeightToWidthRatio(int[][] imageValues){
		int ratio = 0;
		
		if (imageValues.length > imageValues[0].length){
			ratio = 1;
		}
		else if (imageValues.length < imageValues[0].length){
			ratio = -1;
		}
		
		return ratio;
	}
	
	private static int[][] copyImage(int[][] imageValues){
		int[][] copy = new int[imageValues.length][imageValues[0].length];
		
		for (int row = 0; row < imageValues.length; row++){
			for (int col = 0; col < imageValues[0].length; col++){
				copy[row][col] = imageValues[row][col];
			}
		}
		
		return copy;
	}
	
	private static int[][] centerAroundCentroid(int[][] imageValues){
		int[][] modifiedValues = new int[imageValues.length][imageValues[0].length];
		Point centroid = MomentCalculator.calculateCentroid(imageValues);
		
		int leftOfCentroid = centroid.x;
		int rightOfCentroid = imageValues[0].length - leftOfCentroid;
		int aboveCentroid = centroid.y;
		int belowCentroid = imageValues.length - aboveCentroid;
		
		if (belowCentroid > rightOfCentroid){
			modifiedValues = padRight(imageValues, (belowCentroid - rightOfCentroid));
			rightOfCentroid = belowCentroid;
		}
		else if (belowCentroid < rightOfCentroid){
			modifiedValues = padDown(imageValues, (rightOfCentroid - belowCentroid));
			belowCentroid = rightOfCentroid;
		}
		else modifiedValues = copyImage(imageValues);
		
		if (leftOfCentroid < rightOfCentroid){
			modifiedValues = padLeft(modifiedValues, (rightOfCentroid - leftOfCentroid));
		}
		else if (leftOfCentroid > rightOfCentroid){
			modifiedValues = padRight(modifiedValues, (leftOfCentroid - rightOfCentroid));
		}
		
		if (aboveCentroid < belowCentroid){
			modifiedValues = padUp(modifiedValues, (belowCentroid - aboveCentroid));
		}
		else if (aboveCentroid > belowCentroid){
			modifiedValues = padDown(modifiedValues, (aboveCentroid - belowCentroid));
		}
		
		return modifiedValues;
	}

	private static int[][] padRight(int[][] imageValues, int length) {
		int[][] paddedValues = new int[imageValues.length][imageValues[0].length + length];
		
		for (int row = 0; row < imageValues.length; row++){
			for (int col = 0; col < paddedValues[0].length; col++){
				paddedValues[row][col] = (col < imageValues[0].length) ? imageValues[row][col] : 0;
			}
		}
		
		return paddedValues;
	}
	
	private static int[][] padLeft(int[][] imageValues, int length) {
		int[][] paddedValues = new int[imageValues.length][imageValues[0].length + length];
		
		for (int row = 0; row < imageValues.length; row++){
			for (int col = 0; col < paddedValues[0].length; col++){
				paddedValues[row][col] = (col >= length) ? imageValues[row][col - length] : 0;
			}
		}
		
		return paddedValues;
	}
	
	private static int[][] padDown(int[][] imageValues, int length) {
		int[][] paddedValues = new int[imageValues.length + length][imageValues[0].length];
		
		for (int row = 0; row < paddedValues.length; row++){
			for (int col = 0; col < imageValues[0].length; col++){
				paddedValues[row][col] = (row < imageValues.length) ? imageValues[row][col] : 0;
			}
		}
		
		return paddedValues;
	}
	
	private static int[][] padUp(int[][] imageValues, int length) {
		int[][] paddedValues = new int[imageValues.length + length][imageValues[0].length];
		
		for (int row = 0; row < paddedValues.length; row++){
			for (int col = 0; col < imageValues[0].length; col++){
				paddedValues[row][col] = (row >= length) ? imageValues[row - length][col] : 0;
			}
		}
		
		return paddedValues;
	}
}