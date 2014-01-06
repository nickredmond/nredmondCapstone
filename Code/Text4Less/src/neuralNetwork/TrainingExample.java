package neuralNetwork;

public class TrainingExample {
	private int[] input, output;
	
	public TrainingExample(int[] input, int[] output){
		this.input = input;
		this.output = output;
	}
	
	public int[] getInput(){
		return input;
	}
	
	public int[] getOutput(){
		return output;
	}
}
