package threading;

import imageHandling.LeastDistanceHandler;

import java.awt.image.BufferedImage;

public class CorrelationThread implements Runnable{
	private LeastDistanceHandler handler;
	private BufferedImage img;
	private int index;
	
	public CorrelationThread(LeastDistanceHandler handler, BufferedImage img, int index){
		this.handler = handler;
		this.img = img;
		this.index = index;
	}

	@Override
	public void run() {
		handler.handleImage(img, index);
	}

}
