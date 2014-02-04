package app;

import java.util.ArrayList;
import java.util.List;

public class ReadResult {
	private String translationString;
	private List<CharacterResult> rejections;
	
	public ReadResult(){
		rejections = new ArrayList<CharacterResult>();
		translationString = "NO TRANSLATION PERFORMED";
	}
	
	public String getTranslationString() {
		return translationString;
	}
	public void setTranslationString(String translationString) {
		this.translationString = translationString;
	}
	
	public List<CharacterResult> getRejections(){
		return rejections;
	}
	
	public void addRejection(CharacterResult rejection){
		rejections.add(rejection);
	}
}
