package app;

import imageProcessing.INetworkIOTranslator;
import imageProcessing.ImagePreprocessor;
import imageProcessing.NewLineTranslationResult;
import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import neuralNetwork.CharacterReader;
import neuralNetwork.INeuralNetwork;
import threading.CorrelationThreadPool;

public class ImageReader {
	private CharacterReader reader;
	private ICharacterImageHandler handler;
	private List<TranslationResult> result;
	
	public ImageReader(INeuralNetwork network, INetworkIOTranslator translator) throws IOException{
		reader = new CharacterReader(network, translator);
		result = new ArrayList<TranslationResult>();
		handler = ImageHandlerFactory.getImageHandler(result, reader);
	}
	
	public String convertTranslationToText(List<TranslationResult> translation){
		StringBuilder builder = new StringBuilder();
		
		for (TranslationResult nextResult : translation){
			if (nextResult != null){
			String nextCharacter = (nextResult.toString().equals("\r\n")) ? "\r\n" : ((Character)nextResult.getResult()).toString();
			builder.append(nextCharacter);
			}
		}
		
		return builder.toString();
	}
	
	public List<TranslationResult> readTextFromImage(BufferedImage image){
		List<TranslationResult> results = null;
		
		results =  (handler.getClass() == CorrelationHandler.class) ? readMultithreadedTextFromImage(image) :
			readSinglethreadedTextFromImage(image);
		
		return results;
	}
	
	private List<TranslationResult> readSinglethreadedTextFromImage(BufferedImage image){
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
	
	private List<TranslationResult> readMultithreadedTextFromImage(BufferedImage image){
		ImagePreprocessor processor = new ImagePreprocessor();
		BufferedImage trimmedImage = processor.trimMargins(image);
		
		List<BufferedImage> lines = processor.splitIntoLines(trimmedImage);
		result.clear();
		
		
		for (BufferedImage nextLine : lines){
			List<BufferedImage> characters = processor.splitIntoCharacters(nextLine);
			List<TranslationResult> lineResult = new ArrayList<TranslationResult>(characters.size());
			CorrelationHandler lineHandler = new CorrelationHandler(lineResult);
			
			CorrelationThreadPool pool = new CorrelationThreadPool(lineHandler);
			
			int index = 0;
			
			for (BufferedImage nextCharacter : characters){
				pool.addImage(nextCharacter, index);
				index++;
			}
			pool.execute();
			
			result.addAll(lineResult);
			result.add(new NewLineTranslationResult());
		}
		
		return result;
	}
}
