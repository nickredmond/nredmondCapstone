package networkIOtranslation;

import imageProcessing.ImageScaler;
import imageProcessing.NoiseRemover;
import imageProcessing.TranslationResult;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import debug.CharacterViewDebug;
import debug.FeatureExtractionDebug;
import math.ComplexNumber;
import featureExtraction.ChainCodeCreator;
import featureExtraction.CrossingCalculator;
import featureExtraction.FeaturePoint;
import featureExtraction.FeatureType;
import featureExtraction.ImageThinner;
import featureExtraction.MomentCalculator;
import featureExtraction.VectorCalculator;
import featureExtraction.ZernikeImageNormalizer;

public class FeatureExtractionIOTranslator implements INetworkIOTranslator {
private static final int NUMBER_PROFILE_DIRECTIONS = 4;
	
	public final static int DEFAULT_INPUT_LENGTH = 7 + (NUMBER_PROFILE_DIRECTIONS * 3) + 16 + 2; // plus 24 w/ original values
	private int inputLength;
	
	private final int ZONING_DIMENSION_X = 10;
	private final int ZONING_DIMENSION_Y = 10;
	
	private final float TOP_DIMENSION_PERCENT = 0.3f;
	private final float MID_DIMENSION_PERCENT = 0.5f;
	private final float BOTTOM_DIMENSION_PERCENT = 0.8f;
	
	private final float GAMMA = 2.2f;
	private final float R_LUMINANCE = 0.2126f;
	private final float G_LUMINANCE = 0.7152f;
	private final float B_LUMINANCE = 0.0722f;
	
	private final int MAX_NUMBER_CROSSINGS = 4;
	
	private final int DARK_BOUNDARY = 5000;
	
	private final int MAX_ENDPOINTS = 4;
	private final int MAX_T_JUNCS = 4;
	
	// testing method
//	public void printImageBinary(BufferedImage img){
//		int[][] lightValues = getLightValues(img);
//		int[][] croppedLightValues = cropLightValues(lightValues);
//		printImg(croppedLightValues);
//	}
	
	public FeatureExtractionIOTranslator(){
		BufferedImage img = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
		
		for (int row = 0; row < img.getHeight(); row++)
		{
			for (int col = 0; col < img.getWidth(); col++){
				img.setRGB(col, row, -1);
			}
		}
		
		translateImageToNetworkInput(img);
	}
	
	@Override
	public TranslationResult translateNetworkOutputToCharacter(float[] output) {
		return new NetworkIOTranslator().translateNetworkOutputToCharacter(output);
	}

	@Override
	public int[] translateCharacterToNetworkOutput(char c) {
		return new NetworkIOTranslator().translateCharacterToNetworkOutput(c);
	}

	@Override
	public float[] translateImageToNetworkInput(BufferedImage img) {
		
		int[][] lightValues = getLightValues(img);
		NoiseRemover.removeNoise(lightValues);
		
	//	CharacterViewDebug.displayCharacterView(img, lightValues, lightValues.length, lightValues[0].length);
		
		int[][] croppedLightValues = cropLightValues(lightValues);
		float[] input = new float[inputLength];
		
		if(croppedLightValues.length > 1 && croppedLightValues[0].length > 1){					
			float[] percentages = {TOP_DIMENSION_PERCENT, MID_DIMENSION_PERCENT, BOTTOM_DIMENSION_PERCENT};
			
			if (croppedLightValues[0].length < ZONING_DIMENSION_X || croppedLightValues.length < ZONING_DIMENSION_Y){
				croppedLightValues = padWithSpace(croppedLightValues);
			}
			
			List<Float> inputList = new ArrayList<Float>();
			
			int[][] squareCroppedValues = ZernikeImageNormalizer.squareImage(croppedLightValues);
			int[][] scaledImg = ImageScaler.scaleWithBilinearInterpolation(squareCroppedValues, 20, 20);
			
			//int[][] newValues = convertToScale(20, 20, scaledImg);
			
		//	CharacterViewDebug.displayCharacterView(img, scaledImg, 20, 20);
			
			for (int row = 0; row < scaledImg.length; row++){
				for (int col = 0; col < scaledImg[0].length; col++){
					inputList.add((float) scaledImg[row][col]);
				}
			}
			
		//	inputList.add(getHeightToWidthRatio(croppedLightValues));
			
//			FeatureExtractionDebug.printImg(lightValues);
//			System.out.println();
//			FeatureExtractionDebug.printImg(croppedLightValues);
//			System.out.println();
//			FeatureExtractionDebug.printImg(scaledImg);
//			System.out.println("------------------------------------------------------");
						
			addProfilingFeatures(inputList, croppedLightValues, percentages);
			addVectorFeatures(inputList, croppedLightValues);
			addCrossingFeatures(inputList, croppedLightValues);
//			addFeaturePoints(inputList, croppedLightValues);
			
		//	CharacterViewDebug.displayCharacterView(img, scaledImg, 20, 20);
			
			input = new float[inputList.size()];
			
			for (int i = 0; i < input.length; i++){
				input[i] = inputList.get(i);
			}
			
//			for (int i = 0; i < input.length; i++){
//				System.out.print(input[i] + " ");
//			}
//			System.out.println();
			
			inputLength = input.length;
		}

		return input;
	}
	
