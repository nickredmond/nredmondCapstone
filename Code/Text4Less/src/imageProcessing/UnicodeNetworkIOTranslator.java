package imageProcessing;

import java.awt.image.BufferedImage;

public class UnicodeNetworkIOTranslator implements INetworkIOTranslator {
	private final int NUMBER_UNICODE_VALUES = 128;
	
	@Override
	public char translateNetworkOutputToCharacter(float[] output){
		int maxValueIndex = 0;
		float maxValue = 0.0f;
		
		for (int i = 0; i < output.length; i++){
			if (output[i] > maxValue){
				maxValueIndex = i;
				maxValue = output[i];
			}
		}
		
		int[] normalizedOutput = new int[output.length];
		normalizedOutput[maxValueIndex] = 1;
		
		return translateNetworkOutputToCharacter(normalizedOutput);
	}
	
	private char translateNetworkOutputToCharacter(int[] output) {
		int unicodeValue = 0;
		boolean foundValue = false;
		
		for (int i = 0; i < output.length && !foundValue; i++){
			if(output[i] == 1){
				foundValue = true;
				unicodeValue = i;
			}
		}
		
		return (char)unicodeValue;
	}

	@Override
	public int[] translateCharacterToNetworkOutput(char c) {
		int[] result = new int[NUMBER_UNICODE_VALUES];
		int characterUnicodeValue = (int)c;
		result[characterUnicodeValue] = 1;
		
		return result;
	}

	@Override
	public float[] translateImageToNetworkInput(BufferedImage img) {
		// TODO Auto-generated method stub
		return new NetworkIOTranslator().translateImageToNetworkInput(img);
	}
	
}
