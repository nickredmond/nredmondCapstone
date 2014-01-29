package appTest;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CharacterViewerPanel extends JPanel {
	public JLabel image;
	public BinaryImagePanel binaryPanel;
	
	private final int BINARY_WIDTH = 200;
	private final int BINARY_HEIGHT = 300;
	//public JPanel[][] binaryRepresentaion;
	
	public CharacterViewerPanel(int charDimensionX, int charDimensionY){
		image = new JLabel("No Image Data");
		binaryPanel = new BinaryImagePanel(charDimensionX, charDimensionY, BINARY_WIDTH, BINARY_HEIGHT);
		
		setupPanel();
	}
	
	private void setupPanel(){
		image.setSize(new Dimension(100, 100));
		
		this.removeAll();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(binaryPanel);
		this.add(image);
	}
	
	public void setData(BufferedImage img, int[][] imageValues){
		if (img == null){
			image = new JLabel("No Image Data");
		}
		else {
			image = new JLabel(new ImageIcon(img));
		}
		
		setupPanel();
		binaryPanel.setData(imageValues);
	}
}