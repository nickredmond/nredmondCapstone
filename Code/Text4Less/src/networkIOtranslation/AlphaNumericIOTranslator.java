package networkIOtranslation;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;

import app.AlphaNumericCharacterConverter;

public class AlphaNumericIOTranslator implements INetworkIOTranslator{
	private final float THRESHOLD = 0.5f;

	@Override
	public TranslationResult translateNetworkOutputToCharacter(float[] output) {
		int characterClass = -1;
		float maxConfidence = -2.0f;
		char result = ' ';
		
		for (int i = 0; i < output.length; i++){
			if (output[i] > maxConfidence){
				characterClass = i;
				maxConfidence = output[i];
			}
		}
		
		AlphaNumericCharacterConverter converter = new AlphaNumericCharacterConverter();
		
		try {
			result = converter.convertClassNumberToCharacter(characterClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new TranslationResult(result, maxConfidence);
	}

	@Override
	public int[] translateCharacterToNetworkOutput(char c) {
		int classNumber = 32;
		AlphaNumericCharacterConverter converter = new AlphaNumericCharacterConverter();
		
		try {
			classNumber = converter.convertCharacterToClassNumber(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int[] output = new int[AlphaNumericCharacterConverter.NUMBER_CLASSES];
		
		output[classNumber] = 1;
		
		return output;
	}
	
	public int getInputLength(){
		return new FeatureExtractionIOTranslator().getInputLength();
	}

	@Override
	public float[] translateImageToNetworkInput(BufferedImage img) {
		return new FeatureExtractionIOTranslator().translateImageToNetworkInput(img);
	}

}