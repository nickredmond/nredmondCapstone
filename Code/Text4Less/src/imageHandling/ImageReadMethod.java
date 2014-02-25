package imageHandling;

import imageProcessing.TranslationResult;

import java.util.List;

import app.CharacterResult;
import neuralNetwork.CharacterReader;

public enum ImageReadMethod {
	LEAST_DISTANCE{
		@Override
		public ICharacterImageHandler getHandler(CharacterReader reader, List<CharacterResult> results) {
			return new LeastDistanceHandler(results);
		}
	},
	TRAINING_DATA_CREATION{
		@Override
		public ICharacterImageHandler getHandler(CharacterReader reader, List<CharacterResult> results) {
			return new TrainingDataCreationHandler();
		}
	},
	NEURAL_NETWORK{
		@Override
		public ICharacterImageHandler getHandler(CharacterReader reader, List<CharacterResult> results) {
			return new NeuralNetworkHandler(results, reader);
		}
	},
	DECISION_TREE{
		@Override
		public ICharacterImageHandler getHandler(CharacterReader reader, List<CharacterResult> results) {
			return new DecisionTreeHandler(results);
		}
	};
	
	public abstract ICharacterImageHandler getHandler(CharacterReader reader, List<CharacterResult> results);
}
