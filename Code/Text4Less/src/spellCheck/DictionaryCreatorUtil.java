package spellCheck;

public class DictionaryCreatorUtil {
	
	
	public static void createCommonPriorityDictionary(){
		IDictionary fullDictionary = DictionaryFactory.getDictionary(DictionaryFactory.DICTIONARY_FILEPATH);
		IDictionary commonDictionary = DictionaryFactory.getDictionary(DictionaryFactory.COMMON_DICTIONARY_FILEPATH);
		IDictionary priorityDictionary = new TextFileDictionary(DictionaryFactory.PRIORITY_DICTIONARY_FILEPATH);
		
		for (String nextWord : commonDictionary.getWords()){
			if (fullDictionary.contains(nextWord)){
				fullDictionary.removeWord(nextWord);
			}
			priorityDictionary.addWord(nextWord);
		}

		for (String nextWord : fullDictionary.getWords()){
			priorityDictionary.addWord(nextWord);
		}
		
		priorityDictionary.save();
	}
}
