package appTest;

import io.CharacterType;
import io.FileOperations;
import io.NeuralNetworkIO;
import io.ReceptorIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.FeatureExtractionIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.TrainingExample;
import receptors.Receptor;
import spellCheck.SpellChecker;
import ui.DrawingPanel;
import app.AlphaNumericCharacterConverter;
import app.CharacterResult;
import app.ImageHandlerFactory;
import app.ImageReadMethod;
import app.ImageReader;
import app.InputReader;
import app.MultiNetworkReader;
import app.NetworkFactory;
import app.ReadResult;
import debug.CorrelationDebug;
import debug.FeatureExtractionDebug;

public class MainTest {

	public static void main(String[] args) throws Exception {	
		// new HomeWindow();
		
		JFrame f = new JFrame();
		f.getContentPane().add(new DrawingPanel());
		f.pack();
		f.setVisible(true);
		
//		BufferedImage before = 
//				ImageIO.read(new File("C:\\Users\\nredmond\\Workspaces\\CapstoneNickRedmond\\Code\\Text4Less\\trainingImages\\ASCII2\\lowerb2.jpg"));
//		
//		int[][] beforeValues = ImageBinarizer.convertImageToBinaryValues(before);
//		beforeValues = ImageNormalizer.cropImage(beforeValues);
//		CharacterViewDebug.displayCharacterView(before, beforeValues, beforeValues[0].length, beforeValues.length);
//		
//		int[][] afterValues = ImageNormalizer.downSizeImage(beforeValues, 5, 7);
//		
//		CharacterViewDebug.displayCharacterView(null, afterValues, 5, 7);
		
	//	writeTrainingData("C:/Users/nredmond/Pictures/trainingData.png");
	//	engineTestStuff();
		
	//	BufferedImage img = ImageIO.read(new File("C:/Users/nredmond/Pictures/trainingData.png"));
		
//		Set<CharacterTrainingExample> examples = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII2);
//		List<Receptor> receptors = ReceptorGenerator.generateRandomReceptors(1000);
//		
//		List<Receptor> filtered = ReceptorFilter.filterReceptors(receptors, examples, 100);
//		ReceptorIO.saveReceptors(filtered, "myReceptors");
//		
//		System.out.println("finished");
	}
	
	private static void engineTestStuff() throws IOException{
//		INetworkIOTranslator translator = new FeatureExtractionIOTranslator();
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/lowera2.jpg")));
//		translator.translateImageToNetworkInput(ImageIO.read(new File("trainingImages/ASCII/lowera3.jpg")));
		
//		ImageHandlerFactory.setHandlerMethod(ImageReadMethod.TRAINING_DATA_CREATION);
	//	writeTrainingData("C:\\Users\\nredmond\\Pictures\\charTest4.png");
	//	renameCharacters(CharacterType.ASCII2, 2);
		
	//	FileOperations.addAlphanumericsToMetadataFile(CharacterType.ASCII3, 6);
		
//		ImagePreprocessor proc = new ImagePreprocessor();
//		BufferedImage testMe = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest3.png"));
//		int fontSize = proc.getFontSize(testMe);
//		System.out.println("Calculated font size: " + fontSize);
		
	// ---------------------------------------------------------------------------------------------------------------- //
	
//		List<Receptor> receptors = ReceptorIO.readReceptors("myReceptors");
//		
//		INetworkIOTranslator translator = new ReceptorNetworkIOTranslator(receptors);
//		INeuralNetwork network = new MatrixNeuralNetwork(receptors.size(),
//				1, 150, AlphaNumericCharacterConverter.NUMBER_CLASSES, true);
//		INeuralNetwork trainedNetwork = NetworkFactory.getTrainedNetwork(network, translator, CharacterType.ASCII2, new MatrixBackpropTrainer(0.05f, 0.02f));
//		
//		NeuralNetworkIO.writeNetwork(trainedNetwork, "yoloSwaggins2");
//		readFromSavedNetwork("yoloSwaggins2");
		
		INetworkIOTranslator translator = new AlphaNumericIOTranslator();
		INeuralNetwork network = new MatrixNeuralNetwork(new FeatureExtractionIOTranslator().getInputLength(),
				1, 100, AlphaNumericCharacterConverter.NUMBER_CLASSES, true);
		INeuralNetwork trainedNetwork = NetworkFactory.getTrainedNetwork(network, translator, CharacterType.ASCII2, new MatrixBackpropTrainer(0.05f, 0.02f));
		
		NeuralNetworkIO.writeNetwork(trainedNetwork, "yoloSwaggins2");
		readFromSavedNetwork("yoloSwaggins2");
		
//		float correlation = CorrelationDebug.getCorrelationBetweenTrainingSets(CharacterType.ASCII, 9, 10);
//		float correlation2 = CorrelationDebug.getCorrelationBetweenTrainingSets(CharacterType.ASCII, 2, 3);
//		System.out.println("correlation: " + correlation + " " + correlation2);
		
		// BREAK //
		
	//	readWithInputReader();
		
		// BREAK //
		
	//	runSomeShit();
		
	//	correlate(CharacterType.ASCII2, "loweru2", "loweru22");
	}
	
