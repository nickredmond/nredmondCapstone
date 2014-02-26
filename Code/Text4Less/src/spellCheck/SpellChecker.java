package spellCheck;

public class SpellChecker {
	private static final int MAX_DISTANCE = 4;
	private static final char WORD_DELIMITER = ' ';
	private static final int NEWLINE_CODE = 10;
	private static final int RETURN_CODE = 13;
	
	public static String spellCheckText(String text){		
		String correctedText = "";
		
		String[] lines = text.split("\\r\\n");
		
		for (int i = 0; i < lines.length; i++){
			String nextLine = lines[i];
			String[] words = nextLine.split(" ");
			
			for (int j = 0; j < words.length; j++){
				String nextWord = words[j];
				String correctedWord = (nextWord.matches("^[0-9]+$") || nextWord.length() < 2) ? nextWord : correctSpelling(nextWord);
				correctedText += correctedWord;
				
				if (j < words.length - 1){
					correctedText += " ";
				}
			}
			
			if (i < lines.length - 1){
				correctedText += "\r\n";
			}
		}
		
		return correctedText;
	}
	
	private static boolean isNewLine(String correctedText, int index) {
		return correctedText.charAt(index) == NEWLINE_CODE || correctedText.charAt(index) == RETURN_CODE;
	}

	public static String correctSpelling(String word){
		if (word.startsWith("pancake")){
			int x = 0;
		}
		
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
