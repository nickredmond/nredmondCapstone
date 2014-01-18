package app;

import java.awt.Point;

public class MomentCalculator {
	public static int calculateRegularMoment(int[][] imageValues, int p, int q){
		int moment = 0;
		
		for (int x = 0; x < imageValues[0].length; x++){
			for (int y = 0; y < imageValues.length; y++){
				moment += Math.pow(x, p) * Math.pow(y, q) * imageValues[y][x];
			}
		}
		
		return moment;
	}
	
	public static Point calculateCentroid(int[][] imageValues){
		int moment00 = calculateRegularMoment(imageValues, 0, 0);
		int centroidX = calculateRegularMoment(imageValues, 1, 0) / moment00;
		int centroidY = calculateRegularMoment(imageValues, 0, 1) / moment00;
		
		return new Point(centroidX, centroidY);
	}
	
	public static int calculateCentralMoment(int[][] imageValues, int p, int q){
		Point centroid = calculateCentroid(imageValues);
		int centralMoment = 0;
		
		for (int x = 0; x < imageValues[0].length; x++){
			for (int y = 0; y < imageValues.length; y++){
				int xFactor = (int) Math.pow(x - centroid.x, p);
				int yFactor = (int) Math.pow(y - centroid.y, q);
				int pixelValue = imageValues[y][x];
				
				centralMoment += xFactor * yFactor * pixelValue;
			}
		}
		
		return centralMoment;
	}
	
	public static float calculateScaleInvariantCentralMoment(int[][] imageValues, int p, int q){
		float scaleValue = 1.0f + ((float)(p + q)/2);
		float scaledZeroMoment = (float)Math.pow(calculateCentralMoment(imageValues, 0, 0), scaleValue);
		//float yes = 50000000 / 5.56f;
		float scaleInvariantMoment = calculateCentralMoment(imageValues, p, q) / scaledZeroMoment;
		return scaleInvariantMoment;
	}
}
