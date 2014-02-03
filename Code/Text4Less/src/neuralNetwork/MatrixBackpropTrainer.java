package neuralNetwork;

import java.util.Set;

import threading.DeltaValues;
import threading.TrainingExampleThreadPool;

public class MatrixBackpropTrainer implements INetworkTrainer {
	private float learningRate, regularizationParam;
	private final float MAX_ERROR = 0.01f;
	
	private float previousError, previousCve;
	private int numberIterations = 0; // use this later
	
	public MatrixBackpropTrainer(float learningRate, float regParam){
		this.learningRate = learningRate;
		regularizationParam = regParam;
	}
	
	@Override
	public void trainWithTrainingSet(INeuralNetwork network,
			Set<TrainingExample> trainingSet, Set<TrainingExample> testSet) {
		float mse = 1000.0f;
		float cve = 1000.0f;
		
		do{
			previousError = mse;
			previousCve = cve;
			
			mse = performTrainingIteration(trainingSet, network);
			cve = crossValidate(testSet, network);
			System.out.println("MSE: " + mse + ", CVE: " + cve);
		}while(mse > 0.05f);
	}
	
	private float crossValidate(Set<TrainingExample> testSet, INeuralNetwork network){
		float outputErr = 0;
		
		for (TrainingExample nextExample : testSet){
			float[] output = network.forwardPropagate(nextExample.getInput());
			outputErr += calculateOutputError(network, output, nextExample.getOutput(), false);
		}
		
		return outputErr / (2 * testSet.size());
	}

	private float performTrainingIteration(Set<TrainingExample> trainingSet, INeuralNetwork network) {
		float error = 0.0f;
		
		for (TrainingExample nextExample : trainingSet){
			error += calculateErrorsAndDeltas(network, nextExample);
		}
		
		changeWeights(network, trainingSet.size(), regularizationParam);
		
		error = error / (2 * trainingSet.size());
		return error;
	}

	private void changeWeights(INeuralNetwork network, int trainingSize, float regularizationParam) {
		float[][] inputWeights = network.getInputWeights();
		float[][] inputDeltas = network.getInputDeltas();
		changeWeightsForLayer(inputWeights, inputDeltas, trainingSize, regularizationParam);
		
		for (int l = 0; l < network.getNumberHiddenLayers() - 1; l++){
			float[][] hiddenWeights = network.getHiddenWeights(l);
			float[][] hiddenDeltas = network.getHiddenDeltas(l);
			changeWeightsForLayer(hiddenWeights, hiddenDeltas, trainingSize, regularizationParam);
		}
		
		float[][] outputWeights = network.getOutputWeights();
		float[][] outputDeltas = network.getOutputDeltas();
		changeWeightsForLayer(outputWeights, outputDeltas, trainingSize, regularizationParam);
	}
	
	private void changeWeightsForLayer(float[][] weights, float[][] deltas,
			int trainingSize, float regularizationParam){
		for (int i = 0; i < weights.length; i++){
			for (int j = 0; j < weights[0].length; j++){
				float weightChange = ((1.0f / trainingSize) * deltas[i][j]);
				
				if (i > 0){
					weightChange += regularizationParam * weights[i][j];
				}
				
				weights[i][j] += (learningRate * weightChange);
				deltas[i][j] = 0;
			}
		}
	}

	public float calculateErrorsAndDeltas(INeuralNetwork network,
			TrainingExample nextExample) {
		float[] output = network.forwardPropagate(nextExample.getInput());
		float outputErr = calculateOutputError(network, output, nextExample.getOutput(), false);
		
		setOutputDeltas(network);
		
		int layerSize = network.getHiddenValues(0).length;
		
		float[] lastHiddenError = calculateError(network, 
				layerSize, network.getOutputWeights(), network.getOutputErrors(), false);
		network.setHiddenError(network.getNumberHiddenLayers() - 1, lastHiddenError);
		
		for (int l = network.getNumberHiddenLayers() - 2; l >= 0; l--){
			float[] nextError = network.getHiddenErrors(l+1);
			float[] error = calculateError(network, layerSize, network.getHiddenWeights(l), nextError, true);
			network.setHiddenError(l, error);
			
			setHiddenDeltas(network, l);
		}
		float[] input = nextExample.getInput();
		float[] inputValues = new float[input.length + 1];
		
		inputValues[0] = 1.0f;
		for (int i = 1; i < inputValues.length; i++){
			inputValues[i] = input[i-1];
		}
		
		setInputDeltas(network, inputValues);
		return outputErr;
	}
	
	private void setInputDeltas(INeuralNetwork network, float[] inputValues){
		float[][] inputDeltas = network.getInputDeltas();
		float[] errors = network.getHiddenErrors(0);
		
		for (int i = 0; i < network.getInputDeltas().length; i++){
			for (int j = 0; j < network.getInputDeltas()[0].length; j++){
				inputDeltas[i][j] += inputValues[i] * errors[j+1];
			}
		}
		
		network.setInputDelta(inputDeltas);
	}
	
	private void setHiddenDeltas(INeuralNetwork network, int layer){
		float[][] hiddenDeltas = network.getHiddenDeltas(layer);
		float[] activations = network.getHiddenValues(layer);
		float[] errors = network.getHiddenErrors(layer + 1);
		
		for (int i = 0; i < network.getHiddenDeltas(0).length; i++){
			for (int j = 0; j < network.getHiddenDeltas(0)[0].length; j++){
				hiddenDeltas[i][j] += activations[i] * errors[j];
			}
		}
		
		network.setHiddenDelta(layer, hiddenDeltas);
	}
	
	private void setOutputDeltas(INeuralNetwork network) {
		float[][] outputDeltas = network.getOutputDeltas();
		float[] activations = network.getHiddenValues(network.getNumberHiddenLayers() - 1);
		float[] errors = network.getOutputErrors();
		
		for (int i = 0; i < network.getOutputDeltas().length; i++){
			for (int j = 0; j < network.getOutputDeltas()[0].length; j++){
				outputDeltas[i][j] += activations[i] * errors[j];
			}
		}
		
		network.setOutputDelta(outputDeltas);
	}

	private float calculateOutputError(INeuralNetwork network, float[] output, int[] desired, boolean isTest){
		float[] outputError = new float[output.length];
		float errorSum = 0.0f;
		
		for (int i = 0; i < outputError.length; i++){
			float nextError = desired[i] - output[i];
			
			if(!isTest)
				errorSum += (nextError * nextError);
			
			outputError[i] = nextError;
		}
		
		network.setOutputError(outputError);
		return errorSum;
	}
	
	private float[] calculateError(INeuralNetwork network, int layerSize, float[][] weights, float[] nextLayerError, boolean isHidden){
		float[] error = new float[layerSize];
		
		for (int i = 0; i < error.length; i++){
			float errorSum = 0.0f;
			
			for (int k = 0; k < nextLayerError.length && k < (isHidden ? weights[i].length - 1 : weights[i].length); k++){
				errorSum += weights[i][k] * nextLayerError[(isHidden ? k+1 : k)];
			}
			
			error[i] = errorSum;
		}
		
		return error;
	}
}
