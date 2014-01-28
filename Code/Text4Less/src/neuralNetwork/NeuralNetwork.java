package neuralNetwork;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NeuralNetwork implements Serializable, Cloneable {
	NetworkLayer inputLayer, outputLayer;
	List<NetworkLayer> hiddenLayers;
	private final int FIRST_HIDDEN_LAYER_INDEX = 0;
	private final float MAX_RANDOM_WEIGHT = 1.0f;
	private final float ACTIVATION_BOUNDARY = 0.5f;
	
	@Override
	public NeuralNetwork clone(){
		int numInputs = inputLayer.getNeurons().size() - 1;
		int numHiddenLayers = hiddenLayers.size();
		int numNeuronsPerHidden = hiddenLayers.get(0).getNeurons().size() - 1;
		int numberOutputs = outputLayer.getNeurons().size();
		boolean isRandom = true;
		
		return new NeuralNetwork(numInputs, numHiddenLayers, numNeuronsPerHidden,
				numberOutputs, isRandom);
	}
	
	public NetworkLayer getOutputLayer(){
		return outputLayer;
	}
	
	public List<NetworkLayer> getHiddenLayers(){
		return hiddenLayers;
	}
	
	public NetworkLayer getInputLayer(){
		return inputLayer;
	}
	
	public NeuralNetwork(int numberInputs, int numberHiddenLayers,
			int numNeuronsPerHiddenLayer, int numberOutputs,
			boolean isRandomWeights){
		if (numberInputs == 0 || numberHiddenLayers == 0 || numNeuronsPerHiddenLayer == 0 
				|| numberOutputs == 0){
			throw new IllegalArgumentException("Illegal arguments for neural network.");
		}
		
		inputLayer = createBlankLayer(numberInputs);
		setupHiddenLayers(numberHiddenLayers, numNeuronsPerHiddenLayer);
		setupOutputLayer(numberOutputs);
		
		setupConnections(isRandomWeights);
	}
	
	/** -- ATTENTION: THIS IS A TEST METHOD --
	 * 
	 * @param layerNumber - one based index
	 * @param neuronNumber - zero based index
	 */
	public void setWeightForNeuron(int layerNumber, int firstNeuronNumber, int secondNeuronNumber,
			float weight){
		NetworkLayer selectedLayer = null;
		NetworkLayer nextLayer = null;
		
		if (layerNumber == 1){
			selectedLayer = inputLayer;
			nextLayer = hiddenLayers.get(0);
		}
		else if (layerNumber == hiddenLayers.size() + 2){ // hiddenlayers + input + output
			selectedLayer = outputLayer;
		}
		else{
			selectedLayer = hiddenLayers.get(layerNumber - 2);
			nextLayer = (hiddenLayers.size() > layerNumber - 1) ? hiddenLayers.get(layerNumber - 1) :
				outputLayer;
		}
		
		Neuron firstNeuron = selectedLayer.getNeurons().get(firstNeuronNumber);
		Neuron secondNeuron = (nextLayer == null) ? null : nextLayer.getNeurons().get(secondNeuronNumber);
		
		if (secondNeuron != null){
			for (NeuronConnection nextConnection : firstNeuron.getOutputConnections()){
				if (nextConnection.getRightConnector() == secondNeuron){
					nextConnection.setWeight(weight);
				}
			}
		}
	}
	
	private void setupConnections(boolean isRandomWeights){
		setupConnectionsBetweenLayers(inputLayer, hiddenLayers.get(FIRST_HIDDEN_LAYER_INDEX),
				isRandomWeights);
		
		for (int i = 0; i < hiddenLayers.size() - 1; i++){
			setupConnectionsBetweenLayers(hiddenLayers.get(i), hiddenLayers.get(i + 1),
					isRandomWeights);
		}
		
		int lastHiddenLayerIndex = hiddenLayers.size() - 1;
		setupConnectionsBetweenLayers(hiddenLayers.get(lastHiddenLayerIndex),
				outputLayer, isRandomWeights);
	}
	
	private void setupConnectionsBetweenLayers(NetworkLayer firstLayer, NetworkLayer secondLayer,
			boolean isRandomWeights){
		Random rand = new Random();
		
		for (Neuron nextLeftNeuron : firstLayer.getNeurons()){			
			for (Neuron nextRightNeuron : 
				secondLayer.getNeurons()){
				if (!nextRightNeuron.isBias()){
					float weight = (isRandomWeights ? (rand.nextFloat() / (1.0f / MAX_RANDOM_WEIGHT) * 2)
							- MAX_RANDOM_WEIGHT : 0.0f);
					
					NeuronConnection conn = new NeuronConnection(nextLeftNeuron, nextRightNeuron, weight);
					nextLeftNeuron.addOutputConnection(conn);
					nextRightNeuron.addInputConnection(conn);
				}
			}
		}
	}
	
	private void setupHiddenLayers(int numberHiddenLayers, 
			int numNeuronsPerLayer){
		hiddenLayers = new LinkedList<NetworkLayer>();
		
		for (int i = 0; i < numberHiddenLayers; i++){
			hiddenLayers.add(createBlankLayer(numNeuronsPerLayer));
		}
	}
	
	private void setupOutputLayer(int numberOutputs){
		outputLayer = new NetworkLayer();
		
		for (int i = 0; i < numberOutputs; i++){
			outputLayer.addNeuron(new Neuron());
		}
	}
	
	private NetworkLayer createBlankLayer(int numberOfNeurons){
		NetworkLayer layer = new NetworkLayer();
		
		Neuron biasNeuron = new Neuron();
		biasNeuron.setBias(true);
		layer.addNeuron(biasNeuron);
		
		for (int i = 0; i < numberOfNeurons; i++){
			layer.addNeuron(new Neuron());
		}
		
		return layer;
	}
	
	private float[] normalizeNetworkInput(float[] input){
		float[] normalizedInput = new float[inputLayer.getNeurons().size() - 1];
		float normalizationRatio = (float)input.length / normalizedInput.length;
		
		int numInputNeuronsCopied = 0;
		int normalizedInputIndex = 0;
		float inputSumForIndex = 0.0f;
		
		for (int i = 0; i < input.length; i++){
			inputSumForIndex += input[i];
			numInputNeuronsCopied++;
					
			if ((float)numInputNeuronsCopied / normalizationRatio >= 1.0f){
				normalizedInput[normalizedInputIndex] = inputSumForIndex / (float)numInputNeuronsCopied;
				
				inputSumForIndex = 0.0f;
				numInputNeuronsCopied = 0;
				normalizedInputIndex++;
			}
		}
		
		return normalizedInput;
	}
	
	public float[] getOutputForInput(float[] input){
		List<Neuron> inputNeurons = inputLayer.getNeurons();
		float[] normalizedInput = new float[inputNeurons.size()];
		
		if (input.length != inputNeurons.size() - 1){
			normalizedInput = normalizeNetworkInput(input);
		}
		else normalizedInput = input;
		
		for (int i = 1; i <= normalizedInput.length; i++){
			inputNeurons.get(i).setValue(normalizedInput[i - 1]);
		}
		
		forwardPropagate();
		
		List<Neuron> outputNeurons = outputLayer.getNeurons();
		float[] output = new float[outputNeurons.size()];
		
		for (int i = 0; i < output.length; i++){
			float nextOutputValue = outputNeurons.get(i).getValue();
			output[i] = nextOutputValue;
		}
		
		return output;
	}
	
	public float[] getNormalizedOutput(){
		List<Neuron> outputNeurons = outputLayer.getNeurons();
		float[] output = new float[outputNeurons.size()];
		
		for (int i = 0; i < output.length; i++){
			output[i] = outputNeurons.get(i).getValue();
		}
		
		return output;
	}
	
	private void forwardPropagate(){
		for (NetworkLayer nextLayer : hiddenLayers){			
			for (Neuron nextNeuron : nextLayer.getNeurons()){
				calculateActivationForNeuron(nextNeuron);
			}
		}
		
		for (Neuron nextNeuron : outputLayer.getNeurons()){
			calculateActivationForNeuron(nextNeuron);
		}
	}

	private void calculateActivationForNeuron(Neuron nextNeuron) {
		if (!nextNeuron.isBias()){
			float zValue = 0.0f;
			
			for (NeuronConnection nextConnection : nextNeuron.getInputConnections()){
				zValue += nextConnection.getWeight() * 
						nextConnection.getLeftConnector().getValue();
				
				SigmoidActivationCalculator calc = new SigmoidActivationCalculator();
				float activationValue = calc.calculateActivationValue(zValue);
				nextNeuron.setValue(activationValue);
			}
		}
	}
}
