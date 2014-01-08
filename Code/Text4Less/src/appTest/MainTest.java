package appTest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import neuralNetwork.BackpropagationTrainer;
import neuralNetwork.CharacterNetworkTrainer;
import neuralNetwork.CharacterReader;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.TrainingExample;

public class MainTest {

	public static void main(String[] args) throws IOException {
//		NeuralNetwork network = new NeuralNetwork(2, 1, 3, 2, true);
//		
//		TrainingExample e1 = new TrainingExample(new float[]{0.41f, 0.07f}, new int[]{0, 0});
//		TrainingExample e2 = new TrainingExample(new float[]{-0.45f, -0.05f}, new int[]{0, 1});
//		TrainingExample e3 = new TrainingExample(new float[]{0.27f, -0.17f}, new int[]{1, 1});
//		
//		Set<TrainingExample> set = new HashSet<TrainingExample>();
//		set.add(e1);
//		set.add(e2);
//		set.add(e3);
//		
//		testMe(network,set);
//		
//		BackpropagationTrainer trainer = new BackpropagationTrainer();
//		trainer.trainWithTrainingSet(network, set);
//		
//		testMe(network,set);
		
		//System.out.println("yes: " + (char)58);
		
		NeuralNetwork network = new NeuralNetwork(900, 3, 20, 7, true);
		CharacterReader reader = new CharacterReader(network);
		BufferedImage testA = 
				ImageIO.read(new File("C:\\Users\\nredmond\\Documents\\Capstone\\Training Data\\Set1 - Capital Letters\\A.jpg"));
		BufferedImage testZ = 
				ImageIO.read(new File("C:\\Users\\nredmond\\Documents\\Capstone\\Training Data\\Set1 - Capital Letters\\Z.jpg"));
		
		CharacterNetworkTrainer trainer = new CharacterNetworkTrainer();
		CharacterTrainingExample example1 = new CharacterTrainingExample(testA, 'A');
		CharacterTrainingExample example2 = new CharacterTrainingExample(testZ, 'Z');
		
		trainer.addTrainingExample(example1);
		trainer.addTrainingExample(example2);
		
		trainer.trainNeuralNetwork(network, new BackpropagationTrainer());
		
		char result1 = reader.readCharacter(testA);
		char result2 = reader.readCharacter(testZ);
		
		System.out.println("This should be A: " + result1);
		System.out.println("This should be Z: " + result2);
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
