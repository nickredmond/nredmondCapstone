package neuralNetwork;

import java.io.Serializable;

public class NeuronConnection implements Serializable {
	private Neuron leftConnector, rightConnector;
	private float weight, deltaValue;
	
	public NeuronConnection(Neuron leftConnector, Neuron rightConnector, float weight){
		this.leftConnector = leftConnector;
		this.rightConnector = rightConnector;
		this.weight = weight;
	}
	
	public Neuron getLeftConnector() {
		return leftConnector;
	}
	public void setLeftConnector(Neuron connector) {
		this.leftConnector = connector;
	}
	public Neuron getRightConnector() {
		return rightConnector;
	}
	public void setRightConnector(Neuron connector) {
		this.rightConnector = connector;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getDeltaValue() {
		return deltaValue;
	}

	public void setDeltaValue(float deltaValue) {
		this.deltaValue = deltaValue;
	}
}
