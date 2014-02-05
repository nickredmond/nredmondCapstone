package debug;

import imageProcessing.ImagePreprocessor;
import imageProcessing.NewLineTranslationResult;
import io.CharacterType;
import io.TrainingDataReader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import app.CharacterResult;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterTrainingExample;

public class CorrelationDebug {
	public static void correlateAgainstTestCharacters(String expectedText, BufferedImage testImage){		
		int index = 0;
		
		Set<CharacterTrainingExample> examples = null;
		try {
			examples = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII);
		} catch (IOException e) {
			System.out.println("FAILED TO CREATE TRAINING SET.");
			e.printStackTrace();
		}
		
		int numRight = 0;
		int totalComparisons = 0;
		
		ImagePreprocessor processor = new ImagePreprocessor();
		BufferedImage trimmedImage = processor.trimMargins(testImage);
		
		List<BufferedImage> lines = processor.splitIntoLines(trimmedImage);
		
		for (BufferedImage nextLine : lines){
			List<BufferedImage> characters = processor.splitIntoCharacters(nextLine);
			
			for (BufferedImage nextCharacter : characters){
				while(expectedText.charAt(index) == ' '){
					index++;
				}
				
				char chosenCharacter = 'A';
				float highestCorrelation = -1.0f;
				System.out.println("--- SHOULD BE '" + expectedText.charAt(index) + "'");
				
				Iterator<CharacterTrainingExample> iter = examples.iterator();
				while(iter.hasNext()){
					CharacterTrainingExample nextExample = iter.next();
					
					BufferedImage nextTrainingImg = nextExample.getCharacterImage();
					float nextCorrelation = FeatureExtractionDebug.getCorrelation(nextCharacter, nextTrainingImg);
					
					if (nextExample.getCharacterValue() == expectedText.charAt(index)){
						System.out.println("correlation between same chars: " + nextCorrelation);
					}
					
					if (nextCorrelation > highestCorrelation || highestCorrelation < 0){
						highestCorrelation = nextCorrelation;
						chosenCharacter = nextExample.getCharacterValue();
					}
				}
				if (chosenCharacter != ' '){
					if (chosenCharacter == expectedText.charAt(index)){ numRight++;}
					else{System.out.println("bestChar: " + chosenCharacter + " with " + highestCorrelation + " correlation");}
					totalComparisons++;
					index++;
				}
			}
		}
		System.out.println("\r\nACCURACY: " + ((float)numRight / totalComparisons));
	}
	
	private boolean isSpace(BufferedImage image, INetworkIOTranslator translator){
		float[] input = translator.translateImageToNetworkInput(image);
		boolean isSpace = true;
		
		for (int i = 0; i < input.length && isSpace; i++){
			isSpace = input[i] == 0;
		}
		
		return isSpace;
	}
}
