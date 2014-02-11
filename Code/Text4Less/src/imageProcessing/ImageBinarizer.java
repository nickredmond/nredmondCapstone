package imageProcessing;

import java.awt.image.BufferedImage;

public class ImageBinarizer {
	private static final float GAMMA = 2.2f;
	private static final float R_LUMINANCE = 0.2126f;
	private static final float G_LUMINANCE = 0.7152f;
	private static final float B_LUMINANCE = 0.0722f;
	
	private static final int DARK_BOUNDARY = 5000;
	
	public static int[][] convertImageToBinaryValues(BufferedImage img){
		int[][] lightValues = new int[img.getHeight()][img.getWidth()];
		
		for (int x = 0; x < img.getWidth(); x++){
			for (int y = 0; y < img.getHeight(); y++){
				int rgbValue = img.getRGB(x, y);
				int rVal = (rgbValue >> 16) & 0xff;
				int gVal = (rgbValue >> 8) & 0xff;
				int bVal = (rgbValue) & 0xff;
				
				float rLinear = (float) Math.pow(rVal, GAMMA);
				float gLinear = (float) Math.pow(gVal, GAMMA);
				float bLinear = (float) Math.pow(bVal, GAMMA);
				
				float luminance = (R_LUMINANCE * rLinear) + (G_LUMINANCE + gLinear) +
						(B_LUMINANCE * bLinear);
				
				int lightness = (int) (116 * Math.pow(luminance, 0.3333) - 16);
				lightValues[y][x] = (lightness < DARK_BOUNDARY) ? 0 : 1;
			}
		}
		
		invertLightValues(lightValues);
		return lightValues;
	}
	
	private static void invertLightValues(int[][] lightValues) {
		for (int row = 0; row < lightValues.length; row++){
			for (int col = 0; col < lightValues[row].length; col++){
				lightValues[row][col] = ((lightValues[row][col] == 0) ? 1 : 0);
			}
		}
	}
}
