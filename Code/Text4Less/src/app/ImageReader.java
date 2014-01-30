package app;

import imageProcessing.INetworkIOTranslator;
import imageProcessing.ImagePreprocessor;
import imageProcessing.NewLineTranslationResult;
import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import neuralNetwork.CharacterReader;
import neuralNetwork.INeuralNetwork;

public class ImageReader {
	private CharacterReader reader;
	
	public ImageReader(INeuralNetwork network, INetworkIOTranslator translator){
		reader = new CharacterReader(network, translator);
	}
	
	public String convertTranslationToText(List<TranslationResult> translation){
		StringBuilder builder = new StringBuilder();
		
		for (TranslationResult nextResult : translation){
			String nextCharacter = (nextResult.toString().equals("\r\n")) ? "\r\n" : ((Character)nextResult.getResult()).toString();
			builder.append(nextCharacter);
		}
		
		return builder.toString();
	}
	
	public List<TranslationResult> readTextFromImage(BufferedImage image){
		ImagePreprocessor processor = new ImagePreprocessor();
		BufferedImage trimmedImage = processor.trimMargins(image);
		
		List<BufferedImage> lines = processor.splitIntoLines(trimmedImage);
		List<TranslationResult> result = new ArrayList<TranslationResult>();
		
		for (BufferedImage nextLine : lines){
			List<BufferedImage> characters = processor.splitIntoCharacters(nextLine);
			
			for (BufferedImage nextCharacter : characters){
				TranslationResult translation = reader.readCharacter(nextCharacter);
				result.add(translation);
			}
			result.add(new NewLineTranslationResult());
		}
		
		return result;
	}
}
