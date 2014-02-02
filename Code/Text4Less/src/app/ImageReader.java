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
	private ICharacterImageHandler handler;
	private List<TranslationResult> result;
	
	public ImageReader(INeuralNetwork network, INetworkIOTranslator translator, boolean doTranslation){
		reader = new CharacterReader(network, translator);
		result = new ArrayList<TranslationResult>();
		handler = (doTranslation ? new TranslationHandler(result, reader) : new TrainingDataCreationHandler());
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
		result.clear();
		
		for (BufferedImage nextLine : lines){
			List<BufferedImage> characters = processor.splitIntoCharacters(nextLine);
			
			for (BufferedImage nextCharacter : characters){
				handler.handleImage(nextCharacter);
			}
			result.add(new NewLineTranslationResult());
		}
		
		return result;
	}
}
