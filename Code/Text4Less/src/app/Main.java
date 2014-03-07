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
import io.MetaclassTreeIO;
import io.NeuralNetworkIO;
import io.ReceptorIO;
import io.TrainingDataReader;
import io.UserPreferencesIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import receptors.Receptor;
import threading.UncaughtExceptionHandler;
import ui.MenuWindow;
import ui.SplashScreen;
import decisionTrees.BasicTreeFinder;
import decisionTrees.IMetaclassTree;
import decisionTrees.ITreeFinder;

public class Main implements IClickHandler {
	private SplashScreen splash;
	private MenuWindow window;
	
	public static void main(String[] args) throws IOException {	
		//UserPreferencesIO.writePreferences(new UserPreferences(true, false, ImageReadMethod.NEURAL_NETWORK));
		new Main().startApplication();
		
	//	FileOperations.renameFilesWithAppendedName("C:/Users/nredmond/Workspaces/CapstoneNickRedmond/Code/Text4Less/trainingImages/nicksFailures", "41");
	}
	
	private void startApplication(){
		splash = new SplashScreen(this);
		splash.setLoadPercentage(0.0f);
		splash.setLoadStatusMessage("Preparing application...");
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {}
		
		ThreadGroup uncaughtExceptionGroup = new UncaughtExceptionHandler();
		
		new Thread(uncaughtExceptionGroup, "Main Thread"){
			public void run(){
				splash.setLoadPercentage(0.75f);
				splash.setLoadStatusMessage("Loading User Preferences...");
				splash.repaint();
				
				window = new MenuWindow();
				window.setPreferences(UserPreferencesIO.readPreferences());
				
				splash.setLoadPercentage(1.0f);
				splash.repaint();
				
				try {
					Thread.sleep(900);
				} catch (InterruptedException e) {}
				
				window.setVisible(true);
				splash.setVisible(false);
				splash.dispose();
				
				window.displayStartupInfo();
			}
		}.start();
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

	@Override
	public void mouseClicked() {
		window.setVisible(true);
		splash.setVisible(false);
		splash.dispose();
	}
}
