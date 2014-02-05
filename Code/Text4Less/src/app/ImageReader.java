package app;

import imageProcessing.ImagePreprocessor;
import imageProcessing.NewLineTranslationResult;
import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterReader;
import neuralNetwork.INeuralNetwork;
import threading.CorrelationThreadPool;

public class ImageReader {
	private CharacterReader reader;
	private ICharacterImageHandler handler;
	private List<CharacterResult> result;
	
	public static final float MINIMUM_REQUIRED_CONFIDENCE = 0.93f;
	
	public ImageReader(INeuralNetwork network, INetworkIOTranslator translator) throws IOException{
		reader = new CharacterReader(network, translator);
		setData();
	}
	
	public ImageReader(INetworkIOTranslator translator) throws IOException{
		reader = new CharacterReader(null, translator);
		setData();
	}
	
	private void setData() throws IOException{
		result = new ArrayList<CharacterResult>();
		handler = ImageHandlerFactory.getImageHandler(result, reader);
	}
	
	public String convertTranslationToText(List<CharacterResult> translation){
		StringBuilder builder = new StringBuilder();
		
		for (CharacterResult nextResult : translation){
			if (nextResult != null){
			String nextCharacter = (nextResult.getResult().toString().equals("\r\n")) ? "\r\n" : ((Character)nextResult.getResult().getCharacter()).toString();
			builder.append(nextCharacter);
			}
		}
		
		return builder.toString();
	}
	
	public List<CharacterResult> readTextFromImage(BufferedImage image){
		List<CharacterResult> results = null;
		
		results =  (handler.getClass() == LeastDistanceHandler.class) ? readMultithreadedTextFromImage(image) :
			readSinglethreadedTextFromImage(image);
		
		return results;
	}
	
//	public List<BufferedImage> getRejectedImages(){
//		return handler.getRejectedImages();
//	}
	
	private List<CharacterResult> readSinglethreadedTextFromImage(BufferedImage image){
		ImagePreprocessor processor = new ImagePreprocessor();
		BufferedImage trimmedImage = processor.trimMargins(image);
		
		List<BufferedImage> lines = processor.splitIntoLines(trimmedImage);
		result.clear();
		
		for (BufferedImage nextLine : lines){
			List<BufferedImage> characters = processor.splitIntoCharacters(nextLine);
			
			for (BufferedImage nextCharacter : characters){
				handler.handleImage(nextCharacter);
			}
			result.add(new CharacterResult(null, new NewLineTranslationResult()));
		}
		
		return result;
	}
	
	private List<CharacterResult> readMultithreadedTextFromImage(BufferedImage image){
		ImagePreprocessor processor = new ImagePreprocessor();
		BufferedImage trimmedImage = processor.trimMargins(image);
		
		List<BufferedImage> lines = processor.splitIntoLines(trimmedImage);
		result.clear();
		
		
		for (BufferedImage nextLine : lines){
			List<BufferedImage> characters = processor.splitIntoCharacters(nextLine);
			List<CharacterResult> lineResult = new ArrayList<CharacterResult>(characters.size());
			LeastDistanceHandler lineHandler = new LeastDistanceHandler(lineResult);
			
			CorrelationThreadPool pool = new CorrelationThreadPool(lineHandler);
			
			int index = 0;
			
			for (BufferedImage nextCharacter : characters){
				pool.addImage(nextCharacter, index);
				index++;
			}
			pool.execute();
			
			result.addAll(lineResult);
			result.add(new CharacterResult(null, new NewLineTranslationResult()));
		}
		
		return result;
	}
}
