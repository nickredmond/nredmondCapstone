package app;

public class AlphaNumericCharacterConverter implements ICharacterConverter {
	private final int UPPER_START = 65;
	private final int UPPER_END = 90;
	private final int LOWER_START = 97;
	private final int LOWER_END = 122;
	private final int NUMERIC_START = 48;
	private final int NUMERIC_END = 57;
	private final int SPACE_INDEX = 62;
	private final char SPACE_VALUE = 32;
	
	public static final int NUMBER_CLASSES = 63;
	
	public int convertCharacterToClassNumber(char character) throws Exception{
		int asciiValue = (int)character;
		int classNumber = 0;
		
		if (asciiValue >= UPPER_START && asciiValue <= UPPER_END){
			classNumber = asciiValue - UPPER_START;
		}
		else if (asciiValue >= LOWER_START && asciiValue <= LOWER_END){
			classNumber = asciiValue - LOWER_START + (UPPER_END - UPPER_START + 1);
		}
		else if (asciiValue >= NUMERIC_START && asciiValue <= NUMERIC_END){
			classNumber = asciiValue - NUMERIC_START + (UPPER_END - UPPER_START + 1) + (LOWER_END - LOWER_START + 1);
		}
		else if (asciiValue == SPACE_VALUE){
			classNumber = SPACE_INDEX;
		}
		else throw new Exception("Character cannot be converted to class (not supported character)");
		
		return classNumber;
	}
	
	public char convertClassNumberToCharacter(int classNumber) throws Exception{
		int asciiValue = 0;
		
		if (classNumber <= (UPPER_END - UPPER_START)){
			asciiValue = UPPER_START + classNumber;
		}
		else if (classNumber > (UPPER_END - UPPER_START) && 
				classNumber <= (LOWER_END - LOWER_START) + (UPPER_END - UPPER_START + 1)){
			asciiValue = LOWER_START + (classNumber - (UPPER_END - UPPER_START + 1));
		}
		else if (classNumber > (LOWER_END - LOWER_START) + (UPPER_END - UPPER_START + 1) &&
				classNumber <= (NUMERIC_END - NUMERIC_START) + (LOWER_END - LOWER_START + 1) + (UPPER_END - UPPER_START + 1)){
			asciiValue = NUMERIC_START + (classNumber - ((LOWER_END - LOWER_START) + (UPPER_END - UPPER_START + 1) + 1));
		}
		else if (classNumber == SPACE_INDEX){
			asciiValue = SPACE_VALUE;
		}
		else throw new Exception("Class number is outside of designated range");
		
		return (char)asciiValue;
	}
}