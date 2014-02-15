package imageProcessing;

import java.util.Arrays;

public class RowPatternTransformer {
	private static final int MAX_CROSSINGS = 4;
	
	private static final int[][] PATTERNS =
		{
			{1,1,1,1,1},
			{1,1,1,1,0},
			{0,1,1,1,1},
			{1,1,1,0,0},
			{0,1,1,1,0},
			{0,0,1,1,1},
			{1,1,0,0,0},
			{0,1,1,0,0},
			{0,0,1,1,0},
			{0,0,0,1,1},
			{1,0,1,1,0},
			{1,0,0,1,1},
			{1,1,0,1,0},
			{1,1,0,0,1},
			{0,1,0,1,1},
			{0,1,1,0,1},
			{1,1,1,0,1},
			{1,1,0,1,1}
		};
	
	private static final int[][][] TRANSFORMATIONS =
		{
			{{1,1,1,1,1},{0,1,0,1,0},{1,0,1,0,1},{1,1,0,1,1}},
			{{1,1,1,1,1},{1,0,0,1,0},{1,1,0,1,0},{1,1,1,1,0}},
			{{1,1,1,1,1},{0,1,0,0,1},{0,1,0,1,1},{0,1,1,1,1}},
			{{1,1,1,0,0},{1,0,1,0,0},{1,1,1,0,0},{1,1,1,0,0}},
			{{0,1,1,1,0},{0,1,0,1,0},{0,1,1,1,0},{0,1,1,1,0}},
			{{0,0,1,1,1},{0,0,1,0,1},{0,0,1,1,1},{0,0,1,1,1}},
			{{1,0,0,0,0},{1,1,0,0,0},{1,1,0,0,0},{1,1,0,0,0}},
			{{0,1,1,0,0},{0,1,1,0,0},{0,1,1,0,0},{0,1,1,0,0}},
			{{0,0,1,1,0},{0,0,1,1,0},{0,0,1,1,0},{0,0,1,1,0}},
			{{0,0,0,0,1},{0,0,0,1,1},{0,0,0,1,1},{0,0,0,1,1}},
			{{1,1,1,1,0},{1,0,1,0,0},{1,0,1,0,1},{1,0,1,1,0}},
			{{1,0,0,1,1},{1,0,0,0,1},{1,0,1,0,1},{1,1,0,1,1}},
			{{1,1,1,1,0},{1,0,0,1,0},{1,0,1,0,1},{1,1,0,1,1}},
			{{1,1,0,0,1},{1,0,0,0,1},{1,0,1,0,1},{1,1,0,1,1}},
			{{0,1,1,1,1},{0,1,0,1,0},{1,0,1,0,1},{1,1,0,1,1}},
			{{0,1,1,1,1},{0,1,0,0,1},{1,0,1,0,1},{0,1,1,0,1}},
			{{1,1,1,1,1},{0,1,0,0,1},{1,0,1,0,1},{1,1,0,1,1}},
			{{1,1,1,1,1},{1,0,0,0,1},{1,0,1,0,1},{1,1,0,1,1}}
		};
	
	public static int[] getTransformationFor(int[] original, int numberCrossings){
		boolean foundIndex = false;
		int patternIndex = 0;
		int[] transformation = null;
		
		for (int i = 0; i < PATTERNS.length && !foundIndex; i++){
			if (Arrays.equals(original, PATTERNS[i])){
				foundIndex = true;
				patternIndex = i;
			}
		}
		
		transformation = canTransform(foundIndex, numberCrossings) ? 
			TRANSFORMATIONS[patternIndex][numberCrossings - 1] : original;
		
		return transformation;
	}
	
	private static boolean canTransform(boolean foundIndex, int numberCrossings){
		return (numberCrossings > 0 && numberCrossings <= MAX_CROSSINGS && foundIndex);
	}
}
