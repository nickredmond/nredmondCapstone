package neuralNetwork;

import imageProcessing.FeatureExtractionIOTranslator;
import imageProcessing.INetworkIOTranslator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CharacterReader {
	private NeuralNetwork network;
	private INetworkIOTranslator translator;
	
	public CharacterReader(NeuralNetwork network, INetworkIOTranslator translator){
		this.network = network;
		this.translator = translator;
	}
	
	public char readCharacter(BufferedImage img){
//        try {
//            ImageIO.write(img, "jpg", new File("C:\\Users\\nredmond\\Workspaces\\CapstoneNickRedmond\\Code\\Text4Less\\trainingImages\\ASCII2\\" + System.nanoTime() + ".jpg"));
//	    } catch (IOException e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	    }
		
		float[] input = translator.translateImageToNetworkInput(img);
		float[] output = network.getOutputForInput(input);
		
		char result = translator.translateNetworkOutputToCharacter(output);
		
		return result;
	}
}
