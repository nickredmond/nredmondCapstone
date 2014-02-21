package genetics;

import java.util.Arrays;
import java.util.Random;

public class RouletteChromosomeChooser implements IChromosomeChooser{
	private final int MAX_NUMBER_SLICES = 10;
	
	@Override
	public int[] chooseChromosome(Chromosome[] chromosomes) {
		float totalFitness = 0.0f;
		
		for (int i = 0; i < chromosomes.length; i++){
			totalFitness += chromosomes[i].getFitness();
		}
		
		float[] relativeFitness = new float[chromosomes.length];
		
		for (int i = 0; i < chromosomes.length; i++){
			relativeFitness[i] = (chromosomes[i].getFitness() / totalFitness);
		}
		
		float[] probabilityDistribution = new float[relativeFitness.length];
		float relativeSum = 0.0f;
		
		for (int i = 0; i < relativeFitness.length; i++){
			relativeSum += relativeFitness[i];
			probabilityDistribution[i] = relativeSum;
		}
		
		//System.out.println(Arrays.toString(probabilityDistribution));
		Random rand = new Random();
		
		float probabilityValue = rand.nextFloat();
		boolean foundValue = false;
		
		int[] chosenChromosome = null;
		
		for (int j = 0; j < chromosomes.length && !foundValue; j++){
			if (probabilityValue <= probabilityDistribution[j]){
				chosenChromosome = chromosomes[j].getGenes();
				foundValue = true;
				System.out.println("Fitness: " + chromosomes[j].getFitness() + " " + j);
			}
		}
		
		return chosenChromosome;
		
//		int totalSlices = 0;
//		
//		float maxFitness = 0.0f;
//		float minFitness = 1.0f;
//		
//		for (int i = 0; i < chromosomes.length; i++){
//			float fitness = chromosomes[i].getFitness();
//			if (fitness > maxFitness){
//				maxFitness = fitness;
//			}
//			if (fitness < minFitness){
//				minFitness = fitness;
//			}
//		}
//		
//		float fitnessDifference = maxFitness - minFitness;
//		float differencePerSlice = fitnessDifference / MAX_NUMBER_SLICES;
//		
//		for (int i = 0; i < chromosomes.length; i++){
//			int nextSliceNumber = (int)((chromosomes[i].getFitness() - minFitness) / differencePerSlice);
//		//	System.out.println("next: " + nextSliceNumber);
//			totalSlices += (nextSliceNumber > 0) ? nextSliceNumber : 1;
//		}
//		
//		int[][] rouletteChromosomeSet = new int[totalSlices][chromosomes[0].getGenes().length];
//		int setIndex = 0;
//		
//		for (int i = 0; i < chromosomes.length; i++){
//			int numSlices = (int)((chromosomes[i].getFitness() - minFitness) / differencePerSlice);
//			System.out.println("son of a bith: " + chromosomes[i].getFitness() + " " + numSlices);
//			
//			if (numSlices == 0)
//				numSlices++;
//			
//			for (int j = 0; j < numSlices; j++){
//				rouletteChromosomeSet[setIndex] = chromosomes[i].getGenes();
//				setIndex++;
//			}
//		}
//		
//		Random rand = new Random();
//		int index = 0;
//		try{
//		index = rand.nextInt(rouletteChromosomeSet.length);
//		}catch(IllegalArgumentException e){
//			int x = 0;
//		}
//		
//		return rouletteChromosomeSet[index];
	}

	@Override
	public int[][] chooseNewChromosomeSet(Chromosome[] chromosomes) {
		int[][] chromosomeSet = new int[chromosomes.length][chromosomes[0].getGenes().length];
		
		for (int i = 0; i < chromosomeSet.length; i++){
			chromosomeSet[i] = chooseChromosome(chromosomes);
		}
		
		return chromosomeSet;
	}
}