package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SaveImageWindow extends JFrame{
	private JLabel selectedFolderLabel;
	private JButton chooseFolderButton, saveButton;
	private JTextField characterTextField;
	
	private File selectedFolder;
	
	public SaveImageWindow(){
		setupWindow();
	}

	private void setupWindow() {
		JPanel setFolderPanel = new JPanel();
		selectedFolderLabel = new JLabel("Selected Training Set: ---");
		chooseFolderButton = new JButton("Choose Folder...");
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == chooseFolderButton){
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int result = chooser.showOpenDialog(SaveImageWindow.this);
				
				if (result == JFileChooser.APPROVE_OPTION){
					selectedFolder = chooser.getSelectedFile();
				}
			}
			else if (evt.getSource() == saveButton){
				
			}
		}
	}
}
