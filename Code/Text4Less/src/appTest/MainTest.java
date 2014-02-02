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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import spellCheck.DictionaryCreatorUtil;
import spellCheck.EditDistanceCalculator;
import spellCheck.SpellChecker;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.TrainingExample;
import app.ImageReader;
import app.MultiNetworkReader;

public class MainTest {

	public static void main(String[] args) throws IOException, URISyntaxException {	
//		INetworkIOTranslator translator = new FeatureExtractionIOTranslator();
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/u.jpg")));
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/u3.jpg")));
		
		runApp();
		//FileOperations.addAlphanumericsToMetadataFile(CharacterType.ASCII, 6);

		//testSpellCheck();
		
		//writeTrainingData("C:\\Users\\nredmond\\Pictures\\charTest.png");
		//renameCharacters(CharacterType.ASCII, 6);
	}
	
	private static void writeTrainingData(String imgFilepath) throws IOException{
		BufferedImage img = ImageIO.read(new File(imgFilepath));
		ImageReader reader = new ImageReader(null, null, false);
		reader.readTextFromImage(img);
	}
	
	private static void renameCharacters(CharacterType type, int setNumber) throws IOException{
		String dir = "C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Code/Text4Less/trainingImages/unformatted";
		String appendix = ((Integer)setNumber).toString();
		FileOperations.renameFilesWithAppendedName(dir, appendix);
		
		FileOperations.addAlphanumericsToMetadataFile(type, setNumber);
	}
	
	private static void readFromSavedNetwork(String networkName) throws IOException{
		INeuralNetwork savedNetwork = NeuralNetworkIO.readNetwork(networkName);
		INetworkIOTranslator t = new FeatureExtractionIOTranslator();
		ImageReader reader = new ImageReader(savedNetwork, t, true);
		
		BufferedImage img = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest3.png"));
		List<TranslationResult> translation = reader.readTextFromImage(img);
		String result = reader.convertTranslationToText(translation);
		
		System.out.println("RESULT: " + result);
	}
	
	private static void testSpellCheck(){
		String result = " \\his is smaiief teK| tha| Miii a m}fe feas}Nadie size t} eKPec| aN image t}\r\n"+ 
			"  c}N|aiNV \\HiS iS \\EyT iN @LL CAPSV Y}uf iucg Numdefs afe 4 8 1 5 1 4 33\r\n"+
			"  43V H}W Miii i| haNdie PuNc|ua|i}N siNce i| has Nevef deeN tfaiNed }N \r\n"+
			"  mhi}p \r\n"+
			"  Hefe is a iiNe dfeak Mi|h a PafagfaPh iNdeN|a|i}NV i have ais} Nevef \r\n"+
			"  d}Ne mhis demfeV ";
		System.out.println("RESULT: " + result);
		System.out.println();
		
		String correctedResult = SpellChecker.spellCheckText(result);
		System.out.println("AFTER SPELL CHECK: " + correctedResult);
	}
	
	private static void runApp() throws IOException{
		BufferedImage img = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest3.png"));
		CharacterType[] types = {CharacterType.ASCII};
		
		String result = MultiNetworkReader.getTextFromImage(img, types);
		System.out.println("RESULT: " + result);
		System.out.println();
		
		String correctedResult = SpellChecker.spellCheckText(result);
		System.out.println("AFTER SPELL CHECK: " + correctedResult);
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
