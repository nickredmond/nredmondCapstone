package imageProcessing;

import java.awt.image.BufferedImage;

public enum RgbValueReader {
	ROW_VALUE_READER{
		@Override
		public long readRgbValue(BufferedImage img, int position) {
			long totalRgbForLine = 0;
			
			for (int x = 0; x < img.getWidth(); x++){
				int nextPixelValue = Math.abs(img.getRGB(x, position));
				totalRgbForLine += nextPixelValue;
			}
			
			return totalRgbForLine;
		}
	},
	COLUMN_VALUE_READER{
		@Override
		public long readRgbValue(BufferedImage img, int position) {
			long totalRgbForLine = 0;
			
			for (int x = 0; x < img.getHeight(); x++){
				int nextPixelValue = Math.abs(img.getRGB(position, x));
				totalRgbForLine += nextPixelValue;
			}
			
			return totalRgbForLine;
		};
	};
	
	public abstract long readRgbValue(BufferedImage img, int position);
}
