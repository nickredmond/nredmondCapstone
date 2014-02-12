package io;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import neuralNetwork.CharacterTrainingExample;

public class TrainingDataReader {
	private static final int CHAR_INDEX = 0;
	private static final int FILENAME_INDEX = 1;
	private static final char SPACE_CHAR = ' ';
	private static final int SPACE_INT = 32;
	
	public static final String DELIMITER = "#BRK#";
	
	public static Set<CharacterTrainingExample> createTestSetFromFile(CharacterType type) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("metaDataFiles/" + type.toString() +
				"/testImageLocations.txt"));
		String imagePath = "trainingImages/" + type.toString() + "/testImages/";
		
		return readCharacterSet(reader, type, imagePath);
	}
	
	private static Set<CharacterTrainingExample> readCharacterSet(BufferedReader reader, CharacterType type,
			String imagePath) throws IOException{
		Set<CharacterTrainingExample> trainingSet = new HashSet<CharacterTrainingExample>();
		
		String nextLine = null;
		
		while ((nextLine = reader.readLine()) != null){
			String[] lineParts = nextLine.split(DELIMITER);
			
			if (lineParts.length > 0){
				char nextCharacter = lineParts[CHAR_INDEX].charAt(0);
				String nextFilename = lineParts[FILENAME_INDEX];
			//	System.out.println(nextFilename);
				BufferedImage nextImage = 
						ImageIO.read(new File(imagePath + nextFilename + ".jpg"));
				
				CharacterTrainingExample nextExample = new CharacterTrainingExample(nextImage, nextCharacter);
				trainingSet.add(nextExample);
			}
		}
		
		reader.close();
		return trainingSet;
	}
	
	public static Set<CharacterTrainingExample> createTrainingSetFromFile(CharacterType type) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("metadataFiles/" + type.toString() + 
				"/characterImageLocations.txt"));
		String imagePath = "trainingImages/" + type.toString() + "/";
		
		return readCharacterSet(reader, type, imagePath);
	}
}
