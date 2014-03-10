package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.CharacterResult;

public class ResultPanel extends JPanel {
	private JButton saveButton;
	private JScrollPane resultArea;
	private JTextArea textArea;
	
	public ResultPanel(ImageTranslationTab window){
		setupPanel();
	}
	
	public void setResultText(String text, List<CharacterResult> rejections){
		textArea.setEnabled(true);
		textArea.setText(text);
		saveButton.setEnabled(true);
		
		String message = "The system rejected " + rejections.size() + " characters because it was not " +
				"\r\nconfident enough in their translations.";
		JOptionPane.showMessageDialog(this, message, "Rejected Characters", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void reset(){
		textArea.setText("");
		textArea.setEnabled(false);
		saveButton.setEnabled(false);
	}
	
	private void setupPanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel title = new JLabel("RESULTS:");
		title.setFont(HomeWindow.DEFAULT_LABEL_FONT);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Helvetica", Font.PLAIN, 18));
		textArea.setEnabled(false);
		textArea.setLineWrap(true);
		resultArea = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		saveButton = new JButton("Save Text");
		saveButton.setEnabled(false);
		saveButton.addActionListener(new ButtonListener());
		
		this.add(title);
		this.add(resultArea);
		this.add(saveButton);
	}
	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == saveButton){
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
				JFileChooser chooser = new JFileChooser();
				chooser.addChoosableFileFilter(filter);
				int returnValue = chooser.showSaveDialog(ResultPanel.this);
				
				if (returnValue == JFileChooser.APPROVE_OPTION){
					File savedFile = chooser.getSelectedFile();
					
					if (!savedFile.getName().endsWith(".txt")){
						savedFile = new File(savedFile.getAbsolutePath() + ".txt");
					}

					try{
						BufferedWriter writer = new BufferedWriter(new FileWriter(savedFile));
						writer.write(textArea.getText());
						writer.close();
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		
	}
}
