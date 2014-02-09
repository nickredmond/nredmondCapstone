package ui;

import io.NeuralNetworkIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import neuralNetwork.INeuralNetwork;
import app.ImageReadMethod;
import app.InputReader;

public class AdvancedOptionsWindow extends JFrame {
	private JButton saveChangesButton, chooseNetworkButton;
	private Map<String, ImageReadMethod> readMethods;
	private MainWindow window;
	
	private JCheckBox nnCheckBox, ldCheckBox;
	private JLabel chosenNetworkLabel;
	
	private List<ImageReadMethod> selectedReadMethods;
	private INeuralNetwork chosenNetwork;
	private String networkName;
	
	public AdvancedOptionsWindow(MainWindow window, List<ImageReadMethod> selectedMethods, INeuralNetwork chosenNetwork, String networkName){
		this(window, selectedMethods);
		
		if (chosenNetwork != null && networkName != null){
			this.networkName = networkName;
			this.chosenNetwork = chosenNetwork;
			chosenNetworkLabel.setText(networkName);
		}
	}
	
	public AdvancedOptionsWindow(MainWindow window, List<ImageReadMethod> selectedMethods){
		this.window = window;
		
		selectedReadMethods = new ArrayList<ImageReadMethod>();
		if(selectedMethods != null){
			for (ImageReadMethod nextMethod : selectedMethods){
				selectedReadMethods.add(nextMethod);
			}
		}
		
		
		initializeReadMethods();
		setupClassificationOptions();
		setupSaveButton();
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		setupReadMethodsPanel();
		setupNetworkToUsePanel();
		
		this.getContentPane().add(saveChangesButton);
		
		this.pack();
		this.setVisible(true);
	}
	
	private void setupNetworkToUsePanel() {
		JPanel networkPanel = new JPanel();
		networkPanel.setLayout(new BoxLayout(networkPanel, BoxLayout.X_AXIS));
		
		if (chosenNetwork == null){
			chosenNetwork = NeuralNetworkIO.readNetwork(InputReader.TRAINED_NETWORK_NAME);		
			chosenNetworkLabel = new JLabel(InputReader.TRAINED_NETWORK_NAME);
		}
		chooseNetworkButton = new JButton("Choose Different Network");
		chooseNetworkButton.addActionListener(new ButtonListener());
		
		networkPanel.setBorder(BorderFactory.createTitledBorder("Chosen Neural Network"));
		networkPanel.add(chosenNetworkLabel);
		networkPanel.add(chooseNetworkButton);
		this.getContentPane().add(networkPanel);
	}

	private void setupReadMethodsPanel() {
		JPanel readMethodsPanel = new JPanel();
		readMethodsPanel.setLayout(new BoxLayout(readMethodsPanel, BoxLayout.Y_AXIS));
		readMethodsPanel.add(nnCheckBox);
		readMethodsPanel.add(ldCheckBox);
		
		readMethodsPanel.setBorder(BorderFactory.createTitledBorder("Classification Methods"));
		this.getContentPane().add(readMethodsPanel);
	}

	private void setupSaveButton(){
		saveChangesButton = new JButton("Save Changes");
		saveChangesButton.addActionListener(new ButtonListener());
	}
	
	private void setupClassificationOptions() {
		nnCheckBox = new JCheckBox("Neural Network");
		ldCheckBox = new JCheckBox("Least Distance");
		
		if(selectedReadMethods.contains(ImageReadMethod.NEURAL_NETWORK)){
			nnCheckBox.setSelected(true);
		}
		if(selectedReadMethods.contains(ImageReadMethod.LEAST_DISTANCE)){
			ldCheckBox.setSelected(true);
		}
		
		nnCheckBox.addActionListener(new ButtonListener());
		ldCheckBox.addActionListener(new ButtonListener());
	}

	private void initializeReadMethods(){
		readMethods = new HashMap<String, ImageReadMethod>();
		readMethods.put("Neural Network", ImageReadMethod.NEURAL_NETWORK);
		readMethods.put("Least Distance", ImageReadMethod.LEAST_DISTANCE);
	}
	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == saveChangesButton){
				if (selectedReadMethods.size() > 0){
					window.advancedOptionsSaveChangesClicked(selectedReadMethods, chosenNetwork, networkName);
					AdvancedOptionsWindow.this.dispose();
				}
				else JOptionPane.showMessageDialog(AdvancedOptionsWindow.this, "Must select at least one method", "Selection Error", JOptionPane.ERROR_MESSAGE);
			}
			else if (evt.getSource().getClass() == JCheckBox.class){
				JCheckBox clickedBox = (JCheckBox)evt.getSource();
				
				if (clickedBox.isSelected()){
					selectedReadMethods.add(readMethods.get(clickedBox.getText()));
				}
				else selectedReadMethods.remove(readMethods.get(clickedBox.getText()));
			}
			else if (evt.getSource() == chooseNetworkButton){
				chooseNetworkWithDialogue();
			}
		}

		private void chooseNetworkWithDialogue() {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("savedNetworks/customNetworks"));
			int result = chooser.showOpenDialog(AdvancedOptionsWindow.this);
			
			if (result == JFileChooser.APPROVE_OPTION){
				String filePath = chooser.getSelectedFile().getAbsolutePath();
				
				if (filePath.endsWith(".ann")){
					chosenNetwork = NeuralNetworkIO.readFromFilepath(filePath);
					networkName = chooser.getSelectedFile().getName();
					chosenNetworkLabel.setText(networkName);
				}
				else JOptionPane.showMessageDialog(AdvancedOptionsWindow.this, "Must select a neural network file.", "Invalid File", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
}
