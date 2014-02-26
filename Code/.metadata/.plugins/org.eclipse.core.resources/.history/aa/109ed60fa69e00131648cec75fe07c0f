package decisionTrees;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import networkIOtranslation.FeatureExtractionIOTranslator;

public class BasicSplitMetaclassTree implements IMetaclassTree {
	private MetaclassNode rootNode;
	private FeatureExtractionIOTranslator translator;
	
	public BasicSplitMetaclassTree(MetaclassNode rootNode, FeatureExtractionIOTranslator translator){
		this.rootNode = rootNode;
		this.translator = translator;
	}
	
	@Override
	public TranslationResult readCharacter(BufferedImage image) {
		MetaclassNode leafNode = getLeafNode(image, rootNode);
		
		float[] input = translator.translateImageToNetworkInput(image);
		float[] output = leafNode.getNetwork().forwardPropagate(input);
		
		int maxIndex = 0;
		float maxOutput = -2.0f;
		
		for (int i = 0; i < output.length; i++){
			if (output[i] > maxOutput){
				maxIndex = i;
				maxOutput = output[i];
			}
		}
		
		char result = leafNode.getClasses()[maxIndex];
		
		return new TranslationResult(result, maxOutput);
	}
	
	public MetaclassNode getLeafNode(BufferedImage image, MetaclassNode currentNode){
		float[] input = translator.translateImageToNetworkInput(image);
		float[] output = currentNode.getNetwork().forwardPropagate(input);
		
		int classNr = (output[0] > output[1]) ? 0 : 1;
		
		MetaclassNode child = (classNr == 0) ? currentNode.getLeftChild() : currentNode.getRightChild();
		MetaclassNode leafNode = (child.isLeafNode() ? child : getLeafNode(image, child));
		
		return leafNode;
	}
}
