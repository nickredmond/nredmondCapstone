package receptors;

import featureExtraction.ImageThinner;
import featureExtraction.ZernikeImageNormalizer;
import imageProcessing.ImageBinarizer;
import imageProcessing.ImageNormalizer;
import imageProcessing.ImageScaler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import debug.FeatureExtractionDebug;
import math.EntropyCalculator;
import math.StatisticalMath;
import neuralNetwork.CharacterTrainingExample;
import app.AlphaNumericCharacterConverter;

public class ReceptorFilter {
	private static final int NUMBER_CLASSES = AlphaNumericCharacterConverter.NUMBER_CLASSES;
	private static final char[] CLASSES = {'0', '1'};
	
	public static List<Receptor> filterReceptors(List<Receptor> receptors, Set<CharacterTrainingExample> trainingSet,
			int finalSize) throws Exception{
		String[][] receptorActivations = new String[NUMBER_CLASSES][receptors.size()];
		
		for (int y = 0; y < receptorActivations.length; y++){
			for (int x = 0; x < receptorActivations[0].length; x++){
				receptorActivations[y][x] = "";
			}
		}
		
		AlphaNumericCharacterConverter converter = new AlphaNumericCharacterConverter();
		
		for (CharacterTrainingExample nextExample : trainingSet){
			char nextChar = nextExample.getCharacterValue();
			int classNumber = converter.convertCharacterToClassNumber(nextChar);
			
			int receptorIndex = 0;
			
			for (Receptor nextReceptor : receptors){
				receptorActivations[classNumber][receptorIndex] += (crossesImage(nextReceptor, nextExample.getCharacterImage()) ?
						"1" : "0");
				receptorIndex++;
			}
		}
		
		List<FloatPoint> usabilities = new ArrayList<FloatPoint>();
		
		for (int i = 0; i < receptorActivations[0].length; i++){
			String outerActivationString = "";
			float[] innerEntropies = new float[receptorActivations.length];
			
			for (int classNumber = 0; classNumber < receptorActivations.length; classNumber++){
				outerActivationString += getActivationForCell(receptorActivations[classNumber][i]);
				innerEntropies[classNumber] = 
						EntropyCalculator.calculateShannonEntropy(receptorActivations[classNumber][i], CLASSES);
				
			}
			
			float outerEntropy = EntropyCalculator.calculateShannonEntropy(outerActivationString, CLASSES);
			float avgInnerEntropy = StatisticalMath.average(innerEntropies);
			
			float nextUsability =  (outerEntropy * (1.0f - avgInnerEntropy));
			usabilities.add(new FloatPoint(i, nextUsability));
		}
		
		Collections.sort(usabilities);
		
		List<Receptor> chosenReceptors = new ArrayList<Receptor>();
		
		int numReceptorsChosen = 0;
		
		for (int i = usabilities.size() - 1; i >= 0 && numReceptorsChosen < finalSize; i--){
			Receptor nextReceptor = receptors.get((int)usabilities.get(i).X());
			System.out.println((int)usabilities.get(i).X() + " " + usabilities.get(i).Y());
			chosenReceptors.add(nextReceptor);
			
			numReceptorsChosen++;
		}
		
		return chosenReceptors;
	}
	
	private static int getActivationForCell(String cellData){
		int numberZeros = 0;
		int numberOnes = 0;
		
		for (int i = 0; i < cellData.length(); i++){
			if (cellData.charAt(i) == '1'){
				numberOnes++;
			}
			else numberZeros++;
		}
		
		return (numberOnes >= numberZeros) ? 1 : 0;
	}
	
	public static boolean crossesImage(Receptor receptor, BufferedImage image){
		boolean crosses = false;
		
		float slopeY = receptor.getEndingPoint().Y() - receptor.getStartingPoint().Y();
		float slopeX = receptor.getEndingPoint().X() - receptor.getStartingPoint().X();
		
		float scaleFactor = (slopeX != 0) ? Math.abs(1.0f / slopeX) : Math.abs(1.0f / slopeY);
		
		float xTravel = (scaleFactor * slopeX);
		float yTravel = (scaleFactor * slopeY);
		
		int startingX = (int)(receptor.getStartingPoint().X() * image.getWidth());
		int startingY = (int)(receptor.getStartingPoint().Y() * image.getHeight());
		
		if (startingX == image.getWidth())
			startingX--;
		if (startingY == image.getHeight())
			startingY--;
		
		int endingX = (int)(receptor.getEndingPoint().X() * image.getWidth());
		int endingY = (int)(receptor.getEndingPoint().Y() * image.getHeight());
		
		if (endingX == image.getWidth())
			endingX--;
		if (endingY == image.getHeight())
			endingY--;
		
		int[][] preCroppedValues = ImageBinarizer.convertImageToBinaryValues(image);
		int[][] croppedValues = ImageNormalizer.cropImage(preCroppedValues);
		int[][] squareCroppedValues = ZernikeImageNormalizer.squareImage(croppedValues);
		int[][] imageValues = ImageScaler.scaleWithBilinearInterpolation(squareCroppedValues, 20, 20);
		
		float currentY = startingY;

		for (float currentX = startingX; isInBounds(imageValues, currentX, currentY, endingX, endingY, xTravel, yTravel) 
				&& !crosses; currentX += xTravel){			
			int col = (int)currentX;
			
			crosses = (imageValues[(int)currentY][col] == 1);
			
			float oldY = currentY;
			currentY += yTravel;
			
			boolean isInImage = true;
			
			while (Math.abs(currentY - oldY) > 1 && isInImage){
				oldY += (yTravel > 0) ? 1 : -1;
				isInImage = (oldY >= 0 && oldY < imageValues.length);
				
				if (isInImage){
					imageValues[(int)oldY][col] = 1;
				}
			}
		}
		
		return crosses;
	}
	
	private static boolean isInBounds(int[][] imageValues, float currentX, float currentY,
			int endingX, int endingY, float xTravel, float yTravel){
		int currentYint = (int)currentY;
		
		boolean isInImage = ((int)currentX < imageValues[0].length && (int)currentX >= 0 && currentYint < imageValues.length &&
				currentYint >= 0);
		boolean isInVectorX = ((xTravel > 0) ? (currentX <= endingX) : (currentX >= endingX)) && xTravel != 0;
		boolean isInVectorY = ((yTravel > 0) ? (currentY <= endingY) : (currentY >= endingY)) && yTravel != 0;
		
		return isInImage && (isInVectorX || isInVectorY);
	}
}
