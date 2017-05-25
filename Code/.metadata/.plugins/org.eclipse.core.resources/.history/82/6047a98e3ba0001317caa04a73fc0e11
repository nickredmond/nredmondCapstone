package ui;

import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class DigitalDrawingWindow extends JFrame {
	private DrawingPanel drawing;
	private CharacterRecognitionPanel recognition;
	
	public DigitalDrawingWindow(){
		drawing = new DrawingPanel();
		recognition = new CharacterRecognitionPanel(this);
		
		drawing.setBorder(BorderFactory.createTitledBorder("Drawing"));
		recognition.setBorder(BorderFactory.createTitledBorder("Recognition"));
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		this.getContentPane().add(drawing);
		this.getContentPane().add(recognition);
		
		this.pack();
		this.setVisible(true);
	}
	
	public void recognizeClicked(){
		BufferedImage userImage = drawing.getUserImage();
		recognition.setImage(userImage);
	}
}
