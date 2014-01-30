package io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import neuralNetwork.INeuralNetwork;
import neuralNetwork.NeuralNetwork;

public class NeuralNetworkIO {
	private static final String NETWORK_DIRECTORY = "savedNetworks/";
	private static final String EXTENSION = ".ann";
	
	public static void writeNetwork(INeuralNetwork network, String networkName){
		try {
			FileOutputStream outputStream = new FileOutputStream(NETWORK_DIRECTORY +
					 networkName + EXTENSION);
			ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
			objectOutput.writeObject(network);
			
			objectOutput.close();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static INeuralNetwork readNetwork(String networkName){
		String path = NETWORK_DIRECTORY + networkName + EXTENSION;
		INeuralNetwork network = null;
		
		try {
			FileInputStream inputStream = new FileInputStream(path);
			ObjectInputStream objectInput = new ObjectInputStream(inputStream);
			network = (INeuralNetwork) objectInput.readObject();
			
			objectInput.close();
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return network;
	}
}
