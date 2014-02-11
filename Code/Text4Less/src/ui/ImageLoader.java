package ui;

import java.io.File;

import javax.swing.JFileChooser;

public class ImageLoader {
	private static final String[] ACCEPTED_IMAGE_TYPES = {"jpg", "bmp", "tiff", "png"};
	
	public static void loadImage(MainWindow window){
		JFileChooser chooser = new JFileChooser();
		int returnValue = chooser.showOpenDialog(window);
		
		if (returnValue == JFileChooser.APPROVE_OPTION){
			File selectedFile = chooser.getSelectedFile();
			String filename = selectedFile.getName();
			
			if (isImage(filename)){
				window.imageLoaded(selectedFile);
			}
			else window.imageLoadFailed("The selected file is not a supported image type.");
		}
	}
	
	private static boolean isImage(String filename){
		String[] nameParts = filename.split("[.]");
		String fileType = nameParts[1];
		boolean isImage = false;
		
		for(int i = 0; i < ACCEPTED_IMAGE_TYPES.length && !isImage; i++){
			isImage = (fileType.equals(ACCEPTED_IMAGE_TYPES[i]));
		}
		
		return isImage;
	}
}