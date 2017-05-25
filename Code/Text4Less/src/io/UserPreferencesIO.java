package io;

import imageHandling.ImageReadMethod;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import app.Main;
import app.UserPreferences;

public class UserPreferencesIO {
	private static final String FILEPATH = "preferences.t4l";
	
	public static UserPreferences readPreferences() {
		UserPreferences preferences = null;
		
		try {
			FileInputStream inputStream = new FileInputStream(new Main().getWorkingDirectory() + FILEPATH);
			ObjectInputStream objectInput = new ObjectInputStream(inputStream);
			preferences = (UserPreferences) objectInput.readObject();
			
			objectInput.close();
			inputStream.close();
		} catch (IOException | ClassNotFoundException e) {
			preferences = new UserPreferences(true, false, ImageReadMethod.NEURAL_NETWORK);
			JOptionPane.showMessageDialog(null, "Could not load user preferences", "Error", JOptionPane.ERROR_MESSAGE);
			Logger.logMessage("Error loading user preferences. Message: " + e.getMessage());
		}
		
		return preferences;
	}
	
	public static void writePreferences(UserPreferences preferences){
		try{
			FileOutputStream outputStream = new FileOutputStream(new Main().getWorkingDirectory() + FILEPATH);
			ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
			objectOutput.writeObject(preferences);
			
			objectOutput.close();
			outputStream.close();
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, "Could not save user preferences", "Error", JOptionPane.ERROR_MESSAGE);
			Logger.logMessage("Error while saving user preferences. Message: " + e.getMessage());
		}
	}
}
