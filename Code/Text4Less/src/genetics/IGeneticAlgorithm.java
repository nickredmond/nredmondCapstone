package genetics;

public interface IGeneticAlgorithm {
	public int[][] generateRandomChromosomeSet(int numberChromosomes, int chromosomeLength);
	public int[][] breed(Chromosome[] originalChromosomes);
	public Chromosome[] getChromosomeArray(int[][] chromosomeValues);
	public void setMutationRate(float rate);
	public float getMutationRate();
	public void setCrossoverRate(float rate);
	public float getCrossoverRate();
	public boolean isElitism();
	public void setElitism(boolean isElitism);
}
