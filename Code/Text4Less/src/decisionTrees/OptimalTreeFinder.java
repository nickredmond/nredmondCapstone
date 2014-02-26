package decisionTrees;

import genetics.BasicGeneticAlgorithm;
import genetics.Chromosome;
import genetics.IChromosomeChooser;
import genetics.IFitnessCalculator;
import genetics.MetaclassFitnessCalculator;
import genetics.TournamentChooser;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import networkIOtranslation.FeatureExtractionIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import neuralNetwork.TrainingExample;

public class OptimalTreeFinder extends MetaclassTreeFinder {
	private float minRequiredFitness;
	private int numberLeafClasses;
	private IChromosomeChooser chooser;

	private final float DEFAULT_REQUIRED_FITNESS = 0.15f;
	private final int DEFAULT_NUMBER_LEAF_CLASSES = 3;
	protected float bestFitness;
	
	FeatureExtractionIOTranslator translator;
	
	public OptimalTreeFinder(Set<CharacterTrainingExample> trainingSet, Set<CharacterTrainingExample> testSet, char[] classes){
		super(trainingSet, testSet, classes);
		
		chooser = new TournamentChooser();
		
		minRequiredFitness = DEFAULT_REQUIRED_FITNESS;
		numberLeafClasses = DEFAULT_NUMBER_LEAF_CLASSES;
		
		translator = new FeatureExtractionIOTranslator();
	}
	
	@Override
	public IMetaclassTree getTree(){
		INeuralNetwork network = 
				new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, classes.length, true);
		
		MetaclassNode rootNode = new MetaclassNode(classes, network);
		addNodesToTree(rootNode);
		
		return new GeneticMetaclassTree(rootNode, translator);
	}
	
	private void addNodesToTree(MetaclassNode parent){
		char[] parentClasses = parent.getClasses();
		
		IFitnessCalculator fitnessCalc = new MetaclassFitnessCalculator(trainingSet, testSet, parentClasses);
		BasicGeneticAlgorithm geneticAlgo = new BasicGeneticAlgorithm(fitnessCalc, chooser);
		int[][] genePool = geneticAlgo.generateRandomChromosomeSet(parentClasses.length / 2, parentClasses.length);
		
		Chromosome[] chromosomes = null;	
		boolean isTargetReached = false;
		
		while (!isTargetReached){
			chromosomes = geneticAlgo.getChromosomeArray(genePool);
			
			bestFitness = 0.0f; // FOR TESTING
			
			for (int i = 0; i < chromosomes.length && !isTargetReached; i++){
				float fitness = chromosomes[i].getFitness();
				
				if (fitness > bestFitness) // FOR TESTING
					bestFitness = fitness; // FOR TESTING
				
				isTargetReached = (fitness >= minRequiredFitness);
			}
			
			new Thread(){
				public void run(){
					JOptionPane.showMessageDialog(null, "Completed Iteration: " + OptimalTreeFinder.this.bestFitness);
				}
			}.start();
			
			if (!isTargetReached){
				genePool = geneticAlgo.breed(chromosomes);
			}
		}
		
		Chromosome bestChromosome = getFittestChromosome(chromosomes);
		
		char[][] combinedClasses = MetaclassConverter.getLeftRightClasses(bestChromosome.getGenes(), classes);
		char[] leftClasses = combinedClasses[MetaclassConverter.LEFT_CLASS_INDEX];
		char[] rightClasses = combinedClasses[MetaclassConverter.RIGHT_CLASS_INDEX];
		
		INeuralNetwork trainedNetwork = ((MetaclassFitnessCalculator)fitnessCalc).getTrainedNetworksForSubclasses(leftClasses, rightClasses); // NetworkSet
		parent.setNetwork(trainedNetwork);
		
		MetaclassNode leftChild = new MetaclassNode(leftClasses, null);
		MetaclassNode rightChild = new MetaclassNode(rightClasses, null);
		
		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);
		
		if (leftClasses.length <= numberLeafClasses){
			prepareLeafNode(leftChild);
		}
		else addNodesToTree(leftChild);
		
		if (rightClasses.length <= numberLeafClasses){
			prepareLeafNode(rightChild);
		}
		else addNodesToTree(rightChild);
	}
	
	private void prepareLeafNode(MetaclassNode leafNode){
		MatrixBackpropTrainer trainer = new MatrixBackpropTrainer(0.05f, 0.02f);
		leafNode.setLeafNode(true);
		INeuralNetwork network = new MatrixNeuralNetwork(new FeatureExtractionIOTranslator().getInputLength(), 1, 100, leafNode.getClasses().length, true);
		Set<TrainingExample> examples = getLeafNodeExamples(leafNode.getClasses(), trainingSet);
		
		trainer.trainWithTrainingSet(network, examples, new HashSet<TrainingExample>());
		leafNode.setNetwork(network);
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
			int classNr = getClassNr(nextExample.getCharacterValue(), combinedClasses[0], combinedClasses[1]);
				
			if (classNr != -1){
				int[] left = {1,0};
				int[] right = {0,1};
				
				float[] input = translator.translateImageToNetworkInput(nextExample.getCharacterImage());
				int[] output = (classNr == 0) ? left : right;
				
				formatted.add(new TrainingExample(input, output));
			}
		}
		
		return formatted;
	}
	
	private char[][] getCombinedClasses(char[] currentClasses){
		int splitIndex = currentClasses.length / 2;
		char[] leftClasses = new char[splitIndex];
		char[] rightClasses = new char[currentClasses.length - leftClasses.length];
		
		for (int i = 0; i < splitIndex; i++){
			leftClasses[i] = currentClasses[i];
		}
		for (int i = 0; i < rightClasses.length; i++){
			rightClasses[i] = currentClasses[i + splitIndex];
		}
		
		char[][] combined = {leftClasses, rightClasses};
		return combined;
	}
	
	private int getClassNr(char character, char[] classes){
		int classNr = -1;
		boolean foundClass = false;
		
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
	
	private Chromosome getFittestChromosome(Chromosome[] chromosomes){
		float maxFitness = 0.0f;
		Chromosome fittest = null;
		
		for (int i = 0; i < chromosomes.length; i++){
			if (chromosomes[i].getFitness() > maxFitness){
				maxFitness = chromosomes[i].getFitness();
				fittest = chromosomes[i];
			}
		}
		
		return fittest;
	}
	
	public void setNumberLeafClasses(int numberClasses){
		numberLeafClasses = numberClasses;
	}

	public float getMinRequiredFitness() {
		return minRequiredFitness;
	}

	public void setMinRequiredFitness(float minRequiredFitness) {
		this.minRequiredFitness = minRequiredFitness;
	}
}
