package neuralNetwork;

import java.util.Set;

public interface INetworkTrainer {
	public void trainWithSingleTrainingExample(NeuralNetwork network,
			TrainingExample example);
	
	public void trainWithTrainingSet(NeuralNetwork network,
			Set<TrainingExample> trainingSet);
}
