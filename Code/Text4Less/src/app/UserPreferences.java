package app;

import imageHandling.ImageReadMethod;

import java.io.Serializable;

public class UserPreferences implements Serializable{
	private boolean isAccuracyPromptEnabled;
	private boolean isSpellCheckEnabled;
	private ImageReadMethod readMethod;
	
	public UserPreferences(boolean accuracyPrompt, boolean spellCheck, ImageReadMethod method){
		readMethod = method;
		isSpellCheckEnabled = spellCheck;
		isAccuracyPromptEnabled = accuracyPrompt;
	}
	
	public boolean isAccuracyPromptEnabled() {
		return isAccuracyPromptEnabled;
	}
	public boolean isSpellCheckEnabled() {
		return isSpellCheckEnabled;
	}
	public ImageReadMethod getReadMethod() {
		return readMethod;
	}
}
