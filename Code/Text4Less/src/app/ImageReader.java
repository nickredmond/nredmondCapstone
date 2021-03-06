package app;

import imageProcessing.INetworkIOTranslator;
import imageProcessing.ImagePreprocessor;

import java.awt.image.BufferedImage;
import java.util.List;

import neuralNetwork.CharacterReader;
import neuralNetwork.NeuralNetwork;

public class ImageReader {
	private CharacterReader reader;
	
	public ImageReader(NeuralNetwork network, INetworkIOTranslator translator){
		reader = new CharacterReader(network, translator);
	}
	
	public String readTextFromImage(BufferedImage image){
		ImagePreprocessor processor = new ImagePreprocessor();
		BufferedImage trimmedImage = processor.trimMargins(image);
		
		List<BufferedImage> lines = processor.splitIntoLines(trimmedImage);
		String result = "";
		
		for (BufferedImage nextLine : lines){
			List<BufferedImage> characters = processor.splitIntoCharacters(nextLine);
			
			for (BufferedImage nextCharacter : characters){
				char translation = reader.readCharacter(nextCharacter);
				result += translation;
			}
			result += "\r\n";
		}
		
		return result;
	}
}
