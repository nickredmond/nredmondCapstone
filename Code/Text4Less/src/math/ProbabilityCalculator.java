package math;

import java.util.Random;

public class ProbabilityCalculator {
	private final static int TOTAL_PIECES = 10000;
	
	public static boolean didEventHappen(float probability){
		int numberEventSlices = (int)(probability * TOTAL_PIECES);
		Random rand = new Random();
		int eventNumber = rand.nextInt(TOTAL_PIECES);
		
		return (eventNumber < numberEventSlices);
	}
}
