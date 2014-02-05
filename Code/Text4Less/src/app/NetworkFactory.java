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
	public static INeuralNetwork getTrainedNetwork(INeuralNetwork originalNetwork, INetworkIOTranslator translator,
			CharacterType type, INetworkTrainer networkTrainer) throws IOException{
		INeuralNetwork networkCopy = originalNetwork.cloneNetwork();
		
		Set<CharacterTrainingExample> trainingSet = TrainingDataReader.createTrainingSetFromFile(type);
		Set<CharacterTrainingExample> testSet = TrainingDataReader.createTestSetFromFile(type);
		CharacterNetworkTrainer trainer = new CharacterNetworkTrainer(translator);
		
		for (CharacterTrainingExample nextExample : trainingSet){
			trainer.addTrainingExample(nextExample);
		}
		for(CharacterTrainingExample nextExample : testSet){
			trainer.addTestExample(nextExample);
		}
		
		long before = System.nanoTime();
		trainer.trainNeuralNetwork(networkCopy, networkTrainer);
		long after = System.nanoTime();
		
		long nanoSecs = after - before;
		long secs = nanoSecs / 1000000000;
		
		System.out.println("Training time: " + secs + " seconds");
		
		return networkCopy;
	}
}
