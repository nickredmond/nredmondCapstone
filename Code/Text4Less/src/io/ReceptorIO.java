package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import receptors.Receptor;

public class ReceptorIO {
	private static String FILEPATH = "savedReceptors/";
	private static String EXTENSION = ".rec";
	
	public static void saveReceptors(List<Receptor> receptors, String filename) throws IOException{
		FileOutputStream outputStream = new FileOutputStream(new File(FILEPATH + filename + EXTENSION));
		ObjectOutputStream objOut = new ObjectOutputStream(outputStream);
		objOut.writeObject(receptors);
		objOut.close();
	}
	
	public static List<Receptor> readReceptors(String filename) throws IOException{
		FileInputStream inputStream = new FileInputStream(new File(FILEPATH + filename + EXTENSION));
		ObjectInputStream objIn = new ObjectInputStream(inputStream);
		
		List<Receptor> receptors = null;
		
		try {
			receptors = (List<Receptor>) objIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		objIn.close();
		
		return receptors;
	}
}
