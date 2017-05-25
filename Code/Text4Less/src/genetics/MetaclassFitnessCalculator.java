package genetics;

import java.util.HashSet;
import java.util.Set;

import networkIOtranslation.FeatureExtractionIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import neuralNetwork.TrainingExample;
import decisionTrees.MetaclassConverter;
import decisionTrees.NetworkSet;

public class MetaclassFitnessCalculator implements IFitnessCalculator {
	private Set<CharacterTrainingExample> trainingSet, testSet;
	private char[] classes;
	private INetworkIOTranslator translator;
//	private INeuralNetwork network;
	
	public MetaclassFitnessCalculator(Set<CharacterTrainingExample> trainingSet, Set<CharacterTrainingExample> testSet,
			char[] classes){
		this.trainingSet = trainingSet;
		this.testSet = testSet;
		this.classes = classes;
		
		translator = new FeatureExtractionIOTranslator();
	}
	
	public INeuralNetwork getTrainedNetworksForSubclasses(char[] leftClasses, char[] rightClasses){ // return type used to be networkSet
		FeatureExtractionIOTranslator translator = new FeatureExtractionIOTranslator();
		MatrixBackpropTrainer trainer = new MatrixBackpropTrainer(0.05f, 0.02f);
		INeuralNetwork leftNetwork = new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, 2, true);
	//	INeuralNetwork rightNetwork = new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, rightClasses.length, true);
		
		Set<TrainingExample> leftSet = convertToTrainingSet(leftClasses, rightClasses, trainingSet);
	//	Set<TrainingExample> rightSet = convertToTrainingSet(rightClasses, trainingSet);
		
		trainer.trainWithTrainingSet(leftNetwork, leftSet, new HashSet<TrainingExample>());
	//	trainer.trainWithTrainingSet(rightNetwork, rightSet, new HashSet<TrainingExample>());
		
		return leftNetwork; // new NetworkSet()
	}
	
	@Override
	public float getFitness(int[] chromosome) {
		if (chromosome.length != classes.length){
			throw new IllegalArgumentException("Chromosome must be of same length as classes.");
		}
		
		float fitness = 0.0f;
		
		char[][] combinedClasses = MetaclassConverter.getLeftRightClasses(chromosome, classes);
		char[] leftClasses = combinedClasses[MetaclassConverter.LEFT_CLASS_INDEX];
		char[] rightClasses = combinedClasses[MetaclassConverter.RIGHT_CLASS_INDEX];
		
		if (leftClasses.length > 0 && rightClasses.length > 0){
			Set<TrainingExample> trainingSet = convertToTrainingSet(leftClasses, rightClasses, testSet);
		//	Set<TrainingExample> rightTestSet = convertToTrainingSet(rightClasses, testSet);
			
			INeuralNetwork trainedNetwork = getTrainedNetworksForSubclasses(leftClasses, rightClasses);
//			INeuralNetwork leftNetwork = trainedNetworks.getLeftNetwork();
//			INeuralNetwork rightNetwork = trainedNetworks.getRightNetwork();
			
			float leftFitness = getFitnessValue(trainedNetwork, leftClasses, rightClasses, trainingSet);
		//	float rightFitness = getFitnessValue(rightNetwork, rightClasses, rightTestSet);
			
			fitness = leftFitness; //(leftFitness + rightFitness) / 2;
		}
		
		return fitness;
	}
	
	private float getFitnessValue(INeuralNetwork network, char[] leftClasses, char[] rightClasses, Set<TrainingExample> examples){ // replaced 'classes' w/ left/right
		int numberRight = 0;
		
		for (TrainingExample nextExample : examples){
		//	char desiredChar = translateOutputToCharacter(nextExample.getOutput(), classes);
			
			int desiredClass = 0; // TEST CODE (AND BELOW)
			boolean foundClass = false;
			int[] nextOutput = nextExample.getOutput();
			
			for (int i = 0; i < nextOutput.length && !foundClass; i++){ // TEST CODE
				if (nextOutput[i] == 1){
					desiredClass = i;
					foundClass = true;
				}
			}
			
			float[] output = network.forwardPropagate(nextExample.getInput());
		//	char actualChar = MetaclassConverter.translateOutputToCharacter(output, classes);
			
			int actualClass = 0;
			float maxValue = -2.0f;
			
			for (int i = 0; i < output.length; i++){
				if (output[i] > maxValue){
					maxValue = output[i];
					actualClass = i;
				}
			}
			
			if (desiredClass == actualClass){
				numberRight++;
			}
		}
		
		return ((float)(numberRight) / examples.size());
	}

	private Set<TrainingExample> convertToTrainingSet(char[] leftClasses, char[] rightClasses, Set<CharacterTrainingExample> examples){ // changed from 'classes' to left/right
		Set<TrainingExample> result = new HashSet<TrainingExample>();
		
		for (CharacterTrainingExample nextExample : examples){
			char nextChar = nextExample.getCharacterValue();
			float[] input = translator.translateImageToNetworkInput(nextExample.getCharacterImage());
			int[] output = translateCharacterToOutput(nextChar, leftClasses, rightClasses);
			result.add(new TrainingExample(input, output));
		}
		
		return result;
	}
	
	private char translateOutputToCharacter(int[] output, char[] classes){
		if (output.length != 2){ // equate to classes.length
			throw new IllegalArgumentException("Output must be same length as classes.");
		}
		
		boolean foundCharacter = false;
		int index = 0;
		
		for (int i = 0; i < output.length && !foundCharacter; i++){
			if (output[i] == 1){
				foundCharacter = true;
				index = i;
			}
		}
		
		return classes[index];
	}
	
	private int[] translateCharacterToOutput(char character, char[] leftClasses, char[] rightClasses){
		int[] output = new int[2];
		boolean foundClass = false;
		
		for (int i = 0; i < leftClasses.length && !foundClass; i++){
			if (character == leftClasses[i]){
				foundClass = true;
				output[0] = 1;
			}
		}
		if (!foundClass){
			output[1] = 1;
		}
		
		return output;
	}
	
//	private int[] translateCharacterToOutput(char character, char[] classes){
//		int[] output = new int[classes.length];
//		boolean foundCharacter = false;
//		
//		for (int i = 0; i < output.length && !foundCharacter; i++){
//			if (classes[i] == character){
//				output[i] = 1;
//				foundCharacter = true;
//			}
//		}
//		return output;
//	}
}
