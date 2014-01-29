package math;

public class GeneralMath {
	private final static int EXPANSION_ORDER = 39;
	
	public static float arcTangent(float value){
		float atan = 0.0f;
		
		if (Math.abs(value) > 1){
			atan = (float)(Math.PI / 2) - normalizedArcTangent(1.0f / value);
		}
		else atan = normalizedArcTangent(value);
		
		return atan;
	}
	
	private static float normalizedArcTangent(float value){
		int currentOperation = 1;
		float arcTan = 0.0f;
		
		for (int power = 1; power <= EXPANSION_ORDER; power += 2){
			arcTan += currentOperation * (Math.pow(value, power) / power);
			currentOperation *= -1;
		}
		
		return arcTan;
	}
	
	public static int factorial(int value){
		int factorial = 0;

		if (value < 1){
			factorial = 0;
		}
		else if (value == 1){
			factorial = 1;
		}
		else factorial = value * factorial(value - 1);
		
		return factorial;
	}
}