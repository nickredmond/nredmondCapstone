package app;

import io.FileOperations;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TrainingDataCreationHandler implements ICharacterImageHandler {
	
	@Override
	public void handleImage(BufferedImage img){
	  try {		  
	      ImageIO.write(img, "jpg", new File("trainingImages/unformatted/" + System.nanoTime() + ".jpg"));
	  } catch (IOException e) {
	          e.printStackTrace();
	  }
	}
}
