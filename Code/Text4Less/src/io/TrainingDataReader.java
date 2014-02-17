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

import ui.TrainingDataNameAssigner;
import app.AlphaNumericCharacterConverter;
import neuralNetwork.CharacterTrainingExample;

public class TrainingDataReader {
	private static final int CHAR_INDEX = 0;
	private static final int FILENAME_INDEX = 1;
	private static final char SPACE_CHAR = ' ';
	private static final int SPACE_INT = 32;
	
	private static final int LOWER_START_INDEX = 5;
	
	private static final String IMAGE_EXTENSION = "jpg";
	
	public static final String DELIMITER = "#BRK#";
	
	public static Set<CharacterTrainingExample> createTestSetFromFile(CharacterType type) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("metaDataFiles/" + type.toString() +
				"/testImageLocations.txt"));
		String imagePath = "trainingImages/" + type.toString() + "/testImages/";
		
		return readCharacterSet(reader, type, imagePath);
	}
	
	public static Set<CharacterTrainingExample> createTriningSetFromFile(File trainingSetDirectory) throws IllegalStateException, IOException{
		File[] trainingImageFiles = trainingSetDirectory.listFiles();
		Set<CharacterTrainingExample> examples = new HashSet<CharacterTrainingExample>();
		
		for (int i = 0; i < trainingImageFiles.length; i++){
			File nextFile = trainingImageFiles[i];
			
			if (!nextFile.isDirectory()){
				String filename = nextFile.getName();
				String[] nameParts = filename.split("[.]");
				
				String imageName = nameParts[0];
				String extension = nameParts[1];
				
				if (!extension.equals("db")){
					if (!extension.equals(IMAGE_EXTENSION)){
						throw new IllegalStateException("Invalid file format");
					}
					
				//	CharacterTrainingExample nextExample = new CharacterTrainingExample(img, value);
					char nextCharacter = 'A';
					
					if (imageName.matches("^space[0-9]*$")){
						nextCharacter = ' ';
					}
					else if (imageName.toLowerCase().startsWith("lower")){
						if (imageName.length() > LOWER_START_INDEX){
							char character = imageName.charAt(LOWER_START_INDEX);
							int charValue = (int)character;
							
							if (charValue >= AlphaNumericCharacterConverter.UPPER_START && charValue <= AlphaNumericCharacterConverter.UPPER_END){
								character = (char)(charValue + TrainingDataNameAssigner.UPPER_LOWER_DIFFERENCE);
							}
							if ((int)character < AlphaNumericCharacterConverter.LOWER_START || (int)character > AlphaNumericCharacterConverter.LOWER_END){
								throw new IllegalStateException("Invalid file name");
							}
							
							nextCharacter = character;
						}
						else{
							throw new IllegalStateException("Invalid file name");
						}
					}
					else if (imageName.toLowerCase().matches("^[a-z]{1}[0-9]*$")){
						char character = imageName.charAt(0);
						int charValue = (int)character;
						
						if (charValue >= AlphaNumericCharacterConverter.LOWER_START && charValue <= AlphaNumericCharacterConverter.LOWER_END){
							character = (char)(charValue - TrainingDataNameAssigner.UPPER_LOWER_DIFFERENCE);
						//	System.out.println("yes: " + imageName + " " + character);
						}
						if ((int)character < AlphaNumericCharacterConverter.UPPER_START || (int)character > AlphaNumericCharacterConverter.UPPER_END){
						//	System.out.println(imageName + " " + charValue);
							throw new IllegalStateException("Invalid file name");
						}
						
						nextCharacter = character;
					}
					else if (imageName.matches("^[0-9]+")){
						nextCharacter = imageName.charAt(0);
					}
					else{
						System.out.println(imageName);
						throw new IllegalStateException("Invalid file name");
					}
					
					BufferedImage nextImage = ImageIO.read(nextFile);
					examples.add(new CharacterTrainingExample(nextImage, nextCharacter));
				}
			}
		}
		
		return examples;
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

				BufferedImage nextImage = null;
				try{
					nextImage = ImageIO.read(new File(imagePath + nextFilename + ".jpg"));
				}
				catch(IOException e){
					System.out.println("Name that failed: " + nextFilename);
					throw e;
				}
				
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
