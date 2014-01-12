package appTest;

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
		NeuralNetwork network = new NeuralNetwork(900, 2, 109, 7, true);
		//NeuralNetwork trainedNetwork = NeuralNetworkIO.readNetwork("myNetwork");
		
		Set<CharacterTrainingExample> trainingSet = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII);
		CharacterNetworkTrainer trainer = new CharacterNetworkTrainer();	
		
		for (CharacterTrainingExample nextExample : trainingSet){
			trainer.addTrainingExample(nextExample);
		}
		
		trainer.trainNeuralNetwork(network, new BackpropagationTrainer(0.03f, 0.5f));
		
		BufferedImage test = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\textExample.png"));
		ImageReader reader = new ImageReader(network);
		
		String result = reader.readTextFromImage(test);
		System.out.println("RESULT: " + result);
		
		//NeuralNetworkIO.writeNetwork(network, "myNetwork");
	}

	private static void testMe(NeuralNetwork network, Set<TrainingExample> set){
		
		for (TrainingExample nextExample : set){
			int[] outputs = network.getOutputForInput(nextExample.getInput());
			
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
