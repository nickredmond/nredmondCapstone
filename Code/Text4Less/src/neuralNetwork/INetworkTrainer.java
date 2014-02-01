package neuralNetwork;

import java.util.Set;

public interface INetworkTrainer {	
	public void trainWithTrainingSet(INeuralNetwork network,
			Set<TrainingExample> trainingSet, Set<TrainingExample> testSet);
	public float calculateErrorsAndDeltas(INeuralNetwork network, TrainingExample example);
}
