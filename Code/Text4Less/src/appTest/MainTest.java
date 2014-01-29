package appTest;

import imageProcessing.FeatureExtractionIOTranslator;
import imageProcessing.INetworkIOTranslator;
import io.CharacterType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import neuralNetwork.NeuralNetwork;
import neuralNetwork.TrainingExample;
import app.MultiNetworkReader;

public class MainTest {

	public static void main(String[] args) throws IOException {		
//		INetworkIOTranslator translator = new FeatureExtractionIOTranslator();
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/b2.jpg")));
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/b3.jpg")));
		
		MainTest.runApp();
	}
	
	private static void runApp() throws IOException{
		//NeuralNetwork savedNetwork = NeuralNetworkIO.readNetwork("myNetwork");
		
		//System.out.println("length: " + ((FeatureExtractionIOTranslator)t).getInputLength());
		
//		NeuralNetwork trainedNetwork1 = NetworkFactory.getTrainedNetwork(network, t, CharacterType.ASCII, new BackpropagationTrainer(0.05f, 0.02f));
//		NeuralNetwork trainedNetwork2 = NetworkFactory.getTrainedNetwork(network, t, CharacterType.ASCII2, new BackpropagationTrainer(0.05f, 0.02f));
//		NeuralNetwork trainedNetwork3 = NetworkFactory.getTrainedNetwork(network, t, CharacterType.ASCII4, new BackpropagationTrainer(0.05f, 0.02f));
//		
		BufferedImage img = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest3.png"));
		CharacterType[] types = {CharacterType.ASCII};
//		ImageReader reader = new ImageReader(trainedNetwork, t);
		
		String result = MultiNetworkReader.getTextFromImage(img, types);
		System.out.println("RESULT: " + result);
		
//		for (int i = 65; i <=90; i++){
//			char y = (char)i;
//			BufferedImage c = 
//					ImageIO.read(new File("C:\\Users\\nredmond\\Workspaces\\CapstoneNickRedmond\\Code\\Text4Less\\trainingImages\\ASCII\\" + y + ".jpg"));
//			System.out.println("this should be " + y + ": " + t.translateNetworkOutputToCharacter(network.getOutputForInput(t.translateImageToNetworkInput(c))));
//		}
	//	NeuralNetworkIO.writeNetwork(trainedNetwork, "myNetwork");
	}

	private static void testMe(NeuralNetwork network, Set<TrainingExample> set){
		
		for (TrainingExample nextExample : set){
			float[] outputs = network.forwardPropagate(nextExample.getInput());
			
			System.out.print("Desired: ");
			
			for (int i = 0; i < nextExample.getOutput().length; i++){
				System.out.print(nextExample.getOutput()[i] + " ");
			}
			
			System.out.print(" --- Actual: ");
			
			for (int i = 0; i < outputs.length; i++){
				System.out.print(outputs[i] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
	}
}
