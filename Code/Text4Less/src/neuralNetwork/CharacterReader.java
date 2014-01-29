package neuralNetwork;

import imageProcessing.INetworkIOTranslator;
import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;

import debug.CharacterViewDebug;

public class CharacterReader {
	private INeuralNetwork network;
	private INetworkIOTranslator translator;
	
	public CharacterReader(INeuralNetwork network, INetworkIOTranslator translator){
		this.network = network;
		this.translator = translator;
	}
	
	public TranslationResult readCharacter(BufferedImage img){
//        try {
//            ImageIO.write(img, "jpg", new File("C:\\Users\\nredmond\\Workspaces\\CapstoneNickRedmond\\Code\\Text4Less\\trainingImages\\ASCII4\\" + System.nanoTime() + ".jpg"));
//	    } catch (IOException e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	    }
		
		float[] input = translator.translateImageToNetworkInput(img);		
		float[] output = network.forwardPropagate(input);
		
		TranslationResult result = translator.translateNetworkOutputToCharacter(output);
		
		return result;
	}
}
