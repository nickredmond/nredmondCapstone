package appTest;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class SegmentationHistogramPanel extends JPanel {
	private int width, height, maxHistogramValue;
	private int[] histogramData;
	
	public SegmentationHistogramPanel(int width, int height){
		this.width = width;
		this.height = height;
		this.setSize(new Dimension(width, height));
	}
	
	public void setHistogramData(int[] data){
		if (data.length > width){
			throw new IllegalArgumentException("Data length cannot be larger than panel width");
		}
		else{
			histogramData = data;
			maxHistogramValue = 0;
			
			for (int i = 0; i < histogramData.length; i++){
				if (histogramData[i] > maxHistogramValue){
					maxHistogramValue = histogramData[i];
				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		float columnWidth = (float)width / histogramData.length;
		
		for (int i = 0; i < histogramData.length; i++){
			int currentX = (int)(columnWidth * i);
			int columnHeight = (int)((float)histogramData[i] / maxHistogramValue * height);
			int currentY = height - columnHeight;
			
			g.fillRect(currentX, currentY, (int)columnWidth, columnHeight);
			g.drawRect(0, 0, width, height);
		}
	}
}