package imageProcessing;

import java.awt.image.BufferedImage;

public class ImageScaler {
	public static int[][] scaleWithBilinearInterpolation(int[][] imageValues, int newWidth, int newHeight){
		int[] linearizedImgValues = new int[imageValues.length * imageValues[0].length];
		
		int index = 0;
		for (int row = 0; row < imageValues.length; row++){
			for (int col = 0; col < imageValues[0].length; col++){
				linearizedImgValues[index] = imageValues[row][col];
				index++;
			}
		}
		
		int[] interpolatedValues = resizeBilinearly(linearizedImgValues, imageValues.length, imageValues[0].length, newWidth, newHeight);
		
		int[][] scaledImageValues = new int[newHeight][newWidth];
		for (int row = 0; row < newHeight; row++){
			for (int col = 0; col < newWidth; col++){
				int currentIndex = (row * newHeight) + col;
				scaledImageValues[row][col] = interpolatedValues[currentIndex];
			}
		}
		
		return scaledImageValues;
	}
	
	private static int[] resizeBilinearly(int[] imageValues, int oldHeight, int oldWidth, int newHeight, int newWidth){
		int[] interpolatedValues = new int[newHeight * newWidth];
		float widthRatio = ((float)(oldWidth - 1) / newWidth);
		float heightRatio = ((float)(oldHeight - 1) / newHeight);
		int currentIndex = 0;
		
		for (int row = 0; row < newHeight; row++){
			for (int col = 0; col < newWidth; col++){
				int currentX = (int)(widthRatio * col);
				int currentY = (int)(heightRatio * row);
				float xWidthDiff = (widthRatio * col) - currentX;
				float yWidthDiff = (heightRatio * row) - currentY;
				int boundaryPixelIndex = (currentY * oldWidth) + currentX;
				
				int pixelA = imageValues[boundaryPixelIndex];
				int pixelB = imageValues[boundaryPixelIndex + 1];
				int pixelC = imageValues[boundaryPixelIndex + oldWidth];
				int pixelD = imageValues[boundaryPixelIndex + oldWidth + 1];
				
				float newPixelValue = (pixelA * (1 - xWidthDiff) * (1 - yWidthDiff)) +
						(pixelB * xWidthDiff * (1 - yWidthDiff)) + (pixelC * yWidthDiff * (1 - xWidthDiff)) +
						(pixelD * xWidthDiff * yWidthDiff);
				int newPixelBinary = (newPixelValue >= 0.5f) ? 1 : 0;
				
				interpolatedValues[currentIndex] = newPixelBinary;
				currentIndex++;
			}
		}
		
		return interpolatedValues;
	}
	
	public static BufferedImage scaleToHeight(int dimension, BufferedImage img){
        BufferedImage trimmedImg = trimImage(img);
		
		int newHeight = 0;
		int newWidth = 0;
		float scaleValue = 0.0f;
		
		if (trimmedImg.getHeight() > trimmedImg.getWidth()){
			newHeight = dimension;
			scaleValue = dimension / trimmedImg.getHeight();
			newWidth = (int) (trimmedImg.getWidth() * scaleValue);
		}
		else if (trimmedImg.getWidth() > trimmedImg.getHeight()){
			newWidth = dimension;
			scaleValue = dimension / trimmedImg.getWidth();
			newHeight = (int) (trimmedImg.getHeight() / dimension);
		}
		else{
			scaleValue = (dimension / trimmedImg.getHeight());
			newHeight = dimension;
			newWidth = dimension;
		}
		
        int startingX = (dimension - newWidth) / 2;
        int startingY = (dimension - newHeight) / 2;
        
        BufferedImage scaledImg = scaleToProperHeight(trimmedImg, scaleValue, dimension, startingX, startingY);
        
        for (int x = 0; x < startingX; x++){
            for (int y = 0; y < scaledImg.getHeight(); y++){
                    scaledImg.setRGB(x, y, NetworkIOTranslator.WHITE_RGB_VALUE);
            }
        }
    
	     for (int x = (startingX + newWidth + 1); x < scaledImg.getWidth(); x++){
            for (int y = 0; y < scaledImg.getHeight(); y++){
                    scaledImg.setRGB(x, y, NetworkIOTranslator.WHITE_RGB_VALUE);
            }
	     }
	     
	     for (int y = 0; y < startingY; y++){
	    	 for (int x = 0; x < scaledImg.getWidth(); x++){
	    		 scaledImg.setRGB(x, y, NetworkIOTranslator.WHITE_RGB_VALUE);
	    	 }
	     }
	     
	     for (int y = (startingY + newHeight + 1); y < scaledImg.getHeight(); y++){
	    	 for(int x = 0; x < scaledImg.getWidth(); x++){
	    		 scaledImg.setRGB(x, y, NetworkIOTranslator.WHITE_RGB_VALUE);
	    	 }
	     }
	     
	     return scaledImg;
	}
	
