package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.AlphaNumericCharacterConverter;

public class TrainingDataNameAssigner {
	private static final String EXTENSION_DELIMITER = "[.]";
	private static final String LOWER_PREFIX = "lower";
	private static final String IMAGE_EXTENSION = ".jpg";
	
	private static final int LOWER_START = 97;
	private static final int UPPER_START = 122;
	
	private static final int LOWER_SETNUM_INDEX = 6;
	private static final int NORMAL_SETNUM_INDEX = 1;
	private static final int LOWER_CHAR_INDEX = 5;
	private static final int NORMAL_CHAR_INDEX = 0;
	
	public static final int UPPER_LOWER_DIFFERENCE = 32;
	
	public static String assignName(File trainingDataDir, char character){
		File[] trainingImages = trainingDataDir.listFiles();
		List<Integer> takenNumbers = new ArrayList<Integer>();
		
		int charValue = (int)character;
		boolean isLower = (charValue >= LOWER_START && charValue <= UPPER_START);
		int trainingImgNumberIndex = (isLower ? LOWER_SETNUM_INDEX : NORMAL_SETNUM_INDEX);
		int trainingImgCharIndex = (isLower ? LOWER_CHAR_INDEX : NORMAL_CHAR_INDEX);
		
		for (int i = 0; i < trainingImages.length; i++){
			String nextFilename = trainingImages[i].getName();
			String[] nameParts = nextFilename.split(EXTENSION_DELIMITER);
			
			String nextName = nameParts[0];
			
			String indexCharacter = (nextName.length() > trainingImgNumberIndex) ? nextName.substring(trainingImgNumberIndex) : "0";
			char trainingImgChar = (nextName.startsWith(LOWER_PREFIX)) ? nextName.charAt(LOWER_CHAR_INDEX) : nextName.charAt(NORMAL_CHAR_INDEX);
			
			if (isLower && ((int)trainingImgChar >= AlphaNumericCharacterConverter.UPPER_START && 
					(int)trainingImgChar <= AlphaNumericCharacterConverter.UPPER_END)){
				trainingImgChar = (char) ((int)trainingImgChar + UPPER_LOWER_DIFFERENCE);
			}
			
			if (trainingImgChar == character && indexCharacter.toString().matches("^[0-9]+$")){
				int nextTrainingImgNumber = Integer.parseInt(indexCharacter.toString());
				takenNumbers.add(nextTrainingImgNumber);
			}
		}
		
		int availableImgNumber = getFirstAvailableNumber(takenNumbers);
		String assignedName = (isLower ? "lower" : "") + character + ((availableImgNumber == 0) ? "" : availableImgNumber) + IMAGE_EXTENSION;
		
		return assignedName;
	}
	
	private static int getFirstAvailableNumber(List<Integer> takenNumbers){
		int availableNumber = -1;
		int currentIndex = 0;
		boolean foundNumber = false;
		
		if (takenNumbers.size() > 0){
			Collections.sort(takenNumbers);
			
			do{
				int nextTakenNumber = takenNumbers.get(currentIndex);
				int nextPossibleValue = currentIndex;
				
				if (nextTakenNumber != nextPossibleValue){
					foundNumber = true;
					availableNumber = currentIndex;
				}
				currentIndex++;
			}while(!foundNumber && currentIndex < takenNumbers.size());
			
			if (availableNumber == -1){
				availableNumber = currentIndex + 1;
			}
		}
		else availableNumber = 0;
		
		return availableNumber;
	}
}
