package debug;

import app.ICharacterConverter;

public class ConfusionMatrixDebug {
	public static void printConfusionMatrix(int[][] matrix, ICharacterConverter converter) throws Exception{
		if (matrix[0].length != converter.getNumberClasses() || matrix.length != converter.getNumberClasses()){
			throw new IllegalArgumentException("Matrix dimensions are incorrect");
		}
		
		System.out.print("  ");
		for (int i = 0; i < converter.getNumberClasses(); i++){
			char nextChar = converter.convertClassNumberToCharacter(i);
			System.out.print(nextChar + " ");
		}
		System.out.println();
		for (int i = 0; i < converter.getNumberClasses(); i++){
			System.out.print("__");
		}
		System.out.println();
		
		for (int y = 0; y < converter.getNumberClasses(); y++){
			System.out.print(converter.convertClassNumberToCharacter(y) + "|");
			for (int x = 0; x < converter.getNumberClasses(); x++){
				System.out.print(matrix[y][x] + " ");
			}
			System.out.println();
		}
	}
}