package genetics;

import java.util.Arrays;
import java.util.Random;

import math.ProbabilityCalculator;

public class BasicGeneticAlgorithm implements IGeneticAlgorithm {
	private float mutationRate, crossoverRate;
	private IFitnessCalculator fitnessCalc;
	private IChromosomeChooser chooser;
	
	private Chromosome[] lastCheckedChromosomes;
	
	private final float DEFAULT_CROSSOVER_RATE = 0.7f;
	private final float DEFAULT_MUTATION_RATE = 0.001f;
	
	private boolean isElitism = false;
	
	public BasicGeneticAlgorithm(IFitnessCalculator fitnessCalc, IChromosomeChooser chooser){
		this.fitnessCalc = fitnessCalc;
		this.chooser = chooser;
		
		crossoverRate = DEFAULT_CROSSOVER_RATE;
		mutationRate = DEFAULT_MUTATION_RATE;
	}
	
//	public Chromosome[] getLastCheckedChromosomes
	
	@Override
	public int[][] generateRandomChromosomeSet(int numberChromosomes,
			int chromosomeLength) {
		int[][] chromosomes = new int[numberChromosomes][chromosomeLength];
		Random rand = new Random();
		
		for (int i = 0; i < chromosomes.length; i++){
			for (int j = 0; j < chromosomes[0].length; j++){
				int nextGene = rand.nextInt(2);
				chromosomes[i][j] = nextGene;
			}
		}
		
		return chromosomes;
	}
	
	@Override
	public Chromosome[] getChromosomeArray(int[][] chromosomeValues){
		Chromosome[] chromosomes = new Chromosome[chromosomeValues.length];
		
		for (int i = 0; i < chromosomes.length; i++){
			float nextFitness = fitnessCalc.getFitness(chromosomeValues[i]);
			Chromosome nextChromosome = new Chromosome(chromosomeValues[i], nextFitness);
			chromosomes[i] = nextChromosome;
		}
		
		return chromosomes;
	}

	@Override
	public int[][] breed(Chromosome[] originalChromosomes) {
		int chromosomeLength =  originalChromosomes[0].getGenes().length;
		int[][] nextGeneration = new int[originalChromosomes.length][chromosomeLength]; //chooser.chooseNewChromosomeSet(originalChromosomes);
		
		int startingIndex = (isElitism ? 1 : 0);
		
		if (isElitism){
			float maxFitness = 0.0f;
			Chromosome fittest = null;
			
			for (int i = 0; i < originalChromosomes.length; i++){
				float nextFitness = originalChromosomes[i].getFitness();
				if (nextFitness > maxFitness){
					maxFitness = nextFitness;
					fittest = originalChromosomes[i];
				}
			}
			
			nextGeneration[0] = fittest.getGenes();
		}
		
		for (int n = startingIndex; n < originalChromosomes.length; n ++){
			int[] daughter = new int[chromosomeLength];
			
			int[] male = chooser.chooseChromosome(originalChromosomes);
			int[] female = chooser.chooseChromosome(originalChromosomes);
			
			boolean isMutation = ProbabilityCalculator.didEventHappen(mutationRate);
			boolean isCrossover = ProbabilityCalculator.didEventHappen(crossoverRate);
			
			Random rand = new Random();
			
			if (isCrossover){	
				//System.out.println("performed crossover");
				int crossoverIndex = getRandomIndex(rand, chromosomeLength);
				
				for (int i = 0; i < crossoverIndex; i++){
					daughter[i] = male[i];
				}
				for (int i = crossoverIndex; i < chromosomeLength; i++){
					daughter[i] = female[i];
				}
			}
			else daughter = male;
			
			if (isMutation){
				//System.out.println("performed mutation");
				int mutationIndex = getRandomIndex(rand, chromosomeLength);
				daughter[mutationIndex] = (daughter[mutationIndex] == 0) ? 1 : 0;
			}
			
			nextGeneration[n] = daughter;
		}
		
		if (originalChromosomes.length % 2 != 0){
			nextGeneration[nextGeneration.length - 1] = chooser.chooseChromosome(originalChromosomes);
		}
		
		return nextGeneration;
		
		// BREED
		// repeat n/2 times...
			// get fitness for each chromosome
			// choose 2 chromosomes w/ roulette selection
			// if crossover occurs, choose random point and swap bits
			// if mutation occurs, mutate at random gene
		// return result
	}

	private int getRandomIndex(Random rand, int chromosomeLength) {
		int randomIndex = (int)(rand.nextFloat() * chromosomeLength);
		if (randomIndex < 0){
			randomIndex = 0;
		}
		if (randomIndex >= chromosomeLength){
			randomIndex--;
		}
		return randomIndex;
	}

	@Override
	public void setMutationRate(float rate) {
		mutationRate = rate;
	}

	@Override
	public float getMutationRate() {
		return mutationRate;
	}

	@Override
	public void setCrossoverRate(float rate) {
		crossoverRate = rate;
	}

	@Override
	public float getCrossoverRate() {
		return crossoverRate;
	}

	public boolean isElitism() {
		return isElitism;
	}

	public void setElitism(boolean isElitism) {
		this.isElitism = isElitism;
	}

}