	private int[][] padWithSpace(int[][] croppedLightValues) {
		int width = (croppedLightValues[0].length < ZONING_DIMENSION_X) ? ZONING_DIMENSION_X : croppedLightValues[0].length;
		int height = (croppedLightValues.length < ZONING_DIMENSION_Y) ? ZONING_DIMENSION_Y : croppedLightValues.length;
		
		int[][] paddedLightValues = new int[height][width];
		
		for (int row = 0; row < croppedLightValues.length; row++){
			for (int col = 0; col < croppedLightValues[0].length; col++){
				paddedLightValues[row][col] = croppedLightValues[row][col];
			}
		}
		
		return paddedLightValues;
	}
	
	private void addChainCodeFeatures(List<Float> inputList, int[][] croppedLightValues){
		int[][] vectorValues = VectorCalculator.calculateVectorsForSkeleton(croppedLightValues);
		VectorCalculator.correctVectorValues(vectorValues);
		VectorCalculator.removeDiagonalLineThickness(vectorValues);
		
		ChainCodeCreator codeGen = new ChainCodeCreator();
		int[] chainCode = codeGen.generateChainCode(vectorValues);
		
		for (int i = 0; i < chainCode.length; i++){
			inputList.add((float)chainCode[i]);
		}
	}
	
	private void addFeaturePoints(List<Float> inputList, int[][] croppedLightValues){
		ImageThinner t = new ImageThinner();	
		t.thinImage(croppedLightValues);
		int[][] croppedSkeleton = cropLightValues(croppedLightValues);
		
//		System.out.println("H: " + croppedSkeleton.length + ", a: " + croppedLightValues.length);
//		FeatureExtractionDebug.printImg(croppedLightValues);
		
		int[][] directionVectors = VectorCalculator.calculateVectorsForSkeleton(croppedSkeleton);
		
		List<FeaturePoint> points = VectorCalculator.calculateFeaturePoints(directionVectors, 4, 4);
		Collections.sort(points);
		
		int[] featurePoints = new int[(MAX_ENDPOINTS * 2) + (MAX_T_JUNCS * 2)];
		int currentEndPtIndex = 0;
		int currentTjuncIndex = MAX_ENDPOINTS * 2;
		
		for (FeaturePoint nextPoint : points){
			if (nextPoint.getType() == FeatureType.END_POINT && currentEndPtIndex < (MAX_ENDPOINTS * 2)){
				featurePoints[currentEndPtIndex++] = nextPoint.y();
				featurePoints[currentEndPtIndex++] = nextPoint.x();
			}
			else if (nextPoint.getType() == FeatureType.T_JUNCTION && currentTjuncIndex < featurePoints.length){
				featurePoints[currentTjuncIndex++] = nextPoint.y();
				featurePoints[currentTjuncIndex++] = nextPoint.x();
			}
		}
		
		for (int i = 0; i < featurePoints.length; i++){
			inputList.add((float)featurePoints[i]);
		}
		
	//	FeatureExtractionDebug.printPointFeatures(points, ZONING_DIMENSION_X, ZONING_DIMENSION_Y);
		
//		VectorCalculator.correctVectorValues(directionVectors);
//		System.out.println();
//		FeatureExtractionDebug.printImg(directionVectors);
//		System.out.println();
	}
	
