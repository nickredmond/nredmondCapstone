package appTest;

import imageProcessing.FeatureExtractionIOTranslator;
import imageProcessing.INetworkIOTranslator;
import imageProcessing.TranslationResult;
import io.CharacterType;
import io.FileOperations;
import io.NeuralNetworkIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import neuralNetwork.INeuralNetwork;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.TrainingExample;
import app.ImageReader;
import app.MultiNetworkReader;

public class MainTest {

	public static void main(String[] args) throws IOException {		
//		INetworkIOTranslator translator = new FeatureExtractionIOTranslator();
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/u.jpg")));
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/u3.jpg")));

		readFromSavedNetwork("myNetwork");
	}
	
	private static void renameCharacters(CharacterType type, int setNumber) throws IOException{
//		String dir = "C:/Users/nredmond/Documents/testData";
//		String appendix = setNumber.toString();
//		FileOperations.renameFilesWithAppendedName(dir, appendix);
		
		FileOperations.addAlphanumericsToMetadataFile(type, setNumber);
	}
	
	private static void readFromSavedNetwork(String networkName) throws IOException{
		INeuralNetwork savedNetwork = NeuralNetworkIO.readNetwork(networkName);
		INetworkIOTranslator t = new FeatureExtractionIOTranslator();
		ImageReader reader = new ImageReader(savedNetwork, t);
		
		BufferedImage img = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest3.png"));
		List<TranslationResult> translation = reader.readTextFromImage(img);
		String result = reader.convertTranslationToText(translation);
		
		System.out.println("RESULT: " + result);
	}
	
	private static void runApp() throws IOException{
		BufferedImage img = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest3.png"));
		CharacterType[] types = {CharacterType.ASCII};
		
		String result = MultiNetworkReader.getTextFromImage(img, types);
		System.out.println("RESULT: " + result);
		
	//	NeuralNetworkIO.writeNetwork(trainedNetwork, "myNetwork");
	}

	private static void testMe(NeuralNetwork network, Set<TrainingExample> set){
		
		for (TrainingExample nextExample : set){
			float[] outputs = network.forwardPropagate(nextExample.getInput());
			
			System.out.print("Desired: ");
			
			for (int i = 0; i < nextExample.getOutput().length; i++){
				System.out.print(nextExample.getOutput()[i] + " ");
			}
			
			System.out.print(" --- Actual: ");
			
			for (int i = 0; i < outputs.length; i++){
				System.out.print(outputs[i] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
	}
}
