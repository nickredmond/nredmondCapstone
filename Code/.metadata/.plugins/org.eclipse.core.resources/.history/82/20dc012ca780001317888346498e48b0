package neuralNetwork;

import imageProcessing.INetworkIOTranslator;
import imageProcessing.NetworkIOTranslator;

import java.awt.image.BufferedImage;

public class CharacterReader {
	private NeuralNetwork network;
	private INetworkIOTranslator translator;
	
	public CharacterReader(NeuralNetwork network, INetworkIOTranslator translator){
		this.network = network;
		this.translator = translator;
	}
	
	public char readCharacter(BufferedImage img){
		float[] input = translator.translateImageToNetworkInput(img);
		float[] output = network.getOutputForInput(input);
		
		char result = translator.translateNetworkOutputToCharacter(output);
		
		return result;
	}
}
