package neuralNetwork;

public class SigmoidActivationCalculator implements IActivationFunctionCalculator{

	@Override
	public float calculateActivationValue(float zValue) {
		return (float) (1 / (1 + Math.pow(Math.E, zValue * -1.0)));
	}

}
