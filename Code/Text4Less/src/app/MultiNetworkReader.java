package app;

import imageProcessing.TranslationResult;
import io.CharacterType;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.FeatureExtractionIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;

public class MultiNetworkReader {	
	public static String getTextFromImage(BufferedImage img, CharacterType[] types) throws IOException{
		//String[] results = new String[types.length];
		List<List<CharacterResult>> results = new ArrayList<List<CharacterResult>>();
		
		INetworkIOTranslator translator = new AlphaNumericIOTranslator();
		INeuralNetwork network = new MatrixNeuralNetwork(((AlphaNumericIOTranslator)translator).getInputLength(), 1, 100, 7, true);
		
		for (int i = 0; i < types.length; i++){
			INeuralNetwork trainedNetwork = NetworkFactory.getTrainedNetwork(network, translator, types[i], new MatrixBackpropTrainer(0.05f, 0.02f));
			ImageReader reader = new ImageReader(trainedNetwork, translator);
			results.add(reader.readTextFromImage(img));
			
			// test code //
//			JFrame frame = new JFrame();
//			frame.add(new RejectedImagesPanel(reader.getRejectedImages()));
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.setVisible(true);
		}
		
		return getMultiNetworkString(results);
	}
	
	public static String getMultiNetworkString(List<List<CharacterResult>> results){ // networks<lines<chars<result>>>
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
			System.out.print("GUESSES: ");
			
			for (int networkIndex = 0; networkIndex < results.size() && !isNewLine; networkIndex++){
				TranslationResult nextResult = results.get(networkIndex).get(charIndex).getResult();
				
				System.out.print(nextResult.toString() + " -- ");
				
				if (nextResult.toString().equals(TranslationResult.NEWLINE_VALUE)){
					builder.append("\r\n");
				}
				else if (nextResult.getConfidence() > maxConfidence){
					bestChar = nextResult.getCharacter();
					maxConfidence = nextResult.getConfidence();
				}
			}
			
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
