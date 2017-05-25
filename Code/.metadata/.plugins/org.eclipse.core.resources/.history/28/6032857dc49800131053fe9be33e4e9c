package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import neuralNetwork.CharacterReader;

public class ImageHandlerFactory {
	public static final String CONFIG_FILEPATH = "config.txt";
	
	public static ICharacterImageHandler getImageHandler(List<CharacterResult> results, CharacterReader reader) throws IOException{
		BufferedReader configReader = new BufferedReader(new FileReader(new File(CONFIG_FILEPATH)));
		String handlerType = configReader.readLine();
		ImageReadMethod type = ImageReadMethod.valueOf(handlerType);
		configReader.close();
		return type.getHandler(reader, results);
	}
	
	public static void setHandlerMethod(ImageReadMethod method) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(CONFIG_FILEPATH)));
		writer.write(method.toString());
		writer.close();
	}
}
