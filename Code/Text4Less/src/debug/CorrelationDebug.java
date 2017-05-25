package debug;

import imageProcessing.ImagePreprocessor;
import io.CharacterType;
import io.MetaclassTreeIO;
import io.NeuralNetworkIO;
import io.TrainingDataReader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import app.LeastDistanceCalculator;
import decisionTrees.IMetaclassTree;

public class CorrelationDebug {
	public static float getCorrelationBetweenTrainingSets(CharacterType type, int firstSetNum, int secondSetNum) throws IOException{
		List<CharacterTrainingExample> list1 = new ArrayList<CharacterTrainingExample>();
		List<CharacterTrainingExample> list2 = new ArrayList<CharacterTrainingExample>();

		String nextLine = null;
		
		BufferedReader reader = new BufferedReader(new FileReader("metadataFiles/" + type.toString() + 
				"/characterImageLocations.txt"));
		String imagePath = "trainingImages/" + type.toString() + "/";
		
		while ((nextLine = reader.readLine()) != null){
			String[] lineParts = nextLine.split("#BRK#");
			
			if (lineParts.length > 0){
				char nextCharacter = lineParts[0].charAt(0);
				String nextFilename = lineParts[1];
				
				if (nextFilename.matches("^[A-z0-9]+" + ((firstSetNum == 0) ? "" : firstSetNum) + "$")){
					BufferedImage nextImage = 
							ImageIO.read(new File(imagePath + nextFilename + ".jpg"));
					
					CharacterTrainingExample nextExample = new CharacterTrainingExample(nextImage, nextCharacter);
					list1.add(nextExample);
				}
				else if (nextFilename.matches("^[A-z0-9]+" + ((secondSetNum == 0) ? "" : secondSetNum) + "$")){
						BufferedImage nextImage = 
								ImageIO.read(new File(imagePath + nextFilename + ".jpg"));
						
						CharacterTrainingExample nextExample = new CharacterTrainingExample(nextImage, nextCharacter);
						list2.add(nextExample);
				}
			}
		}
		
		if (list1.size() != list2.size()){
			System.out.println("huh: " + list1.size() + " " + list2.size());
			throw new IOException("dammit.");
		}
		
		float totalCorrts = 0.0f;
		
		for (int i = 0; i < list1.size(); i++){
			CharacterTrainingExample example1 = list1.get(i);
			CharacterTrainingExample example2 = list2.get(i);
			
			totalCorrts += LeastDistanceCalculator.getCorrelation(example1.getCharacterImage(), example2.getCharacterImage());
		}
		
		return totalCorrts / list1.size();
	}
	
	
	public static void correlateAgainstTestCharacters(String expectedText, BufferedImage testImage){		
		int index = 0;
		
		int numRight = 0;
		int totalComparisons = 0;
		
		ImagePreprocessor processor = new ImagePreprocessor();
		BufferedImage trimmedImage = processor.trimMargins(testImage);
		
		List<BufferedImage> lines = processor.splitIntoLines(trimmedImage);
		
		for (BufferedImage nextLine : lines){
			List<BufferedImage> characters = processor.splitIntoCharacters(nextLine);
			
			for (BufferedImage nextCharacter : characters){
				while(expectedText.charAt(index) == ' '){
					index++;
				}
				
				char chosenCharacter = 'A';
		//		System.out.println("--- SHOULD BE '" + expectedText.charAt(index) + "'");
				
				chosenCharacter = getChosenCharacterNeuralNetwork(nextCharacter); //getChosenCharacterEuclideanDistance(nextCharacter, index, expectedText); // CAN CHANGE THIS
				
				if (chosenCharacter != ' '){
					if (chosenCharacter == expectedText.charAt(index)){
						numRight++;
					}
					totalComparisons++;
					index++;
				}
			}
		}
		System.out.println("\r\nACCURACY: " + ((float)numRight / totalComparisons) + "(" + numRight + " / " + totalComparisons + ")");
	}
	
	private static char getChosenCharacterDecisionTree(BufferedImage nextCharacter){
		IMetaclassTree tree = null;
		
		try {
			tree = MetaclassTreeIO.readTree("defaultTree");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tree.readCharacter(nextCharacter).getCharacter();
	}
	
	private static char getChosenCharacterNeuralNetwork(BufferedImage nextCharacter){
		INeuralNetwork trainedNet = NeuralNetworkIO.readNetwork("endOfLineTestNetArial"); // CAN CHANGE THIS
		AlphaNumericIOTranslator translator = new  AlphaNumericIOTranslator();
		
		float[] input = translator.translateImageToNetworkInput(nextCharacter);
		float[] output = trainedNet.forwardPropagate(input);
		
		return translator.translateNetworkOutputToCharacter(output).getCharacter();
	}
	
	private static char getChosenCharacterEuclideanDistance(BufferedImage nextCharacter,
			int index, String expectedText){
		Set<CharacterTrainingExample> examples = null;
		try {
			examples = TrainingDataReader.createTrainingSetFromFile(CharacterType.ASCII);
		} catch (IOException e) {
			System.out.println("FAILED TO CREATE TRAINING SET.");
			e.printStackTrace();
		}
		
		float highestCorrelation = -1.0f;
		char chosenCharacter = 'A';
		
		Iterator<CharacterTrainingExample> iter = examples.iterator();
		while(iter.hasNext()){
			CharacterTrainingExample nextExample = iter.next();
			
			BufferedImage nextTrainingImg = nextExample.getCharacterImage();
			float nextCorrelation = LeastDistanceCalculator.getEuclideanDistance(nextCharacter, nextTrainingImg);
			
			if (nextExample.getCharacterValue() == expectedText.charAt(index)){
				System.out.println("correlation between same chars: " + nextCorrelation);
			}
			
			if (nextCorrelation > highestCorrelation || highestCorrelation < 0){
				highestCorrelation = nextCorrelation;
				chosenCharacter = nextExample.getCharacterValue();
			}
		}
		
		return chosenCharacter;
	}
	
	private boolean isSpace(BufferedImage image, INetworkIOTranslator translator){
		float[] input = translator.translateImageToNetworkInput(image);
		boolean isSpace = true;
		
		for (int i = 0; i < input.length && isSpace; i++){
			isSpace = input[i] == 0;
		}
		
		return isSpace;
	}
}
