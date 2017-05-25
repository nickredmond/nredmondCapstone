package appTest;

import io.MnistReader;
import io.NeuralNetworkIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import networkIOtranslation.AlphaNumericIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;

public class TestMnistNetwork {
	public static void testTrainedNetwork(INeuralNetwork network) throws IOException{
		Set<CharacterTrainingExample> testSet = MnistReader.readHandwrittenImages(MnistReader.DEFAULT_TEST_FILEPATH);
		int totalRight = 0;
		
		for (CharacterTrainingExample nextExample : testSet){
			BufferedImage image = nextExample.getCharacterImage();
			AlphaNumericIOTranslator t = new AlphaNumericIOTranslator();
			float[] input = t.translateImageToNetworkInput(image);
			float[] output = network.forwardPropagate(input);
			char yes = t.translateNetworkOutputToCharacter(output).getCharacter();
			System.out.println("should be: " + nextExample.getCharacterValue() + ", actual: " + yes);
			
			if (yes == nextExample.getCharacterValue()){
				totalRight++;
			}
		}
		
		System.out.println("RECOGNITION: " + totalRight + " out of " + testSet.size());
	}
}
