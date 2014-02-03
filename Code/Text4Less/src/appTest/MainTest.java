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

import debug.FeatureExtractionDebug;
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
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/lowera2.jpg")));
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/lowera3.jpg")));
		
	//	runApp();
		//FileOperations.addAlphanumericsToMetadataFile(CharacterType.ASCII, 6);

	//	writeTrainingData("C:\\Users\\nredmond\\Pictures\\charTest.png");
	//	renameCharacters(CharacterType.ASCII, 9);
		
	//	FileOperations.addAlphanumericsToMetadataFile(CharacterType.ASCII, 2);
		
	//	correlate(CharacterType.ASCII, "ZZZZZZ", "I");
	//	correlate(CharacterType.ASCII, "ZZZZZZ", "lowert");
	}
	
	private static void correlate(CharacterType type, String firstName, String secondName) throws IOException{
		BufferedImage img1 = ImageIO.read(new File("trainingImages/" + type.toString() + "/" + firstName + ".jpg"));
		BufferedImage img2 = ImageIO.read(new File("trainingImages/" + type.toString() + "/" + secondName + ".jpg"));
		float correlation = FeatureExtractionDebug.getCorrelation(img1, img2);
		
		System.out.println("Correlation between " + firstName + " and " + secondName + ": " + correlation);
	}
	
	private static void writeTrainingData(String imgFilepath) throws IOException{
		BufferedImage img = ImageIO.read(new File(imgFilepath));
		ImageReader reader = new ImageReader(null, null);
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
		ImageReader reader = new ImageReader(savedNetwork, t);
		
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
		
		new ReadAnimation().start();
		
		long start = System.nanoTime();
		ImageReader reader = new ImageReader(null, new FeatureExtractionIOTranslator());
		List<TranslationResult> translation = reader.readTextFromImage(img);
		String result = reader.convertTranslationToText(translation);
		long end = System.nanoTime();
		
		long seconds = (end - start) / 1000000000;
		
		System.out.println("Time to read: " + seconds + " seconds");
		
	//	String result = MultiNetworkReader.getTextFromImage(img, types);
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
	
	private static class ReadAnimation extends Thread{
		@Override
		public void run(){
			final int numDots = 5;
			final int sleepTime = 500;
			System.out.print("Reading");
			
			for (int i = 0; i < numDots; i++){
				System.out.print(".");
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println();
		}
	}
}
