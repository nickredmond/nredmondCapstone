package decisionTrees;

import genetics.BasicGeneticAlgorithm;
import genetics.Chromosome;
import genetics.IChromosomeChooser;
import genetics.IFitnessCalculator;
import genetics.MetaclassFitnessCalculator;
import genetics.TournamentChooser;

import java.util.Set;

import javax.swing.JOptionPane;

import networkIOtranslation.FeatureExtractionIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixNeuralNetwork;

public class OptimalTreeFinder {
	private Set<CharacterTrainingExample> trainingSet, testSet;
	private float minRequiredFitness;
	private char[] classes;
	private int numberLeafClasses;
	private IChromosomeChooser chooser;

	private final float DEFAULT_REQUIRED_FITNESS = 0.1f;
	private final int DEFAULT_NUMBER_LEAF_CLASSES = 3;
	protected float bestFitness;
	
	public OptimalTreeFinder(Set<CharacterTrainingExample> trainingSet, Set<CharacterTrainingExample> testSet, char[] classes){
		this.trainingSet = trainingSet;
		this.testSet = testSet;
		this.classes = classes;
		
		chooser = new TournamentChooser();
		
		minRequiredFitness = DEFAULT_REQUIRED_FITNESS;
		numberLeafClasses = DEFAULT_NUMBER_LEAF_CLASSES;
	}
	
	public MetaclassTree getOptimalTree(){
		FeatureExtractionIOTranslator translator = new FeatureExtractionIOTranslator();
		INeuralNetwork network = 
				new MatrixNeuralNetwork(translator.getInputLength(), 1, 100, classes.length, true);
		
		MetaclassNode rootNode = new MetaclassNode(classes, network);
		addNodesToTree(rootNode);
		
		return new MetaclassTree(rootNode, translator);
	}
	
	private void addNodesToTree(MetaclassNode parent){
		char[] parentClasses = parent.getClasses();
		
		IFitnessCalculator fitnessCalc = new MetaclassFitnessCalculator(trainingSet, testSet, parentClasses);
		BasicGeneticAlgorithm geneticAlgo = new BasicGeneticAlgorithm(fitnessCalc, chooser);
		int[][] genePool = geneticAlgo.generateRandomChromosomeSet(parentClasses.length, parentClasses.length);
		
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
		
		NetworkSet trainedNetworks = ((MetaclassFitnessCalculator)fitnessCalc).getTrainedNetworksForSubclasses(leftClasses, rightClasses);
		
		MetaclassNode leftChild = new MetaclassNode(leftClasses, trainedNetworks.getLeftNetwork());
		MetaclassNode rightChild = new MetaclassNode(rightClasses, trainedNetworks.getRightNetwork());
		
		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);
		
		if (leftClasses.length <= numberLeafClasses){
			leftChild.setLeafNode(true);
		}
		else addNodesToTree(leftChild);
		
		if (rightClasses.length <= numberLeafClasses){
			leftChild.setLeafNode(true);
		}
		else addNodesToTree(rightChild);
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
