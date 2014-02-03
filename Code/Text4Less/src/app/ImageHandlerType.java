package app;

import imageProcessing.TranslationResult;

import java.util.List;

import neuralNetwork.CharacterReader;

public enum ImageHandlerType {
	CORRELATION{
		@Override
		public ICharacterImageHandler getHandler(CharacterReader reader, List<TranslationResult> results) {
			return new CorrelationHandler(results);
		}
	},
	TRAINING_DATA_CREATION{
		@Override
		public ICharacterImageHandler getHandler(CharacterReader reader, List<TranslationResult> results) {
			return new TrainingDataCreationHandler();
		}
	},
	TRANSLATION{
		@Override
		public ICharacterImageHandler getHandler(CharacterReader reader, List<TranslationResult> results) {
			return new TranslationHandler(results, reader);
		}
	};
	
	public abstract ICharacterImageHandler getHandler(CharacterReader reader, List<TranslationResult> results);
}