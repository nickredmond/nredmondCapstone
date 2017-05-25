package spellCheck;

import debug.FeatureExtractionDebug;

public class EditDistanceCalculator {
	public static int calculateLevenshteinDistance(String startingWord, String endingWord, boolean caseSensitive){
		if (!caseSensitive){
			startingWord = startingWord.toLowerCase();
			endingWord = endingWord.toLowerCase();
		}
		
		char[] startingChars = startingWord.toCharArray();
		char[] endingChars = endingWord.toCharArray();
		
		int[][] distances = new int[endingChars.length + 1][startingChars.length + 1];
		
		for (int j = 1; j < endingWord.length() + 1; j++){
			distances[j][0] = j;
		}
		for (int i = 1; i < startingWord.length() + 1; i++){
			distances[0][i] = i;
		}
		
		for (int j = 1; j <= startingChars.length; j++){
			for (int i = 1; i <= endingChars.length; i++){
				distances[i][j] = (startingChars[j-1] == endingChars[i-1]) ? distances[i-1][j-1] : findCheapestOperation(distances, i, j);
			}
		}

		return distances[distances.length - 1][distances[0].length - 1];
	}
	
	private static int findCheapestOperation(int[][] distances, int i, int j){
		int substitutionCost = distances[i-1][j] + 1;
		int insertionCost = distances[i][j-1] + 1;
		int deletionCost = distances[i-1][j-1] + 1;
		
		return min(substitutionCost, insertionCost, deletionCost);
	}
	
	private static int min(int... arguments){
		int result = 0;
		boolean hasBeenSet = false;
		
		for (int i = 0; i < arguments.length; i++){
			if (!hasBeenSet || arguments[i] < result){
				hasBeenSet = true;
				result = arguments[i];
			}
		}
		
		return result;
	}
}
