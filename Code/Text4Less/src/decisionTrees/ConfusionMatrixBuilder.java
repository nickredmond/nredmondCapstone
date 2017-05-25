package decisionTrees;

import java.util.HashSet;
import java.util.Set;

import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import neuralNetwork.TrainingExample;
import app.ICharacterConverter;

public class ConfusionMatrixBuilder {
	private ICharacterConverter converter;
	private INetworkIOTranslator translator;
	
	private int[][] confusionMatrix;
	
	public ConfusionMatrixBuilder(ICharacterConverter converter, INetworkIOTranslator translator){
		this.converter = converter;
		this.translator = translator;
		
		confusionMatrix = new int[converter.getNumberClasses()][converter.getNumberClasses()];
	}
	
	public int[][] buildConfusionMatrix(Set<CharacterTrainingExample> trainingSet,
			Set<CharacterTrainingExample> testSet, float errorGoal) throws Exception{
		INeuralNetwork trainedNetwork = trainNetwork(trainingSet, errorGoal);
		
		Set<TrainingExample> testExamples = getTrainingExamples(testSet);
		
		for (TrainingExample nextExample : testExamples){
			float[] output = trainedNetwork.forwardPropagate(nextExample.getInput());
			
			int desiredClass = convertOutputToClassNumber(nextExample.getOutput());
			int actualClass = convertOutputToClassNumber(output);
			
			confusionMatrix[desiredClass][actualClass]++;
		}
		
		return confusionMatrix;
	}
	
	private INeuralNetwork trainNetwork(Set<CharacterTrainingExample> trainingSet, float errorGoal) throws Exception {
		Set<TrainingExample> examples = getTrainingExamples(trainingSet);
		MatrixBackpropTrainer trainer = new MatrixBackpropTrainer(0.05f, 0.02f);
		trainer.setErrorGoal(errorGoal);
		
		INeuralNetwork network = new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, converter.getNumberClasses(), true);
		trainer.trainWithTrainingSet(network, examples, new HashSet<TrainingExample>());
		
		return network;
	}
	
	private Set<TrainingExample> getTrainingExamples(Set<CharacterTrainingExample> characterSet) throws Exception{
		Set<TrainingExample> examples = new HashSet<TrainingExample>();
		
		for (CharacterTrainingExample nextExample : characterSet){
			int classNr = converter.convertCharacterToClassNumber(nextExample.getCharacterValue());
			
			int[] output = convertClassNrToOutput(converter.getNumberClasses(), classNr);
			float[] input = translator.translateImageToNetworkInput(nextExample.getCharacterImage());
			
			examples.add(new TrainingExample(input, output));
		}
		
		return examples;
	}
	
	private int[] convertClassNrToOutput(int numberOfClasses, int classNr){
		int[] output = new int[numberOfClasses];
		output[classNr] = 1;
		
		return output;
	}
	
	private int convertOutputToClassNumber(int[] output){
		int classNr = -1;
		boolean foundClass = false;
		
		for (int i = 0; i < output.length && !foundClass; i++){
			if (output[i] == 1){
				foundClass = true;
				classNr = i;
			}
		}
		
		return classNr;
	}

	private int convertOutputToClassNumber(float[] output){
		int classNr = -1;
		float maxOutput = -2.0f;
		
		for (int i = 0; i < output.length; i++){
			if (output[i] > maxOutput){
				maxOutput = output[i];
				classNr = i;
			}
		}
		
		return classNr;
	}
}