	private static BufferedImage trimImage(BufferedImage img){
		int left = getLeft(img);
		int right = getRight(img);
		int top = getTop(img);
		int bottom = getBottom(img);
		
		right = (right == 0) ? img.getWidth() : right;
		bottom = (bottom == 0) ? img.getHeight() : bottom;
		
		left = (left == right) ? 0 : left;
		top = (top == bottom) ? 0 : top;
		
		return img.getSubimage(left, top, right - left, bottom - top);
	}
	
	private static int getLeft(BufferedImage img){
		int left = 0;
		boolean foundLeft = false;
		
		for (int x = 0; x < img.getWidth() && !foundLeft; x++){
			for (int y = 0; y < img.getHeight() && !foundLeft; y++){
				if (img.getRGB(x, y) < NetworkIOTranslator.BLACK_RGB){
					left = x;
					foundLeft = true;
				}
			}
		}
		
		return left;
	}
	
	private static int getRight(BufferedImage img){
		int right = img.getWidth();
		boolean foundRight = false;
		
		for (int x = img.getWidth() - 1; x >= 0 && !foundRight; x--){
			for (int y = 0; y < img.getHeight() && !foundRight; y++){
				if (img.getRGB(x, y) < NetworkIOTranslator.BLACK_RGB){
					right = x;
					foundRight = true;
				}
			}
		}
		
		return right;
	}
	
	private static int getBottom(BufferedImage img){
		int bottom = img.getWidth();
		boolean foundBottom = false;
		
		for (int y = img.getHeight() - 1; y >= 0 && !foundBottom; y--){
			for (int x = 0; x < img.getWidth() && !foundBottom; x++){
				if (img.getRGB(x, y) < NetworkIOTranslator.BLACK_RGB){
					bottom = y;
					foundBottom = true;
				}
			}
		}
		
		return bottom;
	}
	
	private static int getTop(BufferedImage img){
		int top = 0;
		boolean foundTop = false;
		
		for (int y = 0; y < img.getHeight() && !foundTop; y++){
			for (int x = 0; x < img.getWidth() && !foundTop; x++){
				if (img.getRGB(x, y) < NetworkIOTranslator.BLACK_RGB){
					top = y;
					foundTop = true;
				}
			}
		}
		
		return top;
	}
	
	private static boolean isValidPoint(int x, int y, BufferedImage img){
		return (x < img.getWidth() && y < img.getHeight());
	}
	
	private static BufferedImage scaleToProperHeight(BufferedImage img, float scaleValue, int dimension,
			int startingX, int startingY) {
		BufferedImage scaledImg = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_RGB);
//		int currentScaledImgX = 0;
//		int currentScaledImgY = 0;
		
		for (int x = startingX; x < scaledImg.getWidth(); x++){
			for (int y = startingY; y < scaledImg.getHeight(); y++){
				float xRatio = ((float)x / (scaledImg.getWidth() - startingX));
				float yRatio = ((float)y / (scaledImg.getHeight() - startingY));
				int imageX = (int)(xRatio * img.getWidth());
				int imageY = (int)(yRatio * img.getHeight());
				
				if (isValidPoint(imageX, imageY, img)){
					scaledImg.setRGB(x, y, img.getRGB(imageX, imageY));
				}
			}
		}
		
//		for (float x = 0; x < img.getWidth(); x += scaleValue){
//			currentScaledImgY = 0;
//			
//			for (float y = 0; y < img.getHeight(); y += scaleValue){
//				int xPos = (int) x;
//				int yPos = (int) y;
//				
//				if (isValidPoint(currentScaledImgX, currentScaledImgY, scaledImg)){
//					scaledImg.setRGB(currentScaledImgX, currentScaledImgY, img.getRGB(xPos, yPos));
//				}
//				
//				currentScaledImgY++;
//			}
//			currentScaledImgX++;
//		}
		return scaledImg;
	}
}