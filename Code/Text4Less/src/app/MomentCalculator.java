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
		int centroidX = (moment00 != 0) ? calculateRegularMoment(imageValues, 1, 0) / moment00 : imageValues[0].length / 2;
		int centroidY = (moment00 != 0) ? calculateRegularMoment(imageValues, 0, 1) / moment00 : imageValues.length / 2;
		
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
		float scaleInvariantMoment = calculateCentralMoment(imageValues, p, q) / scaledZeroMoment;
		return scaleInvariantMoment;
	}
	
	public static float[] calculateHuInvariantMoments(int[][] imageValues){
		float moment11 = calculateScaleInvariantCentralMoment(imageValues, 1, 1);
		float moment20 = calculateScaleInvariantCentralMoment(imageValues, 2, 0);
		float moment02 = calculateScaleInvariantCentralMoment(imageValues, 0, 2);
		float moment21 = calculateScaleInvariantCentralMoment(imageValues, 2, 1);
		float moment12 = calculateScaleInvariantCentralMoment(imageValues, 1, 2);
		float moment03 = calculateScaleInvariantCentralMoment(imageValues, 0, 3);
		float moment30 = calculateScaleInvariantCentralMoment(imageValues, 3, 0);
		
		float central03 = calculateCentralMoment(imageValues, 0, 3);
		
		float order1 = moment20 + moment02;
		float order2 = (float) (Math.pow(moment20 - moment02, 2) + (4 * (moment11 * moment11)));
		float order3 = (float) (Math.pow(moment30 - (3 * moment12), 2) + Math.pow((3 * moment21) - moment03, 2));
		float order4 = ((moment30 + moment12) * (moment30 + moment12)) + ((moment21 + moment03) * (moment21 + moment03));
		
		float n30plusn12 = moment30 + moment12;
		float n21plusn03 = moment21 + moment03;
		
		float firstTerm = (float) ((moment30 - (3 * moment12)) * n30plusn12 * (Math.pow(n30plusn12, 2) - (3 * Math.pow(n21plusn03, 2))));
		float secondTerm = (float) (((3 * moment21) - moment03) * n21plusn03 * ((3 * Math.pow(n30plusn12, 2)) - Math.pow(n21plusn03, 2)));
		float order5 = firstTerm + secondTerm;
		
		float term1_6 = (float) ((moment20 - moment02) * (Math.pow(n30plusn12, 2) - Math.pow(n21plusn03, 2)));
		float term2_6 = (4 * moment11) * n30plusn12 * n21plusn03;
		float order6 = term1_6 + term2_6;
		
		float term1_7 = (float) (((3 * moment21) - moment03) * n30plusn12 * (Math.pow(n30plusn12, 2) - (3 * Math.pow(n21plusn03, 2))));
		float term2_7 = (float) ((moment30 - (3 * moment12)) * n21plusn03 * ((3 * Math.pow(n30plusn12, 2)) - Math.pow(n21plusn03, 2)));
		float order7 = term1_7 - term2_7;
		
		float[] huMoments = {order1, order2, order3, order4, order5, order6, order7};
		
		if (order1 != order1){
			for (int i = 0; i < huMoments.length; i++){
				huMoments[i] = 0;
			}
		}
		
		return huMoments;
	}
}