	private void addZernikeFeatures(List<Float> inputList, int[][] croppedLightValues){
		int[][] normalizedLightValues = ZernikeImageNormalizer.normalizeImageOnCentroidScaleInvariant(croppedLightValues);
		int[][] zernikeLightValues = ZernikeImageNormalizer.padToRequiredDimension(normalizedLightValues, 30);
		
		for (int order = 5; order <= 15; order++){
			for (int repetition = (order % 2 == 0) ? 2 : 3; repetition < (order - 2); repetition+=2){
				ComplexNumber nextZernike = MomentCalculator.calculateZernikeMoment(zernikeLightValues, order, repetition);
				
				float realZernikeValue = (nextZernike.a() == nextZernike.a()) ? nextZernike.a() : 0.0f;
				float imaginaryZernikeValue = (nextZernike.b() == nextZernike.b()) ? nextZernike.b() : 0.0f;
				
				//System.out.println("n: " + order + ", m: " + repetition + ", r: " + realZernikeValue + ", i: " + imaginaryZernikeValue);
				
				inputList.add(realZernikeValue);
				inputList.add(imaginaryZernikeValue);
			}
		}
	}
	
	private void addZoningFeatures(List<Float> inputList, int[][] croppedLightValues){
		float[] zoningValues = getZoningValues(croppedLightValues);
		
		for (int i = 0; i < zoningValues.length; i++){
			inputList.add(zoningValues[i]);
		}
	}
	
	private void addPercentDimensionFeatures(List<Float> inputList, int[][] croppedLightValues, float[] percentages){
		for (int i = 0; i < percentages.length; i++){
			inputList.add(getHeightPercentage(percentages[i], croppedLightValues));
		}
		
		for (int i = 0; i < percentages.length; i++){
			inputList.add(getWidthPercentage(percentages[i], croppedLightValues));
		}
	}
	
	private void addProfilingFeatures(List<Float> inputList, int[][] croppedLightValues, float[] percentages){
		float[] profilingValues = new float[NUMBER_PROFILE_DIRECTIONS * percentages.length];
		
		for (int i = 0; i < percentages.length; i++){
			int index = NUMBER_PROFILE_DIRECTIONS * i;
			profilingValues[index] = getTopProfileAtPercent(percentages[i], croppedLightValues);
			profilingValues[index + 1] = getRightProfileAtPercent(percentages[i], croppedLightValues);
			profilingValues[index + 2] = getBottomProfileAtPercent(percentages[i], croppedLightValues);
			profilingValues[index + 3] = getLeftProfileAtPercent(percentages[i], croppedLightValues);
		}
		
		for (int i = 0; i < profilingValues.length; i++){
			inputList.add(profilingValues[i]);
		}
	}
	
	private void addHuMomentFeatures(List<Float> inputList, int[][] croppedLightValues){
		float[] huMoments = MomentCalculator.calculateHuInvariantMoments(croppedLightValues);
		
		for (int i = 0; i < huMoments.length; i++){
			inputList.add(huMoments[i]);
		}
	}
	
	private void addCrossingFeatures(List<Float> inputList, int[][] croppedLightValues){
		Point centroid = MomentCalculator.calculateCentroid(croppedLightValues);		
		int vertCrossings = CrossingCalculator.getNumberVerticalCrossings(centroid.x, croppedLightValues);
		int horizCrossings = CrossingCalculator.getNumberHorizontalCrossings(centroid.y, croppedLightValues);
		
		inputList.add((float)vertCrossings / MAX_NUMBER_CROSSINGS);
		inputList.add((float)horizCrossings / MAX_NUMBER_CROSSINGS);
	}

