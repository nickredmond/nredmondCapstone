package neuralNetwork;

import java.util.LinkedList;
import java.util.List;

public class Neuron {
	private float value, errorValue;
	private List<NeuronConnection> inputConnections, outputConnections;
	private boolean isBias;
	
	public Neuron(){
		inputConnections = new LinkedList<NeuronConnection>();
		outputConnections = new LinkedList<NeuronConnection>();
	}
	
	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public List<NeuronConnection> getInputConnections() {
		return inputConnections;
	}

	public void setInputConnections(List<NeuronConnection> inputConnections) {
		this.inputConnections = inputConnections;
	}

	public List<NeuronConnection> getOutputConnections() {
		return outputConnections;
	}

	public void setOutputConnections(List<NeuronConnection> outputConnections) {
		this.outputConnections = outputConnections;
	}
	
	public void addInputConnection(NeuronConnection conn){
		inputConnections.add(conn);
	}
	
	public void addOutputConnection(NeuronConnection conn){
		outputConnections.add(conn);
	}

	public boolean isBias() {
		return isBias;
	}

	public void setBias(boolean isBias) {
		this.isBias = isBias;
		
		if (isBias){
			setValue(1.0f);
		}
	}

	public float getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(float errorValue) {
		this.errorValue = errorValue;
	}
}
