package receptors;

import java.awt.Point;

public class Receptor {
	private FloatPoint startingPoint, endingPoint;
	
	public Receptor(FloatPoint startingPoint, FloatPoint endingPoint){
		this.startingPoint = startingPoint;
		this.endingPoint = endingPoint;
	}

	public FloatPoint getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(FloatPoint startingPoint) {
		this.startingPoint = startingPoint;
	}

	public FloatPoint getEndingPoint() {
		return endingPoint;
	}

	public void setEndingPoint(FloatPoint endingPoint) {
		this.endingPoint = endingPoint;
	}
}