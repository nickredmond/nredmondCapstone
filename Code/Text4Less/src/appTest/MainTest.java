package appTest;

import featureExtraction.MomentCalculator;
import imageProcessing.FeatureExtractionIOTranslator;
import imageProcessing.INetworkIOTranslator;
import io.CharacterType;
import io.NeuralNetworkIO;
import io.TrainingDataReader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import neuralNetwork.BackpropagationTrainer;
import neuralNetwork.CharacterNetworkTrainer;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.TrainingExample;
import app.ImageReader;

public class MainTest {

	public static void main(String[] args) throws IOException {		
		INetworkIOTranslator translator = new FeatureExtractionIOTranslator();
		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/loweri2.jpg")));
		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/loweri3.jpg")));
		
		//MainTest.runApp();
	}
	
	private static void runApp() throws IOException{
		//NeuralNetwork trainedNetwork = NeuralNetworkIO.readNetwork("myNetwork");
		
		Set<CharacterTrainingExample> trainingSet1 = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII);
		Set<CharacterTrainingExample> testSet = TrainingDataReader.createTestSetFromFile(CharacterType.ASCII);

		INetworkIOTranslator t = new FeatureExtractionIOTranslator();
		NeuralNetwork network = new NeuralNetwork(((FeatureExtractionIOTranslator)t).getInputLength(), 1, 160, 7, true);
		System.out.println("length: " + ((FeatureExtractionIOTranslator)t).getInputLength());
		
		CharacterNetworkTrainer trainer1 = new CharacterNetworkTrainer(t);	
		
		for (CharacterTrainingExample nextExample : trainingSet1){
			trainer1.addTrainingExample(nextExample);
		}
		for(CharacterTrainingExample nextExample : testSet){
			trainer1.addTestExample(nextExample);
		}
		
		trainer1.trainNeuralNetwork(network, new BackpropagationTrainer(0.05f, 0.02f));
		
		BufferedImage test = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest3.png"));
		ImageReader reader = new ImageReader(network, t);
		
		String result = reader.readTextFromImage(test);
		System.out.println("RESULT: " + result);
		
//		for (int i = 65; i <=90; i++){
//			char y = (char)i;
//			BufferedImage c = 
//					ImageIO.read(new File("C:\\Users\\nredmond\\Workspaces\\CapstoneNickRedmond\\Code\\Text4Less\\trainingImages\\ASCII\\" + y + ".jpg"));
//			System.out.println("this should be " + y + ": " + t.translateNetworkOutputToCharacter(network.getOutputForInput(t.translateImageToNetworkInput(c))));
//		}
	//	NeuralNetworkIO.writeNetwork(network, "myNetwork");
	}

	private static void testMe(NeuralNetwork network, Set<TrainingExample> set){
		
		for (TrainingExample nextExample : set){
			float[] outputs = network.getOutputForInput(nextExample.getInput());
			
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
