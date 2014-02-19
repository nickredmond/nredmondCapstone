package genetics;

import java.util.Random;

import math.ProbabilityCalculator;

public class BasicGeneticAlgorithm implements IGeneticAlgorithm {
	private float mutationRate, crossoverRate;
	private IFitnessCalculator fitnessCalc;
	private IChromosomeChooser chooser;
	
	private Chromosome[] lastCheckedChromosomes;
	
	private final float DEFAULT_CROSSOVER_RATE = 0.7f;
	private final float DEFAULT_MUTATION_RATE = 0.001f;
	
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
		int[][] nextGeneration = chooser.chooseNewChromosomeSet(originalChromosomes);
		
		for (int n = 0; n < originalChromosomes.length; n += 2){
			int[] male = nextGeneration[n];
			int[] female = nextGeneration[n+1];
			
			boolean isMutation = ProbabilityCalculator.didEventHappen(mutationRate);
			boolean isCrossover = ProbabilityCalculator.didEventHappen(crossoverRate);
			
			Random rand = new Random();
			int chromosomeLength =  originalChromosomes[0].getGenes().length;
			
			if (isCrossover){	
				//System.out.println("performed crossover");
				int crossoverIndex = getRandomIndex(rand, chromosomeLength);
				
				for (int i = crossoverIndex; i < chromosomeLength; i++){
					int maleGene = male[i];
					int femaleGene = female[i];
					
					male[i] = femaleGene;
					female[i] = maleGene;
				}
			}
			if (isMutation){
				//System.out.println("performed mutation");
				int mutationIndex = getRandomIndex(rand, chromosomeLength);
				
				male[mutationIndex] = (male[mutationIndex] == 1 ? 0 : 1);
				female[mutationIndex] = (female[mutationIndex] == 1 ? 0 : 1);
			}
			
			nextGeneration[n] = male;
			nextGeneration[n+1] = female;
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

}
