package imageProcessing;

public class TranslationResult {
	private char result;
	private float confidence;
	
	public TranslationResult(char result, float confidence){
		this.result = result;
		this.confidence = confidence;
	}
	
	public TranslationResult(){
		result = ' ';
	}
	
	public char getResult() {
		return result;
	}
	public float getConfidence() {
		return confidence;
	}
	
	@Override
	public String toString(){
		return result + "(" + confidence + ")";
	}
}