package debug;

import imageProcessing.FeatureExtractionIOTranslator;
import imageProcessing.INetworkIOTranslator;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import featureExtraction.FeaturePoint;

public class FeatureExtractionDebug {
	public static float getCorrelation(BufferedImage first, BufferedImage second){
		final float MAX_DIFFERENCE = 1.0f;
		INetworkIOTranslator translator = new FeatureExtractionIOTranslator();
		
		float[] input1 = translator.translateImageToNetworkInput(first);
		float[] input2 = translator.translateImageToNetworkInput(second);
		
		float differenceSum = 0.0f;
		
		for (int i = 0; i < input1.length; i++){
			differenceSum += Math.abs(input1[i] - input2[i]);
		}
		
		float differenceAvg = differenceSum / input1.length;
		return MAX_DIFFERENCE - differenceAvg;
	}
	
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
	
	public static void printFloatImg(float[][] values){
		for (int row = 0; row < values.length; row++){
			for (int col = 0; col < values[row].length; col++){
				Float nextValue = new Float(values[row][col]);
				String nextFloatString = nextValue.toString();
				
				System.out.print(nextFloatString.substring(0, 3) + " ");
			}
			System.out.println();
		}
	}
	
	public static void printPointFeatures(List<FeaturePoint> points, int zonesX, int zonesY){
		String[][] featurePoints = new String[zonesY][zonesX];
		
		for (int y = 0; y < featurePoints.length; y++){
			for (int x = 0; x < featurePoints[0].length; x++){
				featurePoints[y][x] = "";
			}
		}
		
		for (FeaturePoint nextPoint : points){
			featurePoints[nextPoint.y() - 1][nextPoint.x() - 1] += nextPoint.getType().toString();
		}
		
		for (int y = 0; y < featurePoints.length; y++){
			for (int x = 0; x < featurePoints[0].length; x++){
				System.out.print(featurePoints[y][x]);
				
				int numSpaces = 4 - featurePoints[y][x].length();
				
				for (int i = 0; i < numSpaces; i++){
					System.out.print("-");
				}
				
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void printChainCode(int[] chainCode){
		System.out.print("CHAIN CODE: ");
		for (int i = 0; i < chainCode.length; i++){
			System.out.print(chainCode[i] + " ");
		}
		System.out.println("\r\n");
	}
}
