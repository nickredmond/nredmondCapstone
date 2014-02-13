package debug;

import app.AlphaNumericCharacterConverter;

public class EntropyDebug {
	public static void printDataMatrix(String[][] dataMatrix){
		try{
			int greatestLength = 0;
			AlphaNumericCharacterConverter converter = new AlphaNumericCharacterConverter();
			
			for (int y = 0; y < dataMatrix.length; y++){
				
				for (int x = 0; x < dataMatrix[0].length; x++){
				if (dataMatrix[y][x].length() > greatestLength){
						greatestLength = dataMatrix[y][x].length();
					}	
				}
			}
			
			for (int y = 0; y < dataMatrix.length; y++){
				char nextClass = converter.convertClassNumberToCharacter(y);
				System.out.print(nextClass + " | ");
				for (int x = 0; x < dataMatrix[0].length; x++){
					int numExtraSpaces = greatestLength - dataMatrix[y][x].length();
					System.out.print(dataMatrix[y][x]);
					for (int i = 0; i <= numExtraSpaces; i++){
						System.out.print(" ");
					}
				}
				System.out.println();
			}
			System.out.println();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
