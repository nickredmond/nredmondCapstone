package imageProcessing;

import java.awt.Rectangle;

import debug.FeatureExtractionDebug;

public class ImageNormalizer {
	public static final int BETA = 800;
	
	public static int[][] translateImage(int[][] lightValues){
		return null;
	}
	
	public static int[][] scaleImage(int[][] lightValues){
		return null;
	}
	
	public static int[][] downSizeImage(int[][] imageValues, int width, int height){
		if (imageValues.length < height || imageValues[0].length < width){
			throw new IllegalArgumentException("New dimensions must be smaller than current dimensions.");
		}
		
		float scaleX = (float)imageValues[0].length / width;
		float scaleY = (float)imageValues.length / height;
		
		int[][] downsizedValues = new int[height][width];
		int[][] yes = new int[height][width];
		
		for (int row = 0; row < height; row++){
			int startingY = (int)(row * scaleY);
			int endingY = (int)((row+1) * scaleY) - 1;
			
			int crossings = calculateCrossings(imageValues, startingY, endingY);
			int[] preTransformValues = new int[width];
			
			for (int col = 0; col < width; col++){
				preTransformValues[col] = calculateAvgValueForCol(col, imageValues, scaleX, startingY, endingY);
			}
			
			downsizedValues[row] = RowPatternTransformer.getTransformationFor(preTransformValues, crossings);
			
			yes[row] = preTransformValues;
		}
		
	//	FeatureExtractionDebug.printImg(yes);
		
		return yes;
	}
	
	private static int calculateAvgValueForCol(int col, int[][] imageValues,
			float scaleX, int startingY, int endingY) {
		int startingX = (int)(col * scaleX);
		int endingX = (int)((col+1) * scaleX) - 1;
		
		int numberBlacks = 0;
		int numberWhites = 0;
		
		for (int y = startingY; y < endingY; y++){
			for (int x = startingX; x < endingX; x++){
				if (imageValues[y][x] == 1){
					numberBlacks++;
				}
				else numberWhites++;
			}
		}
		
		float intensity = (numberWhites > 0) ? (float)(numberBlacks) / (numberBlacks + numberWhites) : 1.0f;
		
		return ((numberBlacks >= numberWhites) ? 1 : 0);
	}

	private static int calculateCrossings(int[][] imageValues, int startingY,
			int endingY) {
		int y = (int) ((float)(endingY + startingY) / 2.0f);
		int numberCrossings = 0;
		boolean wasDark = false;
		
	//	System.out.println("there it is " + y);
		
		for (int x = 0; x < imageValues[y].length; x++){
			if (wasDark && imageValues[y][x] == 1){
				numberCrossings++;
			}
			wasDark = (imageValues[y][x] == 0);
		}
		
		return numberCrossings;
	}

	public static int[][] cropImage(int[][] lightValues){
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
	
	private static Rectangle getCroppedLightValueRectangle(int[][] lightValues){
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
}
