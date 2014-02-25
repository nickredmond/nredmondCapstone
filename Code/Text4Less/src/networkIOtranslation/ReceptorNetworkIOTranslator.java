package networkIOtranslation;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.util.List;

import receptors.Receptor;
import receptors.ReceptorFilter;

public class ReceptorNetworkIOTranslator implements INetworkIOTranslator {
	private List<Receptor> receptors;
	
	public ReceptorNetworkIOTranslator(List<Receptor> receptors){
		this.receptors = receptors;
	}
	
	@Override
	public TranslationResult translateNetworkOutputToCharacter(float[] output) {
		return new AlphaNumericIOTranslator().translateNetworkOutputToCharacter(output);
	}

	@Override
	public int[] translateCharacterToNetworkOutput(char c) {
		return new AlphaNumericIOTranslator().translateCharacterToNetworkOutput(c);
	}

	@Override
	public float[] translateImageToNetworkInput(BufferedImage img) {
		float[] input = new float[receptors.size()];
		
		for (int i = 0; i < input.length; i++){
			Receptor nextReceptor = receptors.get(i);
			boolean crosses = ReceptorFilter.crossesImage(nextReceptor, img);
			
			input[i] = (crosses ? 0.5f : -0.5f);
		}
		
		return input;
	}

	@Override
	public int getInputLength() {
		return receptors.size();
	}

}
