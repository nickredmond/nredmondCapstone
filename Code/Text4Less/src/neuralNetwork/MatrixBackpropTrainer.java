package neuralNetwork;

import java.util.Set;

public class MatrixBackpropTrainer implements INetworkTrainer {
	private float learningRate, regularizationParam;
	private float MAX_ERROR = 0.05f;
	
	public MatrixBackpropTrainer(float learningRate, float regParam){
		this.learningRate = learningRate;
		regularizationParam = regParam;
	}
	
	@Override
	public void trainWithTrainingSet(INeuralNetwork network,
			Set<TrainingExample> trainingSet, Set<TrainingExample> testSet) {
		float mse = 0.0f;
		do{
			mse = performTrainingIteration(trainingSet, network);
		}while(mse > MAX_ERROR);
	}

	private float performTrainingIteration(Set<TrainingExample> trainingSet, INeuralNetwork network) {
		float error = 0.0f;
		
		for (TrainingExample nextExample : trainingSet){
			error += calculateErrorsAndDeltas(network, nextExample);
		}
		changeWeights(network, trainingSet.size(), regularizationParam);
		
		error = error / (2 * trainingSet.size());
		System.out.println("MSE: " + error);
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
				//deltas[i][j] = 0.0f;
			}
		}
	}

	private float calculateErrorsAndDeltas(INeuralNetwork network,
			TrainingExample nextExample) {
		float[] output = network.forwardPropagate(nextExample.getInput());
		float outputErr = calculateOutputError(network, output, nextExample.getOutput(), false);
		
		setOutputDeltas(network);
		
		int layerSize = network.getHiddenValues(0).length;
		
		float[] lastHiddenError = calculateError(network, 
				layerSize, network.getOutputWeights(), network.getOutputErrors());
		network.setHiddenError(network.getNumberHiddenLayers() - 1, lastHiddenError);
		
		for (int l = network.getNumberHiddenLayers() - 2; l >= 0; l--){
			float[] nextError = network.getHiddenErrors(l+1);
			float[] error = calculateError(network, layerSize, network.getHiddenWeights(l), nextError);
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
				inputDeltas[i][j] += inputValues[i] * errors[j];
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
	
	private float[] calculateError(INeuralNetwork network, int layerSize, float[][] weights, float[] nextLayerError){
		float[] error = new float[layerSize - 1];
		
		for (int i = 0; i < error.length; i++){
			float errorSum = 0.0f;
			
			for (int k = 0; k < nextLayerError.length; k++){
				errorSum += weights[i][k] * nextLayerError[k];
			}
			
			error[i] = errorSum;
		}
		
		return error;
	}
}