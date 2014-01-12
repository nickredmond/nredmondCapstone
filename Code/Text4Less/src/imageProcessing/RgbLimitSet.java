package imageProcessing;

public class RgbLimitSet {
	private long lowestRgbValue, highestRgbValue;
	
	public RgbLimitSet(long low, long high){
		lowestRgbValue = low;
		highestRgbValue = high;
	}

	public long getLowestRgbValue() {
		return lowestRgbValue;
	}

	public long getHighestRgbValue() {
		return highestRgbValue;
	}
	
	public long getRgbRange(){
		return highestRgbValue - lowestRgbValue;
	}
}
