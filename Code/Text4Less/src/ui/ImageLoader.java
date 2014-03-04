package ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageLoader {
	private static final String[] ACCEPTED_IMAGE_TYPES = {"jpg", "bmp", "tiff", "png"};
	
	public static void loadImage(ImageTranslationTab window){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Image Files", ACCEPTED_IMAGE_TYPES));
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
	
	public static boolean isImage(String filename){
		boolean isImage = false;
		
		String[] nameParts = filename.split("[.]");
		
		if (nameParts.length >= 2){
			String fileType = nameParts[1];
			
			for(int i = 0; i < ACCEPTED_IMAGE_TYPES.length && !isImage; i++){
				isImage = (fileType.equals(ACCEPTED_IMAGE_TYPES[i]));
			}
		}
		
		return isImage;
	}
}
