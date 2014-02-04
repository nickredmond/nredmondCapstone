package app;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;

public class CharacterResult {
	private BufferedImage image;
	private TranslationResult result;
	
	private boolean isRejected;
	
	public CharacterResult(BufferedImage img, TranslationResult result){
		image = img;
		this.result = result;
	}

	public BufferedImage getImage() {
		return image;
	}

	public TranslationResult getResult() {
		return result;
	}

	public boolean isRejected() {
		return isRejected;
	}

	public void setRejected(boolean isRejected) {
		this.isRejected = isRejected;
	}
}
