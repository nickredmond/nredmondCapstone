package neuralNetwork;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import math.GpuNetworkCalculator;
import math.INetworkCalculator;

public class MatrixNeuralNetwork implements INeuralNetwork, Serializable {
	private float[] inputValues, outputValues;
	private float[][] hiddenValues;
	
	private float[] outputErrors;
	private float[][] hiddenErrors;
	
	private float[][] inputWeights, outputWeights;
	private float[][][] hiddenWeights;
	
	private float[][] inputDeltas, outputDeltas;
	private float[][][] hiddenDeltas;
	
	private final float MAX_RANDOM_WEIGHT = 0.5f;
	
	private INetworkCalculator calculator;

	public MatrixNeuralNetwork(int numberInputs, int numberHiddenLayers, int numberHiddenNeurons,
			int numberOutputs, boolean useRandomWeights){
		inputValues = new float[numberInputs + 1];
		outputValues = new float[numberOutputs];
		hiddenValues = new float[numberHiddenLayers][numberHiddenNeurons + 1];
		
		inputWeights = new float[numberInputs + 1][numberHiddenNeurons];
		outputWeights = new float[numberHiddenNeurons + 1][numberOutputs];
		hiddenWeights = new float[numberHiddenLayers - 1][numberHiddenNeurons + 1][numberHiddenNeurons];
		
		hiddenErrors = new float[numberHiddenLayers][numberHiddenNeurons + 1];
		outputErrors = new float[numberOutputs];
		
		inputDeltas = new float[numberInputs + 1][numberHiddenNeurons];
		outputDeltas = new float[numberHiddenNeurons + 1][numberOutputs];
		hiddenDeltas = new float[numberHiddenLayers - 1][numberHiddenNeurons + 1][numberHiddenNeurons];
		
		if (useRandomWeights){
			initializeRandomWeights();
		}
		
		//calculator = new GpuNetworkCalculator();
	}
	
	public int getNumberHiddenLayers(){
		return hiddenErrors.length;
	}
	
	public float[] getOutputForInput(float[] input){
		float[] output = forwardPropagate(input);
		float[] copiedOutput = new float[output.length];
		System.arraycopy(output, 0, copiedOutput, 0, output.length);
		return copiedOutput;
	}
	
	public float[] forwardPropagate(float[] input){
		if (input.length != inputValues.length - 1){
			System.out.println("Invalid input size! " + input.length + " " + inputValues.length);
		}
		for (int k = 1; k < inputValues.length && k < input.length; k++){
			inputValues[k] = input[k-1];
		}
		
		calculateActivationsForLayer(inputValues, hiddenValues[0], inputWeights, hiddenValues[0].length - 1, 1);
		for (int l = 0; l < hiddenWeights.length; l++){
			calculateActivationsForLayer(hiddenValues[l], hiddenValues[l+1], hiddenWeights[l], hiddenValues[l+1].length - 1, 1);
		}
		calculateActivationsForLayer(hiddenValues[hiddenValues.length - 1], outputValues, outputWeights, outputValues.length, 0);
		
		return outputValues;
	}
	
	private void calculateActivationsForLayer(float[] previousLayerVals, float[] currentLayerVals, 
			float[][] weights, int layerLength, int additive){
		float[] zValues = new float[layerLength];
		
		for (int k = 0; k < zValues.length; k++){
			float zValue = 0.0f;
			
			for (int index = 0; index < previousLayerVals.length; index++){
				zValue += previousLayerVals[index] * weights[index][k];
			}
			
			zValues[k] = zValue;
		}
		
		SigmoidActivationCalculator calc = new SigmoidActivationCalculator();
		for (int k = 0; k < zValues.length; k++){
			currentLayerVals[k+additive] = calc.calculateActivationValue(zValues[k]);
		}
	}
	
	private void initializeRandomWeights(){
		initializeWeightsForLayer(inputWeights);
		for (int i = 0; i < hiddenWeights.length; i++){
			initializeWeightsForLayer(hiddenWeights[i]);
		}
		initializeWeightsForLayer(outputWeights);
		
		initializeBias();
	}
	
	private void initializeBias(){
		inputValues[0] = 1.0f;
		
		for (int l = 0; l < hiddenValues.length; l++){
			hiddenValues[l][0] = 1.0f;
		}
	}
	
