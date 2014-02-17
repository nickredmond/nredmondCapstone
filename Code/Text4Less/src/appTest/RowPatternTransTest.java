package appTest;

import static org.junit.Assert.*;
import imageProcessing.RowPatternTransformer;

import java.util.Arrays;

import org.junit.Test;

public class RowPatternTransTest {

	@Test
	public void test() {
		int[] pattern1 = {0,1,1,1,0};
		int[] pattern2 = {1,1,0,1,1};
		int[] pattern3 = {1,1,1,1,1};
		int[] pattern4 = {1,1,0,0,1};
		int[] pattern5 = {0,0,0,0,0};
		
		int[] test1 = {0,1,0,1,0};
		int[] test2 = {1,0,0,0,1};
		int[] test3 = {1,0,1,0,1};
		int[] test4 = {1,0,0,0,1};
		int[] test5 = {0,0,0,0,0};
		
		int[] result1 = RowPatternTransformer.getTransformationFor(pattern1, 2);
		int[] result2 = RowPatternTransformer.getTransformationFor(pattern2, 2);
		int[] result3 = RowPatternTransformer.getTransformationFor(pattern3, 3);
		int[] result4 = RowPatternTransformer.getTransformationFor(pattern4, 2);
		int[] result5 = RowPatternTransformer.getTransformationFor(pattern5, 0);
		
		org.junit.Assert.assertArrayEquals(result1, test1);
		org.junit.Assert.assertArrayEquals(result2, test2);
		org.junit.Assert.assertArrayEquals(result3, test3);
		org.junit.Assert.assertArrayEquals(result4, test4);
		org.junit.Assert.assertArrayEquals(result5, test5);
	}

}