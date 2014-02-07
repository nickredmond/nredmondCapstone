package ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import app.ImageReadMethod;
import app.InputReader;
import app.ReadResult;

public class MainWindow extends JFrame{
	private ImageLoaderPanel loaderPanel;
	private ResultPanel resultPanel;
	private List<ImageReadMethod> selectedReadMethods;
	private final ImageReadMethod DEFAULT_READ_METHOD = ImageReadMethod.NEURAL_NETWORK;
	private File imageFile;
	
	public MainWindow(){
		selectedReadMethods = new ArrayList<ImageReadMethod>();
		selectedReadMethods.add(DEFAULT_READ_METHOD);
		
		loaderPanel = new ImageLoaderPanel(this);
		resultPanel = new ResultPanel(this);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.getContentPane().add(loaderPanel);
		this.getContentPane().add(resultPanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	public void advancedOptionsSaveChangesClicked(List<ImageReadMethod> readMethods){
		selectedReadMethods = readMethods;
	}
	
	public void imageLoaded(File selectedImage){
		imageFile = selectedImage;
		loaderPanel.setPreviewImage(selectedImage);
		resultPanel.reset();
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
		if (imageFile == null){
			JOptionPane.showMessageDialog(this, "Please upload an image before translation.", "No Image", JOptionPane.ERROR_MESSAGE);
		}
		else{
			BufferedImage image;
			try {
				image = ImageIO.read(imageFile);
				ReadResult result = InputReader.readImageInput(image, selectedReadMethods);
				String resultText = result.getTranslationString();
				resultPanel.setResultText(resultText);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
