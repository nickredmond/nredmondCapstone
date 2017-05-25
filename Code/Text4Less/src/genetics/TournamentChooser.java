package genetics;

import java.util.Random;

public class TournamentChooser implements IChromosomeChooser {

	@Override
	public int[] chooseChromosome(Chromosome[] chromosomes) {
		Chromosome fittestChromosome = null;
		float maxFitness = 0.0f;
		
		Random rand = new Random();
		
		for (int i = 0; i < chromosomes.length; i++){
			int nextChromosomeIndex = rand.nextInt(chromosomes.length);
			
			float nextFitness = chromosomes[nextChromosomeIndex].getFitness();
			if (nextFitness > maxFitness){
				maxFitness = nextFitness;
				fittestChromosome = chromosomes[nextChromosomeIndex];
			}
		}
	//	System.out.println("fittest: " + fittestChromosome.getFitness());
		
		return fittestChromosome.getGenes();
	}

	@Override
	public int[][] chooseNewChromosomeSet(Chromosome[] chromosomes) {
		int[][] newSet = new int[chromosomes.length][chromosomes[0].getGenes().length];
		
		for (int i = 0; i < chromosomes.length; i++){
			newSet[i] = chooseChromosome(chromosomes);
		}
		
		return newSet;
	}

}
