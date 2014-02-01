package threading;

import neuralNetwork.INetworkTrainer;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.TrainingExample;

public class TrainingExampleThread implements Runnable {
	private INeuralNetwork network;
	private INetworkTrainer trainer;
	private TrainingExample example;
	private DeltaValues deltas;
	
	public TrainingExampleThread(INeuralNetwork network, INetworkTrainer trainer, 
			TrainingExample example, DeltaValues deltas){
		this.network = network;
		this.trainer = trainer;
		this.example = example;
		this.deltas = deltas;
	}
	
	@Override
	public void run() {
		float error = trainer.calculateErrorsAndDeltas(network, example);
		deltas.getError().addToError(error);
		
		float[][][] hiddenDeltas = new float[network.getNumberHiddenLayers() - 1][][];
		for (int i = 0; i < hiddenDeltas.length; i++){
			hiddenDeltas[i] = network.getHiddenDeltas(i);
		}
		
		deltas.addHiddenDeltaValues(hiddenDeltas);
		deltas.addInputDeltaValues(network.getInputDeltas());
		deltas.addOutputDeltaValues(network.getOutputDeltas());
	}
}
