package ui;

import io.Logger;
import io.WordDocWriter;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import spellCheck.SpellChecker;
import app.DirectoryReader;
import app.InputReader;

public class FolderTranslationTab extends JPanel {
	private JButton chooseFolderButton, translateButton, saveButton;
	private JLabel selectedFolderLabel;
	
	private SpellCheckPanel spellCheckPanel;
	private File selectedFolder;
	private List<String> translations;
	
	private WordDocWriter writer;
	
	private final int DEFAULT_WIDTH = 550;
	private final int DEFAULT_HEIGHT = 600;
	
	private JTextArea textArea;
	private LoadingScreen screen;
	
	public FolderTranslationTab(){
		setupDisplay();
		InputReader.setNetwork(null);
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setVisible(true);
	}

	private void setupDisplay() {
		ButtonListener listener = new ButtonListener();		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		setupFolderSelectionPanel(listener, this);
		setupSpellCheckPanel(this);
		
		translateButton = new JButton("Translate Images");
		translateButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
		translateButton.addActionListener(listener);
		this.add(translateButton);
		
		setupResultPanel(listener, this);
		
		try {
			writer = new WordDocWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setupSpellCheckPanel(Container contentPane){
		spellCheckPanel = new SpellCheckPanel();
		contentPane.add(spellCheckPanel);
	}
	
	private void setupResultPanel(ButtonListener listener, Container contentPane){
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		
		JLabel title = new JLabel("RESULTS:");
		title.setFont(HomeWindow.DEFAULT_LABEL_FONT);
		
		textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setLineWrap(true);
		JScrollPane resultArea = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		saveButton = new JButton("Save as MS Word Doc");
		saveButton.setEnabled(false);
		saveButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
		saveButton.addActionListener(listener);
		
		resultPanel.add(title);
		resultPanel.add(resultArea);
		resultPanel.add(saveButton);
		
		contentPane.add(resultPanel);
	}
	
	private void setupFolderSelectionPanel(ButtonListener listener, Container contentPane){
		JPanel chooseFolderPanel = new JPanel();
		chooseFolderPanel.setLayout(new BoxLayout(chooseFolderPanel, BoxLayout.X_AXIS));
		
		JLabel folderLabel = new JLabel("Selected Folder: ");
		folderLabel.setFont(HomeWindow.SUB_LABEL_FONT);
		
		chooseFolderPanel.add(folderLabel);
		
		selectedFolderLabel = new JLabel("(None selected)");
		selectedFolderLabel.setFont(HomeWindow.SUB_LABEL_FONT);
		chooseFolderPanel.add(selectedFolderLabel);
		
		chooseFolderButton = new JButton("Choose Folder");
		chooseFolderButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
		chooseFolderButton.addActionListener(listener);
		chooseFolderPanel.add(chooseFolderButton);
		
		contentPane.add(chooseFolderPanel);
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == chooseFolderButton){
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int result = chooser.showOpenDialog(FolderTranslationTab.this);
				
				if (result == JFileChooser.APPROVE_OPTION){
					selectedFolder = chooser.getSelectedFile();
					selectedFolderLabel.setText("Selected Folder: " + selectedFolder.getName());
				}
			}
			else if (evt.getSource() == translateButton){
				if (selectedFolder == null){
					JOptionPane.showMessageDialog(FolderTranslationTab.this, "Must select a folder for translation.",
							"No Folder Selected", JOptionPane.ERROR_MESSAGE);
				}
				else{
					screen = new LoadingScreen("Reading Images", false, FolderTranslationTab.this, 400, 250);
				
					new Thread(){
						public void run(){
							try {
								saveButton.setEnabled(false);
								translateButton.setEnabled(false);
								
								translations = DirectoryReader.readDirectoryImages(selectedFolder);
								String resultText = "";
								int index = 0;
								
								for (String nextResult : translations){
									resultText += nextResult;
									
									if (index < translations.size() - 1){
										resultText += "\r\n--- PAGE BREAK ---\r\n";
									}
									
									index++;
								}
								
								if (spellCheckPanel.isSpellCheckEnabled()){
									resultText = SpellChecker.spellCheckText(resultText);
								}
								
								textArea.setText(resultText);
								
								saveButton.setEnabled(true);
								textArea.setEnabled(true);
							}
							catch (IOException e) {
									Logger.logMessage("Error occured while translating folder. Message: " + e.getMessage() + "; Source: " + Arrays.toString(e.getStackTrace()));
									JOptionPane.showMessageDialog(FolderTranslationTab.this, "Due to an error, the folder could not be translated.", "Error", JOptionPane.ERROR_MESSAGE);
							}
							finally{
								translateButton.setEnabled(true);
								screen.closeScreen();
							}
						}
					}.start();
				}
			}
			else if (evt.getSource() == saveButton){
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("MS Word Document", ".doc"));
				
				int result = chooser.showSaveDialog(FolderTranslationTab.this);
				
				if (result == JFileChooser.APPROVE_OPTION){
					File selectedFile = chooser.getSelectedFile();
					
					String filepath = selectedFile.getAbsolutePath();
					if (!filepath.endsWith(".doc")){
						filepath += ".doc";
					}
					
					writer.clearTranslations();
					writer.addTranslations(translations);
					
					try {
						writer.writeDocument(new File(filepath));
					} catch (IOException e) {
						Logger.logMessage("Exception occurred while attempting to export text to Word document. Message: " + e.getMessage() + "; Source: FolderTranslationTab");
						JOptionPane.showMessageDialog(FolderTranslationTab.this, "An unexpected error has occurred. Could not export the translation as a MS Word document",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}
}
