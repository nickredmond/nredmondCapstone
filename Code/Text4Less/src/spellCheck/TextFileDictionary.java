package spellCheck;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileDictionary implements IDictionary {
	private List<String> words;
	private String filepath;
	
	public TextFileDictionary(String filepath){
		words = new ArrayList<String>();
		this.filepath = filepath;
	}
	
	@Override
	public boolean contains(String word){
		return words.contains(word);
	}
	
	@Override
	public void addWord(String word){
		words.add(word);
	}
	
	@Override
	public void removeWord(String word){
		words.remove(word);
	}

	@Override
	public void save() {
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filepath)));
			
			for (String nextWord : words){
				writer.write(nextWord + "\r\n");
			}
			
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getWords() {
		return words;
	}
}
