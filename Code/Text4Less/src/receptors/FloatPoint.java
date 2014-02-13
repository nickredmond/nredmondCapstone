package receptors;

import java.io.Serializable;

public class FloatPoint implements Comparable<FloatPoint>, Serializable {
	private float x, y;
	
	public FloatPoint(float x, float y){
		this.x = x;
		this.y = y;
	}

	public float X() {
		return x;
	}
	
	public float Y(){
		return y;
	}

	@Override
	public int compareTo(FloatPoint otherPoint) {
		int result = 0;
		
		if (this.y > otherPoint.y){
			result = 1;
		}
		else if (this.y < otherPoint.y){
			result = -1;
		}
		
		return result;
	}
}
