package neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NetworkLayer implements Serializable {
	private List<Neuron> neurons;
	
	public NetworkLayer(){
		neurons = new ArrayList<Neuron>();
	}

	public List<Neuron> getNeurons() {
		return neurons;
	}

	public void addNeuron(Neuron n){
		neurons.add(n);
	}
	
	public void removeNeuron(Neuron n){
		neurons.remove(n);
	}
}
