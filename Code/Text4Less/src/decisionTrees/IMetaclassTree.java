package decisionTrees;

import imageProcessing.TranslationResult;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public interface IMetaclassTree extends Serializable {
	public TranslationResult readCharacter(BufferedImage image);
}
