package ui;

import io.Logger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import app.IClickHandler;
import app.Main;

public class SplashScreen extends JFrame {
	private final String IMAGE_PATH = "splashScreen.png";
	private Image splashImage;
	
	private IClickHandler handler;
	
	private float loadPercentage = 0.0f;
	private String loadStatus = "";
	private Font loadFont, clickFont;
	private boolean isClickable;
	
	private final int SCREEN_WIDTH = 800;
	private final int SCREEN_HEIGHT = 600;
	private final int LOADING_BAR_X = 155;
	private final int LOADING_BAR_Y = 280;
	private final int MESSAGE_X = LOADING_BAR_X;
	private final int MESSAGE_Y = 300;
	private final int CLICK_X = LOADING_BAR_X;
	private final int CLICK_Y = 350;
	
	private final int MAX_LOADING_BAR_WIDTH = 400;
	private final int LOADING_BAR_HEIGHT = 7;
	
	private final String CLICK_MESSAGE = "Click to Begin";	
	private final Color LOADING_BAR_COLOR = new Color(0,0,0);
	
	public SplashScreen(IClickHandler handler){
		this.handler = handler;
		
		try {
			splashImage = ImageIO.read(new File(new Main().getWorkingDirectory() + IMAGE_PATH));
			loadFont = new Font("Arial", Font.PLAIN, 12);
			clickFont = new Font("Arial", Font.BOLD, 18);
			
			this.setLocationRelativeTo(null);
			this.setUndecorated(true);
			this.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
			
			int width = this.getWidth() / 2;
			int height = this.getHeight() / 2;
			this.setLocation(new Point(this.getLocation().x - width, this.getLocation().y - height));
			
			this.addMouseListener(new ClickListener());
			this.setVisible(true);
		} catch (IOException e) {
			Logger.logMessage("Could not load splash screen image. Message: " + e.getMessage());
			JOptionPane.showMessageDialog(this, "Error occurred while loading splash screen. Application must exit.",
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void setClickable(boolean clickable){
		isClickable = clickable;
	}
	
	public void setLoadStatusMessage(String message){
		loadStatus = message;
	}
	
	public void setLoadPercentage(float percent){
		loadPercentage = percent;
	}
	
	@Override
	public void paint(Graphics g){
		g.drawImage(splashImage, 0, 0, null);
		
		int loadingBarWidth = (int)(loadPercentage * (float)MAX_LOADING_BAR_WIDTH);
		g.setColor(LOADING_BAR_COLOR);
		g.fillRect(LOADING_BAR_X, LOADING_BAR_Y, loadingBarWidth, LOADING_BAR_HEIGHT);
		
		g.setColor(Color.BLACK);
		g.setFont(loadFont);
		g.drawString(loadStatus, MESSAGE_X, MESSAGE_Y);
		
		if (isClickable){
			g.setFont(clickFont);
			g.drawString(CLICK_MESSAGE, CLICK_X, CLICK_Y);
		}
	}
	
	private class ClickListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent evt){
			if (isClickable){
				handler.mouseClicked();
			}
		}
	}
}
