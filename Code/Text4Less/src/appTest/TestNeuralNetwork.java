package appTest;

import java.util.HashSet;
import java.util.Set;

import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.TrainingExample;

import org.junit.Test;

public class TestNeuralNetwork {

	@Test
	public void testWithoutWeights() {
		NeuralNetwork network1 = new NeuralNetwork(3, 5, 3, 1, false);
		float[] testinput = {0.1f, 0.2f, 0.7f};
		
		float[] yes = network1.forwardPropagate(testinput);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalArgument(){
		NeuralNetwork network = new NeuralNetwork(1, 0, 4, 5, false);
	}
	
	@Test 
	public void testRandomWeights(){
		NeuralNetwork network1 = new NeuralNetwork(3, 5, 3, 1, true);
		float[] testinput = {0.1f, 0.2f, 0.7f};
		
		float[] yes = network1.forwardPropagate(testinput);
	}
	
	@Test
	public void testMatrixNetwork(){
		Set<TrainingExample> examples = new HashSet<TrainingExample>();
		
		float[] input1 = {1, 1};
		float[] input2 = {1, 0};
		float[] input3 = {0, 1};
		float[] input4 = {0, 0};
		
		int[] output1 = {0};
		int[] output2 = {1};
		int[] output3 = {1};
		int[] output4 = {0};
		
		examples.add(new TrainingExample(input1, output1));
		examples.add(new TrainingExample(input2, output2));
		examples.add(new TrainingExample(input3, output3));
		examples.add(new TrainingExample(input4, output4));
		
		MatrixNeuralNetwork net = new MatrixNeuralNetwork(2, 1, 3, 1, true);
		MatrixBackpropTrainer t = new MatrixBackpropTrainer(0.05f, 0.02f);
		
		t.trainWithTrainingSet(net, examples, new HashSet<TrainingExample>());
		
		float[] test1 = input3;
		float[] test2 = input4;
		
		float[] doutput1 = net.getOutputForInput(test1);
		float[] doutput2 = net.getOutputForInput(test2);
		
		assert(doutput1[0] > 0.5f && doutput2[0] < 0.5f);
	}
}
