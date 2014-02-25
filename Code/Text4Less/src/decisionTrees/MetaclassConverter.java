package decisionTrees;

public class MetaclassConverter {
	public static final int LEFT_CLASS_INDEX = 0;
	public static final int RIGHT_CLASS_INDEX = 1;
	
//	public static char translateSplitOutputToCharacter(float[] output, char[] leftClass)
	
	public static char translateOutputToCharacter(float[] output, char[] classes){
		if (output.length != classes.length){
			throw new IllegalArgumentException("Output must be same length as classes.");
		}
		
		int index = 0;
		float maxValue = 0.0f;
		
		for (int i = 0; i < output.length; i++){
			if (output[i] > maxValue){
				maxValue = output[i];
				index = i;
			}
		}
		
		return classes[index];
	}
	
	public static char[][] getLeftRightClasses(int[] chromosome, char[] classes){
		int numLeftClasses = 0;
		int numRightClasses = 0;
		
		for (int i = 0; i < chromosome.length; i++){
			if (chromosome[i] == 0){
				numLeftClasses++;
			}
			else numRightClasses++;
		}
		
		char[] leftClasses = new char[numLeftClasses];
		char[] rightClasses = new char[numRightClasses];
		
		int currentLeftIndex = 0;
		int currentRightIndex = 0;
		
		for (int i = 0; i < chromosome.length; i++){
			if (chromosome[i] == 0){
				leftClasses[currentLeftIndex] = classes[i];
				currentLeftIndex++;
			}
			else{
				rightClasses[currentRightIndex] = classes[i];
				currentRightIndex++;
			}
		}
		
		char[][] combined = {leftClasses, rightClasses};
		return combined;
	}
}
