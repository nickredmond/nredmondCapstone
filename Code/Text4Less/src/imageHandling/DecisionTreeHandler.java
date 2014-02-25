package imageHandling;

import imageProcessing.TranslationResult;
import io.MetaclassTreeIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import app.CharacterResult;
import app.Logger;
import decisionTrees.IMetaclassTree;

public class DecisionTreeHandler implements ICharacterImageHandler{
	private final String DECISION_TREE_NAME = "treeTest";
	private IMetaclassTree decisionTree;
	private List<CharacterResult> results;
	
	public DecisionTreeHandler(List<CharacterResult> results){
		try {
			decisionTree = MetaclassTreeIO.readTree(DECISION_TREE_NAME);
		} catch (IOException e) {
			Logger.logMessage("Error reading decision tree (Source: DecisionTreeHandler)");
		}
		this.results = results;
	}
	
	@Override
	public void handleImage(BufferedImage img) {
		TranslationResult translation = decisionTree.readCharacter(img);
		results.add(new CharacterResult(img, translation));
	}
	
	
}
