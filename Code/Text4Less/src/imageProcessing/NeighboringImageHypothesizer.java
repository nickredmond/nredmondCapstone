package imageProcessing;

import io.CharacterType;
import io.NeuralNetworkIO;
import io.TrainingDataReader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.FeatureExtractionIOTranslator;
import neuralNetwork.CharacterReader;
import neuralNetwork.CharacterTrainingExample;
import neuralNetwork.INeuralNetwork;
import app.CharacterResult;
import debug.FeatureExtractionDebug;

public class NeighboringImageHypothesizer {
	private static final float MIN_CONFIDENCE_REQUIRED = 0.8f;
	private static final CharacterType TRAINING_TYPE = CharacterType.ASCII;
	
	public static List<BufferedImage> combineImagesThatFormCharacters(List<BufferedImage> line) throws IOException{
		List<BufferedImage> combinedImages = new ArrayList<BufferedImage>();
		
		for (int i = 0; i < line.size() - 2; i++){
			BufferedImage left = line.get(i);
			BufferedImage center = line.get(i+1);
			BufferedImage right = line.get(i+2);
			
			if (i == 15){
				int x = 6;
			}
			
			if (isSpaceImage(left) || isSpaceImage(center) || isSpaceImage(right)){
				combinedImages.add(left);
				
				if (i == line.size() - 3){
					combinedImages.add(center);
					combinedImages.add(right);
				}
			}
			else{
				BufferedImage combined = new BufferedImage(left.getWidth()+center.getWidth()+right.getWidth(), left.getHeight(), BufferedImage.TYPE_INT_RGB);
				
				for (int col = 0; col < combined.getWidth(); col++){
					for (int row = 0; row < left.getHeight(); row++){
						//int rgb = (col < left.getWidth()) ? left.getRGB(col, row) : right.getRGB(col - left.getWidth(), row);
						int rgb = 0;
						
						if (col < left.getWidth()){
							rgb = left.getRGB(col, row);
						}
						else if (col < left.getWidth() + center.getWidth()){
							rgb = center.getRGB(col - left.getWidth(), row);
						}
						else{
							rgb = right.getRGB(col - left.getWidth() - center.getWidth(), row);
						}
						
						combined.setRGB(col, row, rgb);
					}
				}
				
				INeuralNetwork trainedNetwork = NeuralNetworkIO.readNetwork("yoloSwaggins");
				CharacterReader reader = new CharacterReader(trainedNetwork, new AlphaNumericIOTranslator());
				
				CharacterResult result = reader.readCharacter(combined);
				float bestCorrelation = result.getResult().getConfidence();
				
			//	float bestCorrelation = getBestCorrelationFor(combined);
				
				if (i == 21){
					int x = 0;
				}
				
				if (bestCorrelation >= 0.95f){
					combinedImages.add(combined);
					System.out.println("result: " + result.getResult().getCharacter() + " @ " + result.getResult().getConfidence());
					i+=2;
					
					if (i+1 < line.size() && i+1 >= line.size() - 2){
						i++;
						while (i < line.size()){
							combinedImages.add(line.get(i));
							i++;
						}
					}
				}
				else {
					combinedImages.add(left);
					
					if (i == line.size() - 3){
						combinedImages.add(center);
						combinedImages.add(right);
					}
				}
				
				System.out.println(i + " " + line.size());
			}
		}
		
		return combinedImages;
	}
	
	private static float getBestCorrelationFor(BufferedImage combined) throws IOException {
		float highestCorrelation = -1.0f;
		boolean found = false;
		
		Set<CharacterTrainingExample> examples = TrainingDataReader.createTrainingSetFromFile(TRAINING_TYPE);
		Iterator<CharacterTrainingExample> iter = examples.iterator();
		
		while(iter.hasNext() && !found){
			CharacterTrainingExample nextExample = iter.next();
			
			BufferedImage nextTrainingImg = nextExample.getCharacterImage();
			float nextCorrelation = FeatureExtractionDebug.getCorrelation(combined, nextTrainingImg);
			
			if (nextCorrelation > highestCorrelation || highestCorrelation < 0){
				highestCorrelation = nextCorrelation;
			}
			
			found = (highestCorrelation >= 0.99f);
		}
		return highestCorrelation;
	}

	private static boolean isSpaceImage(BufferedImage image){
		float[] features = new FeatureExtractionIOTranslator().translateImageToNetworkInput(image);
		boolean isSpace = true;
		
		for (int i = 0; i < features.length && isSpace; i++){
			isSpace = (features[i] == 0);
		}
		
		return isSpace;
	}
}
