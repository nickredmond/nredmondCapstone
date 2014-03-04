package app;

import genetics.BasicGeneticAlgorithm;
import genetics.Chromosome;
import genetics.IChromosomeChooser;
import genetics.IFitnessCalculator;
import genetics.IGeneticAlgorithm;
import genetics.TestFitnessCalculator;
import genetics.TournamentChooser;
import imageHandling.ImageHandlerFactory;
import imageHandling.ImageReadMethod;
import imageProcessing.TranslationResult;
import io.CharacterType;
import io.FileOperations;
import io.MetaclassTreeIO;
import io.NeuralNetworkIO;
import io.ReceptorIO;
import io.TrainingDataReader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.FeatureExtractionIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import receptors.Receptor;
import spellCheck.SpellChecker;
import threading.UncaughtExceptionHandler;
import ui.HomeWindow;
import decisionTrees.BasicTreeFinder;
import decisionTrees.IMetaclassTree;
import decisionTrees.ITreeFinder;

public class Main {	
	public static void main(String[] args) {
		ThreadGroup uncaughtExceptionGroup = new UncaughtExceptionHandler();
		
		new Thread(uncaughtExceptionGroup, "Main Thread"){
			public void run(){
				new HomeWindow();
			}
		}.start();
	}
	
	private static char correlateChainCodeEuc(BufferedImage testImg) throws IOException{
		AlphaNumericIOTranslator translator = new AlphaNumericIOTranslator();
		Set<CharacterTrainingExample> trainingSet = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII2);
		
		char chosenChar = 'A';
		float minDistance = 10000.0f;
		
		for (CharacterTrainingExample nextExample : trainingSet){
			float distance = LeastDistanceCalculator.getEuclideanDistance(testImg, nextExample.getCharacterImage());
			
			if (distance < minDistance){
				minDistance = distance;
				chosenChar = nextExample.getCharacterValue();
			}
		}
		
		//System.out.println("RESULT: " + chosenChar);
		System.out.println("letter");
		return chosenChar;
	}
	
	private static void testDecisionTrees() throws IOException{
		Set<CharacterTrainingExample> trainingSet = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII3);
	//	trainingSet.addAll(TrainingDataReader.createTestSetFromFile(CharacterType.ASCII3));
		
		Set<CharacterTrainingExample> testSet = TrainingDataReader.createTestSetFromFile(CharacterType.ASCII3);
		
		char[] classes = {'j', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U'
				, 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', ' ', 'i', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's'
				, 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		
		char[] handPickedClasses = {'C', 'c', 'M', 'H', 'Z', 'z', 'A', 'B', 'E', 'F', 'G', 'J', 'K', 'L', 'N', 'O', 'P', 'Q', 'R', 'S', 'U'
				, 'V', 'W', 'X', 'Y', ' ', 'a', 'b', 'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'q', 'r', 's'
				, 't', 'u', 'v', 'w', 'x', 'y', '0', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'I', 'i', 'j', 'l', '1', 'D', 'o',};
		
		ITreeFinder treeFinder = new BasicTreeFinder(trainingSet, testSet, classes);
		IMetaclassTree tree = treeFinder.getTree();
		System.out.println("tree: " + tree);
		
		MetaclassTreeIO.saveTree(tree, "tnrTree");
		IMetaclassTree savedTree = MetaclassTreeIO.readTree("tnrTree");
		System.out.println("tree: " + savedTree);
		
		BufferedImage img = ImageIO.read(new File("C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Code/Text4Less/trainingImages/ASCII/t5.jpg"));
		TranslationResult result = savedTree.readCharacter(img);
		
		System.out.println("The character: " + result.getCharacter());
	}
	
	private static void testGenetics(){
		int[] targetValue = {1,1,0,1,0,0,1,1,0,0,0,0,1,1,0,0,0,1,0,0,1,1,0,0,0,1,0,1,0,1,0,0};
		int chromosomeSize = targetValue.length;
		int numberChromosomes = 32;
		IFitnessCalculator calc = new TestFitnessCalculator(targetValue);
		IChromosomeChooser chooser = new TournamentChooser(); //RouletteChromosomeChooser();
		IGeneticAlgorithm algo = new BasicGeneticAlgorithm(calc, chooser);
	//	algo.setElitism(true);
		algo.setCrossoverRate(0.5f);
		algo.setMutationRate(0.015f);
		
		int[][] chromosomes = algo.generateRandomChromosomeSet(numberChromosomes, chromosomeSize);
		
		for (int i = 0; i < chromosomes.length; i++){
			System.out.println(Arrays.toString(chromosomes[i]) + " F:" + calc.getFitness(chromosomes[i]));
		}
		
		boolean isTargetReached = false;		
		int numEvolutions = 0;
		Chromosome[] chromes = null;
		
		while(!isTargetReached){
			chromes = algo.getChromosomeArray(chromosomes);
			for (int i = 0; i < chromes.length && !isTargetReached; i++){
				float fitness = chromes[i].getFitness();
				isTargetReached = (fitness == targetValue.length);
			}
			
			if (!isTargetReached){
				chromosomes = algo.breed(chromes);
				numEvolutions++;
			}
			//System.out.println("iteration");
		}
		
		for (int i = 0; i < chromes.length; i++){
			if (chromes[i].getFitness() == targetValue.length){
				System.out.println("Chromosome: " + Arrays.toString(chromes[i].getGenes()));
				System.out.println("Value: " + new TestFitnessCalculator(targetValue).getValueFor(chromes[i].getGenes()));
			}
		}
		System.out.println("Number evolutions: " + numEvolutions);
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
		ReadResult yes = null; // InputReader.readImageInput(image, readMethods);
		
		String result = yes.getTranslationString();
		System.out.println("RESULT: " + result);
		
		String correctedResult = SpellChecker.spellCheckText(result);
		System.out.println("AFTER SPELL CHECK: " + correctedResult);
		
		System.out.println("\r\nNumber characters rejected: " + yes.getRejections().size());
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
		
		BufferedImage img = ImageIO.read(new File("C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Vendor/handwrittenData/trainingSet/129.bmp"));
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
}