package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import neuralNetwork.INeuralNetwork;
import neuralNetwork.NeuralNetwork;

public class NeuralNetworkIO {
	private static final String NETWORK_DIRECTORY = "savedNetworks/";
	private static final String EXTENSION = ".ann";
	
	public static void writeNetworkToPath(INeuralNetwork network, String networkAbsolutePath){
		try {
			FileOutputStream outputStream = new FileOutputStream(networkAbsolutePath);
			write(outputStream, network);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void write(FileOutputStream outputStream, INeuralNetwork network){
		try{
			ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
			objectOutput.writeObject(network);
			
			objectOutput.close();
			outputStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void writeNetwork(INeuralNetwork network, String networkName){
		try {
			FileOutputStream outputStream = new FileOutputStream(NETWORK_DIRECTORY +
					 networkName + EXTENSION);
			write(outputStream, network);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static INeuralNetwork readFromFilepath(String filepath){
		return read(filepath);
	}
	
	private static INeuralNetwork read(String filepath) {
		INeuralNetwork network = null;
		
		try {
			FileInputStream inputStream = new FileInputStream(filepath);
			ObjectInputStream objectInput = new ObjectInputStream(inputStream);
			network = (INeuralNetwork) objectInput.readObject();
			
			objectInput.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return network;
	}

	public static INeuralNetwork readNetwork(String networkName){
		String path = NETWORK_DIRECTORY + networkName + EXTENSION;
		return read(path);
	}
}
