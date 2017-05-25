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
		
		String attempt1 = word.replace('I', 'l');
		
		if (dictionary.contains(attempt1)){
			canFix = true;
			cache = attempt1;
		}
		else cache = "";
		
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
