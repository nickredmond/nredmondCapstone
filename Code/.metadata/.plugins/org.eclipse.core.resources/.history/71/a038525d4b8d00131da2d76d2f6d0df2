package threading;

import java.awt.image.BufferedImage;

import app.CorrelationHandler;

public class CorrelationThread implements Runnable{
	private CorrelationHandler handler;
	private BufferedImage img;
	private int index;
	
	public CorrelationThread(CorrelationHandler handler, BufferedImage img, int index){
		this.handler = handler;
		this.img = img;
		this.index = index;
	}

	@Override
	public void run() {
		handler.handleImage(img, index);
	}

}
