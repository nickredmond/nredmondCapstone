package imageProcessing;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import app.ImageThinner;
import app.MomentCalculator;

public class FeatureExtractionIOTranslator implements INetworkIOTranslator {
	private final static int NUMBER_VERTICAL_BINS = 6;
	private final static int NUMBER_HORIZONTAL_BINS = 7;
	
	private static final int NUMBER_PROFILE_DIRECTIONS = 4;
	
	public final static int DEFAULT_INPUT_LENGTH = 24 + 7 + (NUMBER_PROFILE_DIRECTIONS * 3);
	
	private final int ZONING_DIMENSION_X = 4;
	private final int ZONING_DIMENSION_Y = 4;
	
	private final float TOP_DIMENSION_PERCENT = 0.3f;
	private final float MID_DIMENSION_PERCENT = 0.5f;
	private final float BOTTOM_DIMENSION_PERCENT = 0.8f;
	
	private final float GAMMA = 2.2f;
	private final float R_LUMINANCE = 0.2126f;
	private final float G_LUMINANCE = 0.7152f;
	private final float B_LUMINANCE = 0.0722f;
	
	private final int DARK_BOUNDARY = 5000;
	
	// testing method
	public void printImageBinary(BufferedImage img){
		int[][] lightValues = getLightValues(img);
		int[][] croppedLightValues = cropLightValues(lightValues);
		printImg(croppedLightValues);
	}
	
	@Override
	public char translateNetworkOutputToCharacter(float[] output) {
		return new NetworkIOTranslator().translateNetworkOutputToCharacter(output);
	}

	@Override
	public int[] translateCharacterToNetworkOutput(char c) {
		return new NetworkIOTranslator().translateCharacterToNetworkOutput(c);
	}

	@Override
	public float[] translateImageToNetworkInput(BufferedImage img) {
		
		int[][] lightValues = getLightValues(img);
		int[][] croppedLightValues = cropLightValues(lightValues);
		float[] input = new float[DEFAULT_INPUT_LENGTH];
		
		printImg(croppedLightValues);
		
		ImageThinner thinner = new ImageThinner();
		thinner.thinImage(croppedLightValues);
		
		System.out.println("\r\n");
		
		printImg(croppedLightValues);
		
		int[][] values = {{1,1,1},{1,1,1},{1,1,1}};
		boolean yes = thinner.matchesTemplate(values, thinner.TEMPLATE_2, 0, 0);
		System.out.println("conn: " + yes);
		
		if(croppedLightValues.length > 1 && lightValues[0].length > 1){			
			
		}		
		
		return input;
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
	
	private float[] getHorizontalHistogram(int[][] lightValues, int numberBins){
		float rowsPerBin = (float)lightValues.length / numberBins;
		int stdRowsPerBin = (int)Math.floor(rowsPerBin);
		float remainder = 0.0f;
		float[] histogram = new float[numberBins];
		int binNumber = 0;
		int rowsForCurrentBin = 0;
		int sumForBin = 0;
		
		for (int row = 0; row < lightValues.length; row++){
			for (int col = 0; col < lightValues[row].length; col++){
				sumForBin += lightValues[row][col];
			}
			
			rowsForCurrentBin++;
			
			if (rowsForCurrentBin >= stdRowsPerBin){
				if (remainder >= 1.0f){
					remainder = 0.0f;
				}
				else{
					histogram[binNumber] = (float)sumForBin / rowsForCurrentBin;
					rowsForCurrentBin = 0;
					sumForBin = 0;
					binNumber++;
				}
				
				remainder += rowsPerBin - stdRowsPerBin;
			}
		}
		
		return histogram;
	}
	
	private float[] getVerticalHistogram(int[][] lightValues, int numberBins){
		float columnsPerBin = (float)lightValues[0].length / numberBins;
		int stdColsPerBin = (int)Math.floor(columnsPerBin);
		float remainder = 0.0f;
		float[] histogram = new float[numberBins];
		int binNumber = 0;
		int columnsForCurrentBin = 0;
		int sumForBin = 0;
		
		for (int col = 0; col < lightValues[0].length; col++){			
			for (int row = 0; row < lightValues.length; row++){
				sumForBin += lightValues[row][col];
			}
			
			columnsForCurrentBin++;
			
			if (columnsForCurrentBin >= stdColsPerBin){
				if (remainder >= 1.0f){
					remainder = 0.0f;
				}
				else{
					histogram[binNumber] = (float)sumForBin / columnsForCurrentBin;
					columnsForCurrentBin = 0;
					sumForBin = 0;
					binNumber++;
				}
				
				remainder += columnsPerBin - stdColsPerBin;
			}
		}

		
		return histogram;
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
		}
		
		startingRow = (startingRow == -1) ? 0 : startingRow;
		endingRow = (endingRow <= 0) ? lightValues.length : endingRow;
		startingCol = (startingCol == -1) ? 0 : startingCol;
		endingCol = (endingCol <= 0) ? lightValues[0].length : endingCol;
		
		return new Rectangle(startingRow, startingCol, endingCol - startingCol, endingRow - startingRow);
	}

	private int[][] getLightValues(BufferedImage img){
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
	
	/** USED FOR TESTING PURPOSES ONLY
	 * 
	 * @param lightValues
	 */
	private void printImg(int[][] lightValues){
		for (int row = 0; row < lightValues.length; row++){
			for (int col = 0; col < lightValues[row].length; col++){
				System.out.print(lightValues[row][col] + " ");
			}
			System.out.println();
		}
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
		float[] percentages = {TOP_DIMENSION_PERCENT, MID_DIMENSION_PERCENT, BOTTOM_DIMENSION_PERCENT};
		
		List<Float> inputList = new ArrayList<Float>();
		
		float[] huMoments = MomentCalculator.calculateHuInvariantMoments(croppedLightValues);
		
		for (int i = 0; i < huMoments.length; i++){
			inputList.add(huMoments[i]);
		}

		for (int i = 0; i < percentages.length; i++){
			inputList.add(getHeightPercentage(percentages[i], croppedLightValues));
		}
		
		for (int i = 0; i < percentages.length; i++){
			inputList.add(getWidthPercentage(percentages[i], croppedLightValues));
		}

		inputList.add(getVerticalSymmetryValue(croppedLightValues));
		inputList.add(getHorizontalSymmetryValue(croppedLightValues));
		
		float[] zoningValues = getZoningValues(croppedLightValues);
		
		for (int i = 0; i < zoningValues.length; i++){
			inputList.add(zoningValues[i]);
		}
		
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
		
		input = new float[inputList.size()];
		
		for (int i = 0; i < input.length; i++){
			input[i] = inputList.get(i);
		}
		
//		for (int i = 0; i < input.length; i++){
//			if (input[i] != input[i]){
//				printImg(croppedLightValues);
//			}
//		}
	}
}