	private void addVectorFeatures(List<Float> inputList, int[][] croppedLightValues){
		ImageThinner thinner = new ImageThinner();
		thinner.thinImage(croppedLightValues);
		
		float[] zoneVectors = VectorCalculator.calculateZonedVectorsForSkeleton(croppedLightValues, 4, 4, true);
		
		for (int i = 0; i < zoneVectors.length; i++){
			inputList.add(zoneVectors[i]);
		}
	}
	
	private float getHeightToWidthRatio(int[][] lightValues){
		float htwRatio = 0.0f;
		boolean isSpaceCharacter = true;
		int height = lightValues.length;
		int width = lightValues[0].length;
		
		for (int row = 0; row < lightValues.length && isSpaceCharacter; row++){
			for (int col = 0; col < lightValues[0].length && isSpaceCharacter; col++){
				isSpaceCharacter = (lightValues[row][col] == 0);
			}
		}
		
		if (isSpaceCharacter || (height == width)){
			htwRatio = 1.0f;
		}
		else if (height > width){
			htwRatio = (float)width / height;
		}
		else htwRatio = (-1.0f * height) / width;
		
		return htwRatio;
	}
	
	private float getLeftProfileAtPercent(float percentHeight, int[][] lightValues){
		int row = (int)(percentHeight * lightValues.length);
		boolean foundCharacter = false;
		int currentColumn = 0;
		
		for (int col = 0; col < lightValues[row].length && !foundCharacter; col++){
			foundCharacter = (lightValues[row][col] == 1);
			currentColumn = col;
		}
		
		return (float)currentColumn / lightValues[row].length;
	}
	
	private float getRightProfileAtPercent(float percentHeight, int[][] lightValues){
		int row = (int)(percentHeight * lightValues.length);
		boolean foundCharacter = false;
		int currentColumn = lightValues[row].length - 1;
		
		for (int col = lightValues[row].length - 1; col >= 0 && !foundCharacter; col--){
			foundCharacter = (lightValues[row][col] == 1);
			currentColumn = col;
		}
		
		return (float)(lightValues[row].length - 1 - currentColumn) / lightValues[row].length;
	}
	
	private float getTopProfileAtPercent(float percentWidth, int[][] lightValues){
		int col = (int) (percentWidth * lightValues[0].length);
		boolean foundCharacter = false;
		int currentRow = 0;
		
		for (int row = 0; row < lightValues.length && !foundCharacter; row++){
			foundCharacter = (lightValues[row][col] == 1);
			currentRow = row;
		}
		
		return (float)currentRow / lightValues.length;
	}
	
	private float getBottomProfileAtPercent(float percentWidth, int[][] lightValues){
		int col = (int) (percentWidth * lightValues[0].length);
		boolean foundCharacter = false;
		int currentRow = lightValues.length - 1;
		
		for (int row = lightValues.length - 1; row >= 0 && !foundCharacter; row--){
			foundCharacter = (lightValues[row][col] == 1);
			currentRow = row;
		}
		
		return (float)(lightValues.length - 1 - currentRow) / lightValues.length;
	}
	
	private int[][] cropLightValues(int[][] lightValues){
		Rectangle croppedRectangle = getCroppedLightValueRectangle(lightValues);
		
		int[][] croppedLightValues = new int[croppedRectangle.height][croppedRectangle.width];
		
		int croppedRow = 0;
		int croppedCol = 0;
		
		for (int row = croppedRectangle.x; row < croppedRectangle.x + croppedRectangle.height &&
				row < lightValues.length; row++){
			for (int col = croppedRectangle.y; col < croppedRectangle.y + croppedRectangle.width &&
					col < lightValues[0].length; col++){
				croppedLightValues[croppedRow][croppedCol] = lightValues[row][col];
				croppedCol++;
			}
			
			croppedCol = 0;
			croppedRow++;
		}
		
		return croppedLightValues;
	}
	
