package appTest;

import static org.junit.Assert.fail;

import org.junit.Test;

import app.AlphaNumericCharacterConverter;

public class TestAlphaNumCharConverter {

	@Test
	public void testClassToCharConvert() throws Exception {
		int capAclass = 0;
		int capSclass = 18;
		int capZclass = 25;
		int loweraclass = 26;
		int lowerzclass = 51;
		int class0 = 52;
		int class4 = 56;
		int class9 = 61;
		
		AlphaNumericCharacterConverter converter = new AlphaNumericCharacterConverter();
		
		char capA = converter.convertClassNumberToCharacter(capAclass);
		char capS = converter.convertClassNumberToCharacter(capSclass);
		char capZ = converter.convertClassNumberToCharacter(capZclass);
		char lowerA = converter.convertClassNumberToCharacter(loweraclass);
		char lowerZ = converter.convertClassNumberToCharacter(lowerzclass);
		char char0 = converter.convertClassNumberToCharacter(class0);
		char char4 = converter.convertClassNumberToCharacter(class4);
		char char9 = converter.convertClassNumberToCharacter(class9);
		
		assert(capA == 'A');
		assert(capS == 'S');
		assert(capZ == 'Z');
		assert(lowerA == 'a');
		assert(lowerZ == 'z');
		assert(char0 == '0');
		assert(char4 == '4');
		assert(char9 == '9');
	}
	
	@Test
	public void testCharToClassConvert() throws Exception {
		char capA = 'A';
		char capS = 'S';
		char capZ = 'Z';
		char lowerA = 'a';
		char lowerZ = 'z';
		char char0 = '0';
		char char4 = '4';
		char char9 = '9';
		
		AlphaNumericCharacterConverter converter = new AlphaNumericCharacterConverter();
		
		int capaclass = converter.convertCharacterToClassNumber(capA);
		int capsclass = converter.convertCharacterToClassNumber(capA);
		int capzclass = converter.convertCharacterToClassNumber(capA);
		int loweraclass = converter.convertCharacterToClassNumber(capA);
		int lowerzclass = converter.convertCharacterToClassNumber(capA);
		int class0 = converter.convertCharacterToClassNumber(capA);
		int class4 = converter.convertCharacterToClassNumber(capA);
		int class9 = converter.convertCharacterToClassNumber(capA);
		
		assert(capaclass == 0);
		assert(capsclass == 18);
		assert(capzclass == 25);
		assert(loweraclass == 26);
		assert(lowerzclass == 51);
		assert(class0 == 52);
		assert(class4 == 56);
		assert(class9 == 61);
	}

	@Test(expected=Exception.class)
	public void testCharToClassException() throws Exception{
		AlphaNumericCharacterConverter converter = new AlphaNumericCharacterConverter();
		converter.convertCharacterToClassNumber('!');
	}
	
	@Test(expected=Exception.class)
	public void testClassToCharException() throws Exception{
		AlphaNumericCharacterConverter converter = new AlphaNumericCharacterConverter();
		converter.convertClassNumberToCharacter(63);
	}
}
