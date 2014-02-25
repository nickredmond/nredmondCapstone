package app;

public interface ICharacterConverter {
	public int convertCharacterToClassNumber(char character) throws Exception;
	public char convertClassNumberToCharacter(int classNumber) throws Exception;
	public int getNumberClasses();
}
