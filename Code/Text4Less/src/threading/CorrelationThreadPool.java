package threading;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import neuralNetwork.TrainingExample;
import app.LeastDistanceHandler;


public class CorrelationThreadPool {
	LeastDistanceHandler handler;
	Map<BufferedImage, Integer> mappedImages;
	
	public CorrelationThreadPool(LeastDistanceHandler handler){
		this.handler = handler;
		mappedImages = new HashMap<BufferedImage, Integer>();
	}
	
	public void addImage(BufferedImage image, int index){
		mappedImages.put(image, index);
	}
	
	public void execute(){
		ExecutorService service = Executors.newFixedThreadPool(8);
		
		for (BufferedImage nextImage : mappedImages.keySet()){
			Runnable nextCorrelation = new CorrelationThread(handler, nextImage, (int)mappedImages.get(nextImage));
			service.execute(nextCorrelation);
		}
		service.shutdown();
		
		while(!service.isTerminated()){}
	}
}
