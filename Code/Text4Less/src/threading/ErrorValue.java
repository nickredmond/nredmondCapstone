package threading;

public class ErrorValue {
	private float error;
	
	public ErrorValue(){
		error = 0;
	}
	
	public float getError(){
		return error;
	}
	
	public void addToError(float value){
		error += value;
	}
	
	public void resetError(){
		error = 0;
	}
}