	private Rectangle getCroppedLightValueRectangle(int[][] lightValues){
		int startingRow = -1;
		int endingRow = -1;
		int startingCol = -1;
		int endingCol = -1;
		
		for (int row = 0; row < lightValues.length; row++){
			for (int col = 0; col < lightValues[0].length; col++){
				boolean isDark = (lightValues[row][col] == 1);
				
				if (isDark){
					startingRow = (startingRow < 0) ? row : startingRow;
					endingRow = (endingRow < 0) ? endingRow : -1;
					
					startingCol = (startingCol > col || startingCol < 0) ? col : startingCol;
					endingCol = (endingCol < col) ? -1 : endingCol;
				}
				else{
					endingRow = (endingRow < 0) ? row : endingRow;
					endingCol = (endingCol < 0 && startingCol >= 0) ? col : endingCol;
				}
			}
			if (endingCol < 0){
				endingCol = lightValues[0].length - 1;
			}
		}
		
		startingRow = (startingRow == -1) ? 0 : startingRow;
		endingRow = (endingRow <= 0) ? lightValues.length : endingRow;
		startingCol = (startingCol == -1) ? 0 : startingCol;
		endingCol = (endingCol <= 0) ? lightValues[0].length : endingCol;
		
		endingRow = (endingRow < lightValues.length - 1) ? endingRow + 1 : endingRow;
		
		return new Rectangle(startingRow, startingCol, endingCol - startingCol, endingRow - startingRow);
	}

	public int[][] getLightValues(BufferedImage img){
		int[][] lightValues = new int[img.getHeight()][img.getWidth()];
		
		for (int x = 0; x < img.getWidth(); x++){
			for (int y = 0; y < img.getHeight(); y++){
				int rgbValue = img.getRGB(x, y);
				int rVal = (rgbValue >> 16) & 0xff;
				int gVal = (rgbValue >> 8) & 0xff;
				int bVal = (rgbValue) & 0xff;
				
				float rLinear = (float) Math.pow(rVal, GAMMA);
				float gLinear = (float) Math.pow(gVal, GAMMA);
				float bLinear = (float) Math.pow(bVal, GAMMA);
				
				float luminance = (R_LUMINANCE * rLinear) + (G_LUMINANCE + gLinear) +
						(B_LUMINANCE * bLinear);
				
				int lightness = (int) (116 * Math.pow(luminance, 0.3333) - 16);
				lightValues[y][x] = (lightness < DARK_BOUNDARY) ? 0 : 1;
			}
		}
		
		invertLightValues(lightValues);
		return lightValues;
	}
	
	private void invertLightValues(int[][] lightValues) {
		for (int row = 0; row < lightValues.length; row++){
			for (int col = 0; col < lightValues[row].length; col++){
				lightValues[row][col] = ((lightValues[row][col] == 0) ? 1 : 0);
			}
		}
	}
	
	private float getSumOfFeatures(List<Float> input){
		float sum = 0.0f;
		
		for (int i = 0; i < input.size(); i++){
			sum += input.get(i);
		}
		
		return sum;
	}

	private float getWidthPercentage(float percentTotalHeight, int[][] lightValues){
		int row = (int)(percentTotalHeight * lightValues.length);
		int widthSum = 0;
		
		for (int col = 0; col < lightValues[row].length; col++){
			widthSum += lightValues[row][col];
		}
		
		return (float)widthSum / lightValues[0].length;
	}
	
	private float getHeightPercentage(float percentTotalWidth, int[][] lightValues){
		int col = (int)(percentTotalWidth * lightValues[0].length);
		int heightSum = 0;
		for (int row = 0; row < lightValues.length; row++){
			heightSum += lightValues[row][col];
		}
		
		return (float)heightSum / lightValues.length;
	}
	
	private float getHorizontalSymmetryValue(int[][] lightValues){
		int imageHalf = lightValues.length / 2;
		float totalSymmetryDifference = 0;
		
		for (int col = 0; col < lightValues[0].length; col++){
			int colSymmetryDifference = 0;
			
			for (int distance = 0; distance < imageHalf; distance++){				
				int topRow = imageHalf - 1 - distance;
				int bottomRow = imageHalf + distance;
				
				int topLightValue = lightValues[topRow][col];
				int bottomLightValue = lightValues[bottomRow][col];
				
				colSymmetryDifference += Math.abs(topLightValue - bottomLightValue);
			}
		//	colSymmetryDifference *= (1.001f * (colSymmetryDifference + 1));
			totalSymmetryDifference += (float)colSymmetryDifference / imageHalf;
		}
		
		return (float)totalSymmetryDifference / lightValues[0].length;
	}
	