	private void initializeWeightsForLayer(float[][] layerWeights){
		Random rand = new Random();
		
		for (int i = 0; i < layerWeights.length; i++){
			for (int j = 0; j < layerWeights[0].length; j++){
				float nextWeight = rand.nextFloat() - (1.0f - (2 * MAX_RANDOM_WEIGHT)) - MAX_RANDOM_WEIGHT;
				layerWeights[i][j] = nextWeight;
			}
		}
	}
	
	// --- GETTERS AND SETTERS --- //
	
	public float[] getInputValues(){
		return inputValues;
	}
	
	public float[][] getInputWeights(){
		return inputWeights;
	}
	
	public float[][] getInputDeltas(){
		return inputDeltas;
	}
	
	public float[] getHiddenValues(int layer){
		return hiddenValues[layer];
	}
	
	public float[] getHiddenErrors(int layer){
		return hiddenErrors[layer];
	}
	
	public float[][] getHiddenWeights(int layer){
		return hiddenWeights[layer];
	}
	
	public float[][] getHiddenDeltas(int layer){
		return hiddenDeltas[layer];
	}
	
	public float[] getOutputValues(){
		return outputValues;
	}
	
	public float[] getOutputErrors(){
		return outputErrors;
	}
	
	public float[][] getOutputWeights(){
		return outputWeights;
	}
	
	public float[][] getOutputDeltas(){
		return outputDeltas;
	}
	
	public void setInputWeight(int i, int j, float value){
		inputWeights[i][j] = value;
	}
	
	public void setHiddenWeight(int layer, int i, int j, float value){
		hiddenWeights[layer][i][j] = value;
	}
	
	public void setOutputWeight(int i, int j, float value){
		inputWeights[i][j] = value;
	}
	
	public void setHiddenError(int layer, float[] value){
		hiddenErrors[layer] = value;
	}
	
	public void setOutputError(float[] error){
		outputErrors = error;
	}
	
	public void setInputDelta(float[][] value){
		inputDeltas = value;
	}
	
	public void setHiddenDelta(int layer, float[][] value){
		hiddenDeltas[layer] = value;
	}
	
	public void setOutputDelta(float[][] value){
		outputDeltas = value;
	}
	
	@Override
	public INeuralNetwork cloneNetwork() {
		INeuralNetwork network = new MatrixNeuralNetwork(
				inputValues.length - 1, hiddenValues.length, hiddenValues[0].length - 1, outputValues.length, true);
		
		float[] newInput = new float[inputValues.length];
		float[] newOutput = new float[outputValues.length];
		float[][] newHidden = new float[hiddenValues.length][hiddenValues[0].length];
		
		float[][] newInWeights = new float[inputWeights.length][inputWeights[0].length];
		float[][][] newHiddenWeights = (hiddenWeights.length > 0) ? 
			new float[hiddenWeights.length][hiddenWeights[0].length][hiddenWeights[0][0].length] : new float[0][0][0];
		float[][] newOutWeights = new float[outputWeights.length][outputWeights[0].length];
		
		System.arraycopy(inputValues, 0, newInput, 0, newInput.length);
		System.arraycopy(outputValues, 0, newOutput, 0, newOutput.length);
		
		for (int i = 0; i < newHidden.length; i++){
			System.arraycopy(hiddenValues[i], 0, newHidden[i], 0, newHidden[i].length);
		}
		for (int i = 0; i < newInWeights.length; i++){
			System.arraycopy(inputWeights[i], 0, newInWeights[i], 0, newInWeights[i].length);
		}
		for (int i = 0; i < newHiddenWeights.length; i++){
			for (int j = 0; j < newHiddenWeights[0].length; j++){
				System.arraycopy(hiddenWeights[i][j], 0, newHiddenWeights[i][j], 0, newHiddenWeights[i][j].length);
			}
		}
		for (int i = 0; i < newOutWeights.length; i++){
			System.arraycopy(outputWeights[i], 0, newOutWeights[i], 0, newOutWeights[i].length);
		}
		
		return network;
	}
	
	// -- IGNORE THIS STUFF -- //

	@Override
	public NetworkLayer getOutputLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NetworkLayer> getHiddenLayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkLayer getInputLayer() {
		// TODO Auto-generated method stub
		return null;
	}
}
