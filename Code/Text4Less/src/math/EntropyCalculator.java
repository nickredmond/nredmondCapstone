package math;

import java.util.ArrayList;
import java.util.List;

public class EntropyCalculator {
	public static float calculateShannonEntropy(String input, char[] classes){
		float entropySum = 0;
		
		for (int i = 0; i < classes.length; i++){
			float nextClassProbability = probability(classes[i], input);
			entropySum += (nextClassProbability == 0) ? 0 :
					(nextClassProbability * GeneralMath.logarithm(classes.length, nextClassProbability));
		}
		
		return (entropySum * -1);
	}
	
	public static float calculateShannonEntropy(String input){
		List<Character> encounteredClasses = new ArrayList<Character>();
		int numberClasses = 0;
		
		for (int i = 0; i < input.length(); i++){
			char nextCharacter = input.charAt(i);
			
			if (!encounteredClasses.contains(nextCharacter)){
				encounteredClasses.add(nextCharacter);
				numberClasses++;
			}
		}
		
		char[] classes = new char[encounteredClasses.size()];
		
		for (int i = 0; i < classes.length; i++){
			classes[i] = encounteredClasses.get(i);
		}
		
		return calculateShannonEntropy(input, classes);
	}
	
	private static float probability(char symbol, String input){
		int totalOccurrences = 0;
		
		for (int i = 0; i < input.length(); i++){
			if (input.charAt(i) == symbol){
				totalOccurrences++;
			}
		}
		
		return (float)totalOccurrences / input.length();
	}
}
