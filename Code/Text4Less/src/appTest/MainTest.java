package appTest;

import imageHandling.ImageHandlerFactory;
import imageHandling.ImageReadMethod;
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
import ui.HomeWindow;
import app.AlphaNumericCharacterConverter;
import app.CharacterResult;
import app.ImageReader;
import app.InputReader;
import app.LeastDistanceCalculator;
import app.MultiNetworkReader;
import app.NetworkFactory;
import app.ReadResult;
import debug.FeatureExtractionDebug;

public class MainTest {

	public static void main(String[] args) throws Exception {
//		BufferedImage miami = ImageIO.read(new File("C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Code/Text4Less/trainingImages/ASCII2/e2.jpg"));
//		BufferedImage nyc = ImageIO.read(new File("C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Code/Text4Less/trainingImages/ASCII2/e3.jpg"));
//		
//		float correlation = LeastDistanceCalculator.getCorrelation(miami, nyc);
//		float euclidian = LeastDistanceCalculator.getEuclideanDistance(miami, nyc);
//		
//		System.out.println("cor: " + correlation + ", euc: " + euclidian);
		
		new HomeWindow();
		
//		String dir = new MainTest().getWorkingDirectory();
//		JOptionPane.showMessageDialog(null, "dir: " + dir);
		
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
	//	
	//	BufferedImage img = ImageIO.read(new File("C:/Users/nredmond/Pictures/trainingData.png"));
		
	//	FileOperations.addAlphanumericsToMetadataFile(CharacterType.ASCII4, 5);
		
//		Set<CharacterTrainingExample> examples = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII2);
//		List<Receptor> receptors = ReceptorGenerator.generateRandomReceptors(1000);
//		
//		List<Receptor> filtered = ReceptorFilter.filterReceptors(receptors, examples, 100);
//		ReceptorIO.saveReceptors(filtered, "myReceptors");
//		
//		System.out.println("finished");
	}
	
	public String getWorkingDirectory(){
		String currentExec = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		String[] dirParts = currentExec.split("/");
		
		String result = "";
		
		for (int i = 0; i < dirParts.length; i++){
			if (!dirParts[i].contains(".") && !dirParts[i].startsWith("bin") && !dirParts[i].startsWith("file:")){
				result += dirParts[i];
				
				if (i < dirParts.length - 1){
					result += "/";
				}
			}
		}
		
		return result;
	}
	
	private static void engineTestStuff() throws IOException{	
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
		
		NeuralNetworkIO.writeNetwork(trainedNetwork, "yoloSwaggins");
		readFromSavedNetwork("yoloSwaggins");
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
		float correlation = LeastDistanceCalculator.getCorrelation(img1, img2);
		
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
		
		BufferedImage img = ImageIO.read(new File("C:\\Users\\nredmond\\Pictures\\charTest2.png"));
		List<CharacterResult> translation = reader.readTextFromImage(img);
		
		float totalConfidence = 0.0f;
		
		for (CharacterResult nextResult : translation){
			totalConfidence += nextResult.getResult().getConfidence();
		}
		
		float avgConfidence = totalConfidence / translation.size();
		
		String result = reader.convertTranslationToText(translation);
		
		System.out.println("RESULT: " + result);
		System.out.println("AVG CONFIDENCE: " + avgConfidence);
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
}
