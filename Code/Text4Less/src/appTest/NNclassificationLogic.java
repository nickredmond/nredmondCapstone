package appTest;

import io.CharacterType;
import io.NeuralNetworkIO;
import io.TrainingDataReader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import networkIOtranslation.FeatureExtractionIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import neuralNetwork.TrainingExample;

public class NNclassificationLogic {
	private static final String NETWORK_NAME = "testClassNet";
	
	static char[] leftClasses = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U'
			, 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f'};
	static char[] rightClasses = {'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's'
			, 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '};
	
	public static void trainNetworkOnClasses() throws IOException{
		FeatureExtractionIOTranslator translator = new FeatureExtractionIOTranslator();
		INeuralNetwork network = new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, 2, true);
		
		Set<CharacterTrainingExample> examples = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII2);
		Set<TrainingExample> trainingSet = new HashSet<TrainingExample>();
		
		for (CharacterTrainingExample nextExample : examples){
			int classNr = getClassNr(nextExample.getCharacterValue(), leftClasses, rightClasses);
			
			int[] leftOutput = {1,0};
			int[] rightOutput = {0,1};
			int[] output = ((classNr == 0) ? leftOutput : rightOutput);
			
			float[] input = translator.translateImageToNetworkInput(nextExample.getCharacterImage());
			
			trainingSet.add(new TrainingExample(input, output));
		}
		
		MatrixBackpropTrainer trainer = new MatrixBackpropTrainer(0.05f, 0.02f);
		trainer.trainWithTrainingSet(network, trainingSet, new HashSet<TrainingExample>());
		
		NeuralNetworkIO.writeNetwork(network, NETWORK_NAME);
	}
	
	public static void read(BufferedImage testCharacter){
		FeatureExtractionIOTranslator translator = new FeatureExtractionIOTranslator();
		INeuralNetwork network = NeuralNetworkIO.readNetwork(NETWORK_NAME);
		
		float[] output = network.forwardPropagate(translator.translateImageToNetworkInput(testCharacter));
		int classNr = getClassNr(output);
		
		String result = (classNr == 0) ? (leftClasses[0] + "-" + leftClasses[leftClasses.length - 1]) : 
			(rightClasses[0] + "-" + rightClasses[rightClasses.length - 1]);
		
		System.out.println("Classified as: " + result);
	}
	
	private static int getClassNr(float[] output){
		int classNr = 0;
		
		if (output[1] > output[0]){
			classNr = 1;
		}
		
		return classNr;
	}
	
	private static int getClassNr(char character, char[] leftClasses, char[] rightClasses){
		int classNr = 0;
		boolean foundClass = false;
		
		for (int i = 0; i < rightClasses.length && !foundClass; i++){
			if (character == rightClasses[i]){
				foundClass = true;
				classNr = 1;
			}
		}
		
		return classNr;
	}
}