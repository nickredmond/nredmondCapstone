package app;

import imageHandling.ImageReadMethod;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import neuralNetwork.INeuralNetwork;
import ui.ImageLoader;

public class DirectoryReader {
	
	
	public static void setNetwork(INeuralNetwork network){
		InputReader.setNetwork(network);
	}
	
	public static List<String> readDirectoryImages(File directory) throws IOException{
		File[] files = directory.listFiles();
		List<String> translationList = new ArrayList<String>();
		
		for (File nextFile : files){
			String fileName = nextFile.getName();
			
			if (ImageLoader.isImage(fileName)){
				BufferedImage image = ImageIO.read(nextFile);
				ReadResult result = InputReader.readImageInput(image, ImageReadMethod.NEURAL_NETWORK);
				translationList.add(result.getTranslationString());
			}
		}
		
		return translationList;
	}
}
