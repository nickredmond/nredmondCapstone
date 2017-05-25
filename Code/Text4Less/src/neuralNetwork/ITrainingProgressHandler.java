package neuralNetwork;

public interface ITrainingProgressHandler {
	public void progressUpdate(float mse, int iterationsPerformed);
	public void setTrainingSummary(float mse, int iterationsPerformed, boolean success, long secsToTrain);
}
