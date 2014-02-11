package ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageLoaderPanel extends JPanel {
	private JPanel buttonPanel, previewPanel;
	private JButton loadImageButton, translateButton, advancedButton;
	private JLabel imagePreview;
	
	private MainWindow window;
	
	private final int MAX_IMAGE_WIDTH = 400;
	
	public ImageLoaderPanel(MainWindow window){
		this.window = window;
		
		setupButtonPanel();
		setupPreviewPanel();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(buttonPanel);
		this.add(previewPanel);
	}
	
	public void setPreviewImage(File imageFile){
	//	ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
		Image iconImg = null;
		try {
			iconImg = ImageIO.read(imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		float percentHeightToWidth = (float)iconImg.getHeight(null) / iconImg.getWidth(null);
		
		int newWidth = (iconImg.getWidth(null) > MAX_IMAGE_WIDTH) ? MAX_IMAGE_WIDTH : iconImg.getWidth(null);
		int newHeight = (int)(percentHeightToWidth * newWidth);
		
		System.out.println("stuff: " + newWidth + " " + newHeight + " " + MAX_IMAGE_WIDTH + " " + iconImg.getHeight(null));
		
		BufferedImage baseImg = new BufferedImage(newWidth,
				newHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = baseImg.createGraphics();
		g.drawImage(iconImg, 0, 0, newWidth, newHeight, null);
		ImageIcon finalIcon = new ImageIcon(baseImg);
		
		imagePreview.setText("");
		imagePreview.setIcon(finalIcon);
	}

	private void setupPreviewPanel() {
		previewPanel = new JPanel();
		previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
		
		JLabel title = new JLabel("PREVIEW:");
		imagePreview = new JLabel("No Image Selected");
		
		previewPanel.add(title);
		previewPanel.add(imagePreview);
	}

	private void setupButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		loadImageButton = new JButton("Load Image");
		translateButton = new JButton("Translate");
		advancedButton = new JButton("Advanced Options...");
		
		loadImageButton.addActionListener(new ButtonListener());
		translateButton.addActionListener(new ButtonListener());
		advancedButton.addActionListener(new ButtonListener());
		
		buttonPanel.add(loadImageButton);
		buttonPanel.add(translateButton);
		buttonPanel.add(advancedButton);
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == loadImageButton){
				window.loadImageClicked();
			}
			else if (evt.getSource() == translateButton){
				window.translateClicked();
			}
			else if (evt.getSource() == advancedButton){
				window.advancedOptionsClicked();
			}
		}
	}
}