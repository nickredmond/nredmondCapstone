package io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import neuralNetwork.CharacterTrainingExample;

public class MnistParser {
	private static final int NUMBER_TRAINING_IMAGES = 60000;
	private static final int NUMBER_TEST_IMAGES = 10000;
	
	private static final int LABEL_METADATA_BUFFER_SIZE = 8;
	private static final int IMAGE_METADATA_BUFFER_SIZE = 16;
	
	private static final int DATA_BUFFER_SIZE = 1;
	
	private static final int LABEL_START = 48;
	
	public static Set<CharacterTrainingExample> readImages(String imagesFilepath, String labelsFilepath, int numberImages) throws IOException{
		List<Character> setLabels = readLabels(numberImages, labelsFilepath);
		
		return null;
	}
	
	private static List<Character> readLabels(int numberImages, String labelsFilepath) throws IOException{
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File(labelsFilepath)));
		DataInputStream labelInput = new DataInputStream(input);
		
		byte[] buffer = new byte[LABEL_METADATA_BUFFER_SIZE];
		
//		int position = labelInput.read(buffer, 0, LABEL_METADATA_BUFFER_SIZE);
		
		byte[] flipped = new byte[buffer.length];
		
		for (int i = 0; i < flipped.length; i++){
			flipped[i] = buffer[buffer.length - 1 - i];
		}
		
		int ohFuckYes = labelInput.readInt();
		
//		ByteBuffer b = ByteBuffer.wrap(buffer);
//		Buffer f = b.flip();
//		Object o = f.array();
//		System.out.println(o.getClass().toString());
		
		int magicNumber = convertByteArrayToInt(flipped, 0);
		int numberLabels = convertByteArrayToInt(flipped, 4);
		
		System.out.println("Magic number: " + magicNumber + ", num: " + numberLabels);
		
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