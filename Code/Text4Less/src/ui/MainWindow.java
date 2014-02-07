package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import app.ImageReadMethod;

public class MainWindow extends JFrame{
	private ImageLoaderPanel loaderPanel;
	private List<ImageReadMethod> selectedReadMethods;
	private final ImageReadMethod DEFAULT_READ_METHOD = ImageReadMethod.NEURAL_NETWORK;
	
	public MainWindow(){
		selectedReadMethods = new ArrayList<ImageReadMethod>();
		selectedReadMethods.add(DEFAULT_READ_METHOD);
		
		loaderPanel = new ImageLoaderPanel(this);
		this.getContentPane().add(loaderPanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	public void advancedOptionsSaveChangesClicked(List<ImageReadMethod> readMethods){
		selectedReadMethods = readMethods;
	}
	
	public void imageLoaded(File selectedImage){
		loaderPanel.setPreviewImage(selectedImage);
	}
	
	public void imageLoadFailed(String message){
		JOptionPane.showMessageDialog(this, message, "Image Load Failed", JOptionPane.ERROR_MESSAGE);
	}
	
	public void advancedOptionsClicked(){
		new AdvancedOptionsWindow(this, selectedReadMethods);
	}
	
	public void loadImageClicked(){
		ImageLoader.loadImage(this);
	}
	
	public void translateClicked(){
		
	}
}
