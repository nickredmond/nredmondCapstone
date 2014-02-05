package neuralNetwork;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import networkIOtranslation.INetworkIOTranslator;
import debug.CharacterViewDebug;

public class CharacterNetworkTrainer {
	private List<CharacterTrainingExample> trainingExamples, testExamples;
	INetworkIOTranslator translator;
	
	public CharacterNetworkTrainer(INetworkIOTranslator translator){
		trainingExamples = new LinkedList<CharacterTrainingExample>();
		testExamples = new LinkedList<CharacterTrainingExample>();
		
		this.translator = translator;
	}
	
	public void addTrainingExample(CharacterTrainingExample example){
		trainingExamples.add(example);
	}
	
	public void addTestExample(CharacterTrainingExample example){
		testExamples.add(example);
	}
	
	public void trainNeuralNetwork(INeuralNetwork network, INetworkTrainer trainer){
		Set<TrainingExample> trainingSet = setupNetworkTrainingExamples(trainingExamples);
		Set<TrainingExample> testSet = setupNetworkTrainingExamples(testExamples);
		
		trainer.trainWithTrainingSet(network, trainingSet, testSet);
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
