package neuralNetwork;

import java.util.List;
import java.util.Set;

public class BackpropagationTrainer implements INetworkTrainer {
	private final float LEARNING_RATE = 0.0001f;
	private final float MAXIMUM_ALLOWABLE_ERROR = 0.05f;

	@Override
	public void trainWithTrainingSet(NeuralNetwork network,
			Set<TrainingExample> trainingSet) {
		long start = System.nanoTime();
		float sumOfErrors = 0.0f;
		
		do{
		sumOfErrors = 0.0f;		
		for (TrainingExample nextExample : trainingSet){
			List<Neuron> outputNeurons = network.getOutputLayer().getNeurons();
			sumOfErrors += calculateOutputErrors(network, nextExample, outputNeurons);
			
			List<NetworkLayer> hiddenLayers = network.getHiddenLayers();
			calculateHiddenErrorsAndDeltas(hiddenLayers, network.getOutputLayer());
		}
		
		changeWeights(trainingSet.size(), network);
		}
		while(sumOfErrors > MAXIMUM_ALLOWABLE_ERROR);
		
		long end = System.nanoTime();
		
		long time = (end - start);
		System.out.println("time taken to train: " + time);
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
					changeAmount += LEARNING_RATE * nextConnection.getWeight();
				}
				
				nextConnection.setWeight(nextConnection.getWeight() - changeAmount);
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
		
		network.getOutputForInput(nextExample.getInput());
		float[] actualOutput = network.getNormalizedOutput();
		int[] desiredOutput = nextExample.getOutput();
		
		for (int i = 0; i < actualOutput.length; i++){
			float nextErrorValue = (float)(actualOutput[i] - desiredOutput[i]);
			outputNeurons.get(i).setErrorValue(nextErrorValue);
			totalError += Math.abs(nextErrorValue);
		}
		
		return totalError;
	}

}
