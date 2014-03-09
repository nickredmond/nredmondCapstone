package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import decisionTrees.GeneticMetaclassTree;
import decisionTrees.IMetaclassTree;

public class MetaclassTreeIO {
	private static String FILEPATH = "savedDecisionTrees/";
	private static String EXTENSION = ".tree";
	
	public static void saveTree(IMetaclassTree tree, String filename) throws IOException{
		FileOutputStream outputStream = new FileOutputStream(new File(FILEPATH + filename + EXTENSION));
		ObjectOutputStream objOut = new ObjectOutputStream(outputStream);
		objOut.writeObject(tree);
		objOut.close();
	}
	
	public static IMetaclassTree readTree(String filename) throws IOException{
		FileInputStream inputStream = new FileInputStream(new File(FILEPATH + filename + EXTENSION));
		ObjectInputStream objIn = new ObjectInputStream(inputStream);
		
		IMetaclassTree tree = null;
		
		try {
			tree = (IMetaclassTree) objIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		objIn.close();
		
		return tree;
	}
}
