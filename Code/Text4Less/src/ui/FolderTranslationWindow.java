package ui;

import io.WordDocWriter;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.DirectoryReader;
import app.InputReader;

public class FolderTranslationWindow extends JFrame {
	private JButton chooseFolderButton, translateButton, saveButton;
	private JLabel selectedFolderLabel;
	
	private SpellCheckPanel spellCheckPanel;
	private File selectedFolder;
	private List<String> translations;
	
	private WordDocWriter writer;
	
	private final int DEFAULT_WIDTH = 550;
	private final int DEFAULT_HEIGHT = 600;
	
	private JTextArea textArea;
	
	public FolderTranslationWindow(){
		setupDisplay();
		InputReader.setNetwork(null);
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setVisible(true);
	}

	private void setupDisplay() {
		ButtonListener listener = new ButtonListener();		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		setupFolderSelectionPanel(listener, contentPane);
		setupSpellCheckPanel(contentPane);
		
		translateButton = new JButton("Translate Images");
		translateButton.addActionListener(listener);
		contentPane.add(translateButton);
		
		setupResultPanel(listener, contentPane);
		
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
		
		textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setLineWrap(true);
		JScrollPane resultArea = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		saveButton = new JButton("Save as MS Word Doc");
		saveButton.setEnabled(false);
		saveButton.addActionListener(listener);
		
		resultPanel.add(title);
		resultPanel.add(resultArea);
		resultPanel.add(saveButton);
		
		contentPane.add(resultPanel);
	}
	
	private void setupFolderSelectionPanel(ButtonListener listener, Container contentPane){
		JPanel chooseFolderPanel = new JPanel();
		chooseFolderPanel.setLayout(new BoxLayout(chooseFolderPanel, BoxLayout.X_AXIS));
		chooseFolderPanel.add(new JLabel("Selected Folder: "));
		
		selectedFolderLabel = new JLabel("(None selected)");
		chooseFolderPanel.add(selectedFolderLabel);
		
		chooseFolderButton = new JButton("Choose Folder");
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
				//chooser.setCurrentDirectory(new File(new MainTest().getWorkingDirectory() + "/books"));
				
				int result = chooser.showOpenDialog(FolderTranslationWindow.this);
				
				if (result == JFileChooser.APPROVE_OPTION){
					selectedFolder = chooser.getSelectedFile();
					selectedFolderLabel.setText("Selected Folder: " + selectedFolder.getName());
				}
			}
			else if (evt.getSource() == translateButton){
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
					
					textArea.setText(resultText);
					
					saveButton.setEnabled(true);
					textArea.setEnabled(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally{
					translateButton.setEnabled(true);
				}
			}
			else if (evt.getSource() == saveButton){
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("MS Word Document", ".doc"));
				
				int result = chooser.showSaveDialog(FolderTranslationWindow.this);
				
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
