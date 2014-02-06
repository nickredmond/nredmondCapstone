package app;

import imageProcessing.TranslationResult;
import io.CharacterType;
import io.TrainingDataReader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import neuralNetwork.CharacterTrainingExample;
import debug.FeatureExtractionDebug;

public class LeastDistanceHandler implements ICharacterImageHandler {
	private final float MIN_CONFIDENCE = 0.4f;
	private final CharacterType TRAINING_TYPE = CharacterType.ASCII;
	
	private List<CharacterResult> results;
	private Set<CharacterTrainingExample> examples;
	
	public LeastDistanceHandler(List<CharacterResult> results){
		this.results = results;
		
		try {
			examples = TrainingDataReader.createTrainingSetFromFile(TRAINING_TYPE);
		} catch (IOException e) {
			System.out.println("FAILED TO CREATE TRAINING SET.");
			e.printStackTrace();
		}
	}
	
	public LeastDistanceHandler(List<CharacterResult> results, Set<CharacterTrainingExample> examples){
		this.results = results;
		this.examples = examples;
	}
	
	public void handleImage(BufferedImage img, int index){
		char chosenCharacter = 'A';
		float highestCorrelation = -1.0f;
		boolean found = false;
		
		Iterator<CharacterTrainingExample> iter = examples.iterator();
		while(iter.hasNext() && !found){
			CharacterTrainingExample nextExample = iter.next();
			
			BufferedImage nextTrainingImg = nextExample.getCharacterImage();
			float nextCorrelation = FeatureExtractionDebug.getCorrelation(img, nextTrainingImg);
			
			if (nextCorrelation > highestCorrelation || highestCorrelation < 0){
				highestCorrelation = nextCorrelation;
				chosenCharacter = nextExample.getCharacterValue();
			}
			
			found = (highestCorrelation >= 0.99f);
		}
		
		CharacterResult result = new CharacterResult(img, new TranslationResult(chosenCharacter, highestCorrelation));
		
		if (highestCorrelation < MIN_CONFIDENCE){
			result.setRejected(true);
		}
		//System.out.println("char: " + chosenCharacter + ", highestCorrelation: " + highestCorrelation + ", i: " + index);
		
		if (index == -1){
			results.add(result);
		}
		else{
			while (index > results.size()){
				results.add(null);
			}
			
			results.add(index, result);
			
			if (index < results.size() - 1){
				results.remove(index+1);
			}
		}
	}
	
	@Override
	public void handleImage(BufferedImage img) {
		char chosenCharacter = 'A';
		float highestCorrelation = -1.0f;
		
		for (CharacterTrainingExample nextExample : examples){
			BufferedImage nextTrainingImg = nextExample.getCharacterImage();
			float nextCorrelation = FeatureExtractionDebug.getCorrelation(img, nextTrainingImg);
			
			if (nextCorrelation > highestCorrelation || highestCorrelation < 0){
				highestCorrelation = nextCorrelation;
				chosenCharacter = nextExample.getCharacterValue();
			}
		}
		
		TranslationResult result = new TranslationResult(chosenCharacter, highestCorrelation);
		System.out.println("char: " + chosenCharacter + ", highestCorrelation: " + highestCorrelation);
	}

}