package decisionTrees;

import java.util.Set;

import neuralNetwork.CharacterTrainingExample;

public abstract class MetaclassTreeFinder implements ITreeFinder {
	protected Set<CharacterTrainingExample> trainingSet, testSet;
	protected char[] classes;
	
	public MetaclassTreeFinder(Set<CharacterTrainingExample> trainingSet, Set<CharacterTrainingExample> testSet, char[] classes){
		this.trainingSet = trainingSet;
		this.testSet = testSet;
		this.classes = classes;
	}
}
