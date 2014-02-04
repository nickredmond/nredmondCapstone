package neuralNetwork;

import imageProcessing.INetworkIOTranslator;
import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;

import app.CharacterResult;

public class CharacterReader {
	private INeuralNetwork network;
	private INetworkIOTranslator translator;
	
	public CharacterReader(INeuralNetwork network, INetworkIOTranslator translator){
		this.network = network;
		this.translator = translator;
	}
	
	public CharacterResult readCharacter(BufferedImage img){		
		float[] input = translator.translateImageToNetworkInput(img);		
		float[] output = network.forwardPropagate(input);
		
		TranslationResult result = translator.translateNetworkOutputToCharacter(output);
		CharacterResult charResult = new CharacterResult(img, result);
		
		return charResult;
	}
}
