package imageProcessing;

import java.util.LinkedList;
import java.util.List;

public class Receptor {
	private int x, y;
	private List<Integer> activationValues;
	
	public Receptor(int x, int y){
		this.setX(x);
		this.setY(y);
		activationValues = new LinkedList<Integer>();
	}
	
	public void addActivationValue(int value){
		activationValues.add(value);
	}
	
	public float calculateAverageActivation(){
		int activationSum = 0;
		
		for (Integer nextValue : activationValues){
			activationSum += nextValue;
		}
		
		return activationSum / activationValues.size();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
