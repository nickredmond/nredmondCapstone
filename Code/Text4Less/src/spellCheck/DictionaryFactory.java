package spellCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryFactory {
	public static final String DICTIONARY_FILEPATH = "C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Vendor/dictionaries/dictionary.txt";
	public static final String COMMON_DICTIONARY_FILEPATH = "C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Vendor/dictionaries/commonDictionary.txt";
	public static final String PRIORITY_DICTIONARY_FILEPATH = "C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Vendor/dictionaries/priorityictionary.txt";
	
	public static IDictionary getDictionary(String filepath){
		IDictionary dictionary = new TextFileDictionary(filepath);
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
			String nextLine = "";
			
			while ((nextLine = reader.readLine()) != null){
				dictionary.addWord(nextLine);
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return dictionary;
	}
}
