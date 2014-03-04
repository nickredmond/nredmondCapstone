package app;

import io.CharacterType;
import io.TrainingDataReader;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
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
		return getTrainedNetwork(originalNetwork, translator, type, networkTrainer, 10000, 0.03f);
	}
	
	public static INeuralNetwork getTrainedNetwork(INeuralNetwork originalNetwork, INetworkIOTranslator translator,
			File trainingSetDirectory, INetworkTrainer networkTrainer, int numIterations, float errorGoal) throws IOException{
		Set<CharacterTrainingExample> trainingSet = TrainingDataReader.createTriningSetFromFile(trainingSetDirectory);
		Set<CharacterTrainingExample> testSet = new HashSet<CharacterTrainingExample>();
		
		return getTrainedNetworkWithExamples(originalNetwork, translator, trainingSet, testSet, networkTrainer, numIterations, errorGoal);
	}
	
	public static INeuralNetwork getTrainedNetworkWithExamples(INeuralNetwork originalNetwork, INetworkIOTranslator translator,
			Set<CharacterTrainingExample> trainingSet, Set<CharacterTrainingExample> testSet, 
			INetworkTrainer networkTrainer, int numIterations, float errorGoal) throws IOException{
		CharacterNetworkTrainer trainer = new CharacterNetworkTrainer(translator);
		
		for (CharacterTrainingExample nextExample : trainingSet){
			trainer.addTrainingExample(nextExample);
		}
		for(CharacterTrainingExample nextExample : testSet){
			trainer.addTestExample(nextExample);
		}
		
		networkTrainer.setErrorGoal(errorGoal);
		networkTrainer.setIterations(numIterations);
		
		INeuralNetwork networkCopy = originalNetwork.cloneNetwork();
		trainer.trainNeuralNetwork(networkCopy, networkTrainer);
		
		mse = networkTrainer.getAchievedError();
		iterations = networkTrainer.getIterations();
		
		return networkCopy;
	}
	
	public static INeuralNetwork getTrainedNetwork(INeuralNetwork originalNetwork, INetworkIOTranslator translator,
			CharacterType type, INetworkTrainer networkTrainer, int numIterations, float errorGoal) throws IOException{		
		Set<CharacterTrainingExample> trainingSet = TrainingDataReader.createTrainingSetFromFile(type);
//		Set<CharacterTrainingExample> trainingSet2 = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII3);
//		trainingSet.addAll(trainingSet2);
		Set<CharacterTrainingExample> testSet = TrainingDataReader.createTestSetFromFile(type);
		
		return getTrainedNetworkWithExamples(originalNetwork, translator, trainingSet, testSet, networkTrainer, numIterations, errorGoal);
	}
	
	public static float getMinErrAchieved(){
		return mse;
	}
	
	public static int getNumIterationsPerformed(){
		return iterations;
	}
}
