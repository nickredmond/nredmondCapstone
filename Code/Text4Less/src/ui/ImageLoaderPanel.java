package ui;

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
	
	public ImageLoaderPanel(MainWindow window){
		this.window = window;
		
		setupButtonPanel();
		setupPreviewPanel();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(buttonPanel);
		this.add(previewPanel);
	}
	
	public void setPreviewImage(File imageFile){
		try {
			BufferedImage image = ImageIO.read(imageFile);
			imagePreview.setText("");
			imagePreview.setIcon(new ImageIcon(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
