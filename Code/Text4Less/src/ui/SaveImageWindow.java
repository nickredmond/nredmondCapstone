package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import app.AlphaNumericCharacterConverter;

public class SaveImageWindow extends JFrame{
	private JButton saveButton;
	private JTextField characterTextField;
	
	private DrawingPanel panel;
	private TrainingSetSelectionPanel setSelectionPanel;
	
	public SaveImageWindow(DrawingPanel panel){
		this.panel = panel;
		setupWindow();
		this.pack();
		this.setVisible(true);
	}

	private void setupWindow() {
		setSelectionPanel = new TrainingSetSelectionPanel(true);		
		
		JPanel characterSelectionPanel = new JPanel();
		JLabel charLabel = new JLabel("Enter character:");
		characterTextField = new JTextField(1);
		characterTextField.setInputVerifier(new InputVerifier(){
			@Override
			public boolean verify(JComponent input) {
				JTextField field = (JTextField)input;
				return (field.getText().length() <= 1);
			}
		});
		
		characterSelectionPanel.setLayout(new BoxLayout(characterSelectionPanel, BoxLayout.X_AXIS));
		characterSelectionPanel.add(charLabel);
		characterSelectionPanel.add(characterTextField);
		
		JLabel infoLabel = new JLabel("Which character should this be recognized as? (Alphanumeric characters only)");
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ButtonListener());
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(setSelectionPanel);
		this.getContentPane().add(infoLabel);
		this.getContentPane().add(characterSelectionPanel);
		this.getContentPane().add(saveButton);
	}
	

	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == saveButton){
				File selectedFolder = setSelectionPanel.getSelectedFolder();
				
				if (selectedFolder != null){
					String selectedChar = characterTextField.getText();
					
					if (selectedChar.length() == 1 && isValidCharacter(selectedChar.charAt(0))){
						char character = selectedChar.charAt(0);
						String filename = TrainingDataNameAssigner.assignName(selectedFolder, character);
						File imageFile = new File(selectedFolder.getAbsoluteFile() + "/" + filename);
						panel.imageSaved(imageFile);
						SaveImageWindow.this.dispose();
					}
					else JOptionPane.showMessageDialog(SaveImageWindow.this, 
							"Must enter a valid character (A-Z, a-z, or 0-9 only).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
				else JOptionPane.showMessageDialog(SaveImageWindow.this, 
						"Must select a destination folder for the image.", "No Folder Selected", JOptionPane.ERROR_MESSAGE);
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