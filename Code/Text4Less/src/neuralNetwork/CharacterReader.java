package neuralNetwork;

import imageProcessing.NetworkIOTranslator;

import java.awt.image.BufferedImage;

public class CharacterReader {
	private NeuralNetwork network;
	
	public CharacterReader(NeuralNetwork network){
		this.network = network;
	}
	
	public char readCharacter(BufferedImage img){
		NetworkIOTranslator translator = new NetworkIOTranslator();
		float[] input = translator.translateImageToNetworkInput(img);
		int[] output = network.getOutputForInput(input);
		
		char result = translator.translateNetworkOutputToCharacter(output);
		
		return result;
	}
}
