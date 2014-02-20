package decisionTrees;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;

import networkIOtranslation.FeatureExtractionIOTranslator;
import neuralNetwork.INeuralNetwork;

public class MetaclassTree {
	private MetaclassNode rootNode;
	private FeatureExtractionIOTranslator translator;
	
	public MetaclassTree(MetaclassNode rootNode, FeatureExtractionIOTranslator translator){
		this.rootNode = rootNode;
		this.translator = translator;
	}
	
	public TranslationResult readCharacter(BufferedImage image){
		MetaclassNode translationNode = getTranslationNode(image);
		INeuralNetwork network = translationNode.getNetwork();
		float[] input = translator.translateImageToNetworkInput(image);
		float[] output = network.forwardPropagate(input);
		
		char result = MetaclassConverter.translateOutputToCharacter(output, translationNode.getClasses());
		
		float confidence = 0.0f;
		
		for (int i = 0; i < output.length; i++){
			if (output[i] > confidence){
				confidence = output[i];
			}
		}
		
		return new TranslationResult(result, confidence);
	}
	
	private MetaclassNode getTranslationNode(BufferedImage image){
		MetaclassNode currentNode = rootNode;
		
		while(!currentNode.isLeafNode()){
			INeuralNetwork leftNetwork = currentNode.getLeftChild().getNetwork();
			INeuralNetwork rightNetwork = currentNode.getRightChild().getNetwork();
			
			float[] input = translator.translateImageToNetworkInput(image);
			
			float[] leftResult = leftNetwork.forwardPropagate(input);
			float[] rightResult = rightNetwork.forwardPropagate(input);
			
			float leftConfidence = getTotalConfidence(leftResult);
			float rightConfidence = getTotalConfidence(rightResult);
			
			currentNode = (leftConfidence > rightConfidence) ? currentNode.getLeftChild() : currentNode.getRightChild();
		}
		
		return currentNode;
	}
	
	private float getTotalConfidence(float[] output){
		float confidence = 0.0f;
		
		for (int i = 0; i < output.length; i++){
			confidence += output[i];
		}
		
		return confidence;
	}
}
