package app;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import neuralNetwork.CharacterReader;

public class NeuralNetworkHandler implements ICharacterImageHandler {
	private final float MIN_CONFIDENCE = 0.4f;
	private List<CharacterResult> results;
	private CharacterReader reader;
	
	public NeuralNetworkHandler(List<CharacterResult> results, CharacterReader reader){
		this.results = results;
		this.reader = reader;
	}
	
	@Override
	public void handleImage(BufferedImage img) {
		CharacterResult translation = reader.readCharacter(img);
		
		if (translation.getResult().getConfidence() < MIN_CONFIDENCE){
			translation.setRejected(true);
		}
		
		results.add(translation);
	}

}
