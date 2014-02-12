package receptors;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReceptorGenerator {	
	public static List<Receptor> generateRandomReceptors(int amountToGenerate){
		Random rand = new Random();
		List<Receptor> receptors = new ArrayList<Receptor>();
		
		for (int i = 0; i < amountToGenerate; i++){
			float startingX = rand.nextFloat();
			float startingY = rand.nextFloat();
			float endingX = rand.nextFloat();
			float endingY = rand.nextFloat();
			
			FloatPoint startingPoint = new FloatPoint(startingX, startingY);
			FloatPoint endingPoint = new FloatPoint(endingX, endingY);
			
			Receptor nextReceptor = new Receptor(startingPoint, endingPoint);
			receptors.add(nextReceptor);
		}
		
		return receptors;
	}
}
