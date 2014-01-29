package appTest;

import neuralNetwork.NeuralNetwork;

import org.junit.Test;

public class TestNeuralNetwork {

	@Test
	public void testWithoutWeights() {
		NeuralNetwork network1 = new NeuralNetwork(3, 5, 3, 1, false);
		float[] testinput = {0.1f, 0.2f, 0.7f};
		
		float[] yes = network1.getOutputForInput(testinput);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalArgument(){
		NeuralNetwork network = new NeuralNetwork(1, 0, 4, 5, false);
	}
	
	@Test 
	public void testRandomWeights(){
		NeuralNetwork network1 = new NeuralNetwork(3, 5, 3, 1, true);
		float[] testinput = {0.1f, 0.2f, 0.7f};
		
		float[] yes = network1.getOutputForInput(testinput);
	}
}
