package ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class LoadingScreen extends JFrame {
	private String text;
	private int maxNumberDots, currentDotNumber;
	private boolean isRunning;
	
	private JLabel screenTextLabel;
	
	private final int DEFAULT_MAX_DOTS = 5;
	private final int WAITING_TIME = 450;
	
	public LoadingScreen(String message, int width, int height){
		maxNumberDots = DEFAULT_MAX_DOTS;
		currentDotNumber = 0;
		
		screenTextLabel = new JLabel();
		screenTextLabel.setFont(new Font("Helvetica", Font.PLAIN, 28));
		screenTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		screenTextLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		
		this.getContentPane().add(screenTextLabel);
		
		this.setUndecorated(true);
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