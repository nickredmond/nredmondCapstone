package math;

public interface INetworkCalculator {
	public float[] calculateSigmoidValues(float[] zValues);
	public float[] calculateErrorValues(float[][] weights, float[] nextLayerError);
	public void close();
}
