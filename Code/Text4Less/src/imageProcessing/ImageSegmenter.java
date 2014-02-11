package imageProcessing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import featureExtraction.ImageThinner;

public class ImageSegmenter {
	public static List<BufferedImage> segmentLineIntoCharacters(BufferedImage line){
		List<BufferedImage> textSegments = separateSpacesFromLine(line);
		List<BufferedImage> characters = new ArrayList<BufferedImage>();
		
		for (BufferedImage nextSegment : textSegments){
			characters.addAll(segmentTextIntoCharacters(nextSegment));
		}
		
		return characters;
	}
	
	private static List<BufferedImage> segmentTextIntoCharacters(BufferedImage text){
		List<BufferedImage> characters = new ArrayList<BufferedImage>();
		
		List<ValueRange> possibleSegmentAreas = getPossibleSegmentAreas(text);
		
		int lastEndingCol = 0;
		
		for (ValueRange nextRange : possibleSegmentAreas){
			long minBlackForSlice = Integer.MAX_VALUE;
			int minBlackCol = 0;
			
			for (int col = nextRange.getStartValue(); col < nextRange.getEndValue(); col++){				
				int blackSumForSlice = 0;
				for (int row = 0; row < text.getHeight(); row++){
					blackSumForSlice += (-1 * text.getRGB(col, row));
				}
				
				if (blackSumForSlice < minBlackForSlice){
					minBlackCol = col;
					minBlackForSlice = blackSumForSlice;
				}
			}
			
			BufferedImage nextCharacter = text.getSubimage(lastEndingCol, 0, minBlackCol - lastEndingCol, text.getHeight());
			characters.add(nextCharacter);
			
			lastEndingCol = minBlackCol;
		}

		BufferedImage nextCharacter = text.getSubimage(lastEndingCol, 0, text.getWidth() - lastEndingCol, text.getHeight());
		characters.add(nextCharacter);

		return characters;
	}
	
	private static List<BufferedImage> separateSpacesFromLine(BufferedImage line){
		int[][] binaryValues = ImageBinarizer.convertImageToBinaryValues(line);
		
		List<ValueRange> whitespaceAreas = new ArrayList<ValueRange>();
		int totalWhitespaceSlices = 0;
		int totalWhitespaces = 0;
		
		boolean isInWhitespace = false;
		int lastWhitespaceStart = 0;
		
		for (int col = 0; col < line.getWidth(); col++){
			boolean foundBlack = false;
			
			for (int row = 0; row < line.getHeight() && !foundBlack; row++){
				foundBlack = (binaryValues[row][col] == 1);
			}
			
			if (!foundBlack){
				totalWhitespaceSlices++;
				
				if (!isInWhitespace){
					isInWhitespace = true;
					lastWhitespaceStart = col;
				}
			}
			else{
				if (isInWhitespace){
					whitespaceAreas.add(new ValueRange(lastWhitespaceStart, col));
					totalWhitespaces++;
				}
				
				isInWhitespace = false;
			}
		}
		
		float avgWhitespaceWidth = (float)totalWhitespaceSlices / totalWhitespaces;
		int lastSubimageEnd = 0;
		List<BufferedImage> subimages = new ArrayList<BufferedImage>();
		
		for (ValueRange nextRange : whitespaceAreas){
			if (nextRange.getEndValue() - nextRange.getStartValue() > avgWhitespaceWidth + 1){
				
				if (nextRange.getStartValue() - lastSubimageEnd > 0){
					subimages.add(line.getSubimage(lastSubimageEnd, 0, nextRange.getStartValue() - lastSubimageEnd, line.getHeight()));
				}
				
				subimages.add(line.getSubimage(nextRange.getStartValue(), 0, nextRange.getEndValue() - nextRange.getStartValue(), line.getHeight()));
				lastSubimageEnd = nextRange.getEndValue();
			}
		}
		
		if (line.getWidth() - lastSubimageEnd > 0){
			subimages.add(line.getSubimage(lastSubimageEnd, 0, line.getWidth() - lastSubimageEnd, line.getHeight()));
		}
		
		return subimages;
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
