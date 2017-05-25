package ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HomeWindow extends JFrame{
	public static Font DEFAULT_BUTTON_FONT = new Font("Arial", Font.BOLD, 16);
	public static Font DEFAULT_LABEL_FONT = new Font("Arial", Font.BOLD, 16);
	
	public static Font SUB_LABEL_FONT = new Font("Arial", Font.BOLD, 14);
	
	private JButton beginButton;
	
	private final int DEFAULT_WIDTH = 600;
	private final int DEFAULT_HEIGHT = 700;
	
	public HomeWindow(){
		JLabel title = new JLabel("Text4Less");
		JLabel subTitle = new JLabel("An Experiment in OCR");
		
		title.setFont(new Font("Helvetica", Font.PLAIN, 72));
		subTitle.setFont(new Font("Helvetica", Font.ITALIC, 28));
		title.setAlignmentX(CENTER_ALIGNMENT);
		subTitle.setAlignmentX(CENTER_ALIGNMENT);
		
		beginButton = new JButton("Begin");
		setupButton(beginButton);
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		contentPane.add(Box.createVerticalGlue());
		
		contentPane.add(title);
		contentPane.add(subTitle);
		
		contentPane.add(Box.createVerticalGlue());
		contentPane.add(beginButton);
		contentPane.add(Box.createVerticalGlue());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Text4Less");
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setVisible(true);
	}

	private void setupButton(JButton button) {
		button.setFont(new Font("Helvetica", Font.PLAIN, 24));
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.addActionListener(new ButtonListener());
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == beginButton){
				new MenuWindow();
				HomeWindow.this.dispose();
			}
		}
	}
}
