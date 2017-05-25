package debug;

import imageProcessing.ImageBinarizer;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import appTest.SegmentationPanel;

public class SegmentationDebug {
	public static void displaySegmentationHistogram(BufferedImage image){
		int[] histogramData = getHistogramForImage(image);
		SegmentationPanel panel = new SegmentationPanel(image, histogramData, 600, 300);
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	private static int[] getHistogramForImage(BufferedImage image){
		int[][] lightValues = ImageBinarizer.convertImageToBinaryValues(image);
		
		int[] histogramData = new int[lightValues[0].length];
		
		for (int col = 0; col < lightValues[0].length; col++){
			int blackSum = 0;
			
			for (int row = 0; row < lightValues.length; row++){
				blackSum += lightValues[row][col];
			}
			
			histogramData[col] = blackSum;
		}
		
		return histogramData;
	}
}
