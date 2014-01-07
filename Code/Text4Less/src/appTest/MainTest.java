package appTest;

import neuralNetwork.NeuralNetwork;
import neuralNetwork.SigmoidActivationCalculator;

public class MainTest {

	public static void main(String[] args) {
		NeuralNetwork network = new NeuralNetwork(2, 1, 3, 2, true);
//		network.setWeightForNeuron(1, 0, 1, 0.98f);
//		network.setWeightForNeuron(1, 0, 2, 0.73f);
//		network.setWeightForNeuron(1, 0, 3, -0.11f);
//		network.setWeightForNeuron(1, 1, 1, 0.26f);
//		network.setWeightForNeuron(1, 1, 2, -0.98f);
//		network.setWeightForNeuron(1, 1, 3, 0.90f);
//		network.setWeightForNeuron(1, 2, 1, 0.98f);
//		network.setWeightForNeuron(1, 2, 2, 0.38f);
//		network.setWeightForNeuron(1, 2, 3, -0.05f);
//		
//		network.setWeightForNeuron(2, 0, 0, 0.5f);
//		network.setWeightForNeuron(2, 0, 1, -0.5f);
//		network.setWeightForNeuron(2, 1, 0, 0.7f);
//		network.setWeightForNeuron(2, 1, 1, 0.52f);
//		network.setWeightForNeuron(2, 2, 0, 0.02f);
//		network.setWeightForNeuron(2, 2, 1, 0.0f);
//		network.setWeightForNeuron(2, 3, 0, -0.22f);
//		network.setWeightForNeuron(2, 3, 1, 0.01f);
		int[] outputs = network.getOutputForInput(new float[]{0.1f, 0.7f});
		
		 SigmoidActivationCalculator calc = new SigmoidActivationCalculator();
		 System.out.println(calc.calculateActivationValue(0.76f));
		
		for (int i = 0; i < outputs.length; i++){
			System.out.println(outputs[i] + " ");
		}
	}

}
