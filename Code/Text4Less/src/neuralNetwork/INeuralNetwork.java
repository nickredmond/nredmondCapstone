package neuralNetwork;

import java.util.List;

public interface INeuralNetwork {
	public float[] getInputValues();
	public float[][] getInputWeights();
	public float[][] getInputDeltas();
	public float[] getHiddenValues(int layer);
	public float[] getHiddenErrors(int layer);
	public float[][] getHiddenWeights(int layer);
	public float[][] getHiddenDeltas(int layer);
	public float[] getOutputValues();
	public float[] getOutputErrors();
	public float[][] getOutputWeights();
	public float[][] getOutputDeltas();
	public void setInputWeight(int i, int j, float value);
	public void setHiddenWeight(int layer, int i, int j, float value);
	public void setOutputWeight(int i, int j, float value);
	public void setHiddenError(int layer, float[] value);
	public void setOutputError(float[] value);
	public void setInputDelta(float[][] value);
	public void setHiddenDelta(int layer, float[][] value);	
	public void setOutputDelta(float[][] value);
	
	public int getNumberHiddenLayers();
	public float[] forwardPropagate(float[] input);
	
	public INeuralNetwork cloneNetwork();
	
	// -- LEGACY -- //
	public NetworkLayer getOutputLayer();
	public List<NetworkLayer> getHiddenLayers();
	public NetworkLayer getInputLayer();
}