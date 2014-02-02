package app;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.util.List;

import neuralNetwork.CharacterReader;

public class TranslationHandler implements ICharacterImageHandler {
	private List<TranslationResult> results;
	private CharacterReader reader;
	
	public TranslationHandler(List<TranslationResult> results, CharacterReader reader){
		this.results = results;
		this.reader = reader;
	}
	
	@Override
	public void handleImage(BufferedImage img) {
		TranslationResult translation = reader.readCharacter(img);
		results.add(translation);
	}

}
