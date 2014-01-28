package app;

import imageProcessing.INetworkIOTranslator;
import imageProcessing.ImagePreprocessor;
import imageProcessing.NewLineTranslationResult;
import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import neuralNetwork.CharacterReader;
import neuralNetwork.NeuralNetwork;

public class ImageReader {
	private CharacterReader reader;
	
	public ImageReader(NeuralNetwork network, INetworkIOTranslator translator){
		reader = new CharacterReader(network, translator);
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
