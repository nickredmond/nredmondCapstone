package ui;

import io.UserPreferencesIO;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import app.UserPreferences;

public class MenuWindow extends JFrame {
	private final int DEFAULT_HEIGHT = 750;
	private final int DEFAULT_WIDTH = 875;
	
	private UserPreferences preferences;
	private ImageTranslationTab imgTab;
	
	private final String ACCURACY_PROMPT = "The translation accuracy (or recogntion rate) of this system has been verified at 92%\n" +
			"for Times New Roman and Arial fonts. Lower accuracies are likely for other fonts.\n Proofreading is " +
			"strongly recommended after translation.";
	
	public MenuWindow(){
		setupWindow();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Text4Less");
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}
	
	public void setPreferences(UserPreferences preferences){
		this.preferences = preferences;
		imgTab.setUserPreferences(preferences);
	}

	private void setupWindow() {
		imgTab = new ImageTranslationTab();
		
		JTabbedPane pane = new JTabbedPane();
		pane.setFont(HomeWindow.DEFAULT_LABEL_FONT);
		pane.addTab("Image Translation", imgTab);
		pane.addTab("Folder Translation", new FolderTranslationTab());
		pane.addTab("Neural Network Training", new CustomNetworkTab());
		pane.addTab("Handwritten Characters", new DigitalDrawingTab());
		
		this.getContentPane().add(pane);
	}
	
	public void displayStartupInfo(){
		if (preferences.isAccuracyPromptEnabled()){
			JCheckBox checkBox = new JCheckBox("Do not show this message again");
			Object[] params = {ACCURACY_PROMPT, checkBox};
			
			JOptionPane.showMessageDialog(this, params, "Application Accuracy", JOptionPane.INFORMATION_MESSAGE);
			boolean requestedBlock = checkBox.isSelected();
			
			if (requestedBlock){
				UserPreferences newPrefs = new UserPreferences(false, preferences.isSpellCheckEnabled(), preferences.getReadMethod());
				UserPreferencesIO.writePreferences(newPrefs);
			}
		}
	}
}
