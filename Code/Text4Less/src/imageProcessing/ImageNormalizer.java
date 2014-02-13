package imageProcessing;

import java.awt.Rectangle;

public class ImageNormalizer {
	public static final int BETA = 800;
	
	public static int[][] translateImage(int[][] lightValues){
		return null;
	}
	
	public static int[][] scaleImage(int[][] lightValues){
		return null;
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
