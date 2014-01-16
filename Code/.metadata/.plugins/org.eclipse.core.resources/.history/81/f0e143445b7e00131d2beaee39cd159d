package appTest;

import static org.junit.Assert.*;

import org.junit.Test;

import app.StatisticalMath;

public class TestStatisticalMath {

	@Test
	public void testStdDev() {
		float[] values = {1, 3, 5, 5, 7, 8, 9, 25};
		float desiredStdDev = 6.92f;
		float tolerance = 0.01f;
		
		float stdDev = StatisticalMath.standardDeviation(values);
		
		assert(Math.abs(desiredStdDev - stdDev) < tolerance);
	}

}
