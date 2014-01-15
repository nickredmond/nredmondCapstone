package neuralNetwork;

import imageProcessing.INetworkIOTranslator;
import imageProcessing.NetworkIOTranslator;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CharacterNetworkTrainer {
	private List<CharacterTrainingExample> trainingExamples;
	INetworkIOTranslator translator;
	
	public CharacterNetworkTrainer(INetworkIOTranslator translator){
		trainingExamples = new LinkedList<CharacterTrainingExample>();
		this.translator = translator;
	}
	
	public void addTrainingExample(CharacterTrainingExample example){
		trainingExamples.add(example);
	}
	
	public void trainNeuralNetwork(NeuralNetwork network, INetworkTrainer trainer){
		Set<TrainingExample> trainingSet = setupNetworkTrainingExamples();
		trainer.trainWithTrainingSet(network, trainingSet);
	}

	private Set<TrainingExample> setupNetworkTrainingExamples() {
		Set<TrainingExample> examples = new HashSet<TrainingExample>();
		
		for (CharacterTrainingExample nextExample : trainingExamples){
			BufferedImage nextImage = nextExample.getCharacterImage();
			char nextCharacter = nextExample.getCharacterValue();
			if (nextCharacter == 'L'){
				int x = 0;
				int avg = x + 3;
			}
			float[] nextInput = translator.translateImageToNetworkInput(nextImage);
			int[] nextOutput = translator.translateCharacterToNetworkOutput(nextCharacter);
			
			examples.add(new TrainingExample(nextInput, nextOutput));
		}
		
		return examples;
	}
}
