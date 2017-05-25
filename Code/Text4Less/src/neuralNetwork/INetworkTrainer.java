package neuralNetwork;

import java.util.Set;

public interface INetworkTrainer {	
	public void trainWithTrainingSet(INeuralNetwork network,
			Set<TrainingExample> trainingSet, Set<TrainingExample> testSet);
	public float calculateErrorsAndDeltas(INeuralNetwork network, TrainingExample example);
	
	public void setIterations(int iterations);
	public int getIterations();
	public void setErrorGoal(float errorGoal);
	public float getAchievedError();
}
