package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import app.AlphaNumericCharacterConverter;
import app.Main;

public class TrainingSetSelectionPanel extends JPanel {
	private JPanel addFolderPanel;
	private JButton newSetButton, saveSetButton, chooseFolderButton;
	private JLabel selectedFolderLabel;
	private JTextField newSetField;
	
	private File selectedFolder;
	private boolean isAddFolderEnabled;
	
	public TrainingSetSelectionPanel(boolean isAddFolderEnabled){
		this.isAddFolderEnabled = isAddFolderEnabled;
		setupPanel();
	}
	
	private void setupPanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel selectedFolderPanel = new JPanel();
		selectedFolderLabel = new JLabel("Selected Training Set: ---");
		chooseFolderButton = new JButton("Choose Folder...");
		chooseFolderButton.addActionListener(new ButtonListener());
		
		selectedFolderLabel.setFont(HomeWindow.SUB_LABEL_FONT);
		chooseFolderButton.setFont(HomeWindow.SUB_LABEL_FONT);
		
		selectedFolderPanel.setLayout(new BoxLayout(selectedFolderPanel, BoxLayout.X_AXIS));
		selectedFolderPanel.add(selectedFolderLabel);
		selectedFolderPanel.add(chooseFolderButton);
		
		if (isAddFolderEnabled){
			newSetButton = new JButton("New Folder");
			newSetButton.addActionListener(new ButtonListener());
			selectedFolderPanel.add(newSetButton);
		}
		
		this.add(selectedFolderPanel);
		
		setupAddFolderPanel();
		this.add(addFolderPanel);
		addFolderPanel.setVisible(false);
	}
	
	public File getSelectedFolder(){
		return selectedFolder;
	}
	
	public void setSelectedFolder(File selected){
		selectedFolder = selected;
		selectedFolderLabel.setText("Selected TrainingSet: " + selectedFolder.getName());
	}
	
	private void setupAddFolderPanel() {
		addFolderPanel = new JPanel();
		newSetField = new JTextField();
		saveSetButton = new JButton("Create Training Set (Adds new folder)");
		saveSetButton.addActionListener(new ButtonListener());
		
		addFolderPanel.setLayout(new BoxLayout(addFolderPanel, BoxLayout.X_AXIS));
		addFolderPanel.add(newSetField);
		addFolderPanel.add(saveSetButton);
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == chooseFolderButton){
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(new File(new Main().getWorkingDirectory() + "/trainingImages"));
				
				int result = chooser.showOpenDialog(TrainingSetSelectionPanel.this);
				
				if (result == JFileChooser.APPROVE_OPTION){
					selectedFolder = chooser.getSelectedFile();
					selectedFolderLabel.setText("Selected Training Set: " + selectedFolder.getName());
				}
			}
			else if (evt.getSource() == newSetButton){
				addFolderPanel.setVisible(true);
				repaint();
			}
			else if (evt.getSource() == saveSetButton){
				String folderName = newSetField.getText();
				
				if (folderName.length() > 0 && !folderName.contains(".")){
					File trainingDataDir = new File(new Main().getWorkingDirectory() + "/trainingImages/" + folderName);
					boolean success = trainingDataDir.mkdir();
					
					addFolderPanel.setVisible(false);
					repaint();
					
					if (!success){
						JOptionPane.showMessageDialog(TrainingSetSelectionPanel.this, "Could not create the specified folder.", "Unknown Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				else JOptionPane.showMessageDialog(TrainingSetSelectionPanel.this, "Training set name is invalid.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
			}
		}

		private boolean isValidCharacter(char character) {
			int charValue = (int)character;
			return ((charValue >= AlphaNumericCharacterConverter.UPPER_START && charValue <= AlphaNumericCharacterConverter.UPPER_END) ||
					(charValue >= AlphaNumericCharacterConverter.LOWER_START && charValue <= AlphaNumericCharacterConverter.LOWER_END) ||
					(charValue >= AlphaNumericCharacterConverter.NUMERIC_START && charValue <= AlphaNumericCharacterConverter.NUMERIC_END));
		}
	}
}
