package imageProcessing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import featureExtraction.ImageThinner;

public class ImageSegmenter {
	public static List<BufferedImage> segmentLineIntoCharacters(BufferedImage line){
		List<BufferedImage> characters = new ArrayList<BufferedImage>();
		
		List<ValueRange> possibleSegmentAreas = getPossibleSegmentAreas(line);
		
		int lastEndingCol = 0;
		
		for (ValueRange nextRange : possibleSegmentAreas){
			long minBlackForSlice = Integer.MAX_VALUE;
			int minBlackCol = 0;
			
			for (int col = nextRange.getStartValue(); col < nextRange.getEndValue(); col++){				
				int blackSumForSlice = 0;
				for (int row = 0; row < line.getHeight(); row++){
					blackSumForSlice += (-1 * line.getRGB(col, row));
				}
				
				if (blackSumForSlice < minBlackForSlice){
					minBlackCol = col;
					minBlackForSlice = blackSumForSlice;
				}
			}
			
			BufferedImage nextCharacter = line.getSubimage(lastEndingCol, 0, minBlackCol - lastEndingCol, line.getHeight());
			characters.add(nextCharacter);
			
			lastEndingCol = minBlackCol;
		}
		
		BufferedImage nextCharacter = line.getSubimage(lastEndingCol, 0, line.getWidth() - lastEndingCol, line.getHeight());
		characters.add(nextCharacter);

		return characters;
	}
	
	private static List<ValueRange> getPossibleSegmentAreas(BufferedImage line){
		int[][] binaryValues = ImageBinarizer.convertImageToBinaryValues(line);
		ImageThinner thinner = new ImageThinner();
		thinner.thinImage(binaryValues);
		
		int lastCharacterEndingCol = 0;
		boolean isInCharacter = false;
		
		List<ValueRange> possibleAreas = new ArrayList<ValueRange>();
		
		for (int col = 0; col < binaryValues[0].length; col++){
			boolean foundBlack = false;
			
			for (int row = 0; row < binaryValues.length && !foundBlack; row++){
				foundBlack = (binaryValues[row][col] == 1);
			}
			
//			if ((foundBlack && !isInCharacter) || (!foundBlack && isInCharacter)){
//				System.out.println("lastCharEnd: " + lastCharacterEndingCol + " col: " + col);
////				BufferedImage nextCharacter = line.getSubimage(lastCharacterEndingCol+1, 0, col - lastCharacterEndingCol, line.getHeight());
////				characters.add(nextCharacter);		
//			}
			
			if (foundBlack && !isInCharacter){
				isInCharacter = true;
				
				if (lastCharacterEndingCol > 0){
					possibleAreas.add(new ValueRange(lastCharacterEndingCol, col));
				}
			}
			else if (!foundBlack && isInCharacter){
				isInCharacter = false;
				lastCharacterEndingCol = col;
			}
		}
		
		return possibleAreas;
	}
}