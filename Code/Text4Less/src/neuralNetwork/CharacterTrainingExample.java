package neuralNetwork;

import java.awt.image.BufferedImage;

public class CharacterTrainingExample {
	private BufferedImage characterImage;
	private char characterValue;
	
	public CharacterTrainingExample(BufferedImage img, char value){
		setCharacterImage(img);
		setCharacterValue(value);
	}

	public BufferedImage getCharacterImage() {
		return characterImage;
	}

	public void setCharacterImage(BufferedImage characterImage) {
		this.characterImage = characterImage;
	}

	public char getCharacterValue() {
		return characterValue;
	}

	public void setCharacterValue(char characterValue) {
		this.characterValue = characterValue;
	}
}
