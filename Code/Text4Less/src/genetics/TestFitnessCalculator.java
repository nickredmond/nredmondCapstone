package genetics;

public class TestFitnessCalculator implements IFitnessCalculator {
	private int[] desiredOutputValue;
	
	public TestFitnessCalculator(int[] desired){
		desiredOutputValue = desired;
	}
	
	@Override
	public float getFitness(int[] chromosome) {		
//		if (chromosome.length % 4 != 0){
//			throw new IllegalArgumentException("Chromosome length must be divisible by 4");
//		}
		
	//	int value = getValueFor(chromosome);
		
		float fitness = 1.0f;
	//	int difference = Math.abs(desiredOutputValue - value); 
		
		int difference = 0;
		
		for (int i = 0; i < chromosome.length; i++){
			difference += Math.abs(chromosome[i] - desiredOutputValue[i]);
		}
		
		if (difference > 0){
			fitness = 1.0f - ((float)difference / chromosome.length);
		}
		
		return fitness * chromosome.length;
	}
	
	public int getValueFor(int[] chromosome){
		int numberValues = chromosome.length / 4;
		
		boolean isValue = true;
		int finalValue = 0;;
		
		String currentOp = "add";
		
		for (int i = 0; i < numberValues; i++){
			int startingIndex = i * 4;
			int geneValue = convertFromBinary(chromosome, startingIndex, 4);
			
			if (geneValue <= 13){
				if (isValue && geneValue < 10){
					if (i > 0){
					//	finalValue = finalValue + (isAdding ? geneValue : (-1 * geneValue));
						switch (currentOp)
						{
							case "add":
								finalValue += geneValue;
								isValue = !isValue;
								break;
							case "subtract":
								finalValue -= geneValue;
								isValue = !isValue;
								break;
							case "multiply":
								finalValue *= geneValue;
								isValue = !isValue;
								break;
							case "divide":
								finalValue /= (geneValue > 0) ? geneValue : 1;
								isValue = !isValue;
								break;
						}
					}
					else{ 
						finalValue += geneValue;
						isValue = !isValue;
					}
				}
				else if (!isValue){
					switch (geneValue)
					{
						case 10:
							currentOp = "add";
							isValue = !isValue;
							break;
						case 11:
							currentOp = "subtract";
							isValue = !isValue;
							break;
						case 12:
							currentOp = "multiply";
							isValue = !isValue;
							break;
						case 13:
							currentOp = "divide";
							isValue = !isValue;
							break;
					}
				}
			}
		}
		
		return finalValue;
	}
	
	private int convertFromBinary(int[] chromosome, int startingIndex, int length){	
		int value = 0;
		
		for (int i = startingIndex; i < (startingIndex + length); i++){
			int currentBitValue = (int) Math.pow(2, length - (i - startingIndex) - 1);
			if (chromosome[i] == 1){
				value += currentBitValue;
			}
		}
		
		return value;
	}
}