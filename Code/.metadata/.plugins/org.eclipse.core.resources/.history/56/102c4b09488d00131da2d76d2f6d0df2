package app;

import imageProcessing.TranslationResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import neuralNetwork.CharacterReader;

public class ImageHandlerFactory {
	public static final String CONFIG_FILEPATH = "config.txt";
	
	public static ICharacterImageHandler getImageHandler(List<TranslationResult> results, CharacterReader reader) throws IOException{
		BufferedReader configReader = new BufferedReader(new FileReader(new File(CONFIG_FILEPATH)));
		String handlerType = configReader.readLine();
		ImageHandlerType type = ImageHandlerType.valueOf(handlerType);
		configReader.close();
		return type.getHandler(reader, results);
	}
}
