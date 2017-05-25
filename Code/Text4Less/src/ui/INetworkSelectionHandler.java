package ui;

import neuralNetwork.INeuralNetwork;

public interface INetworkSelectionHandler {
	public void networkSelected(String networkName, INeuralNetwork network);
}
