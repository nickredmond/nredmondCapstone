package decisionTrees;

import java.util.HashSet;
import java.util.Set;

import networkIOtranslation.FeatureExtractionIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import neuralNetwork.TrainingExample;

public class BasicTreeFinder extends MetaclassTreeFinder {
	private FeatureExtractionIOTranslator translator;
	private MatrixBackpropTrainer trainer;
	
	private final int LEFT_INDEX = 0;
	private final int RIGHT_INDEX = 1;
	
	private final int MAX_CLASS_SIZE = 3;
	
	public BasicTreeFinder(Set<CharacterTrainingExample> trainingSet,
			Set<CharacterTrainingExample> testSet, char[] classes) {
		super(trainingSet, testSet, classes);
		translator = new FeatureExtractionIOTranslator();
		trainer = new MatrixBackpropTrainer(0.05f, 0.02f);
	}

	@Override
	public IMetaclassTree getTree() {
		MetaclassNode rootNode = new MetaclassNode(classes, new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, 2, true));
		addNodes(rootNode, classes);
		
		IMetaclassTree tree = new BasicSplitMetaclassTree(rootNode, translator);
		
		return tree;
	}
	
	private void addNodes(MetaclassNode currentNode, char[] currentClasses){
		INeuralNetwork network = currentNode.getNetwork();
		
		char[][] combinedClasses = getCombinedClasses(currentClasses);
		Set<TrainingExample> trainingExamples = getFormattedExamples(combinedClasses, trainingSet);
		
		trainer.trainWithTrainingSet(network, trainingExamples, new HashSet<TrainingExample>());
		
		MetaclassNode leftNode = new MetaclassNode(combinedClasses[LEFT_INDEX], new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, 2, true));
		MetaclassNode rightNode = new MetaclassNode(combinedClasses[RIGHT_INDEX], new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, 2, true));
		
		currentNode.setLeftChild(leftNode);
		currentNode.setRightChild(rightNode);
		
		if (combinedClasses[LEFT_INDEX].length > MAX_CLASS_SIZE){
			addNodes(leftNode, combinedClasses[LEFT_INDEX]);
		}
		else setupLeafNode(leftNode);
		
		if (combinedClasses[RIGHT_INDEX].length > MAX_CLASS_SIZE){
			addNodes(rightNode, combinedClasses[RIGHT_INDEX]);
		}
		else setupLeafNode(rightNode);
	}
	
	private void setupLeafNode(MetaclassNode currentNode){
		currentNode.setNetwork(new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, currentNode.getClasses().length, true));
		currentNode.setLeafNode(true);
		char[] classes = currentNode.getClasses();
		
		Set<TrainingExample> examples = getLeafNodeExamples(classes, trainingSet);
		INeuralNetwork network = currentNode.getNetwork();
		trainer.trainWithTrainingSet(network, examples, new HashSet<TrainingExample>());
	}
	
	private Set<TrainingExample> getLeafNodeExamples(char[] classes, Set<CharacterTrainingExample> examples){
		Set<TrainingExample> formatted = new HashSet<TrainingExample>();
		
		for (CharacterTrainingExample nextExample : examples){
			int classNr = getClassNr(nextExample.getCharacterValue(), classes);
				
			if (classNr != -1){
				int[] output = new int[classes.length];
				
				for (int i = 0; i < output.length; i++){
					if (i == classNr){
						output[i] = 1;
					}
				}
				
				float[] input = translator.translateImageToNetworkInput(nextExample.getCharacterImage());
				
				formatted.add(new TrainingExample(input, output));
			}
		}
		
		return formatted;
	}
	
	private Set<TrainingExample> getFormattedExamples(char[][] combinedClasses, Set<CharacterTrainingExample> examples){
		Set<TrainingExample> formatted = new HashSet<TrainingExample>();
		
		for (CharacterTrainingExample nextExample : examples){
			int classNr = getClassNr(nextExample.getCharacterValue(), combinedClasses[LEFT_INDEX], combinedClasses[RIGHT_INDEX]);
				
			if (classNr != -1){
				int[] left = {1,0};
				int[] right = {0,1};
				
				float[] input = translator.translateImageToNetworkInput(nextExample.getCharacterImage());
				int[] output = (classNr == LEFT_INDEX) ? left : right;
				
				formatted.add(new TrainingExample(input, output));
			}
		}
		
		return formatted;
	}
	
	private char[][] getCombinedClasses(char[] currentClasses){
		int splitIndex = currentClasses.length / 2;
		char[] leftClasses = new char[splitIndex];
		char[] rightClasses = new char[currentClasses.length - leftClasses.length];
		char[][] combined = null;
		
//		int spaceIndex = -1;
//		boolean hasSpace = false;
//		for (int i = 0; i < currentClasses.length && !hasSpace; i++){
//			if (currentClasses[i] == ' '){
//				hasSpace = true;
//				spaceIndex = i;
//			}
//		}
		
//		if (!hasSpace){
			for (int i = 0; i < splitIndex; i++){
				leftClasses[i] = currentClasses[i];
			}
			for (int i = 0; i < rightClasses.length; i++){
				rightClasses[i] = currentClasses[i + splitIndex];
			}
			char[][] leftRightClasses = {leftClasses, rightClasses};
			combined = leftRightClasses;
//		}
//		else{
//			char[] left = {' '};
//			char[] right = new char[currentClasses.length - 1];
//			int currentIndex = 0;
//			
//			for (int i = 0; i < currentClasses.length; i++){
//				if (i != spaceIndex){
//					right[currentIndex] = currentClasses[i];
//					currentIndex++;
//				}
//			}
//			
//			
//			char[][] leftRightClasses = {left, right};
//			combined = leftRightClasses;
//		}
		
		
		return combined;
	}
	
	private int getClassNr(char character, char[] classes){
		int classNr = -1;
		boolean foundClass = false;
		
		if (character == ' '){
			int x = 0;
		}
		
		for (int i = 0; i < classes.length && !foundClass; i++){
			if (classes[i] == character){
				foundClass = true;
				classNr = i;
			}
		}
		
		return classNr;
	}
	
	private int getClassNr(char character, char[] leftClasses, char[] rightClasses){
		int classNr = -1;
		boolean foundClass = false;
		
		if (character == ' '){
			int x = 0;
		}
		
		for (int i = 0; i < leftClasses.length && !foundClass; i++){
			if (character == leftClasses[i]){
				foundClass = true;
				classNr = 0;
			}
		}
		for (int i = 0; i < rightClasses.length && !foundClass; i++){
			if (character == rightClasses[i]){
				foundClass = true;
				classNr = 1;
			}
		}
		
		return classNr;
	}
}