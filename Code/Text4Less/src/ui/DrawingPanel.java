package ui;

import imageProcessing.ImageBinarizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel{	
	private DrawingArea area;
	private ControlsPanel controls;
	
	public DrawingPanel(){
		area = new DrawingArea(this);
		controls = new ControlsPanel(this);
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(area);
		this.add(controls);
	}
	
	public BufferedImage getUserImage(){
		int[][] imageData = area.getData();
		return ImageBinarizer.convertBinaryValuesToImage(imageData);
	}
	
	public void drawingChanged(){
		controls.repaint();
		this.repaint();
	}
	
	public void saveClicked(){
		new SaveImageWindow(this);
	}
	
	public void imageSaved(File imageFile){
		int[][] imageValues = area.getData();
		BufferedImage image = ImageBinarizer.convertBinaryValuesToImage(imageValues);
		try {
			ImageIO.write(image, "jpg", imageFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "The image could not be saved.", "Save Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void clearClicked(){
		area.clearData();
	}
	
	public void penSizeChanged(int penSize){
		area.setPenSize(penSize);
	}
	
	public void areaSizeChanged(int areaSize){
		area.setDimensions(areaSize, areaSize);
	}
}