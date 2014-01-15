package imageProcessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NetworkIOTranslator implements INetworkIOTranslator {
	private final int IMAGE_DIMENSION = 8;
	private final int MAX_RGB_VALUE = -16777216 / 1;
	
	private final int MAX_BINARY_INDEX = 7;
	private final int WHITE_RGB_VALUE = -1;
	private final int BLACK_RGB_VALUE = -16777216;
	
	private final int BLACK_RGB = (int) (BLACK_RGB_VALUE * 0.5);
	
	private final int BLACK_TOLERANCE = 50000;
	
	private final float CERTAINTY_VALUE = 0.5f;
	
	public int[] translateCharacterToNetworkOutput(char c){
		int unicodeValue = (int)c;
		return translateIntToBinary128(unicodeValue);
	}
	
	public char translateNetworkOutputToCharacter(float[] output){
		int[] normalizedOutput = new int[output.length];
		
		for (int i = 0; i < output.length; i++){
			normalizedOutput[i] = (output[i] > CERTAINTY_VALUE) ? 1 : 0;
		}
		
		return translateNetworkOutputToCharacter(normalizedOutput);
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
	
	private boolean isValidPoint(int x, int y, BufferedImage img){
		return (x < img.getWidth() && y < img.getHeight());
	}

	private float[] scaleToDimension(BufferedImage img) {
         BufferedImage trimmedImg = trimImage(img);
         
		 float scaleValue = (float) trimmedImg.getHeight() / IMAGE_DIMENSION;
         int newWidth = (int) ((1 / scaleValue) * trimmedImg.getWidth());
         int startingX = (IMAGE_DIMENSION - newWidth) / 2;
         
         BufferedImage scaledImg = scaleToProperHeight(trimmedImg, scaleValue, startingX);
         
         for (int x = 0; x < startingX; x++){
             for (int y = 0; y < scaledImg.getHeight(); y++){
                     scaledImg.setRGB(x, y, WHITE_RGB_VALUE);
             }
         }
     
	     for (int x = (startingX + newWidth + 1); x < scaledImg.getWidth(); x++){
             for (int y = 0; y < scaledImg.getHeight(); y++){
                     scaledImg.setRGB(x, y, WHITE_RGB_VALUE);
             }
	     }

//         try {
//                 ImageIO.write(scaledImg, "jpg", new File("C:\\Users\\nredmond\\Workspaces\\CapstoneNickRedmond\\Code\\Text4Less\\trainingImages\\ASCII2\\" + System.nanoTime() + ".jpg"));
//         } catch (IOException e) {
//                 // TODO Auto-generated catch block
//                 e.printStackTrace();
//         }
         
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
	
	private BufferedImage trimImage(BufferedImage img){
		int left = getLeft(img);
		int right = getRight(img);
		int top = 0;
		int bottom = 0;
		
		right = (right == 0) ? img.getWidth() : right;
		bottom = (bottom == 0) ? img.getHeight() : bottom;
		
		left = (left == right) ? 0 : left;
		top = (top == bottom) ? 0 : top;
		
		return img.getSubimage(left, top, right - left, bottom - top);
	}
	
	private int getTop(BufferedImage img){
		int top = 0;
		boolean foundTop = false;
		
		for (int y = 0; y < img.getHeight() && !foundTop; y++){
			for (int x = 0; x < img.getWidth() && !foundTop; x++){
				if (img.getRGB(x, y) < BLACK_RGB){
					top = y;
					foundTop = true;
				}
			}
		}
		
		return top;
	}
	
	private int getRight(BufferedImage img){
		int right = img.getWidth();
		boolean foundRight = false;
		
		for (int x = img.getWidth() - 1; x >= 0 && !foundRight; x--){
			for (int y = 0; y < img.getHeight() && !foundRight; y++){
				if (img.getRGB(x, y) < BLACK_RGB){
					right = x;
					foundRight = true;
				}
			}
		}
		
		return right;
	}
	
	private int getLeft(BufferedImage img){
		int left = 0;
		boolean foundLeft = false;
		
		for (int x = 0; x < img.getWidth() && !foundLeft; x++){
			for (int y = 0; y < img.getHeight() && !foundLeft; y++){
				if (img.getRGB(x, y) < BLACK_RGB){
					left = x;
					foundLeft = true;
				}
			}
		}
		
		return left;
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
}
