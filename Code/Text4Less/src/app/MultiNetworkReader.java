package app;

import imageProcessing.FeatureExtractionIOTranslator;
import imageProcessing.INetworkIOTranslator;
import imageProcessing.TranslationResult;
import io.CharacterType;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;

public class MultiNetworkReader {
	private static final String NEWLINE = "\r\n";
	
	public static String getTextFromImage(BufferedImage img, CharacterType[] types) throws IOException{
		//String[] results = new String[types.length];
		List<List<TranslationResult>> results = new ArrayList<List<TranslationResult>>();
		
		INetworkIOTranslator translator = new FeatureExtractionIOTranslator();
		INeuralNetwork network = new MatrixNeuralNetwork(((FeatureExtractionIOTranslator)translator).getInputLength(), 1, 160, 7, true);
		
		for (int i = 0; i < types.length; i++){
			INeuralNetwork trainedNetwork = NetworkFactory.getTrainedNetwork(network, translator, types[i], new MatrixBackpropTrainer(0.05f, 0.02f));
			ImageReader reader = new ImageReader(trainedNetwork, translator);
			results.add(reader.readTextFromImage(img));
		}
		
		return getMultiNetworkString(results);
	}
	
	public static String getMultiNetworkString(List<List<TranslationResult>> results){ // networks<lines<chars<result>>>
		StringBuilder builder = new StringBuilder();
		int numberCharacters = 0;
		
//		for (TranslationResult nextChar : results.get(0)){
//			if (!nextChar.toString().equals(NEWLINE)){
//				numberCharacters++;
//			}
//		}
		
		boolean isNewLine = false;
		
		for (int charIndex = 0; charIndex < results.get(0).size(); charIndex++){
			isNewLine = false;
			float maxConfidence = 0.0f;
			char bestChar = ' ';
		//	System.out.print("GUESSES: ");
			
			for (int networkIndex = 0; networkIndex < results.size() && !isNewLine; networkIndex++){
				TranslationResult nextResult = results.get(networkIndex).get(charIndex);
			//	System.out.println(nextResult.toString() + " -- ");
				
				if (nextResult.toString().equals(NEWLINE)){
					builder.append("\r\n");
				}
				else if (nextResult.getConfidence() > maxConfidence){
					bestChar = nextResult.getResult();
					maxConfidence = nextResult.getConfidence();
				}
			}
		//	System.out.println();
			
			if (!isNewLine){
				builder.append(bestChar);
			}
		}
		
//		for (int i = 0; i < numberCharacters; i++){
//			Map<Character, Integer> charOccurrences = new HashMap<Character, Integer>();
//			
//			System.out.print("GUESSES: ");
//			for (int j = 0; j < results.size(); j++){
//				char nextGuess = results[j].charAt(i);
//				System.out.print(nextGuess + " ");
//				int occurrence = (charOccurrences.get(nextGuess) != null) ? charOccurrences.get(nextGuess) : 0;
//				
//				charOccurrences.put(nextGuess, occurrence + 1);
//			}
//			System.out.println();
//			
//			int maxOccurrence = 0;
//			char decidedChar = ' ';
//			
//			for (Character nextChar : charOccurrences.keySet()){
//				if (charOccurrences.get(nextChar) > maxOccurrence){
//					decidedChar = nextChar;
//				}
//			}
//			
//			builder.append(decidedChar);
//		}
		
		return builder.toString();
	}
}
