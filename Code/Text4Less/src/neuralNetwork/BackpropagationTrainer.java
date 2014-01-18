package neuralNetwork;

import java.util.List;
import java.util.Set;

public class BackpropagationTrainer implements INetworkTrainer {
	private final float LEARNING_RATE = 0.03f;
	private final float REGULARIZATION_PARAM = 0.1f;
	private final float MAXIMUM_ALLOWABLE_ERROR = 0.1f;
	
	private final float MIN_ALLOWABLE_ERR_CHANGE = 0.00005f;
	
	private float learningRate, regularizationParam;
	
	public BackpropagationTrainer(){
		learningRate = LEARNING_RATE;
		regularizationParam = REGULARIZATION_PARAM;
	}
	
	public BackpropagationTrainer(float learningRate, float regParam){
		this.learningRate = learningRate;
		regularizationParam = regParam;
	}

	@Override
	public void trainWithTrainingSet(NeuralNetwork network,
			Set<TrainingExample> trainingSet, Set<TrainingExample> testSet) {
		float trainingError = 0.0f;
		float crossValidationError = 0.0f;
		
		do{
			trainingError = performTrainingIteration(network, trainingSet);		
			crossValidationError = calculateCrossValidationErr(network, testSet);
		}
		while(trainingError > MAXIMUM_ALLOWABLE_ERROR);
	}
	
	private float calculateCrossValidationErr(NeuralNetwork network,
			Set<TrainingExample> testSet) {
		float cvError = 0.0f;
		
		for (TrainingExample nextTestExample : testSet){
			List<Neuron> outputNeurons = network.getOutputLayer().getNeurons();
			cvError += calculateOutputErrors(network, nextTestExample, outputNeurons);
		}
		
		cvError = cvError / (2 * testSet.size());
		System.out.println("CVE: " + cvError);
		
		return cvError;
	}

	private float performTrainingIteration(NeuralNetwork network,
			Set<TrainingExample> trainingSet){
		float trainingError = 0.0f;
		
		for (TrainingExample nextExample : trainingSet){
			List<Neuron> outputNeurons = network.getOutputLayer().getNeurons();
			trainingError += calculateOutputErrors(network, nextExample, outputNeurons);
			
			List<NetworkLayer> hiddenLayers = network.getHiddenLayers();
			calculateHiddenErrorsAndDeltas(hiddenLayers, network.getOutputLayer());
		}
		
		trainingError = trainingError / (2 * trainingSet.size());
		
		changeWeights(trainingSet.size(), network);
		System.out.println("MSE: " + trainingError);
		
		return trainingError;
	}
	
	private void changeWeights(int trainingSize, NeuralNetwork network){
		NetworkLayer inputLayer = network.getInputLayer();
		changeWeightsForLayer(trainingSize, inputLayer);
		
		for (NetworkLayer nextHiddenLayer : network.getHiddenLayers()){
			changeWeightsForLayer(trainingSize, nextHiddenLayer);
		}
	}
	
	private void changeWeightsForLayer(int trainingSize, NetworkLayer layer){
		for (Neuron nextNeuron : layer.getNeurons()){
			for (NeuronConnection nextConnection : nextNeuron.getOutputConnections()){
				float changeAmount = (1.0f / trainingSize) * nextConnection.getDeltaValue();
				if (!nextNeuron.isBias()){
					changeAmount += regularizationParam * nextConnection.getWeight();
				}
				
				//System.out.println("weight: " + nextConnection.getWeight() + ", change: " + changeAmount);
				
				nextConnection.setWeight(nextConnection.getWeight() - (learningRate * changeAmount));
				nextConnection.setDeltaValue(0.0f);
			}
		}
	}

	private void calculateHiddenErrorsAndDeltas(List<NetworkLayer> hiddenLayers, NetworkLayer outputLayer) {
		for (int i = hiddenLayers.size() - 1; i >= 0; i--){
			NetworkLayer currentLayer = hiddenLayers.get(i);
			
			for (Neuron nextNeuron : currentLayer.getNeurons()){
				float errorValue = 0.0f;
				
				for (NeuronConnection nextConnection : nextNeuron.getOutputConnections()){
					errorValue += nextConnection.getWeight() * nextConnection.getRightConnector().getErrorValue();
					
					float nextDeltaValue = nextConnection.getDeltaValue() + 
							(nextNeuron.getValue() * nextConnection.getRightConnector().getErrorValue());
					
					nextConnection.setDeltaValue(nextDeltaValue);
				}
				
				nextNeuron.setErrorValue(errorValue);
			}
		}
	}

//	private NeuronConnection findInputConnection(NetworkLayer layer,
//			Neuron match) {		
//		for (Neuron nextNeuron : layer.getNeurons()){
//			for (NeuronConnection nextConnection : nextNeuron.getInputConnections()){
//				if (nextConnection.getConnector() == match){
//					return nextConnection;
//				}
//			}
//		}
//		
//		return null;
//	}

	private float calculateOutputErrors(NeuralNetwork network,
			TrainingExample nextExample, List<Neuron> outputNeurons) {
		float totalError = 0.0f;
		
		float[] actualOutput = network.getOutputForInput(nextExample.getInput());
		int[] desiredOutput = nextExample.getOutput();
		
		for (int i = 0; i < actualOutput.length; i++){
			float nextErrorValue = (float)(actualOutput[i] - desiredOutput[i]);
			outputNeurons.get(i).setErrorValue(nextErrorValue);
			totalError += (nextErrorValue * nextErrorValue);
		}
		
		return totalError;
	}

}
