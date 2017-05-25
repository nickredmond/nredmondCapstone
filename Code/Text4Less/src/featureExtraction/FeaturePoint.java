package featureExtraction;

public class FeaturePoint implements Comparable {
	private int x, y;
	private FeatureType type;
	
	public FeaturePoint(int x, int y, FeatureType type){
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	public FeatureType getType(){
		return type;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	@Override
	public int compareTo(Object otherPoint) {
		int value = 0;
		
		if (y < ((FeaturePoint)otherPoint).y()){
			value = -1;
		}
		else if (y == ((FeaturePoint)otherPoint).y() && x < ((FeaturePoint)otherPoint).x()){
			value = -1;
		}
		else if (y > ((FeaturePoint)otherPoint).y() || (y == ((FeaturePoint)otherPoint).y() && x > ((FeaturePoint)otherPoint).x())){
			value = 1;
		}
		
		return value;
	}
}
