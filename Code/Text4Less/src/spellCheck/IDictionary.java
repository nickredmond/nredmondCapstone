package spellCheck;

import java.util.List;

public interface IDictionary {
	public List<String> getWords();
	public void addWord(String word);
	public void removeWord(String word);
	public boolean contains(String word);
	public void save();
}
