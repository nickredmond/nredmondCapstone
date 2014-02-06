package networkIOtranslation;

import imageProcessing.ImageScaler;
import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NetworkIOTranslator implements INetworkIOTranslator {
	private final int IMAGE_DIMENSION = 8;
	private final int MAX_RGB_VALUE = -16777216 / 1;
	
	private final int MAX_BINARY_INDEX = 7;
	public static final int WHITE_RGB_VALUE = -1;
	private static final int BLACK_RGB_VALUE = -16777216;
	
	public static final int BLACK_RGB = (int) (BLACK_RGB_VALUE * 0.5);
	
	private final int BLACK_TOLERANCE = 50000;
	
	private final float CERTAINTY_VALUE = 0.5f;
	
	public int[] translateCharacterToNetworkOutput(char c){
		int unicodeValue = (int)c;
		return translateIntToBinary128(unicodeValue);
	}
	
	public TranslationResult translateNetworkOutputToCharacter(float[] output){
		int[] normalizedOutput = new int[output.length];
		float confidenceSum = 0.0f;
		
		for (int i = 0; i < output.length; i++){
			normalizedOutput[i] = (output[i] > CERTAINTY_VALUE) ? 1 : 0;
			confidenceSum += (output[i] > CERTAINTY_VALUE) ? (1.0f - output[i]) : (output[i]);
		}
		
		float confidence = ((float)output.length - confidenceSum) / output.length;
		char resultChar = translateNetworkOutputToCharacter(normalizedOutput);
		
		return new TranslationResult(resultChar, confidence);
	}
	
	private char translateNetworkOutputToCharacter(int[] output){
		int unicodeValue = translateBinary128ToInt(output);
		return (char)unicodeValue;
	}
	
	private int[] translateIntToBinary128(int value){
		int remainder = value;
		int[] binary = new int[MAX_BINARY_INDEX];
		
		for (int i = binary.length; i >= -1; i--){
			int binaryIndexValue = (int)Math.pow(2, i + 1);
			
			if (remainder / binaryIndexValue > 0){
				binary[binary.length - i - 2] = 1;
			}
			
			remainder = remainder % binaryIndexValue;
		}
		
		return binary;
	}
	
	private int translateBinary128ToInt(int[] value){
		if (value.length != MAX_BINARY_INDEX){
			throw new IllegalArgumentException("Array must be of length " + MAX_BINARY_INDEX);
		}
		
		int result = 0;
		
		for (int i = 0; i < value.length; i++){
			if (value[i] == 1){
				int binaryPower = MAX_BINARY_INDEX - i - 1;
				int indexValue = (int)Math.pow(2, binaryPower);
				result += indexValue;
			}
		}
		
		return result;
	}
	
	public float[] translateImageToNetworkInput(BufferedImage img){
		float[] networkInput = scaleToDimension(img);
		float[] normalizedInput = normalizeNetworkInput(networkInput);
		
		return normalizedInput;
	}

	private float[] normalizeNetworkInput(float[] networkInput) {
		float[] normalizedInput = networkInput;
		
		for (int i = 0; i < networkInput.length; i++){
			normalizedInput[i] = normalizedInput[i] / (float)MAX_RGB_VALUE;
		}
		
		return normalizedInput;
	}

	private float[] scaleToDimension(BufferedImage img) {
         BufferedImage scaledImg = ImageScaler.scaleToHeight(IMAGE_DIMENSION, img);
         return converScaledImageToNetworkInput(scaledImg);
	}
	
	private BufferedImage binarizeImage(BufferedImage scaledImg) {
		BufferedImage binarizedImage = scaledImg;
		
		for(int x = 0; x < scaledImg.getWidth(); x++){
			for (int y = 0; y < scaledImg.getHeight(); y++){
				int rgb = scaledImg.getRGB(x, y);
				if (rgb != WHITE_RGB_VALUE){
					binarizedImage.setRGB(x, y, BLACK_RGB_VALUE);
				}
			}
		}
		
		return binarizedImage;
	}

	private float[] binaryScaleToDimension(BufferedImage img) {			
		BufferedImage scaledImg = new BufferedImage(IMAGE_DIMENSION, IMAGE_DIMENSION, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < IMAGE_DIMENSION; x++){
			for (int y = 0; y < IMAGE_DIMENSION; y++){
				int nextPixelValue = calculatePixelValue(x, y, img);
				scaledImg.setRGB(x, y, nextPixelValue);
			}
		}
		
//		try {
//			ImageIO.write(scaledImg, "jpg", new File("C:\\Users\\nredmond\\Workspaces\\CapstoneNickRedmond\\Code\\Text4Less\\trainingImages\\ASCII2\\" + System.nanoTime() + ".jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return converScaledImageToNetworkInput(scaledImg);
	}
	
	private boolean isDark(int rgb){
		return rgb < BLACK_RGB_VALUE + BLACK_TOLERANCE;
	}

	private int calculatePixelValue(int x, int y, BufferedImage img) {
		double scaleFactorX = (double)img.getWidth() / IMAGE_DIMENSION;
		double scaleFactorY = (double)img.getHeight() / IMAGE_DIMENSION;
		
		int startingX = (int) (x * Math.floor(scaleFactorX));
		int startingY = (int) (y * Math.floor(scaleFactorY));
		
		boolean foundBlack = false;
		int pixelValue = WHITE_RGB_VALUE;
		int numPixelsRequired = (int) ((scaleFactorX * scaleFactorY) / 4);
		int numPixelsFound = 0;
		
		for (int currentX = startingX; currentX < startingX + scaleFactorX && !foundBlack; currentX++){
			for (int currentY = startingY; currentY < startingY + scaleFactorY && !foundBlack; currentY++){
				int nextRgb = img.getRGB(currentX, currentY);
				
				if (isDark(nextRgb)){
					numPixelsFound++;
					
					if (numPixelsFound >= numPixelsRequired){
					foundBlack = true;
					pixelValue = BLACK_RGB_VALUE;
					}
				}
			}
		}
		
		return pixelValue;
	}

	private float[] converScaledImageToNetworkInput(BufferedImage scaledImg) {
		float[] networkInput = new float[IMAGE_DIMENSION * IMAGE_DIMENSION];	
		int currentInputIndex = 0;
		
		for (int x = 0; x < scaledImg.getWidth(); x++){
			for (int y = 0; y < scaledImg.getHeight(); y++){
				networkInput[currentInputIndex] = scaledImg.getRGB(x, y);
				currentInputIndex++;
			}
		}
		
		return networkInput;
	}

	private BufferedImage scaleToProperHeight(BufferedImage img, float scaleValue, int startingX) {
		BufferedImage scaledImg = new BufferedImage(IMAGE_DIMENSION, IMAGE_DIMENSION, BufferedImage.TYPE_INT_RGB);
		int currentScaledImgX = startingX;
		int currentScaledImgY;
		
		if (currentScaledImgX < 0){
			currentScaledImgX = 0;
		}
		
		for (float x = 0; x < img.getWidth(); x += scaleValue){
			currentScaledImgY = 0;
			
			for (float y = 0; y < img.getHeight(); y += scaleValue){
				int xPos = (int) x;
				int yPos = (int) y;
				
				if (isValidPoint(currentScaledImgX, currentScaledImgY, scaledImg)){
					scaledImg.setRGB(currentScaledImgX, currentScaledImgY, img.getRGB(xPos, yPos));
				}
				
				currentScaledImgY++;
			}
			currentScaledImgX++;
		}
		return scaledImg;
	}
	
	private static boolean isValidPoint(int x, int y, BufferedImage img){
		return (x < img.getWidth() && y < img.getHeight());
	}
}