package appTest;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImageTestPanel extends JPanel {
	private BufferedImage testImage;

	public BufferedImage getTestImage() {
		return testImage;
	}

	public void setTestImage(BufferedImage testImage) {
		this.testImage = testImage;
	}
	
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(testImage, 0, 0, null);
	}
}