	private float getVerticalSymmetryValue(int[][] lightValues){ // DO THIS
		int imageHalf = lightValues[0].length / 2;
		float totalSymmetryDifference = 0;
		
		for (int row = 0; row < lightValues.length; row++){
			int rowSymmetryDifference = 0;
			
			for (int distance = 0; distance < imageHalf; distance++){				
				int leftCol = imageHalf - 1 - distance;
				int rightCol = imageHalf + distance;
				
				int leftLightValue = lightValues[row][leftCol];
				int rightLightValue = lightValues[row][rightCol];
				
				rowSymmetryDifference += Math.abs(leftLightValue - rightLightValue);
			}
		//	rowSymmetryDifference *= (1.001f * (rowSymmetryDifference + 1));
			totalSymmetryDifference += (float)rowSymmetryDifference / imageHalf;
		}
		
		return (float)totalSymmetryDifference / lightValues.length;
	}
	
	private float[] getZoningValues(int[][] lightValues){
		float[] zoningValues = new float[ZONING_DIMENSION_X * ZONING_DIMENSION_Y];
		
		int numRowsPerZone = lightValues.length / ZONING_DIMENSION_X;
		int numColsPerZone = lightValues[0].length / ZONING_DIMENSION_Y;
		
		for (int x = 0; x < ZONING_DIMENSION_X; x++){
			for (int y = 0; y < ZONING_DIMENSION_Y; y++){
				
				int nextZoningIndex = (x * ZONING_DIMENSION_X) + y;
				float nextZoneValue = getNextZoningValue(x, y, numRowsPerZone, numColsPerZone, lightValues);
				zoningValues[nextZoningIndex] = nextZoneValue;
			}
		}
		
		return zoningValues;
	}

	private float getNextZoningValue(int row, int col, int numRowsPerZone,
			int numColsPerZone, int[][] lightValues) {
		int startingRow = row * numRowsPerZone;
		int endingRow = (row + 1) * numRowsPerZone;
		int startingCol = col * numColsPerZone;
		int endingCol = (col + 1) * numColsPerZone;
		
		int lightValueSum = 0;
		int numValues = 0;
		
		for (int currentRow = startingRow; currentRow < endingRow; currentRow++){
			for (int currentCol = startingCol; currentCol < endingCol; currentCol++){
				lightValueSum += lightValues[currentRow][currentCol];
				numValues++;
			}
		}
		
		return (float)lightValueSum / numValues;
	}
	
	private void doStuff(int[][] croppedLightValues, float[] input){
		
	}

	public int getInputLength() {
		return inputLength;
	}

	public void setInputLength(int inputLength) {
		this.inputLength = inputLength;
	}
	
	private int[][] convertToScale(int width, int height, int[][] lightValues){
		final float THRESHOLD = 0.0f;
		float zoneWidth = ((float)lightValues[0].length / width);
		float zoneHeight = ((float)lightValues.length / height);
		
		int[] zoneSums = new int[width * height];
		int[][] zonedValues = new int[height][width];
		
		for (int row = 0; row < lightValues.length; row++){
			for (int col = 0; col < lightValues[0].length; col++){
				int zoneX = (int) (col / zoneWidth);
				int zoneY = (int) (row / zoneHeight);
				int zoneNumber = (zoneY * width) + zoneX;

				zoneSums[zoneNumber] += (lightValues[row][col] != 0) ? 1 : 0;
			}
		}
		
		for (int row = 0; row < zonedValues.length; row++){
			for (int col = 0; col < zonedValues[0].length; col++){
				int currentZoneIndex = (row * width) + col;
				int currentZoneSum = zoneSums[currentZoneIndex];
				
				zonedValues[row][col] = ((float)currentZoneSum / (zoneWidth * zoneHeight) > THRESHOLD) ? 1 : 0;
			}
		}
		
		return zonedValues;
	}
}