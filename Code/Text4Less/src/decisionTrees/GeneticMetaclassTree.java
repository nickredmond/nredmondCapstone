package decisionTrees;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import networkIOtranslation.FeatureExtractionIOTranslator;
import neuralNetwork.INeuralNetwork;

public class GeneticMetaclassTree implements IMetaclassTree {
	private MetaclassNode rootNode;
	private FeatureExtractionIOTranslator translator;
	
	public GeneticMetaclassTree(MetaclassNode rootNode, FeatureExtractionIOTranslator translator){
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
			INeuralNetwork network = currentNode.getNetwork();
//			INeuralNetwork rightNetwork = currentNode.getRightChild().getNetwork();
			
			float[] input = translator.translateImageToNetworkInput(image);
			
			float[] leftResult = network.forwardPropagate(input);
//			float[] rightResult = rightNetwork.forwardPropagate(input);
			
			float leftConfidence = leftResult[0]; // getTotalConfidence(leftResult);
			float rightConfidence = leftResult[1]; // getTotalConfidence(rightResult);
			
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
