package spellCheck;

public class SimilarCharacterChecker {
	private IDictionary dictionary;
	private String cache;
	
	private boolean hasFixedWord = false;
	
	public SimilarCharacterChecker(IDictionary dictionary){
		this.dictionary = dictionary;
	}
	
	public boolean canFixWord(String word){
		boolean canFix = false;
		
		String[] attempts = {word.replace('I', 'l'), word.replace('1', 'l')};
		
		for (int i = 0; i < attempts.length && !canFix; i++){
			if (dictionary.contains(attempts[i])){
				canFix = true;
				cache = attempts[i];
			}
		}
		
		if (!canFix){
			cache = "";
		}
		
		hasFixedWord = false;
		return canFix;
	}
	
	public String getFixedWord(){
		String word = null;
		
		if (hasFixedWord){
			throw new IllegalStateException("Did not check word before fixing.");
		}
		else{
			hasFixedWord = true;
			word = cache;
		}
		
		return word;
	}
}
