package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import io.NeuralNetworkIO;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import neuralNetwork.INeuralNetwork;
import app.InputReader;

public class NetworkSelectionPanel extends JPanel {
	private INeuralNetwork chosenNetwork;
	private JLabel chosenNetworkLabel;
	private JButton chooseNetworkButton;
	private String networkName;
	
	private INetworkSelectionHandler handler;
	
	private static final String DEFAULT_NETWORK_NAME = "default.ann";
	
	public NetworkSelectionPanel(INetworkSelectionHandler handler){
		this.handler = handler;
		setupPanel();
	}
	
	private void setupPanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		if (chosenNetwork == null){
			chosenNetwork = NeuralNetworkIO.readNetwork(InputReader.TRAINED_NETWORK_NAME);		
			chosenNetworkLabel = new JLabel("Selected Network: default");
			chosenNetworkLabel.setFont(HomeWindow.SUB_LABEL_FONT);
		}
		chooseNetworkButton = new JButton("Choose Different Network");
		chooseNetworkButton.setFont(HomeWindow.SUB_LABEL_FONT);
		chooseNetworkButton.addActionListener(new ButtonListener());
		
		this.add(chosenNetworkLabel);
		this.add(chooseNetworkButton);
	}
	
	public INeuralNetwork getNetwork(){
		return chosenNetwork;
	}
	
	public void setNetwork(INeuralNetwork network, String networkName){
		chosenNetwork = network;
		this.networkName = networkName;
		chosenNetworkLabel.setText("Selected Network: " + networkName);
	}
	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == chooseNetworkButton){
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("savedNetworks/customNetworks"));
				int result = chooser.showOpenDialog(NetworkSelectionPanel.this);
				
				if (result == JFileChooser.APPROVE_OPTION){
					String filePath = chooser.getSelectedFile().getAbsolutePath();
					
					if (filePath.endsWith(".ann")){
						chosenNetwork = (chooser.getSelectedFile().getName().equals(DEFAULT_NETWORK_NAME)) ? 
								null : NeuralNetworkIO.readFromFilepath(filePath);
						networkName = chooser.getSelectedFile().getName();
						chosenNetworkLabel.setText("Selected Network: " + networkName);
						
						handler.networkSelected(networkName, chosenNetwork);
					}
					else JOptionPane.showMessageDialog(NetworkSelectionPanel.this, "Must select a neural network file.", "Invalid File", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
	}
}
