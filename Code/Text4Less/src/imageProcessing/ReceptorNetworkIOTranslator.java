package imageProcessing;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

public class ReceptorNetworkIOTranslator implements INetworkIOTranslator {
	private List<Point> receptors;
	
	public ReceptorNetworkIOTranslator(List<Point> receptors){
		this.receptors = receptors;
	}
	
	@Override
	public char translateNetworkOutputToCharacter(float[] output) {
		return new NetworkIOTranslator().translateNetworkOutputToCharacter(output);
	}

	@Override
	public int[] translateCharacterToNetworkOutput(char c) {
		return new NetworkIOTranslator().translateCharacterToNetworkOutput(c);
	}

	@Override
	public float[] translateImageToNetworkInput(BufferedImage img) {
		float[] allInputs = new NetworkIOTranslator().translateImageToNetworkInput(img);
		float[] receptorInputs = new float[receptors.size()];
		
		int imageWidth = 30;
		
		int index = 0;
		for (Point nextReceptor : receptors){
			receptorInputs[index] = allInputs[nextReceptor.x * imageWidth + nextReceptor.y];
			index++;
		}
		
		return receptorInputs;
	}

}
