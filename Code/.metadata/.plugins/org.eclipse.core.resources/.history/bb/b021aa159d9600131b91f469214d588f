package app;

import io.CharacterType;
import io.TrainingDataReader;

import java.io.IOException;
import java.util.Set;

import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterNetworkTrainer;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INetworkTrainer;
import neuralNetwork.INeuralNetwork;

public class NetworkFactory {
	private static int iterations;
	private static float mse;
	
	public static INeuralNetwork getTrainedNetwork(INeuralNetwork originalNetwork, INetworkIOTranslator translator,
			CharacterType type, INetworkTrainer networkTrainer) throws IOException{
		return getTrainedNetwork(originalNetwork, translator, type, networkTrainer, 10000, 0.01f);
	}
	
	public static INeuralNetwork getTrainedNetwork(INeuralNetwork originalNetwork, INetworkIOTranslator translator,
			CharacterType type, INetworkTrainer networkTrainer, int numIterations, float errorGoal) throws IOException{
		INeuralNetwork networkCopy = originalNetwork.cloneNetwork();
		
		Set<CharacterTrainingExample> trainingSet = TrainingDataReader.createTrainingSetFromFile(type);
//		Set<CharacterTrainingExample> trainingSet2 = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII3);
//		trainingSet.addAll(trainingSet2);
		Set<CharacterTrainingExample> testSet = TrainingDataReader.createTestSetFromFile(type);
		CharacterNetworkTrainer trainer = new CharacterNetworkTrainer(translator);
		
		for (CharacterTrainingExample nextExample : trainingSet){
			trainer.addTrainingExample(nextExample);
		}
		for(CharacterTrainingExample nextExample : testSet){
			trainer.addTestExample(nextExample);
		}
		
		networkTrainer.setErrorGoal(errorGoal);
		networkTrainer.setIterations(numIterations);
		
		long before = System.nanoTime();
		trainer.trainNeuralNetwork(networkCopy, networkTrainer);
		long after = System.nanoTime();
		
		long nanoSecs = after - before;
		long secs = nanoSecs / 1000000000;
		
		System.out.println("Training time: " + secs + " seconds");
		
		mse = networkTrainer.getAchievedError();
		iterations = networkTrainer.getIterations();
		
		return networkCopy;
	}
	
	public static float getMinErrAchieved(){
		return mse;
	}
	
	public static int getNumIterationsPerformed(){
		return iterations;
	}
}
