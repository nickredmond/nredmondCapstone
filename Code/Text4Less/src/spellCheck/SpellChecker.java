package spellCheck;

public class SpellChecker {
	private static final int MAX_DISTANCE = 4;
	private static final char WORD_DELIMITER = ' ';
	private static final int NEWLINE_CODE = 10;
	private static final int RETURN_CODE = 13;
	
	public static String spellCheckText(String text){
		int currentWordStart = 0;
		int currentWordEnd = 0;
		boolean inWord = false;
		
		boolean isNewLine = false;
		boolean wasNewLine = false;
		
		String correctedText = text;
		
		for (int i = 0; i < correctedText.length(); i++){
			if (correctedText.charAt(i) != 10 || correctedText.charAt(i) != 13){
				
			}
			
			if (correctedText.charAt(i) != WORD_DELIMITER && !isNewLine(correctedText, i) && !inWord){
				currentWordStart = i;
				inWord = true;
			}
			else if (correctedText.charAt(i) == WORD_DELIMITER && inWord){
				//currentWordEnd = i;
				inWord = false;
				
				String word = correctedText.substring(((currentWordStart > 0) ? currentWordStart - 1 : 0), i);
				String before = correctedText.substring(0, currentWordStart);
				String after = correctedText.substring(i);
				
				word = word.trim();
				
				String correctedWord = (word.matches("^[0-9]+$") || word.length() < 2) ? word : correctSpelling(word);
				
				//System.out.println("n: " + word + ", c: " + correctedWord);
				
				correctedText = before + correctedWord + after;
				i += (correctedWord.length() - word.length());
			}
		}
		
		return correctedText;
	}
	
	private static boolean isNewLine(String correctedText, int index) {
		return correctedText.charAt(index) == NEWLINE_CODE || correctedText.charAt(index) == RETURN_CODE;
	}

	public static String correctSpelling(String word){
		IDictionary dictionary = DictionaryFactory.getDictionary(DictionaryFactory.PRIORITY_DICTIONARY_FILEPATH);
		String closestMatch = word;
		
		if (!dictionary.contains(word)){
			SimilarCharacterChecker checker = new SimilarCharacterChecker(dictionary);
			
			if (checker.canFixWord(word)){
				closestMatch = checker.getFixedWord();
			}
			else{
				int smallestDistance = -1;
				
				for (String nextWord : dictionary.getWords()){
					if (Math.abs(nextWord.length() - word.length()) <= MAX_DISTANCE){
						int editDistance = EditDistanceCalculator.calculateLevenshteinDistance(word, nextWord, false);
						
						if (editDistance < smallestDistance || smallestDistance < 0){
							smallestDistance = editDistance;
							closestMatch = nextWord;
						}
					}
				}
			}
		}
		
		return closestMatch;
	}
}
