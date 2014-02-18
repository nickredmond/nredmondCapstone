package app;

import imageHandling.ImageHandlerFactory;
import imageHandling.ImageReadMethod;
import imageProcessing.TranslationResult;
import io.NeuralNetworkIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.INeuralNetwork;

public class InputReader {
	public static final String TRAINED_NETWORK_NAME = "testThis";
	private static final String[] DEFAULT_NETWORKS = {"yoloSwaggins", "yoloSwaggins2"};
	
	private static INeuralNetwork currentNetwork;
	
	public static void setNetwork(INeuralNetwork network){
		currentNetwork = network;
	}
	
	public static ReadResult readImageInput(BufferedImage image, List<ImageReadMethod> readMethods) throws IOException{
		List<CharacterResult> nnTranslation = new ArrayList<CharacterResult>();
		List<CharacterResult> ldTranslation = new ArrayList<CharacterResult>();
		
		List<CharacterResult> finalTranslation = null;//new ArrayList<CharacterResult>();
		
		INetworkIOTranslator translator = new AlphaNumericIOTranslator();
		
		if (readMethods.contains(ImageReadMethod.NEURAL_NETWORK)){
			ImageHandlerFactory.setHandlerMethod(ImageReadMethod.NEURAL_NETWORK);
						
			if (currentNetwork == null){	
				float maxConfidence = 0.0f;
				List<CharacterResult> translation = null;
				
				for (int i = 0; i < DEFAULT_NETWORKS.length; i++){
					INeuralNetwork nextNetwork = NeuralNetworkIO.readNetwork(DEFAULT_NETWORKS[i]);
					ImageReader reader = new ImageReader(nextNetwork, translator);
					List<CharacterResult> results = reader.readTextFromImage(image);
					
					float totalConfidence = 0.0f;
					
					for (CharacterResult nextResult : results){
						totalConfidence += nextResult.getResult().getConfidence();
					}
					
					float avgConfidence = totalConfidence / results.size();
					if (avgConfidence > maxConfidence){
						translation = results;
						maxConfidence = avgConfidence;
					}
					
					System.out.println("avg: " + avgConfidence + " " + DEFAULT_NETWORKS[i]);
				}
				
				if (translation == null){
					Logger.logMessage("Most confident translation was not set during default multi-network reading (Source: InputReader).");
				}
				else{
					nnTranslation = translation;
					finalTranslation = translation;
				}
			}
			else{
				ImageReader reader = new ImageReader(currentNetwork, translator);
				
				nnTranslation = reader.readTextFromImage(image);
				finalTranslation = nnTranslation;
			}
		}
		if (readMethods.contains(ImageReadMethod.LEAST_DISTANCE)){
			ImageHandlerFactory.setHandlerMethod(ImageReadMethod.LEAST_DISTANCE);
			ImageReader reader = new ImageReader(translator);

			ldTranslation = reader.readTextFromImage(image);
			finalTranslation = ldTranslation;
		}
		if (readMethods.contains(ImageReadMethod.NEURAL_NETWORK) && readMethods.contains(ImageReadMethod.LEAST_DISTANCE)){
			int index = 0;
			finalTranslation = new ArrayList<CharacterResult>();
			
			for (CharacterResult nextResult : nnTranslation){
				if (nextResult.isRejected()){
					finalTranslation.add(ldTranslation.get(index));
				}
				else finalTranslation.add(nnTranslation.get(index));
				index++;
			}
		}
		
		float confidence = 0.0f;
		for (CharacterResult nextResult : finalTranslation){
			confidence += nextResult.getResult().getConfidence();
		}
		confidence /= finalTranslation.size();
		
		System.out.println("Confidence: " + confidence);
		
		return convertTranslationToResult(finalTranslation);
	}
	
	private static ReadResult convertTranslationToResult(
			List<CharacterResult> translation) {
		String resultString = "";
		ReadResult result = new ReadResult();
		
		for (CharacterResult nextResult : translation){
			String nextTranslationString = (nextResult.getResult() == null) ? " " : nextResult.getResult().toString();
			String newline = TranslationResult.NEWLINE_VALUE;
			
			if (nextTranslationString.equals(newline)){
				resultString += newline;
			}
			else if (nextResult.isRejected()){
				result.addRejection(nextResult);
			}
			else resultString += nextResult.getResult().getCharacter();
		}
		
		result.setTranslationString(resultString);
		
		return result;
	}

	public static ReadResult readImageInputFromNeuralNetwork(BufferedImage image, INeuralNetwork network) throws IOException{
		ImageHandlerFactory.setHandlerMethod(ImageReadMethod.NEURAL_NETWORK);
		INetworkIOTranslator translator = new AlphaNumericIOTranslator();
		ImageReader reader = new ImageReader(network, translator);
		
		List<CharacterResult> nnTranslation = reader.readTextFromImage(image);
		
		return convertTranslationToResult(nnTranslation);
	}
}