	private static void runSomeShit() throws IOException{
		BufferedImage test = ImageIO.read(new File("C:/Users/nredmond/Pictures/charTest.png"));
		String expectedText = "One night and one more time " +
		"Xanks Xr the memories thanks Xr the memories " +
		"See he tastes like you onX sweeter " +
		"30 perceX predicXd accuracy on this CRAX foX";
		
		CorrelationDebug.correlateAgainstTestCharacters(expectedText, test);
	}
	
	private static void readWithInputReader() throws IOException{
		List<ImageReadMethod> readMethods = new ArrayList<ImageReadMethod>();
	//	readMethods.add(ImageReadMethod.NEURAL_NETWORK);
		readMethods.add(ImageReadMethod.LEAST_DISTANCE);
		
		System.out.println("Reading... (shh... be patient.)");
		
		BufferedImage image = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest2.png"));
		ReadResult yes = InputReader.readImageInput(image, readMethods);
		
		String result = yes.getTranslationString();
		System.out.println("RESULT: " + result);
		
		String correctedResult = SpellChecker.spellCheckText(result);
		System.out.println("AFTER SPELL CHECK: " + correctedResult);
		
		System.out.println("\r\nNumber characters rejected: " + yes.getRejections().size());
	}
	
	private static void correlate(CharacterType type, String firstName, String secondName) throws IOException{
		BufferedImage img1 = ImageIO.read(new File("trainingImages/" + type.toString() + "/" + firstName + ".jpg"));
		BufferedImage img2 = ImageIO.read(new File("trainingImages/" + type.toString() + "/" + secondName + ".jpg"));
		float correlation = FeatureExtractionDebug.getCorrelation(img1, img2);
		
		System.out.println("Correlation between " + firstName + " and " + secondName + ": " + correlation);
	}
	
	private static void writeTrainingData(String imgFilepath) throws IOException{
		ImageHandlerFactory.setHandlerMethod(ImageReadMethod.TRAINING_DATA_CREATION);
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
		List<Receptor> receptors = ReceptorIO.readReceptors("myReceptors");
		INetworkIOTranslator t = new AlphaNumericIOTranslator();
		ImageReader reader = new ImageReader(savedNetwork, t);
		
		ImageHandlerFactory.setHandlerMethod(ImageReadMethod.NEURAL_NETWORK);
		
		BufferedImage img = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest3.png"));
		List<CharacterResult> translation = reader.readTextFromImage(img);
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
		CharacterType[] types = {CharacterType.ASCII4};
		
//		new ReadAnimation().start();
//		
//		long start = System.nanoTime();
//		ImageReader reader = new ImageReader(null, new FeatureExtractionIOTranslator());
//		List<TranslationResult> translation = reader.readTextFromImage(img);
//		String result = reader.convertTranslationToText(translation);
//		long end = System.nanoTime();
//		
//		long seconds = (end - start) / 1000000000;
//		
//		System.out.println("Time to read: " + seconds + " seconds");
		
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
