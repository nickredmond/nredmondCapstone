package imageProcessing;

public class ValueRange {
	private int startValue, endValue;

	public ValueRange(int startValue, int endValue){
		this.startValue = startValue;
		this.endValue = endValue;
	}
	
	public int getStartValue() {
		return startValue;
	}

	public int getEndValue() {
		return endValue;
	}
}
