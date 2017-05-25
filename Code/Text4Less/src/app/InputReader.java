package app;

import imageHandling.ImageHandlerFactory;
import imageHandling.ImageReadMethod;
import imageProcessing.TranslationResult;
import io.Logger;
import io.NeuralNetworkIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.INeuralNetwork;

public class InputReader {
	public static final String TRAINED_NETWORK_NAME = "default";
	private static final String[] DEFAULT_NETWORKS = {"endOfLineTestNetArial", "yoloSwaggins2"};
	
	private static INeuralNetwork currentNetwork;
	
	public static void setNetwork(INeuralNetwork network){
		currentNetwork = network;
	}
	
	public static ReadResult readImageInput(BufferedImage image, ImageReadMethod method) throws IOException{	
		ReadResult result = null;
		ImageHandlerFactory.setHandlerMethod(method);
		
		if (method == ImageReadMethod.NEURAL_NETWORK && currentNetwork == null){
			result = readDefaultNetworkInput(image);
		}
		else{
			INetworkIOTranslator translator = new AlphaNumericIOTranslator();
			ImageReader reader = new ImageReader(currentNetwork, translator);
			List<CharacterResult> results = reader.readTextFromImage(image);
			
			result = convertTranslationToResult(results);
		}
		
		return result;
	}
	
	private static ReadResult readDefaultNetworkInput(BufferedImage image) throws IOException{
		float maxConfidence = 0.0f;
		List<CharacterResult> translation = null;
		ReadResult result = null;
		INetworkIOTranslator translator = new AlphaNumericIOTranslator();
		
		for (int i = 0; i < DEFAULT_NETWORKS.length; i++){
			INeuralNetwork nextNetwork = NeuralNetworkIO.readFromFilepath(new Main().getWorkingDirectory() + 
					"/savedNetworks/" + DEFAULT_NETWORKS[i] + ".ann");
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
			result = convertTranslationToResult(translation);
		}
		
		return result;
	}
	
	private static ReadResult convertTranslationToResult(
			List<CharacterResult> translation) {
		String resultString = "";
		ReadResult result = new ReadResult();
		
		for (CharacterResult nextResult : translation){		
			if (nextResult != null){
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
			else resultString += " ";
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
