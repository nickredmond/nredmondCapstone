package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperations {
	private static final String EXTENSION_DELIMITER = "[.]";
	private static final int NAME_INDEX = 0;
	private static final int EXT_INDEX = 1;
	
	private static final int A_ASCII = 65;
	private static final int Z_ASCII = 90;
	private static final int UPPER_LOWER_DIFFERENCE = 32;
	private static final int ZERO_ASCII = 48;
	private static final int NUM_CHARS_ALPHABET = 26;
	private static final int NUMBER_DIGITS = 10;
	
	public static void renameFile(String filepath, String newName){
		File oldFile = new File(filepath);
		String directory = oldFile.getAbsolutePath().replace(oldFile.getName(), "");
		
		boolean succeeded = oldFile.renameTo(new File(directory + newName));
		
		if (!succeeded){
			System.err.println("Could not rename the file!");
		}
	}
	
	public static void renameFilesWithAppendedName(String dirPath, String appendix){
		File directory = new File(dirPath);
		
		for (File nextFile : directory.listFiles()){
			String filename = nextFile.getName();
			String[] nameParts = filename.split(EXTENSION_DELIMITER);
			
			String extension = nameParts[EXT_INDEX];
			String name = nameParts[NAME_INDEX] + appendix + "." + extension;
			
			renameFile(nextFile.getAbsolutePath(), name);
		}
	}
	
	public static void addAlphanumericsToMetadataFile(CharacterType type, int setNumber) throws IOException{
		File metadataFile = new File("metaDataFiles/" + type.toString() +
				"/characterImageLocations.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(metadataFile, true));
		
		String set = (setNumber == 0) ? "" : new Integer(setNumber).toString();
		
		for (int i = A_ASCII; i < A_ASCII + NUM_CHARS_ALPHABET; i++){
			char upper = (char)i;
			char lower = (char)(i + UPPER_LOWER_DIFFERENCE);
			String upperLine = upper + TrainingDataReader.DELIMITER + upper + "" + set + "\r\n";
			String lowerLine = lower + TrainingDataReader.DELIMITER + "lower" + upper + "" + set + "\r\n";
			
			writer.write(upperLine);
			writer.write(lowerLine);
		}
		
		for (int i = ZERO_ASCII; i < ZERO_ASCII + NUMBER_DIGITS; i++){
			String line = (char)i + TrainingDataReader.DELIMITER + (char)i + "" + set + "\r\n";
			writer.write(line);
		}
		
		writer.flush();
		writer.close();
	}
}