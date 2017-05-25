package decisionTrees;

import neuralNetwork.INeuralNetwork;

public class NetworkSet {
	private INeuralNetwork leftNetwork, rightNetwork;
	
	public NetworkSet(INeuralNetwork left, INeuralNetwork right){
		leftNetwork = left;
		rightNetwork = right;
	}

	public INeuralNetwork getLeftNetwork() {
		return leftNetwork;
	}

	public INeuralNetwork getRightNetwork() {
		return rightNetwork;
	}
}
