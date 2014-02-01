package threading;

import java.util.ArrayList;
import java.util.List;

public class DeltaValues {
	private List<float[][]> inputDeltas, outputDeltas;
	private List<float[][][]> hiddenDeltas;
	
	private float[][] inputTotal, outputTotal;
	private float[][][] hiddenTotal;
	
	private ErrorValue errorValue;
	
	public DeltaValues(){
		inputDeltas = new ArrayList<float[][]>();
		hiddenDeltas = new ArrayList<float[][][]>();
		outputDeltas = new ArrayList<float[][]>();
		
		errorValue = new ErrorValue();
	}
	
	public void addInputDeltaValues(float[][] input){
		inputDeltas.add((float[][])input);
	}
	
	public void addHiddenDeltaValues(float[][][] input){
		hiddenDeltas.add(input);
	}
	
	public void addOutputDeltaValues(float[][] input){
		outputDeltas.add(input);
	}

	public List<float[][]> getInputDeltas() {
		return inputDeltas;
	}

	public void setInputDeltas(List<float[][]> inputDeltas) {
		this.inputDeltas = inputDeltas;
	}

	public List<float[][][]> getHiddenDeltas() {
		return hiddenDeltas;
	}

	public void setHiddenDeltas(List<float[][][]> hiddenDeltas) {
		this.hiddenDeltas = hiddenDeltas;
	}

	public List<float[][]> getOutputDeltas() {
		return outputDeltas;
	}

	public void setOutputDeltas(List<float[][]> outputDeltas) {
		this.outputDeltas = outputDeltas;
	}

	public float[][] getInputTotal() {
		return inputTotal;
	}

	public void setInputTotal(float[][] inputTotal) {
		this.inputTotal = inputTotal;
	}

	public float[][] getOutputTotal() {
		return outputTotal;
	}

	public void setOutputTotal(float[][] outputTotal) {
		this.outputTotal = outputTotal;
	}

	public float[][][] getHiddenTotal() {
		return hiddenTotal;
	}

	public void setHiddenTotal(float[][][] hiddenTotal) {
		this.hiddenTotal = hiddenTotal;
	}

	public ErrorValue getError() {
		return errorValue;
	}

	public void setError(ErrorValue error) {
		this.errorValue = error;
	}
}