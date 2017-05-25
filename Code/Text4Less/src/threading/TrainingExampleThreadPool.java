package threading;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import neuralNetwork.INetworkTrainer;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.TrainingExample;

public class TrainingExampleThreadPool {
	private final int DEFAULT_POOL_SIZE = 50;
	
	private int threadPoolSize;	
	private ExecutorService service;
	private Set<TrainingExample> trainingSet;
	private INetworkTrainer trainer;
	private INeuralNetwork network;
	private DeltaValues deltas;
	
	public TrainingExampleThreadPool(Set<TrainingExample> trainingSet, INeuralNetwork network,
			INetworkTrainer trainer){
		threadPoolSize = DEFAULT_POOL_SIZE;
		
		this.trainingSet = trainingSet;
		this.trainer = trainer;
		this.network = network;
		
		deltas = new DeltaValues();
	}
	
	public void setPoolSize(int poolSize){
		threadPoolSize = poolSize;
	}
	
	public void execute(){
		ErrorValue errorSum = new ErrorValue();
		service = Executors.newFixedThreadPool(threadPoolSize);
		
		for (TrainingExample nextExample : trainingSet){
			Runnable nextTrainingThread = new TrainingExampleThread(network.cloneNetwork(), trainer, nextExample, deltas);
			service.execute(nextTrainingThread);
		}
		service.shutdown();
		
		while(!service.isTerminated()){
		}
		//System.out.println("err: " + errorSum.getError());
	}
	
	public DeltaValues integrateResults(){
		float[][] inputTotal = new float[network.getInputDeltas().length][network.getInputDeltas()[0].length];
		float[][] outputTotal = new float[network.getOutputDeltas().length][network.getOutputDeltas()[0].length];
		float[][][] hiddenTotal = new float[network.getNumberHiddenLayers() - 1][][];
		
		for (float[][] nextInput : deltas.getInputDeltas()){
			if (nextInput != null){
			for (int i = 0; i < nextInput.length; i++){
				for (int j = 0; j < nextInput[0].length; j++){
					inputTotal[i][j] += nextInput[i][j];
				}
			}
			}
		}
		for (float[][] nextOutput : deltas.getOutputDeltas()){
			if (nextOutput != null){
			for (int i = 0; i < nextOutput.length; i++){
				for (int j = 0; j < nextOutput[0].length; j++){
					outputTotal[i][j] += nextOutput[i][j];
				}
			}
			}
		}
		for (float[][][] nextHidden : deltas.getHiddenDeltas()){
			if (nextHidden != null){
			for (int i = 0; i < nextHidden.length; i++){
				for (int j = 0; j < nextHidden[0].length; j++){
					for (int k = 0; k < nextHidden[0][0].length; k++){
						hiddenTotal[i][j][k] += nextHidden[i][j][k];
					}
				}
			}
			}
		}
		
		deltas.setInputTotal(inputTotal);
		deltas.setHiddenTotal(hiddenTotal);
		deltas.setOutputTotal(outputTotal);
		
		return deltas;
	}
}
