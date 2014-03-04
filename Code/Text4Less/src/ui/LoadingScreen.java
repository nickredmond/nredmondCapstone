package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class LoadingScreen extends JFrame {
	private String text;
	private int maxNumberDots, currentDotNumber;
	private boolean isRunning;
	
	private JLabel screenTextLabel;
	private JTextArea subLabel;
	
	private final int DEFAULT_MAX_DOTS = 5;
	private final int WAITING_TIME = 450;
	
	private boolean hasSubmessage;
	private String subMessage;
	
	public LoadingScreen(String message, boolean hasSubmessage, Component parent, int width, int height){
		maxNumberDots = DEFAULT_MAX_DOTS;
		currentDotNumber = 0;
		
		screenTextLabel = new JLabel();
		screenTextLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 10, 0));
		screenTextLabel.setFont(new Font("Helvetica", Font.PLAIN, 28));
		screenTextLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		
		this.hasSubmessage = hasSubmessage;
		int numberRows = (hasSubmessage ? 2 : 1);
		
		this.getContentPane().setLayout(new GridLayout(numberRows, 0));
		this.getContentPane().add(screenTextLabel);
		
		if (hasSubmessage){
			subLabel = new JTextArea();
			subLabel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 0));
			subLabel.setFont(new Font("Helvetica", Font.ITALIC, 16));
			subLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
			
			subLabel.setOpaque(false);
			subLabel.setEditable(false);
			
			this.getContentPane().add(subLabel);
		}
		
		this.setUndecorated(true);
		this.setLocationRelativeTo(parent);
		this.setSize(new Dimension(width, height));
		this.setVisible(true);
		
		text = message;
		isRunning = true;
	
		new ScreenRunner().start();
	}
	
	public void closeScreen(){
		isRunning = false;
		this.dispose();
	}
	
	public boolean hasSubmessage(){
		return hasSubmessage;
	}
	
	public void setSubMessage(String message){
		if (!hasSubmessage){
			throw new IllegalStateException("Loading screen is not set to have a sub message.");
		}
		
		subMessage = message;
	}

	private class ScreenRunner extends Thread{
	@Override
	public void run() {
		while (isRunning){
			String message = text;
			
			for (int i = 0; i < currentDotNumber; i++){
				message += ".";
			}
			
			screenTextLabel.setText(message);
			currentDotNumber = (currentDotNumber >= maxNumberDots) ? 0 : currentDotNumber + 1;
			
			if (hasSubmessage){
				subLabel.setText(subMessage);
			}
			
			pauseThread(WAITING_TIME);
		}
	}

	private void pauseThread(int timeMillis) {
		try {
			Thread.sleep(timeMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	}
}
