package appTest;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SegmentationPanel extends JPanel {
	public SegmentationPanel(BufferedImage image, int[] histogramData, int width, int height){
		this.setLayout(new GridLayout(2, 1));
		setupImage(image, width);
		
		SegmentationHistogramPanel histogramPanel = new SegmentationHistogramPanel(width, height);
		histogramPanel.setHistogramData(histogramData);
		this.add(histogramPanel);
	}
	
	private void setupImage(BufferedImage image, int newWidth){
		Image characterImg = image;
		float percentHeightToWidth = (float)characterImg.getHeight(null) / characterImg.getWidth(null);
		
		int newHeight = (int)(percentHeightToWidth * newWidth);
		
		BufferedImage baseImg = new BufferedImage(newWidth,
				newHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = baseImg.createGraphics();
		g.drawImage(characterImg, 0, 0, newWidth, newHeight, null);
		ImageIcon finalIcon = new ImageIcon(baseImg);
		
		JLabel characterLabel = new JLabel(finalIcon);
		this.add(characterLabel);
	}
}