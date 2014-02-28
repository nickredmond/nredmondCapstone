package ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HomeWindow extends JFrame{
	private JButton translateButton, translateFolderButton, trainNetButton, userCharacterButton;
	
	private final int DEFAULT_WIDTH = 500;
	private final int DEFAULT_HEIGHT = 300;
	
	public HomeWindow(){
		JLabel title = new JLabel("Text4Less");
		JLabel subTitle = new JLabel("An Experiment in OCR");
		
		title.setFont(new Font("Helvetica", Font.PLAIN, 48));
		subTitle.setFont(new Font("Helvetica", Font.ITALIC, 20));
		this.getContentPane().add(Box.createHorizontalGlue());
		title.setAlignmentX(CENTER_ALIGNMENT);
		subTitle.setAlignmentX(CENTER_ALIGNMENT);
		
		translateButton = new JButton("Translate an Image");
		translateFolderButton = new JButton("Translate Folder");
		trainNetButton = new JButton("Train Neural Network");
		userCharacterButton = new JButton("User Characters");
		setupButton(translateButton);
		setupButton(translateFolderButton);
		setupButton(trainNetButton);
		setupButton(userCharacterButton);
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(title);
		contentPane.add(subTitle);
		contentPane.add(translateButton);
		contentPane.add(translateFolderButton);
		contentPane.add(trainNetButton);
		contentPane.add(userCharacterButton);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setVisible(true);
	}

	private void setupButton(JButton button) {
		button.setFont(new Font("Helvetica", Font.PLAIN, 24));
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.addActionListener(new ButtonListener());
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == translateButton){
				new MainWindow();
			}
			else if (evt.getSource() == trainNetButton){
				new CustomNetworkWindow();
			}
			else if (evt.getSource() == userCharacterButton){
				new DigitalDrawingWindow();
			}
			else if (evt.getSource() == translateFolderButton){
				new FolderTranslationWindow();
			}
		}
	}
}
