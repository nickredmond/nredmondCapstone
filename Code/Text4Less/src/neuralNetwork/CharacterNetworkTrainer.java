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
		Set<TrainingExample> trainingSet = setupNetworkTrainingExamples(trainingExamples);
		trainer.trainWithTrainingSet(network, trainingSet, new HashSet<TrainingExample>());
	}

	private Set<TrainingExample> setupNetworkTrainingExamples(List<CharacterTrainingExample> characterExamples) {
		Set<TrainingExample> examples = new HashSet<TrainingExample>();
		
		for (CharacterTrainingExample nextExample : characterExamples){
			BufferedImage nextImage = nextExample.getCharacterImage();
			char nextCharacter = nextExample.getCharacterValue();
			float[] nextInput = translator.translateImageToNetworkInput(nextImage);
			int[] nextOutput = translator.translateCharacterToNetworkOutput(nextCharacter);
			
			examples.add(new TrainingExample(nextInput, nextOutput));
		}
		
		return examples;
	}
}
