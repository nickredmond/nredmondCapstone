package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import receptors.Receptor;
import decisionTrees.MetaclassTree;

public class MetaclassTreeIO {
	private static String FILEPATH = "savedDecisionTrees/";
	private static String EXTENSION = ".tree";
	
	public static void saveTree(MetaclassTree tree, String filename) throws IOException{
		FileOutputStream outputStream = new FileOutputStream(new File(FILEPATH + filename + EXTENSION));
		ObjectOutputStream objOut = new ObjectOutputStream(outputStream);
		objOut.writeObject(tree);
		objOut.close();
	}
	
	public static MetaclassTree readTree(String filename) throws IOException{
		FileInputStream inputStream = new FileInputStream(new File(FILEPATH + filename + EXTENSION));
		ObjectInputStream objIn = new ObjectInputStream(inputStream);
		
		MetaclassTree tree = null;
		
		try {
			tree = (MetaclassTree) objIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		objIn.close();
		
		return tree;
	}
}