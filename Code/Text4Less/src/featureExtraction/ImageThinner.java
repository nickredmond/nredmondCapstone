package featureExtraction;

public class ImageThinner {
	private final int NUM_SURROUNDING_PIXELS = 8;
	
	public final int[][] TEMPLATE_1 = {{-1, 0, -1},{-1, 1, -1},{-1, 1, -1}};
	public final int[][] TEMPLATE_2 = {{-1, -1, -1},{0, 1, 1},{-1, -1, -1}};
	public final int[][] TEMPLATE_3 = {{-1, 1, -1},{-1, 1, -1},{-1, 0, -1}};
	public final int[][] TEMPLATE_4 = {{-1, -1, -1},{1, 1, 0},{-1, -1, -1}};
	
	private void printImg(int[][] lightValues){
		for (int row = 0; row < lightValues.length; row++){
			for (int col = 0; col < lightValues[row].length; col++){
				System.out.print(lightValues[row][col] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void thinImage(int[][] imageValues){
		boolean[][] deletionMarkers = new boolean[imageValues.length][imageValues[0].length];
		
		boolean madeDeletions = false;
		
		do{
			markForDeletion(imageValues, TEMPLATE_1, deletionMarkers);
			markForDeletion(imageValues, TEMPLATE_2, deletionMarkers);
			markForDeletion(imageValues, TEMPLATE_3, deletionMarkers);
			markForDeletion(imageValues, TEMPLATE_4, deletionMarkers);
			
			madeDeletions = deletePixels(imageValues, deletionMarkers);
			deletionMarkers = new boolean[imageValues.length][imageValues[0].length];
		}while (madeDeletions);
	}
	
	private boolean deletePixels(int[][] imageValues, boolean[][] deletionMarkers){
		boolean madeDeletions = false;
		
		for (int row = 0; row < imageValues.length; row++){
			for (int col = 0; col < imageValues[0].length; col++){
				if (deletionMarkers[row][col] && getConnectivityNumber(col, row, imageValues) == 1){
					imageValues[row][col] = 0;
					madeDeletions = true;
				}
			}
		}
		
		return madeDeletions;
	}
	
	private void markForDeletion(int[][] imageValues, int[][] template, boolean[][] deletionMarkers){
		for (int row = 0; row < imageValues.length; row++){
			for (int col = 0; col < imageValues[0].length; col++){
				if (imageValues[row][col] == 1 && matchesTemplate(imageValues, template, row, col, deletionMarkers)){
					if (!isEndpoint(col, row, imageValues) && getConnectivityNumber(col, row, imageValues) == 1){
						deletionMarkers[row][col] = true;
					}
				}
			}
		}
	}
	
	public boolean matchesTemplate(int[][] imageValues, int[][] template, int row, int col, boolean[][] deletionMarkers){
		boolean isMatch = true;
		
		for (int y = 0; y < template.length && isMatch; y++){
			for (int x = 0; x < template.length && isMatch; x++){
				int imageRow = row - 1 + y;
				int imageCol = col - 1 + x;
				
				int imageValue = (imageRow >= 0 && imageRow < imageValues.length && 
						imageCol >= 0 && imageCol < imageValues[0].length) ? imageValues[imageRow][imageCol] : 0;
				boolean deletionValue = (imageRow >= 0 && imageRow < imageValues.length && 
						imageCol >= 0 && imageCol < imageValues[0].length) ? deletionMarkers[imageRow][imageCol] : false;
				int templateValue = template[y][x];
				
				isMatch = ((imageValue == templateValue && !deletionValue) || templateValue == -1);
			}
		}
		
		return isMatch;
	}
	
	public int getConnectivityNumber(int x, int y, int[][] imageValues){
		int[] surroundingPixels = getSurroundingPixels(x, y, imageValues);
		int connectivityNumber = 0;
		
		for (int i = 0; i < NUM_SURROUNDING_PIXELS; i++){
			int currentPixelValue = surroundingPixels[i];
			int nextPixelValue = surroundingPixels[(i+1) % NUM_SURROUNDING_PIXELS];
			int afterPixelValue = surroundingPixels[(i+2) % NUM_SURROUNDING_PIXELS];
			
			connectivityNumber +=  currentPixelValue - (currentPixelValue * nextPixelValue);
		}
		
		return connectivityNumber;
	}
	
	private int[] getSurroundingPixels(int x, int y, int[][] imageValues){
		int[] surroundingPixels = new int[NUM_SURROUNDING_PIXELS];
		surroundingPixels[0] = (x + 1 < imageValues[0].length) ? imageValues[y][x+1] : 0;
		surroundingPixels[7] = (x + 1 < imageValues[0].length && y + 1 < imageValues.length) ? imageValues[y+1][x+1] : 0;
		surroundingPixels[6] = (y + 1 < imageValues.length) ? imageValues[y+1][x] : 0;
		surroundingPixels[5] = (x - 1 >= 0 && y + 1 < imageValues.length) ? imageValues[y+1][x-1] : 0;
		surroundingPixels[4] = (x - 1 >= 0) ? imageValues[y][x-1] : 0;
		surroundingPixels[3] = (x - 1 >= 0 && y - 1 >= 0) ? imageValues[y-1][x-1] : 0;
		surroundingPixels[2] = (y - 1 >= 0) ? imageValues[y-1][x] : 0;
		surroundingPixels[1] = (x + 1 < imageValues[0].length && y - 1 >= 0) ? imageValues[y-1][x+1] : 0;
		
		return surroundingPixels;
	}
	
	private boolean isEndpoint(int x, int y, int[][] imageValues){		
		int[] surroundingPixels = getSurroundingPixels(x, y, imageValues);
		int numBlackNeighbors = 0;
		
		for (int i = 0; i < surroundingPixels.length; i++){
			numBlackNeighbors += surroundingPixels[i];
		}
		
		return numBlackNeighbors == 1;
	}
}