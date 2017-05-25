package appTest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;

import spellCheck.EditDistanceCalculator;

public class EditDistanceTest {

	@Test
	public void test() {
		final int actualDistance = 3;
		int calculatedDistance = EditDistanceCalculator.calculateLevenshteinDistance("kitten", "sitting", false);
		
		if (calculatedDistance != actualDistance){
			fail("Actual: " + actualDistance + ", Calculated: " + calculatedDistance);
		}
	}

}
