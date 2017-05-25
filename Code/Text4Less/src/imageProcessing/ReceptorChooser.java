package imageProcessing;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import receptors.Receptor;

public class ReceptorChooser {
	private static final int WHITE_RGB_VALUE = -20000;
	private static final float TOLERANCE = 0.02f;
	private static final double TARGET_VALUE = 0.5;
	
	public static List<Point> chooseReceptors(List<BufferedImage> samples, int imageDimension){
		List<Point> receptors = new LinkedList<Point>();
		
		int[][] receptorValues = new int[imageDimension][imageDimension];
		
		for (BufferedImage nextImage : samples){
			for (int x = 0; x < nextImage.getWidth(); x++){
				for (int y = 0; y < nextImage.getHeight(); y++){
					if (nextImage.getRGB(x, y) < WHITE_RGB_VALUE){
						receptorValues[x][y]++;
					}
				}
			}
		}
		
		for (int x = 0; x < receptorValues.length; x++){
			for (int y = 0; y < receptorValues[x].length; y++){
				float nextReceptorAvg = (float)receptorValues[x][y] / samples.size();
				
				if (Math.abs(nextReceptorAvg - TARGET_VALUE) <= TOLERANCE){
					receptors.add(new Point(x, y));
				}
			}
		}
		
		return receptors;
	}

	private void chooseReceptors(List<Receptor> receptors,
			BufferedImage nextImage) {
		
	}
}
