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
	
	public NetworkSet getTrainedNetworksForSubclasses(char[] leftClasses, char[] rightClasses){
		FeatureExtractionIOTranslator translator = new FeatureExtractionIOTranslator();
		MatrixBackpropTrainer trainer = new MatrixBackpropTrainer(0.05f, 0.02f);
		INeuralNetwork leftNetwork = new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, leftClasses.length, true);
		INeuralNetwork rightNetwork = new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, rightClasses.length, true);
		
		Set<TrainingExample> leftSet = convertToTrainingSet(leftClasses, trainingSet);
		Set<TrainingExample> rightSet = convertToTrainingSet(rightClasses, trainingSet);
		
		trainer.trainWithTrainingSet(leftNetwork, leftSet, new HashSet<TrainingExample>());
		trainer.trainWithTrainingSet(rightNetwork, rightSet, new HashSet<TrainingExample>());
		
		return new NetworkSet(leftNetwork, rightNetwork);
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
			Set<TrainingExample> leftTestSet = convertToTrainingSet(leftClasses, testSet);
			Set<TrainingExample> rightTestSet = convertToTrainingSet(rightClasses, testSet);
			
			NetworkSet trainedNetworks = getTrainedNetworksForSubclasses(leftClasses, rightClasses);
			INeuralNetwork leftNetwork = trainedNetworks.getLeftNetwork();
			INeuralNetwork rightNetwork = trainedNetworks.getRightNetwork();
			
			float leftFitness = getFitnessValue(leftNetwork, leftClasses, leftTestSet);
			float rightFitness = getFitnessValue(rightNetwork, rightClasses, rightTestSet);
			
			fitness = (leftFitness + rightFitness) / 2;
		}
		
		return fitness;
	}
	
	private float getFitnessValue(INeuralNetwork network, char[] classes, Set<TrainingExample> examples){
		int numberRight = 0;
		
		for (TrainingExample nextExample : examples){
			char desiredChar = translateOutputToCharacter(nextExample.getOutput(), classes);
			float[] output = network.forwardPropagate(nextExample.getInput());
			char actualChar = MetaclassConverter.translateOutputToCharacter(output, classes);
			
			if (desiredChar == actualChar){
				numberRight++;
			}
		}
		
		return ((float)(numberRight) / examples.size());
	}

	private Set<TrainingExample> convertToTrainingSet(char[] classes, Set<CharacterTrainingExample> examples){
		Set<TrainingExample> result = new HashSet<TrainingExample>();
		
		for (CharacterTrainingExample nextExample : examples){
			char nextChar = nextExample.getCharacterValue();
			float[] input = translator.translateImageToNetworkInput(nextExample.getCharacterImage());
			int[] output = translateCharacterToOutput(nextChar, classes);
			result.add(new TrainingExample(input, output));
		}
		
		return result;
	}
	
	private char translateOutputToCharacter(int[] output, char[] classes){
		if (output.length != classes.length){
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
	
	private int[] translateCharacterToOutput(char character, char[] classes){
		int[] output = new int[classes.length];
		boolean foundCharacter = false;
		
		for (int i = 0; i < output.length && !foundCharacter; i++){
			if (classes[i] == character){
				output[i] = 1;
				foundCharacter = true;
			}
		}
		return output;
	}
}