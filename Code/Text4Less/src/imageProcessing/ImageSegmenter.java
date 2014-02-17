package imageProcessing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import math.StatisticalMath;
import debug.SegmentationDataWindow;
import debug.SegmentationDatum;
import featureExtraction.ImageThinner;

public class ImageSegmenter {
	private static final float SPACE_THRESHOLD = 0.1f;
	
	public static List<BufferedImage> segmentLineIntoCharacters(BufferedImage line){
		List<BufferedImage> textSegments = separateSpacesFromLine(line);
		List<BufferedImage> characters = new ArrayList<BufferedImage>();

		for (BufferedImage nextSegment : textSegments){
			characters.addAll(segmentTextIntoCharacters(nextSegment));
		}
		
//		int totalWidth = 0;
//		int numberCharacters = 0;
//		
//		// TEST CODE //
//		List<SegmentationDatum> segmentationData = new ArrayList<SegmentationDatum>();
//		List<Float> widths = new ArrayList<Float>();
//		List<int[][]> stuff = new ArrayList<int[][]>();
//		// END TEST //
//		
//		for (BufferedImage nextCharacter : characters){
//			int[][] imageValues = ImageBinarizer.convertImageToBinaryValues(nextCharacter);
//			int totalBlackPixels = 0;
//			
//			for (int i = 0; i < imageValues.length; i++){
//				for (int j = 0; j < imageValues[0].length; j++){
//					if (imageValues[i][j] == 1){
//						totalBlackPixels++;
//					}
//				}
//			}
//			
//			float percentBlack = (float)totalBlackPixels / (imageValues.length * (imageValues[0].length));
//			if (percentBlack >= SPACE_THRESHOLD){
//				totalWidth += imageValues[0].length;
//				numberCharacters++;
//				
//				// TEST CODE //
//				widths.add((float)imageValues[0].length);
//				stuff.add(imageValues);
//				// END TEST //
//			}
//		}
		
//		float avgWidth = (float)totalWidth / numberCharacters;
//		
//		// TEST CODE //
//		float stdDev = StatisticalMath.standardDeviation(widths);
//		float mean = StatisticalMath.average(widths);
//		System.out.println("avg: " + mean + " " + avgWidth);
//		for (int[][] nextImage : stuff){
//			float numDevs = StatisticalMath.numberStandardDeviationsFromMean(stdDev, mean, nextImage[0].length);
//			SegmentationDatum datum = new SegmentationDatum(ImageBinarizer.convertBinaryValuesToImage(nextImage), stdDev, numDevs);
//			segmentationData.add(datum);
//		}
//		// END TEST //	
//		
//		new SegmentationDataWindow(segmentationData);
//		
//		List<BufferedImage> finalCharacters = new ArrayList<BufferedImage>();
//		
//		for (BufferedImage nextImage : characters){
//			if (nextImage.getWidth() > avgWidth){
//				finalCharacters.addAll(splitSegment(nextImage));
//			}
//			else finalCharacters.add(nextImage);
//		}
		

		return characters;
	}
	
	private static List<BufferedImage> splitSegment(BufferedImage segment){
		List<BufferedImage> splitImages = new ArrayList<BufferedImage>();
		
		BufferedImage leftHalf = segment.getSubimage(0, 0, segment.getWidth() / 2, segment.getHeight());
		
		int rightWidth = segment.getWidth() / 2;
		if ((segment.getWidth()) / 2 + 1 + rightWidth > segment.getWidth()){
			rightWidth--;
		}
		
		BufferedImage rightHalf = segment.getSubimage((segment.getWidth() / 2) + 1, 0, rightWidth, segment.getHeight());
		
		splitImages.add(leftHalf);
		splitImages.add(rightHalf);
		
		return splitImages;
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
