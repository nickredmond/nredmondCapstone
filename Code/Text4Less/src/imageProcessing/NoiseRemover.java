package imageProcessing;

import java.awt.Point;
import java.util.List;

import featureExtraction.FeatureExtractionHelper;

public class NoiseRemover {
	public static void removeNoise(int[][] imageValues){
		int[][] noiseRemovedValues = imageValues;
		
		for (int row = 0; row < noiseRemovedValues.length; row++){
			for (int col = 0; col < noiseRemovedValues[0].length; col++){
				List<Point> blackNeighbors = FeatureExtractionHelper.getBlackNeighbors(noiseRemovedValues, row, col);
				
				if (blackNeighbors.size() == 0){
					noiseRemovedValues[row][col] = 0;
				}
				else if (blackNeighbors.size() == 1){
					Point blackNeighbor = blackNeighbors.get(0);
					List<Point> neighborsBlackNeighbors = 
							FeatureExtractionHelper.getBlackNeighbors(noiseRemovedValues, blackNeighbor.y, blackNeighbor.x);
					if (neighborsBlackNeighbors.size() == 1){
						noiseRemovedValues[row][col] = 0;
						noiseRemovedValues[blackNeighbor.y][blackNeighbor.x] = 0;
					}
				}
			}
		}
	}
	
	private static boolean isOnEdge(int[][] imageValues, int row, int col){
		int height = imageValues.length;
		int width = imageValues[0].length;
		
		return (row == 0 || col == 0 || col == width - 1 || row == height - 1);
	}
}