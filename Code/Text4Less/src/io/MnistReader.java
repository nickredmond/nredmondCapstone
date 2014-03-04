package io;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import neuralNetwork.CharacterTrainingExample;

public class MnistReader {
	private static final int NUMBER_TRAINING_IMAGES = 60000;
	private static final int NUMBER_TEST_IMAGES = 10000;
	
	private static final int LABEL_METADATA_BUFFER_SIZE = 8;
	private static final int IMAGE_METADATA_BUFFER_SIZE = 16;
	
	private static final int DATA_BUFFER_SIZE = 1;
	
	private static final int LABEL_START = 48;
	
	public static final String DEFAULT_TRAINING_FILEPATH = "metadataFiles/trainingMetadata.txt";
	public static final String DEFAULT_VALIDATION_FILEPATH = "metadataFiles/validationMetadata.txt";
	public static final String DEFAULT_TEST_FILEPATH = "metadataFiles/testMetadata.txt";
	
	private static final int FILEPATH_INDEX = 0;
	private static final int LABEL_INDEX = 1;
	
	public static Set<CharacterTrainingExample> readHandwrittenImages(String dataFilepath) throws IOException{
		Set<CharacterTrainingExample> examples = new HashSet<CharacterTrainingExample>();
		
		BufferedReader dataFileReader = new BufferedReader(new FileReader(new File(dataFilepath)));
		
		String nextLine = dataFileReader.readLine();
		
		while ((nextLine = dataFileReader.readLine()) != null){
			String[] lineParts = nextLine.split(" ");
			String filepath = lineParts[FILEPATH_INDEX];
			
			char character = lineParts[LABEL_INDEX].charAt(0);
			BufferedImage image = ImageIO.read(new File(filepath));
			
			examples.add(new CharacterTrainingExample(image, character));
		}
		
		dataFileReader.close();
		return examples;
	}
	
	public static Set<CharacterTrainingExample> readImages(String imagesFilepath, String labelsFilepath, int numberImages) throws IOException{
		List<Character> setLabels = readLabels(numberImages, labelsFilepath);
		
		return null;
	}
	
	private static List<Character> readLabels(int numberImages, String labelsFilepath) throws IOException{
		FileInputStream inStream = new FileInputStream(labelsFilepath);
		inStream.read(new byte[4], 0, 4);
		int magicNr = (inStream.read() << 24) | (inStream.read() << 16) | (inStream.read() << 8) | (inStream.read());
		System.out.println("magicNr: " + magicNr);
		
		inStream.close();
		
//		BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File(labelsFilepath)));
//		DataInputStream labelInput = new DataInputStream(input);
//		
//		byte[] buffer = new byte[LABEL_METADATA_BUFFER_SIZE];
//		
//		int position = labelInput.read(buffer, 0, LABEL_METADATA_BUFFER_SIZE/2);
//		
//		byte[] flipped = new byte[buffer.length];
//		
//		for (int i = 0; i < flipped.length; i++){
//			flipped[i] = buffer[buffer.length - 1 - i];
//		}
//		
//		for (int i = 0; i < buffer.length; i++){
//			System.out.println(Arrays.toString(flipped));
//		}
//		
////		ByteBuffer b = ByteBuffer.wrap(buffer);
////		Buffer f = b.flip();
////		Object o = f.array();
////		System.out.println(o.getClass().toString());
//		
//		int magicNumber = convertByteArrayToInt(flipped, 0);
//		int numberLabels = convertByteArrayToInt(flipped, 4);
//		
//		System.out.println("Magic number: " + magicNumber + ", num: " + numberLabels);
//		
//		labelInput.close();
		return null;
	}
	
	private static int convertByteArrayToInt(byte[] array, int start){
		final int LENGTH = 4;
		int value = 0;
		
		if (start + LENGTH > array.length){
			throw new IllegalArgumentException("Starting point is too far for 32-bit integer.");
		}
		
		for (int i = start; i < start + LENGTH; i++){
			int bitShift = (LENGTH - 1 - (i - start)) * 8;
			value += (array[i] & 0x000000FF) << bitShift;
		}
		
		return value;
	}
}