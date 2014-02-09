package ui;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class HomeWindow extends JFrame{
	private JButton translateButton, trainNetButton;
	
	public HomeWindow(){
		translateButton = new JButton("Translate an Image");
		trainNetButton = new JButton("Train Neural Network");
		setupButton(translateButton);
		setupButton(trainNetButton);
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(translateButton);
		contentPane.add(trainNetButton);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	private void setupButton(JButton button) {
		button.setFont(new Font("Helvetica", Font.PLAIN, 24));
		button.addActionListener(new ButtonListener());
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == translateButton){
				new MainWindow();
			}
			else if (evt.getSource() == trainNetButton){
				new CustomNetworkWindow();
			}
		}
	}
}
