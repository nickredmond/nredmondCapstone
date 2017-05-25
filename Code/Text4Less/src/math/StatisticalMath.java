package math;

import java.util.LinkedList;
import java.util.List;

public class StatisticalMath {
	private static final float MAX_ALLOWABLE_DEVIATIONS = 2.0f;
	
	public static float average(float[] values){
		return average(convertArrayToList(values));
	}
	
	public static float average(List<Float> values){
		float sum = 0.0f;
		
		for (Float nextValue : values){
			sum += nextValue;
		}
		
		return sum / values.size();
	}
	
	private static List<Float> convertArrayToList(float[] values){
		List<Float> valueList = new LinkedList<Float>();
		for (int i = 0; i < values.length; i++){
			valueList.add(values[i]);
		}
		
		return valueList;
	}
	
	public static float standardDeviation(float[] values){
		return standardDeviation(convertArrayToList(values));
	}
	
	public static float standardDeviation(List<Float> values){
		float mean = average(values);
		
		List<Float> squaredDifferences = new LinkedList<Float>();
		
		for (float nextValue : values){
			float nextSquaredDifference = (mean - nextValue) * (mean - nextValue);
			squaredDifferences.add(nextSquaredDifference);
		}
		
		float meanSquaredDifference = average(squaredDifferences);
		float stdDev = (float)Math.sqrt(meanSquaredDifference);
		
		return stdDev;
	}
	
	public static float numberStandardDeviationsFromMean(float stdDev, float mean, float value){
		return (value - mean)/stdDev;
	}
	
	public static boolean isOutlier(float value, float[] valueSet){
		return isOutlier(value, convertArrayToList(valueSet));
	}
	
	public static boolean isOutlier(float value, List<Float> valueSet){
		float mean = average(valueSet);
		float stdDev = standardDeviation(valueSet);
		float numStdDevsFromMean = numberStandardDeviationsFromMean(stdDev, mean, value);
		
		return (numStdDevsFromMean > MAX_ALLOWABLE_DEVIATIONS);
	}
}
