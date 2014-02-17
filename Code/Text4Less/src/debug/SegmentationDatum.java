package debug;

import java.awt.image.BufferedImage;

public class SegmentationDatum {
	private BufferedImage image;
	private float stdDev, numStdDevs;
	
	public SegmentationDatum(BufferedImage image, float stDev, float numStdDevs){
		this.image = image;
		this.stdDev = stDev;
		this.numStdDevs = numStdDevs;
	}
	
	public float getStdDev() {
		return stdDev;
	}
	public void setStdDev(float stdDev) {
		this.stdDev = stdDev;
	}
	public float getNumStdDevs() {
		return numStdDevs;
	}
	public void setNumStdDevs(float numStdDevs) {
		this.numStdDevs = numStdDevs;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
