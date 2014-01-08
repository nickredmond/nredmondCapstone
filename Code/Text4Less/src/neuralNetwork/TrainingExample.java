package neuralNetwork;

public class TrainingExample {
	private float[] input;
	private int[] output;
	
	public TrainingExample(float[] input, int[] output){
		this.input = input;
		this.output = output;
	}
	
	public float[] getInput(){
		return input;
	}
	
	public int[] getOutput(){
		return output;
	}
}
