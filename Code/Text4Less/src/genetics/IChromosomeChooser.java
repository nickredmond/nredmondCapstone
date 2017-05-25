package genetics;

public interface IChromosomeChooser {
	public int[] chooseChromosome(Chromosome[] chromosomes);
	public int[][] chooseNewChromosomeSet(Chromosome[] chromosomes);
}
