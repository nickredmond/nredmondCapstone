package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

public class DigitalDrawingWindow extends JFrame {
	private DrawingPanel drawing;
	private CharacterRecognitionPanel recognition;
	
	private final int DEFAULT_WIDTH = 900;
	private final int DEFAULT_HEIGHT = 720;
	private final int NUMBER_ELEMENTS = 2;
	
	public DigitalDrawingWindow(){
		drawing = new DrawingPanel();
		recognition = new CharacterRecognitionPanel(this);
		
		drawing.setBorder(BorderFactory.createTitledBorder("Drawing"));
		recognition.setBorder(BorderFactory.createTitledBorder("Recognition"));
		
		this.getContentPane().setLayout(new GridLayout(NUMBER_ELEMENTS, 0));
		
		this.getContentPane().add(drawing);
		this.getContentPane().add(recognition);
		
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setVisible(true);
	}
	
	public void recognizeClicked(){
		BufferedImage userImage = drawing.getUserImage();
		recognition.setImage(userImage);
	}
}
