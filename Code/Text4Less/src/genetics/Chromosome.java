package genetics;

public class Chromosome {
	private int[] genes;
	private float fitness;
	
	public Chromosome(int[] genes, float fitness){
		this.genes = genes;
		this.fitness = fitness;
	}
	
	public int[] getGenes() {
		return genes;
	}
	public void setGenes(int[] genes) {
		this.genes = genes;
	}
	public float getFitness() {
		return fitness;
	}
	public void setFitness(float fitness) {
		this.fitness = fitness;
	}
}
