package featureExtraction;

public class CrossingCalculator {
	public static int getNumberVerticalCrossings(int column, int[][] imageValues){
		return getNumberCrossings(column, imageValues.length, imageValues, true);
	}
	
	public static int getNumberHorizontalCrossings(int row, int[][] imageValues){	
		return getNumberCrossings(row, imageValues[row].length, imageValues, false);		
	}
	
	private static int getNumberCrossings(int currentPosition, int maxPosition, int[][]imageValues, boolean isVertical){
		int numberCrossings = 0;
		boolean isInCharacter = false;
		
		for (int position = 0; position < maxPosition; position++){
			int pixelValue = (isVertical ? imageValues[position][currentPosition] : imageValues[currentPosition][position]);
			
			if (pixelValue == 1){
				if (!isInCharacter){
					numberCrossings++;
				}				
				isInCharacter = true;
			}
			else isInCharacter = false;
		}
		
		return numberCrossings;
	}
}