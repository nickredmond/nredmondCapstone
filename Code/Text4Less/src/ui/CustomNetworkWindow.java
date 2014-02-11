package ui;

import io.CharacterType;
import io.NeuralNetworkIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import app.AlphaNumericCharacterConverter;
import app.NetworkFactory;

public class CustomNetworkWindow extends JFrame {
	private JTextField numberLayersBox, numberNeuronsBox, iterationsField, mseField;
	private JButton trainButton, saveButton;
	
	private INeuralNetwork customNetwork;
	private int numberNeurons, numberLayers, numberIterations;
	private float mse;
	
	public CustomNetworkWindow(){
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setupTextFieldPanels();
		setupButtons();
		
		this.pack();
		this.setVisible(true);
	}

	private void setupButtons() {
		trainButton = new JButton("Train Network");
		saveButton = new JButton("Save Network");
		
		trainButton.addActionListener(new ButtonListener());
		saveButton.addActionListener(new ButtonListener());
		
		saveButton.setEnabled(false);
		
		this.add(trainButton);
		this.add(saveButton);
	}

	private void setupTextFieldPanels() {
		JPanel numberLayersPanel = new JPanel();
		JPanel numberNeuronsPanel = new JPanel();
		
		JLabel layersLabel = new JLabel("Number of hidden layers:");
		JLabel neuronsLabel = new JLabel("Number of neurons per hidden layer:");
		
		numberLayersPanel.setLayout(new BoxLayout(numberLayersPanel, BoxLayout.X_AXIS));
		numberNeuronsPanel.setLayout(new BoxLayout(numberNeuronsPanel, BoxLayout.X_AXIS));
		
		numberLayersBox = new JTextField();
		numberNeuronsBox = new JTextField();
		
		numberLayersPanel.add(layersLabel);
		numberLayersPanel.add(numberLayersBox);
		
		numberNeuronsPanel.add(neuronsLabel);
		numberNeuronsPanel.add(numberNeuronsBox);
		
		JPanel iterationsPanel = new JPanel();
		JPanel msePanel = new JPanel();
		
		iterationsPanel.setLayout(new BoxLayout(iterationsPanel, BoxLayout.X_AXIS));
		msePanel.setLayout(new BoxLayout(msePanel, BoxLayout.X_AXIS));
		
		iterationsField = new JTextField();
		mseField = new JTextField();
		
		JLabel iterationsLabel = new JLabel("iterations");
		JLabel mseLabel = new JLabel("mean squared error");
		
		iterationsPanel.add(iterationsField);
		iterationsPanel.add(iterationsLabel);
		
		
		msePanel.add(mseField);
		msePanel.add(mseLabel);
		
		this.add(numberLayersPanel);
		this.add(numberNeuronsPanel);
		this.add(new JLabel("Stop training at:"));
		this.add(iterationsPanel);
		this.add(msePanel);
	}
	
	private boolean parseUserInput(){
		String numberLayersText = numberLayersBox.getText();
		String numberNeuronsText = numberNeuronsBox.getText();
		String iterationsText = iterationsField.getText();
		String mseText = mseField.getText();
		String numericalPattern = "^[0-9]+$";
		String floatNumericalPattern = "^[0-9]+([.][0-9]+)?$";
		
		boolean isValidInput = (numberLayersText.matches(numericalPattern) &&
				numberNeuronsText.matches(numericalPattern) &&
				iterationsText.matches(numericalPattern) &&
				mseText.matches(floatNumericalPattern));
		
		if (isValidInput){
			numberLayers = Integer.parseInt(numberLayersText);
			numberNeurons = Integer.parseInt(numberNeuronsText);
			numberIterations = Integer.parseInt(iterationsText);
			mse = Float.parseFloat(mseText);
		}
		else JOptionPane.showMessageDialog(this, "Input must be non-negative integer values \r\n" +
				"(except for mse, which must be a decimal)", "Invalid Input", JOptionPane.ERROR_MESSAGE);
		
		return isValidInput;
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == trainButton){
				if (parseUserInput()){
					try {
						trainButton.setEnabled(false);
						saveButton.setEnabled(false);
						
						INetworkIOTranslator translator = new AlphaNumericIOTranslator();
						INeuralNetwork network = new MatrixNeuralNetwork(((AlphaNumericIOTranslator)translator).getInputLength(),
								numberLayers, numberNeurons, AlphaNumericCharacterConverter.NUMBER_CLASSES, true);
						customNetwork = NetworkFactory.getTrainedNetwork(network, translator, CharacterType.ASCII2, new MatrixBackpropTrainer(0.05f, 0.02f),
								numberIterations, mse);
						
						trainButton.setEnabled(true);
						saveButton.setEnabled(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else if (evt.getSource() == saveButton){
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("savedNetworks/customNetworks"));
				chooser.addChoosableFileFilter(new FileNameExtensionFilter("Artifical Neural Networks", "ann"));
				int result = chooser.showSaveDialog(CustomNetworkWindow.this);
				
				if (result == JFileChooser.APPROVE_OPTION){
					File networkFile = chooser.getSelectedFile();
					String networkName = networkFile.getName();
					String[] nameParts = networkName.split("[.]");
					
					if (isValidFilename(nameParts)){
						String networkPath = networkFile.getAbsolutePath();
						
						if (!networkPath.endsWith(".ann"))
							networkPath += ".ann";
						
						NeuralNetworkIO.writeNetworkToPath(customNetwork, networkPath);
					}
					else{
						String errorMessage = getErrorMessage(nameParts);
						JOptionPane.showMessageDialog(CustomNetworkWindow.this, errorMessage, "Save Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}

		private boolean isValidFilename(String[] nameParts) {
			return (nameParts.length == 1 || (nameParts.length == 2 && nameParts[1].equals("ann"))) && nameParts[0].length() > 0;
		}

		private String getErrorMessage(String[] nameParts) {
			String errorMessage = "Error saving the file.";
			
			if (nameParts.length > 2){
				errorMessage = "File cannot have more than one extension.";
			}
			else if (nameParts[0].length() == 0){
				errorMessage = "File name must have at least one character.";
			}
			else if (!nameParts[1].equals("ann")){
				errorMessage = "File must be an artificial neural network.";
			}
			
			return errorMessage;
		}
	}
}