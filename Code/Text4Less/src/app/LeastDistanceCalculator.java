package app;

import java.awt.image.BufferedImage;

import networkIOtranslation.FeatureExtractionIOTranslator;
import networkIOtranslation.INetworkIOTranslator;

public class LeastDistanceCalculator {
	public static float getCorrelation(BufferedImage first, BufferedImage second){
		final float MAX_DIFFERENCE = 1.0f;
		INetworkIOTranslator translator = new FeatureExtractionIOTranslator();
		
		float[] input1 = translator.translateImageToNetworkInput(first);
		float[] input2 = translator.translateImageToNetworkInput(second);
		
		float differenceSum = 0.0f;
		
		for (int i = 0; i < input1.length; i++){
			differenceSum += Math.abs(input1[i] - input2[i]);
		}
		
		float differenceAvg = differenceSum / input1.length;
		return MAX_DIFFERENCE - differenceAvg;
	}
	
	public static float getEuclideanDistance(BufferedImage first, BufferedImage second){
		INetworkIOTranslator translator = new FeatureExtractionIOTranslator();
		
		float[] input1 = translator.translateImageToNetworkInput(first);
		float[] input2 = translator.translateImageToNetworkInput(second);
		
		float differenceSum = 0.0f;
		
		for (int i = 0; i < input1.length; i++){
			float delta = input1[i] - input2[i];
			differenceSum += (delta * delta);
		}
		
		return (float)Math.sqrt(differenceSum);
	}
}
