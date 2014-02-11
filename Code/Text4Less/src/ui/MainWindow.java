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

import neuralNetwork.INeuralNetwork;
import app.ImageReadMethod;
import app.InputReader;
import app.ReadResult;

public class MainWindow extends JFrame{
	private ImageLoaderPanel loaderPanel;
	private ResultPanel resultPanel;
	private List<ImageReadMethod> selectedReadMethods;
	private final ImageReadMethod DEFAULT_READ_METHOD = ImageReadMethod.NEURAL_NETWORK;
	private File imageFile;
	
	private INeuralNetwork chosenNetwork;
	private String networkName = null;
	
	public MainWindow(){
		selectedReadMethods = new ArrayList<ImageReadMethod>();
		selectedReadMethods.add(DEFAULT_READ_METHOD);
		
		loaderPanel = new ImageLoaderPanel(this);
		resultPanel = new ResultPanel(this);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.getContentPane().add(loaderPanel);
		this.getContentPane().add(resultPanel);
		
		this.pack();
		this.setVisible(true);
	}
	
	public void advancedOptionsSaveChangesClicked(List<ImageReadMethod> readMethods, INeuralNetwork chosenNetwork,
			String networkName){
		selectedReadMethods = readMethods;
		this.chosenNetwork = chosenNetwork;
		this.networkName = networkName;
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
		new AdvancedOptionsWindow(this, selectedReadMethods, chosenNetwork, networkName);
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
				Thread t = new Thread(){
				public void run(){
				try {
				LoadingScreen loading = new LoadingScreen("Reading image", 400, 250);
				
				System.out.println("yes");
				
				BufferedImage image = ImageIO.read(imageFile);
				InputReader.setNetwork(chosenNetwork);
				ReadResult result = InputReader.readImageInput(image, selectedReadMethods);
				String resultText = result.getTranslationString();
				resultPanel.setResultText(resultText, result.getRejections());
				
				loading.closeScreen();
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
				};
				
				t.start();
		}
	}
}