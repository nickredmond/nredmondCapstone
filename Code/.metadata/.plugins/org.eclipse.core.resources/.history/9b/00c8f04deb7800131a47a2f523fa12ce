package imageProcessing;

import io.CharacterUnicodeReader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

public class NetworkIOTranslator {
	private final int IMAGE_DIMENSION = 30;
	private final int MAX_RGB_VALUE = -16777216;
	
	private final int MAX_BINARY_INDEX = 7;
	
	public int[] translateCharacterToNetworkOutput(char c){
		int unicodeValue = (int)c;
		return translateIntToBinary128(unicodeValue);
	}
	
	public char translateNetworkOutputToCharacter(int[] output){
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
		float[] networkInput = new float[IMAGE_DIMENSION * IMAGE_DIMENSION];		
		float scaleValueX = (float)IMAGE_DIMENSION / img.getWidth();
		float scaleValueY = (float)IMAGE_DIMENSION / img.getHeight();
		
		int currentInputIndex = 0;
		
		for (float x = scaleValueX; x < img.getWidth() && 
				currentInputIndex < networkInput.length; x += scaleValueX){
			for (float y = scaleValueY; y < img.getHeight() && 
					currentInputIndex < networkInput.length; y += scaleValueY){
				int xPos = (int) x;
				int yPos = (int) y;
				
				networkInput[currentInputIndex] = img.getRGB(xPos, yPos);
				currentInputIndex++;
			}
		}
		
		return networkInput;
	}
}
