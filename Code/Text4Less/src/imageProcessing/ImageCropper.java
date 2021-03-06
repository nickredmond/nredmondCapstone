package imageProcessing;

import java.awt.image.BufferedImage;

public enum ImageCropper {
	LEFT_RIGHT_CROPPER {
		@Override
		public BufferedImage cropWithValues(int startingValue,
				int numberPixelsToKeep, BufferedImage original) {
			BufferedImage croppedImg = original.getSubimage(startingValue, 0,
					numberPixelsToKeep, original.getHeight());			
			
			return croppedImg;
		}
	},
	TOP_BOTTOM_CROPPER {
		@Override
		public BufferedImage cropWithValues(int startingValue,
				int numberPixelsToKeep, BufferedImage original) {
			BufferedImage croppedImg = original.getSubimage(0, startingValue,
					original.getWidth(), numberPixelsToKeep);
			
			return croppedImg;
		}
	};
	
	public abstract BufferedImage cropWithValues(int startingValue, int numberPixelsToKeep,
			BufferedImage original);
}
