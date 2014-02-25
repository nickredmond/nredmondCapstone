package networkIOtranslation;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;

public interface INetworkIOTranslator {
	public TranslationResult translateNetworkOutputToCharacter(float[] output);
	public int[] translateCharacterToNetworkOutput(char c);
	public float[] translateImageToNetworkInput(BufferedImage img);
	public int getInputLength();
}
