package imageProcessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImagePreprocessor {
	private final float WHITESPACE_MARGIN = 0.0002f;
	private final int CROP_PIXEL_BUFFER = 5;
	private final int SPLIT_PIXEL_BUFFER = 	1;
	private final float BETWEEN_CHAR_WHITESPACE_MARGIN = 0.057f;
	
	private final long DEFAULT_MAX_WHITESPACE_VALUE = 50000;
	private long maxRowWhitespaceValue;
	
	public ImagePreprocessor(){
		maxRowWhitespaceValue = DEFAULT_MAX_WHITESPACE_VALUE;
	}
	
	public BufferedImage trimMargins(BufferedImage original){
		BufferedImage topBottomCrop = trimTopBottomMargins(original);
		BufferedImage fullCrop = trimLeftRightMargins(topBottomCrop);
		return fullCrop;
	}
	
	// TEST METHOD //
	public void doStuff(BufferedImage image) throws IOException{
		BufferedImage topBottom = trimMargins(image);
		List<BufferedImage> lines = splitIntoLines(topBottom);
		ImageIO.write(lines.get(0), "jpg", new File("trainingImages/unformatted/before.jpg"));
		BufferedImage firstLine = lines.get(0);
		
		BufferedImage yes = trimMargins(firstLine);
		ImageIO.write(yes, "jpg", new File("trainingImages/unformatted/after.jpg"));
	}
	
	public int getLineHeight(BufferedImage line) throws IOException{
		BufferedImage trimmedLine = trimMargins(line);
		return trimmedLine.getHeight();
	}
	
	public int getFontSize(BufferedImage image) throws IOException{
		BufferedImage trimmedImage = trimMargins(image);
		List<BufferedImage> lines = splitIntoLines(trimmedImage);
		
		int totalHeight = 0;
		
		for (BufferedImage nextLine : lines){
		totalHeight += getLineHeight(nextLine);
		}
		
		return (int)((float)totalHeight /lines.size());
	}
	
	private BufferedImage trimTopBottomMargins(BufferedImage original){
		RgbLimitSet rowLimits = 
				getRgbValueLimits(RgbValueReader.ROW_VALUE_READER, original, original.getHeight());
		long maxWhitespaceRowValue = (long) (rowLimits.getLowestRgbValue() + (WHITESPACE_MARGIN * rowLimits.getRgbRange()));
		
		maxRowWhitespaceValue = maxWhitespaceRowValue;
		
		ValueRange cropValues = getCropValueSet(original, RgbValueReader.ROW_VALUE_READER, 
				original.getHeight(), maxWhitespaceRowValue);
		
		int endValue = (cropValues.getEndValue() < original.getHeight()) ? cropValues.getEndValue() : original.getHeight() - 1;
		
		int cropHeight = endValue - cropValues.getStartValue();
		int paddedCropHeight = (cropHeight + CROP_PIXEL_BUFFER < original.getHeight()) ? cropHeight + CROP_PIXEL_BUFFER : cropHeight;
		int paddedCropY = (cropValues.getStartValue() - CROP_PIXEL_BUFFER >= 0) ? 
				cropValues.getStartValue() - CROP_PIXEL_BUFFER : cropValues.getStartValue();
		
		BufferedImage trimmedImg = original.getSubimage(0, paddedCropY, 
				original.getWidth(), paddedCropHeight);
		
		return trimmedImg;
	}
	
	private BufferedImage trimLeftRightMargins(BufferedImage original){
		RgbLimitSet colLimits = 
				getRgbValueLimits(RgbValueReader.COLUMN_VALUE_READER, original, original.getWidth());
		long maxWhitespaceColValue = 
				(long) (colLimits.getLowestRgbValue() + (WHITESPACE_MARGIN * colLimits.getRgbRange()));		
		ValueRange cropValues = getCropValueSet(original, RgbValueReader.COLUMN_VALUE_READER,
				original.getWidth(), maxWhitespaceColValue);
		
		int endValue = (cropValues.getEndValue() < original.getWidth()) ? cropValues.getEndValue() : original.getWidth() - 1;
		
		int cropWidth = endValue - cropValues.getStartValue();
		int paddedCropWidth = ((cropWidth + CROP_PIXEL_BUFFER < original.getWidth()) ? cropWidth + CROP_PIXEL_BUFFER : cropWidth);
		int paddedCropX = (cropValues.getStartValue() - CROP_PIXEL_BUFFER >= 0) ?
				cropValues.getStartValue() - CROP_PIXEL_BUFFER : cropValues.getStartValue();
		;
		BufferedImage trimmedImg = original.getSubimage(paddedCropX, 0, 
			paddedCropWidth, original.getHeight());
		
		return trimmedImg;
	}
	
	private ValueRange getCropValueSet(BufferedImage original, RgbValueReader reader,
			int maxPosition, long maxWhitespaceValue){
		int startValue = 0;
		int endValue = 0;
		
		boolean foundTopCrop = false;
		boolean foundBottomCrop = false;
		
		for (int position = 0; position < maxPosition && !foundTopCrop; position++){
			if (reader.readRgbValue(original, position) > maxWhitespaceValue){
				foundTopCrop = true;
				startValue = position;
			}
		}
		for (int position = maxPosition - 1; position >= 0 && !foundBottomCrop; position--){
			if (reader.readRgbValue(original, position) > maxWhitespaceValue){
				foundBottomCrop = true;
				endValue = position;
			}
		}
		
		return new ValueRange(startValue, endValue + 5);
	}
	
	private RgbLimitSet getRgbValueLimits(RgbValueReader reader, BufferedImage img, int maxPosition){
		long lowest = reader.readRgbValue(img, 0);
		long highest = lowest;
		
		for (int position = 1; position < maxPosition; position++){
			long nextRgbValue = reader.readRgbValue(img, position);
			
			if (nextRgbValue < lowest){
				lowest = nextRgbValue;
			}
			if (nextRgbValue > highest){
				highest = nextRgbValue;
			}
		}
		
		return new RgbLimitSet(lowest, highest);
	}
	
	
	public List<BufferedImage> splitIntoLines(BufferedImage document){
		List<ValueRange> lineValues = new LinkedList<ValueRange>();
		RgbValueReader reader = RgbValueReader.ROW_VALUE_READER;
		boolean isInLine = false;
		
		int nextStartValue = 0;
		int nextEndValue = 0;
		
		for (int row = 0; row < document.getHeight(); row++){
			long nextRgbValue = reader.readRgbValue(document, row);
			
			if (!isInLine && nextRgbValue > maxRowWhitespaceValue){
				nextStartValue = row;
				isInLine = true;
			}
			else if (isInLine && nextRgbValue <= maxRowWhitespaceValue){
				nextEndValue = row;
				
				int cropStart = (nextStartValue - SPLIT_PIXEL_BUFFER >= 0) ? nextStartValue - SPLIT_PIXEL_BUFFER : 0;
				int cropEnd = (nextEndValue + SPLIT_PIXEL_BUFFER < document.getHeight()) ? nextEndValue + SPLIT_PIXEL_BUFFER : document.getHeight() - 1;
				
				ValueRange nextLineValues = new ValueRange(cropStart, 
						cropEnd);
				lineValues.add(nextLineValues);
				isInLine = false;
			}
		}
		
		if (lineValues.size() == 0){
			lineValues.add(new ValueRange(0, document.getHeight()));
		}
		
		List<BufferedImage> croppedImages = convertCropValuesToImages(lineValues, document,
				ImageCropper.TOP_BOTTOM_CROPPER);
		List<BufferedImage> trimmedImages = new LinkedList<BufferedImage>();
		
		for (BufferedImage nextImage : croppedImages){
			trimmedImages.add(trimLeftRightMargins(nextImage));
		}
		
		return trimmedImages;
	}
	
	private List<BufferedImage> convertCropValuesToImages(
			List<ValueRange> lineValues, BufferedImage image, ImageCropper cropper) {
		List<BufferedImage> croppedImages = new LinkedList<BufferedImage>();
		
		for (ValueRange nextSet : lineValues){
			int valueDifference = nextSet.getEndValue() - nextSet.getStartValue();
			
			BufferedImage nextCroppedImage = cropper.cropWithValues(nextSet.getStartValue(), 
					valueDifference, image);
			
			croppedImages.add(nextCroppedImage);
		}
		
		return croppedImages;
	}

	
	
	public List<BufferedImage> splitIntoCharacters(BufferedImage line){
		List<ValueRange> characters = new LinkedList<ValueRange>();
		RgbValueReader reader = RgbValueReader.COLUMN_VALUE_READER;
		int startValue = 0;
		boolean reachedCharacter = false;
		
		RgbLimitSet colLimits = 
				getRgbValueLimits(RgbValueReader.COLUMN_VALUE_READER, line, line.getWidth());
		long maxWhitespaceColValue = 
				(long) (colLimits.getLowestRgbValue() + (BETWEEN_CHAR_WHITESPACE_MARGIN * colLimits.getRgbRange()));	
		
		double averageWhitespaceLength = calculateAverageWhitespace(line, reader, maxWhitespaceColValue);

		
		for (int col = 0; col < line.getWidth(); col++){
			long rgbValue = reader.readRgbValue(line, col);
			
			if (rgbValue <= maxWhitespaceColValue && reachedCharacter){				
				ValueRange nextSet = new ValueRange(startValue, col);
				characters.add(nextSet);
				reachedCharacter = false;
				startValue = col;
			}
			else if (rgbValue > maxWhitespaceColValue && !reachedCharacter){
				reachedCharacter = true;
				
				if (col - startValue > averageWhitespaceLength){
					ValueRange nextSet = new ValueRange(startValue, col);
					characters.add(nextSet);
					startValue = col;
				}
			}
		}
		
		ValueRange nextSet = new ValueRange(startValue, line.getWidth() - 1);
		
		if (nextSet.getEndValue() > nextSet.getStartValue()){
			characters.add(nextSet);
		}
		
		return convertCropValuesToImages(characters, line, ImageCropper.LEFT_RIGHT_CROPPER);
	}

	private double calculateAverageWhitespace(BufferedImage line,
			RgbValueReader reader, long maxWhitespaceColValue) {
		int whitespaceStart = 0;
		int totalWhitespace = 0;
		int numberWhitespaces = 0;
		boolean isInWhiteSpace = false;
		for (int col = 0; col < line.getWidth(); col++){
			long rgbValue = reader.readRgbValue(line, col);
			
			if (rgbValue <= maxWhitespaceColValue && !isInWhiteSpace){
				isInWhiteSpace = true;
				whitespaceStart = col;
			}
			else if (rgbValue > maxWhitespaceColValue && isInWhiteSpace){
				isInWhiteSpace = false;
				int nextWhitespaceLength = col - whitespaceStart;
				totalWhitespace += nextWhitespaceLength;
				numberWhitespaces++;
			}
		}
		return (double)totalWhitespace / numberWhitespaces;
	}
}
