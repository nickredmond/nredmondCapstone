package ui;

import imageHandling.ImageReadMethod;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import neuralNetwork.INeuralNetwork;
import spellCheck.SpellChecker;
import app.InputReader;
import app.ReadResult;

public class ImageTranslationTab extends JPanel{
	private ImageLoaderPanel loaderPanel;
	private ResultPanel resultPanel;
	private ImageReadMethod selectedReadMethod;
	private final ImageReadMethod DEFAULT_READ_METHOD = ImageReadMethod.NEURAL_NETWORK;
	private File imageFile;
	
	private INeuralNetwork chosenNetwork;
	private String networkName = "default";
	
	private boolean isSpellCheckEnabled = false;
	
	private final int DEFAULT_WIDTH = 850;
	private final int DEFAULT_HEIGHT = 550;
	
	public ImageTranslationTab(){
		selectedReadMethod = ImageReadMethod.NEURAL_NETWORK;
		
		loaderPanel = new ImageLoaderPanel(this);
		resultPanel = new ResultPanel(this);
		
		this.setLayout(new GridLayout(0,2));
		this.add(loaderPanel);
		this.add(resultPanel);
		
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setVisible(true);
	}
	
	public void advancedOptionsSaveChangesClicked(ImageReadMethod readMethod, INeuralNetwork chosenNetwork,
			String networkName, boolean useSpellCheck){
		selectedReadMethod = readMethod;
		this.chosenNetwork = chosenNetwork;
		this.networkName = networkName;
		isSpellCheckEnabled = useSpellCheck;
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
		new AdvancedOptionsWindow(this, selectedReadMethod, chosenNetwork, networkName, isSpellCheckEnabled);
	}
	
	public void loadImageClicked(){
		ImageLoader.loadImage(this);
	}
	
	public void translateClicked(){
		if (imageFile == null){
			JOptionPane.showMessageDialog(this, "Please upload an image before translation.", "No Image", JOptionPane.ERROR_MESSAGE);
		}
		else{
			Thread t = new Thread(){
				public void run(){
					try {
						LoadingScreen loading = new LoadingScreen("Reading image", 400, 250);
						
						BufferedImage image = ImageIO.read(imageFile);
						InputReader.setNetwork(chosenNetwork);
						ReadResult result = InputReader.readImageInput(image, selectedReadMethod);
						String resultText = result.getTranslationString();
						
						if (isSpellCheckEnabled){
							resultText = SpellChecker.spellCheckText(resultText);
						}
						
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
