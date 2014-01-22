package debug;

import java.awt.Point;

public class FeatureExtractionDebug {
	public static void printCentroidOnImage(Point centroid, int[][] imageValues){
		boolean hasPrintedDivider = false;
		
		for (int row = 0; row < imageValues.length; row++){
			for (int col = 0; col < imageValues[0].length; col++){
				if (col == centroid.x){
					System.out.print("| ");
				}
				if (row == centroid.y && !hasPrintedDivider){
					for (int y = 0; y <= imageValues[0].length; y++){
						System.out.print("- ");
					}
					hasPrintedDivider = true;
					System.out.println();
				}
				System.out.print(imageValues[row][col] + " ");
			}
			
			System.out.println();
		}
		
		System.out.println("centroid x: " + centroid.x + ", centroid y: " + centroid.y);
	}
	
	public static void printImg(int[][] lightValues){
		for (int row = 0; row < lightValues.length; row++){
			for (int col = 0; col < lightValues[row].length; col++){
				System.out.print(lightValues[row][col] + " ");
			}
			System.out.println();
		}
	}
}
